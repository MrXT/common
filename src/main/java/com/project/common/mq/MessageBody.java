package com.project.common.mq;

public class MessageBody {

    private String body;

    private String application;

    private String ip;

    private long initTime = System.currentTimeMillis();

    private String rspBuildMsg;

    public static final String TOPIC = "topic";

    public static final String TAGS = "tags";

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getApplication() {
        return this.application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getInitTime() {
        return this.initTime;
    }

    public void setInitTime(long initTime) {
        this.initTime = initTime;
    }

    public String getRspBuildMsg() {
        return this.rspBuildMsg;
    }

    public void setRspBuildMsg(String rspBuildMsg) {
        this.rspBuildMsg = rspBuildMsg;
    }
}