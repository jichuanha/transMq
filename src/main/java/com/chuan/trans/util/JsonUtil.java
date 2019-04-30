package com.chuan.trans.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by zengzhangqiang on 4/24/15.
 */
public class JsonUtil {
    public static Gson gson;

    static {
        GsonBuilder gb = new GsonBuilder();
        //禁止html转义
        gb.disableHtmlEscaping();
        //处理下划线分割的json字符串（生成json字符串时候将驼峰转化为下划线，解析json时候将下划线转为驼峰）
        gb.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        //设置json时间格式
        gb.setDateFormat("yyyy-MM-dd HH:mm:ss");
        gson = gb.create();
    }

    public static <T> T parseJson(String jsonStr, Class<T> tClass){
        return gson.fromJson(jsonStr, tClass);
    }

    public static <T> T parseJson(String jsonStr, Type type){
        return gson.fromJson(jsonStr, type);
    }

    /**
     * 使用Gson生成json字符串
     * @param src
     * @return
     */
    public static String toJson(Object src){
        return gson.toJson(src);
    }
}

