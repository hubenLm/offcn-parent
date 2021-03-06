package com.offcn.config;

import com.offcn.util.OssTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class AppProjectConfig {
    @ConfigurationProperties(prefix = "oss")//加载配置文件中的OSS属性
    @Bean
    public OssTemplate ossTemplate(){
        return new OssTemplate();
    }
}
