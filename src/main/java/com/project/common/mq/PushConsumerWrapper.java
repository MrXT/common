package com.project.common.mq;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushConsumerWrapper extends DefaultMQPushConsumer {

    private static Logger logger = LoggerFactory.getLogger(PushConsumerWrapper.class);

    public static final String TOPIC_SUB_SPLIT = "#";

    private List<String> subs;

    private AbstractMsgHandler msgHandler;

    public void init() {
        try {
            logger.info("name : " + getConsumerGroup() + " @ srvAddr : " + getNamesrvAddr() + " @ subs : {}", JSONObject.toJSONString(this.subs));

            for (String sub : this.subs) {
                String[] split = StringUtils.split(StringUtils.trimToNull(sub), "#");
                if (split.length != 2)
                    throw new RuntimeException("subs format error" + sub);
                logger.info(" topic : {} @ subs : {}", split[0], split[1]);
                subscribe(split[0], split[1]);
            }

            registerMessageListener(new MessageListenerConcurrently() {

                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {
                    Message msg = (Message) list.get(0);
                    ConsumeConcurrentlyStatus status = ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    try {
                        status = PushConsumerWrapper.this.msgHandler.handler(msg);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    return status;
                }
            });
            start();

            monitor();

            logger.info("------------------------ConsumerWrapper started----------------------------------------");
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("pushconsumer init fail");
        }
    }

    private void monitor() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                if (PushConsumerWrapper.logger.isInfoEnabled())
                    while (true)
                        try {
                            Thread.sleep(300000L);
                            PushConsumerWrapper.logger.info("------------------pushconsumer still works-------------------");
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public List<String> getSubs() {
        return this.subs;
    }

    public void setSubs(List<String> subs) {
        this.subs = subs;
    }

    public AbstractMsgHandler getMsgHandler() {
        return this.msgHandler;
    }

    public void setMsgHandler(AbstractMsgHandler msgHandler) {
        this.msgHandler = msgHandler;
    }
}