package com.project.common.mq;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @version 1.0
 * @Description: MQ消息实体定义
 * @date 2015年11月26日 下午1:20:08
 */
public class MqMessage {

    private DataType dataType;

    private OptType optType;

    private Object messageBody;

    @JSONField(serialize = false)
    public String getTags() {
        return dataType.value() + "_" + optType.value();
    }

    public MqMessage() {
        super();
    }

    public MqMessage(DataType dataType, OptType optType, Object messageBody) {
        super();
        this.dataType = dataType;
        this.optType = optType;
        this.messageBody = messageBody;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public OptType getOptType() {
        return optType;
    }

    public void setOptType(OptType optType) {
        this.optType = optType;
    }

    public Object getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(Object messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * Copyright (c) 2015, 北京卡拉卡尔科技股份有限公司 All rights reserved.
     *
     * @author xiaolong.li@karakal.com.cn (李小龙)
     * @version 1.0
     * @Description: MQ消息操作实体类型
     * @date 2015年11月26日 下午1:13:35
     */
    public enum DataType {

        ARTIST("artist"),
        ALBUM("album"),
        SONG("song"),
        ARTISTTAG("artisttag"),
        ALBUMTAG("albumtag"),
        SONGTAG("songtag"),
        COPYRIGHT("cp"),
        TAGTREE("tagtree"),
        CATALOG("catalog");

        private String value;

        private DataType(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    /**
     * Copyright (c) 2015, 北京卡拉卡尔科技股份有限公司 All rights reserved.
     *
     * @author xiaolong.li@karakal.com.cn (李小龙)
     * @version 1.0
     * @Description: MQ消息操作类型
     * @date 2015年11月26日 下午1:16:14
     */
    public enum OptType {
        ADD("add"),                //新增消息
        UPDATE("update"),        //更新消息
        DELETE("delete"),        //删除消息
        MERGE("merge"),            //合并消息
        SPLIT("split"),            //拆分消息
        EXTRACT("extract"),        //抽取消息
        ENABLE("enable"),			//启用
        DISABLE("disable"),			//禁用
        CATALOG("catalog");			//编目消息
        private String value;

        private OptType(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}
