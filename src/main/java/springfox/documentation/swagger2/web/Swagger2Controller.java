/*
 * Copyright 2015 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package springfox.documentation.swagger2.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Swagger;

import java.lang.reflect.Method;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.project.common.annotation.ApiResponseDesc;
import com.project.common.annotation.ApiResponseDesc.ApiResponseType;
import com.project.common.annotation.ApiResponseDescItem;
import com.project.common.annotation.ApiResponseDescItemItem;
import com.project.common.annotation.ApiResponseDescs;
import com.project.common.util.JacksonUtils;
import com.project.common.util.MapUtils;
import com.project.common.util.ValidateUtils;

/**
 * 方法重写，覆盖swagger插件的controller,因为与fastjson不兼容，导致数据不正确 ClassName:
 * Swagger2Controller <br/>
 * @author xt
 * @version 2017年1月29日
 */
@Controller
@ApiIgnore
public class Swagger2Controller {

    public static final String DEFAULT_URL = "/v2/api-docs";

    @Value("${springfox.documentation.swagger.v2.host:DEFAULT}")
    private String hostNameOverride;

    @Autowired
    private DocumentationCache documentationCache;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private ServiceModelToSwagger2Mapper mapper;

    @Autowired
    private JsonSerializer jsonSerializer;

    @ApiIgnore
    @RequestMapping(value = "${springfox.documentation.swagger.v2.path:" + DEFAULT_URL + "}", method = RequestMethod.GET)
    public @ResponseBody Object getDocumentation(@RequestParam(value = "group", required = false) String swaggerGroup) {

        String groupName = Optional.fromNullable(swaggerGroup).or(Docket.DEFAULT_GROUP_NAME);
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            return new ResponseEntity<Json>(HttpStatus.NOT_FOUND);
        }
        Swagger swagger = mapper.mapDocumentation(documentation);
        swagger.host(hostName());
        JSONObject json = JacksonUtils.jsonToBean((jsonSerializer.toJson(swagger).value()), JSONObject.class);
        JSONObject result = new JSONObject();
        if (json != null && json.getJSONObject("paths") != null) {// api接口文档，每个app接口，添加参数app，默认为true
            for (String path : json.getJSONObject("paths").keySet()) {
                JSONObject jsonPath = json.getJSONObject("paths").getJSONObject(path);
                for (String key : jsonPath.keySet()) {
                    JSONObject jsonMethod = jsonPath.getJSONObject(key);
                    // 通过ApiResponseDesc方法的注解来自定义返回json数据格式
                    addResponseDesc(path, jsonMethod, json);
                    addRequestParams(path, jsonMethod);
                }
                result.put(path, jsonPath);
            }
        }
        json.put("paths", result);
        return json;
    }

    /**
     * swagger api文档添加相应json描述
     * @author xt
     * @param path
     * @param jsonMethod
     */
    private void addResponseDesc(String path, JSONObject jsonMethod, JSONObject json) {
        for (RequestMappingInfo rmi : handlerMapping.getHandlerMethods().keySet()) {// 获取到所有的url与method的映射
            if (rmi.getPatternsCondition().getPatterns().contains(path)) {
                Method method = handlerMapping.getHandlerMethods().get(rmi).getMethod();
                if (method.isAnnotationPresent(ResponseBody.class) || method.getClass().isAnnotationPresent(RestController.class)) {
                    JSONObject result = new JSONObject();
                    result.put("status", new JSONObject(MapUtils.createMap("type", "integer", "default", 200, "description", "状态码")));
                    result.put("msg", new JSONObject(MapUtils.createMap("type", "string", "default", "操作成功", "description", "返回消息")));
                    result.put("data", new JSONObject(MapUtils.createMap("type", "integer", "default", 1, "description", "成功数量")));
                    if (method.isAnnotationPresent(ApiResponseDescs.class) && method.isAnnotationPresent(ApiOperation.class) && method.getAnnotation(ApiOperation.class).response() != null) {
                        // 获取apiresponsedescs的注解信息
                        JSONObject pro = new JSONObject();
                        JSONObject jsonO = new JSONObject();
                        ApiResponseDesc[] apiResponseDescs = method.getAnnotation(ApiResponseDescs.class).value();
                        if (apiResponseDescs.length > 0) {
                            if (apiResponseDescs.length == 1 && ValidateUtils.isBlank(apiResponseDescs[0].value())) {// 判断是不是基本数据类型的数组,items数组长度为1,且value为null
                                pro.put("type", apiResponseDescs[0].type().getValue());
                                pro.put("description", apiResponseDescs[0].description());
                            } else {
                                // 支持3层嵌套展示
                                for (ApiResponseDesc apiResponseDesc : apiResponseDescs) {// 1层
                                    JSONObject apiResponseJson = new JSONObject();
                                    apiResponseJson.put("type", apiResponseDesc.type().getValue());
                                    apiResponseJson.put("description", apiResponseDesc.description());
                                    if (apiResponseDesc.type() == ApiResponseType.ARRAY) {
                                        JSONObject descItems = new JSONObject();
                                        if (apiResponseDesc.items().length > 0) {
                                            if (apiResponseDesc.items().length == 1 && ValidateUtils.isBlank(apiResponseDesc.items()[0].value())) {// 判断是不是基本数据类型的数组,items数组长度为1，且value为空
                                                descItems.put("type", apiResponseDesc.items()[0].type().getValue());
                                            } else {
                                                JSONObject descItemPro = new JSONObject();
                                                for (ApiResponseDescItem apiResponseDescItem : apiResponseDesc.items()) {// 2层
                                                    JSONObject itemJsonO = new JSONObject();
                                                    itemJsonO.put("type", apiResponseDescItem.type().getValue());
                                                    itemJsonO.put("description", apiResponseDescItem.description());
                                                    if (apiResponseDescItem.type() == ApiResponseType.ARRAY) {
                                                        if (apiResponseDescItem.items().length > 0) {
                                                            JSONObject descItemItems = new JSONObject();
                                                            if (apiResponseDescItem.items().length == 1 && ValidateUtils.isBlank(apiResponseDescItem.items()[0].value())) {// 判断是不是基本数据类型的数组,items数组长度为1，且value为空
                                                                descItemItems.put("type", apiResponseDescItem.items()[0].type().getValue());
                                                            }else{
                                                                JSONObject descItemItemPro = new JSONObject();
                                                                for (ApiResponseDescItemItem apiResponseDescItemItem : apiResponseDescItem.items()) {//3层
                                                                    JSONObject itemItemJsonO = new JSONObject();
                                                                    itemItemJsonO.put("type", apiResponseDescItemItem.type().getValue());
                                                                    itemItemJsonO.put("description", apiResponseDescItemItem.description());
                                                                    descItemItemPro.put(apiResponseDescItemItem.value(), itemItemJsonO);
                                                                }
                                                                descItemItems.put("properties", descItemItemPro);
                                                            }
                                                            itemJsonO.put("items", descItemItems);
                                                        }
                                                    }
                                                    descItemPro.put(apiResponseDescItem.value(), itemJsonO);
                                                }
                                                descItems.put("properties", descItemPro);
                                            }
                                        }
                                        apiResponseJson.put("items", descItems);
                                    }
                                    jsonO.put(apiResponseDesc.value(), apiResponseJson);
                                }
                                pro.put("properties", jsonO);
                            }
                        }
                        // 从apiopreration上获取response的类型目前有三种（List,QueryResult,Object）
                        String reponseClassName = method.getAnnotation(ApiOperation.class).response().getSimpleName();
                        switch (reponseClassName) {
                            case "QueryResult":// 分页
                                JSONObject queryResult = json.getJSONObject("definitions").getJSONObject("QueryResult");
                                queryResult.getJSONObject("properties").getJSONObject("list").put("items", pro);
                                result.put("data", queryResult);
                                break;
                            case "List":// 数组
                                JSONObject listResult = new JSONObject(MapUtils.createMap("type", "array"));
                                listResult.put("items", pro);
                                result.put("data", listResult);
                                break;
                            case "Object":// 单个对象
                                result.put("data",pro);
                                break;
                            default:
                                break;
                        }
                    }
                    JSONObject response = new JSONObject();
                    JSONObject schema = new JSONObject();
                    schema.put("schema", new JSONObject(MapUtils.createMap("properties", result)));
                    response.put("200", schema);
                    jsonMethod.put("responses", response);
                }
            }
        }
    }

    /**
     * 功能:swagger api文档添加请求参数<br/>
     * @author xt
     * @param path
     * @param jsonMethod
     */
    private void addRequestParams(String path, JSONObject jsonMethod) {
        if(jsonMethod.getJSONArray("parameters") == null){
            jsonMethod.put("parameters", new JSONArray());
        }
        if (path.contains("/query")) {// 查询接口（包含query）添加参数，pageNo,pageSize,valid,page
            if (path.endsWith("/query")) {// 默认的query接口,增加参数keyword，orderSorts
                JSONObject keywordJson = new JSONObject();
                keywordJson.put("name", "keyword");
                keywordJson.put("type", "string");
                keywordJson.put("description", "关键字");
                keywordJson.put("in", "query");
                keywordJson.put("required", false);
                jsonMethod.getJSONArray("parameters").add(keywordJson);
                JSONObject orderSortsJson = new JSONObject();
                orderSortsJson.put("name", "orderSorts");
                orderSortsJson.put("type", "string");
                orderSortsJson.put("description", "排序");
                orderSortsJson.put("in", "query");
                orderSortsJson.put("default", "[{\"order\":\"utime\",\"sort\":\"desc\"}]");
                orderSortsJson.put("required", false);
                jsonMethod.getJSONArray("parameters").add(orderSortsJson);
            }
            JSONObject validJson = new JSONObject();
            validJson.put("name", "valid");
            validJson.put("type", "boolean");
            validJson.put("description", "有效");
            validJson.put("in", "query");
            validJson.put("default", "true");
            validJson.put("required", false);
            jsonMethod.getJSONArray("parameters").add(validJson);
            JSONObject pageNoJson = new JSONObject();
            pageNoJson.put("name", "pageNo");
            pageNoJson.put("type", "integer");
            pageNoJson.put("description", "页码");
            pageNoJson.put("in", "query");
            pageNoJson.put("default", "1");
            pageNoJson.put("required", false);
            jsonMethod.getJSONArray("parameters").add(pageNoJson);
            JSONObject pageSizeJson = new JSONObject();
            pageSizeJson.put("name", "pageSize");
            pageSizeJson.put("type", "integer");
            pageSizeJson.put("description", "页面大小");
            pageSizeJson.put("in", "query");
            pageSizeJson.put("default", "20");
            pageSizeJson.put("required", false);
            jsonMethod.getJSONArray("parameters").add(pageSizeJson);
        }
        JSONObject appJson = new JSONObject();
        appJson.put("name", "app");
        appJson.put("type", "boolean");
        appJson.put("description", "app访问");
        appJson.put("in", "query");
        appJson.put("default", "true");
        appJson.put("required", false);
        jsonMethod.getJSONArray("parameters").add(appJson);
    }

    private String hostName() {
        if ("DEFAULT".equals(hostNameOverride)) {
            URI uri = linkTo(Swagger2Controller.class).toUri();
            String host = uri.getHost();
            int port = uri.getPort();
            if (port > -1) {
                return String.format("%s:%d", host, port);
            }
            return host;
        }
        return hostNameOverride;
    }
}
