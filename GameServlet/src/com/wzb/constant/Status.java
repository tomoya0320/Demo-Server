package com.wzb.constant;

public enum Status {
    /**
     * 重复注册
     */
    ACCOUNT_ALREADY_EXIST,
    /**
     * 数据不合法
     */
    DATA_ILLEGAL,
    /**
     * 输入次数过多
     */
    INPUT_TOO_MUCH,

    /**
     * 密码错误
     */
    PASSWORD_INCORRECT,

    /**
     *权限拒绝
     */
    PERMISSION_DENY,


    /**
     * 没有查询结果
     */
    NO_RESULT,
    /**
     * 服务成功
     */
    SUCCESS,
    /**
     * 服务错误
     */
    ERROR;
}
