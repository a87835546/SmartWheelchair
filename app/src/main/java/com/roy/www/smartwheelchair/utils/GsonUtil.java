package com.roy.www.smartwheelchair.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * @author 李杨
 *
 * @time 2016/6/24
 *
 * @desc Gson工具类
 *
 */
public class GsonUtil {

    /**
     *
     * @param json      传入json字符串,进行解析
     * @param tClass    解析成对应的JavaBean
     * @param <T>       泛指的类型
     * @return
     */
    public static <T> T parse(String json, Class<T> tClass){
        try {

            if(TextUtils.isEmpty(json)) {
                return null;
            }

            Gson gson = new Gson();

            T tmp = gson.fromJson(json, tClass);
            return tmp;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将Object装换成String类型
     * @param obj  要转换的对象
     * @return     返回一个String
     */
    public static String ObjectToString(Object obj) {

        Gson gson = new Gson();
        return gson.toJson(obj);
    }


    /**
     * ================================截取Json字符串中指定字段并返回值================================
     *
     * @param jsonBack   收到的JSON指令
     * @param fields     要截取的字段值
     * @return
     */
    public static String acceptAndStopField(String jsonBack, String fields) {

        if (jsonBack == null || "".equals(jsonBack.trim()))
            throw new IllegalArgumentException("JsonBack is illegal, please check args ... ");

        JsonParser jsonParser = new JsonParser();

        JsonObject json = (JsonObject) jsonParser.parse(jsonBack);

        if (json == null)
            throw new JsonParseException("Json Parse error, please check args ... ");

        return json.getAsJsonPrimitive(fields).getAsString();

    }

}
