package com.project.common.bean.jpush;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.project.common.util.LogUtils;

@Component
public class JPush {

    @Value("${masterSecret}")
    private String masterSecret;

    @Value("${appKey}")
    private String appKey;
    
    @Value("${prodEvn}")
    private Boolean prodEvn;

    @SuppressWarnings("deprecation")
    public Boolean sendMsg(String alias[], String message) {
        for (String string : alias) {
            if (string == null) {
                return false;
            }
        }
        JSONObject object = JSON.parseObject(message);
        Map<String, String> map = new HashMap<>();
        for (String key : object.keySet()) {
            map.put(key, object.getString(key));
        }
        try {
            JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
            // PushPayload payload =
            // PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
            // .setNotification(Notification.alert(message)).build();
            PushPayload payload = PushPayload
                .newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(
                    Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().addExtras(map).setAlert(object.getString("content")).build())
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(object.getString("content")).setBadge(5).addExtras(map).build()).build())
                .setOptions(Options.newBuilder().setApnsProduction(prodEvn).build()).setMessage(Message.content(message)).build();
            PushResult result = jpushClient.sendPush(payload);
            if (result.getResponseCode() == 200) {
                return true;
            } else {
                LogUtils.ERROR(result.getOriginalContent());
                return false;
            }
        } catch (APIConnectionException | APIRequestException e) {
            LogUtils.LOGEXCEPTION(e);
            return false;
        }
    }

}
