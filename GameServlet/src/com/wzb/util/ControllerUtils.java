package com.wzb.util;

import com.wzb.constant.Methods;

import net.sf.json.JSONObject;

public class ControllerUtils {
    /**
     * 解析请求的参数，返回对应的枚举项
     *
     * @param request  请求
     * @return Methods
     * @name getMethod
     */
    public static Methods getMethod(JSONObject requestData) {
        String m = requestData.getString("method");
        Methods method = null;
        try {
            method = Methods.valueOf(m);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("非法请求");
        }
        return method;
    }
}