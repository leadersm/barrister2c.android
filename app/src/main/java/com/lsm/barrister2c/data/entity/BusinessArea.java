package com.lsm.barrister2c.data.entity;

/**
 * Created by lvshimin on 16/5/20.
 * 咨询案件领域（类型）
 * 房产纠纷
 * 合同纠纷
 * 劳动用工
 * 交通事故
 * 公司相关
 * 婚姻家庭
 * 工商赔偿
 * 知识产权
 * 债券债务
 * 刑事辩护
 */
public class BusinessArea {
    String id;
    String name;
    String desc;
    String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
