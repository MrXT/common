package com.project.common.bean.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.project.common.util.FastJsonUtils;

import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
/**
 * mongoDB处理工具
 * ClassName: MongoDao <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2016年12月31日
 * @version 1.0
 */
public class MongoDao {

    private DB db;

    private MongoTemplate mongoTemplate;

    public final DB getDb() {
        return this.db;
    }

    public final void setDb(DB db) {
        this.db = db;
    }

    public final MongoTemplate getMongoTemplate() {
        return this.mongoTemplate;
    }

    public final void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.db = mongoTemplate.getDb();
    }

    public void save(String collectionName, Map<String, Object> map) {
        this.db.getCollection(collectionName).save(new BasicDBObject(map));
    }

    public void save(String collectionName, String jsonStr) {
        save(collectionName, FastJsonUtils.stringToCollect(jsonStr));
    }

    public DBObject findById(String collectionName, Object id) {
        return this.db.getCollection(collectionName).findOne(id);
    }
}