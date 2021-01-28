package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.constant.ProjectConstant;
import com.offcn.project.po.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import com.offcn.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "项目基本功能模块（创建、保存、项目信息获取、文件上传等）")
@RequestMapping("/project")
@RestController
public class ProjectCreateController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;

    @ApiOperation("项目发起第一步-阅读同意协议")
    @PostMapping("/init")
    public AppResponse<String> init(BaseVo vo){
        String accessToken = vo.getAccessToken();
        //通过登录令牌获取项目ID
        String memberId = stringRedisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(memberId)){
            return AppResponse.fail("无此权限，请先登录");
        }
        int id = Integer.parseInt(memberId);
        //保存临时项目信息到Redis
        String projectToken = projectCreateService.initCreateProject(id);
        return AppResponse.ok(projectToken);
    }

    @ApiOperation("项目发起第2步-保存项目的基本信息")
    @PostMapping("/savebaseInfo")
    public AppResponse<String> savebaseInfo(ProjectBaseInfoVo vo){
        //1、取得Redis中之前存储json结构的项目信息
        String orignal = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + vo.getProjectToken());
        //2、转换为Redis存储对应的vo
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(orignal,ProjectRedisStorageVo.class);
        //3、将页面收集来的数据，复制到和Redis映射的vo中
        BeanUtils.copyProperties(vo,projectRedisStorageVo);
        //4、将这个vo对象再转换为json字符串
        String jsonString = JSON.toJSONString(projectRedisStorageVo);
        //5、重新更新到Redis
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+vo.getProjectToken(),jsonString);

        return AppResponse.ok("ok");
    }

    @ApiOperation("项目发起第3步-项目保存项目回报信息")
    @PostMapping("/savereturn")

    public AppResponse<Object> saveReturnInfo(@RequestBody List<ProjectReturnVo> pro){
        ProjectReturnVo projectReturnVo = pro.get(0);
        String projectToken = projectReturnVo.getProjectToken();
        //1、取得Redis中之前存储json结构的项目信息
        String projectContext = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken);
        //2、转换为Redis存储对应的vo
        ProjectRedisStorageVo storageVo = JSON.parseObject(projectContext, ProjectRedisStorageVo.class);
        //3、将页面收集来的回报数据封装重新放入Redis
        List<TReturn> returns = new ArrayList<>();
        for (ProjectReturnVo projectReturnVo1  : pro) {
            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(projectReturnVo1 ,tReturn);
            returns.add(tReturn);
        }
        //4、更新return集合
        storageVo.setProjectReturns(returns);
        String jsonString = JSON.toJSONString(storageVo);
        //5、重新更新到Redis
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken,jsonString);
        return AppResponse.ok("ok");

    }


    @ApiOperation("项目发起第4步-项目保存项目回报信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken",value = "用户令牌",required = true),
            @ApiImplicitParam(name = "projectToken",value="项目标识",required = true),
            @ApiImplicitParam(name="ops",value="用户操作类型 0-保存草稿 1-提交审核",required = true)})
    @PostMapping("/submit")
    public AppResponse<Object> submit(String accessToken,String projectToken,String ops ){
        //1、前置校验，检查AccessToken
        String memberId = stringRedisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(memberId)){
            return AppResponse.fail("无权限，请先登录");
        }
        //2、根据项目token，获取项目存储在redis的信息
        String projectJsonStr  = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken);
        //把json格式的项目信息还原为项目对象
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(projectJsonStr, ProjectRedisStorageVo.class);
        //3、判断用户操作类型不为空，进行处理
        if(!StringUtils.isEmpty(ops)){
            //判断操作类型是1，提交审核
            if(ops.equals("1")){
                //获取项目状态提交枚举
                ProjectStatusEnume submitAuth = ProjectStatusEnume.SUBMIT_AUTH;
                //保存项目信息
                projectCreateService.saveProjectInfo(submitAuth,projectRedisStorageVo);

                return AppResponse.ok(null);
            }else if(ops.equals("0")){
                //获取项目 草稿状态
                ProjectStatusEnume projectStatusEnume = ProjectStatusEnume.DRAFT;
                //保存草稿
                projectCreateService.saveProjectInfo(projectStatusEnume,projectRedisStorageVo);
                return AppResponse.ok(null);
            }else {
                AppResponse<Object> appResponse = AppResponse.fail(null);
                appResponse.setMsg("不支持此操作");
                return appResponse;
            }
        }
        return AppResponse.fail(null);
    }
}