package com.qrpay.demo.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义响应结构
 */
public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 将json结果集转化为对象
     * 
     * @param jsonData json数据
     * @param clazz 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    /**
     * 简要说明:可解析对象中包含列表的对象json <br>
     * 详细说明:TODO
     * 创建者：lcwen
     * 创建时间:2019年11月1日 下午2:52:05
     * 更新者:
     * 更新时间:
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList2(String jsonData, Class<T> beanType) {    	
    	try {
    		return JSONArray.parseArray(jsonData,beanType);
		} catch (Exception e) {
			e.printStackTrace();
		}    	
    	return null;
    }
    
    public static String objectToJson2(Object data) {
    	try {
			return JSONObject.toJSONString(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static String objectToJson3(Object data) {
    	try {
			return JSONArray.toJSONString(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static String objectToJson4(Object data) {
    	try {    		
			return JSONArray.toJSONString(JSONArray.toJSON(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    

}
