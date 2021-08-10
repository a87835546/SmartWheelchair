package com.roy.www.smartwheelchair.api;

/**
 * Created by 李杨
 * On 2021/6/21
 * Email: 631934797@qq.com
 * Description:
 */
public class URL_Constant {

    public static final String BASE_VIDEO_URL= "rtmp://47.97.212.252:1935/live/";



    public static final String BASE_URL= "http://znly.api.bangzhong.cc";


    //========================================AUTH========================================

    // 用户登录
    public static final String AUTH_LOGIN_URL = BASE_URL+ "/api/Auth/Login";

    // 用户退出
    public static final String AUTH_LOGOUT_URL = BASE_URL+ "/api/Auth/LogOut";

    // 用户注册
    public static final String AUTH_REGISTER_URL = BASE_URL+ "/api/Auth/Register";

    // 发送验证码
    public static final String AUTH_SEND_MOBILE_CODE_URL = BASE_URL+ "/api/Auth/SendRegisterCode";

    // 重置密码
    public static final String AUTH_RESET_PWD_URL = BASE_URL+ "/api/Auth/ResetPassword";

    // 获取用户信息
    public static final String AUTH_GET_USER_INFO_URL = BASE_URL+ "/api/Auth/GetUserInfo";

    // 返回上传图片的url集合
    public static final String UPLOAD_FILE_URL= BASE_URL+ "/api/Upload/UploadFile";



    //========================================USER========================================


    // 保存电子围栏信息
    public static final String GET_DEVICE_FENCE_INFO_URL = BASE_URL+ "/api/SmartBox/GetSmartBoxByDeviceID";

    // 保存电子围栏信息
    public static final String SAVE_GEO_FENCE_INFO_URL = BASE_URL+ "/api/SmartBox/SaveSmartBoxFence";

    // 获取用户信息
    public static final String GET_USER_INFO_URL = BASE_URL+ "/api/Auth/GetUserInfo";



    //========================================USER========================================


    // 查询列表
    public static final String USER_QUERY_URL = BASE_URL+ "/api/User/Query";

    // 启用
    public static final String USER_ENABLE_URL = BASE_URL+ "/api/User/Enable";

    // 禁用
    public static final String USER_DISABLE_URL = BASE_URL+ "/api/User/Disable";

    // 根据ID查询单条数据
    public static final String USER_GET_URL = BASE_URL+ "/api/User/Get";

    // 保存用户
    public static final String USER_SAVE_URL = BASE_URL+ "/api/User/Save";

    // 软删除用户
    public static final String USER_SOFTDELETE_URL = BASE_URL+ "/api/User/SoftDelete";

    // GGA定位数据坐标提取及转换
    public static final String TOOL_GGACONVERT_URL= BASE_URL+ "/api/Tool/GGAConvert";




}
