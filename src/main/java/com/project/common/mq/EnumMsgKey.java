package com.project.common.mq;
/**
 * 消息传送格式
 * ClassName: EnumMsgKey <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月10日
 * @version 1.0
 */
public abstract interface EnumMsgKey {

    public static final String ID = "id";

    public static final String IDS = "ids";

    public static final String SITE_TYPE = "siteType";

    public static final String SITE_ID = "siteId";

    public static final String SITE_IDS = "siteIds";

    public static final String TYPE_CODE = "typeCode";

    public static final String TARGET_ID = "targetId";

    public static final String RESOURCE_IDS = "resourceIds";

    public static final String STATUS = "status";
}