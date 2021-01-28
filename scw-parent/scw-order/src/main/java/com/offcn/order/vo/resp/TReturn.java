package com.offcn.order.vo.resp;

import java.io.Serializable;

public class TReturn implements Serializable {

    private Integer id;
    private Integer projectid;//项目ID
    private Byte type;//回报类型 0 - 实物回报， 1 虚拟物品回报
    private Integer supportmoney;//支持金额
    private String content;//回报内容
    private Integer count;//回报数量  0 为不限回报数量
    private Integer signalpurchase;//单笔限购
    private Integer purchase;//限购数量
    private Integer freight;//运费
    private Byte invoice;//是否开发票  0 - 不开发票， 1 - 开发票
    private Integer rtndate;//回报时间,众筹成功后多少天进行回报

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getSupportmoney() {
        return supportmoney;
    }

    public void setSupportmoney(Integer supportmoney) {
        this.supportmoney = supportmoney;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSignalpurchase() {
        return signalpurchase;
    }

    public void setSignalpurchase(Integer signalpurchase) {
        this.signalpurchase = signalpurchase;
    }

    public Integer getPurchase() {
        return purchase;
    }

    public void setPurchase(Integer purchase) {
        this.purchase = purchase;
    }

    public Integer getFreight() {
        return freight;
    }

    public void setFreight(Integer freight) {
        this.freight = freight;
    }

    public Byte getInvoice() {
        return invoice;
    }

    public void setInvoice(Byte invoice) {
        this.invoice = invoice;
    }

    public Integer getRtndate() {
        return rtndate;
    }

    public void setRtndate(Integer rtndate) {
        this.rtndate = rtndate;
    }
}
