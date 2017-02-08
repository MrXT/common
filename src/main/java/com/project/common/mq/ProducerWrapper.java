package com.project.common.mq;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerWrapper extends DefaultMQProducer {

    private static Logger logger = LoggerFactory.getLogger(ProducerWrapper.class);

    @Deprecated
    private int idleSec = 30;

    private String application;

    private String ip;

    public void init() {
        try {
            logger.info(" param name : {}  @ srvAddr : {}", getProducerGroup(), getNamesrvAddr());

            start();

            logger.info("------------------------ProducerWrapper started----------------------------------------");
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("produce init fail");
        }
    }

    public MessageBody buildDefaultMessageBody() {
        MessageBody body = new MessageBody();
        if (StringUtils.isEmpty(this.application)) {
            this.application = getProducerGroup();
        }
        body.setApplication(this.application);

        if (StringUtils.isEmpty(this.ip)) {
            try {
                this.ip = getClientIP();
            } catch (Throwable e) {
                e.printStackTrace();
                this.ip = "Unkown";
                logger.info("----------- ip exception ------------");
            }
        }
        body.setIp(this.ip);
        return body;
    }

    public int getIdleSec() {
        return this.idleSec;
    }

    public void setIdleSec(int idleSec) {
        this.idleSec = idleSec;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getApplication() {
        return this.application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}