package com.roy.www.smartwheelchair.api;

import android.util.Log;


import com.roy.www.smartwheelchair.utils.EmptyUtils;
import com.roy.www.smartwheelchair.utils.GsonUtil;

import com.roy.www.smartwheelchair.utils.ThreadPool;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 李杨
 * On 2021/6/25
 * Email: 631934797@qq.com
 * Description:
 */

public class HttpUtil {

    private static final String TAG = HttpUtil.class.getSimpleName();

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static HttpUtil httpUtil;
    private OkHttpClient mClient;
    private static final String TOKEN = "fad5dc58-cd9c-49de-b5f9-031889d8c55d:0";
    private static final String DEVICE_ID = "1";



    //私有化构造方法
    private HttpUtil() {
        mClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        mClient.connectTimeoutMillis();
    }


    /**
     * 创建HttpUtil请求对象
     */
    public static synchronized HttpUtil getInstance() {
        if (httpUtil == null) {
            synchronized (HttpUtil.class) {
                if (httpUtil == null) {
                    httpUtil = new HttpUtil();
                }
            }
        }
        return httpUtil;
    }

    /**
     * 发送请求
     *
     * @param client
     * @param request
     * @param respon
     */
    private void sendRequest(OkHttpClient client, Request request, final HttpResponse respon) {
        // 发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("HttpUtil", "onFailure : " + e.getMessage());
                respon.onError("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("HttpUtil", "onResponse : 请求失败");
                    respon.onError("请求失败");
                    return;
                }
                String json = response.body().string();
                Log.i("HttpUtil", json);
                //操作解析
                respon.parse(json);
            }
        });
    }



    /**
     * POST提交json数据
     *
     * @param url
     * @param map
     * @param respon
     */

    public void doPost(String url,String token, Map<String, Object> map, HttpResponse respon) {
        ThreadPool.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                Log.w(TAG, "Json: "+GsonUtil.ObjectToString(map));
                //创建RequestBody对象
                RequestBody body = RequestBody.create(JSON, GsonUtil.ObjectToString(map));
                //创建一个Request对象

                if (token != null){
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("SYSTOKEN",token)
                            .post(body)
                            .build();
                    sendRequest(mClient, request, respon);
                }else {

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    sendRequest(mClient, request, respon);
                }

            }
        });

    }


    /**
     * POST提交json数据
     *
     * @param url
     * @param map
     * @param respon
     */
    public void doGet(final String url, String token,Map<String, Object> map, HttpResponse respon) {
        ThreadPool.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                Log.w(TAG, "Json: "+GsonUtil.ObjectToString(map));
                String URL = getQueryUrl(url,map);
                if (token != null){
                    //创建一个Request对象
                    Request request = new Request.Builder()
                            .url(URL)
                            .addHeader("SYSTOKEN",token)
                            .get()
                            .build();
                    sendRequest(mClient, request, respon);
                }else {
                    //创建一个Request对象
                    Request request = new Request.Builder()
                            .url(URL)
                            .get()
                            .build();
                    sendRequest(mClient, request, respon);
                }

            }
        });

    }

    /**
     * 拼接get数据
     *
     * @param url    地址
     * @param params get参数
     * @return
     */
    private static String getQueryUrl(String url, Map<String, Object> params) {
        StringBuilder neoUrl = new StringBuilder(url);
        if (params != null) {
            neoUrl.append("?");
            for (Map.Entry<String, Object> stringStringEntry : params.entrySet()) {
                neoUrl.append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("&");
            }
            neoUrl = new StringBuilder(neoUrl.substring(0, neoUrl.length() - 1));
        }
        Log.i(TAG, "getQueryUrl: " + neoUrl.toString());
        return neoUrl.toString();
    }


    private String getParams(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer("?");
        if (EmptyUtils.isEmpty(params)) {
            for (Map.Entry<String, Object> item : params.entrySet()) {
                Object value = item.getValue();
                if (EmptyUtils.isNotEmpty(value)) {
                    sb.append("&");
                    sb.append(item.getKey());
                    sb.append("=");
                    sb.append(value);
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }





    //==============================================================================================





    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param respon
     */
    public void doPostLogin(String userName, String password, HttpResponse respon) {
        ThreadPool.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {

                //创建RequestBody对象
                RequestBody formBody = new FormBody.Builder()

                        .add("userName", userName)
                        .add("passWord", password)
                        .build();
                //创建一个Request对象
                Request request = new Request.Builder()
                        .url(URL_Constant.AUTH_LOGIN_URL)
                        .post(formBody)
                        .build();
                sendRequest(mClient, request, respon);
            }
        });

    }

    /**
     * 退出
     *
     * @param respon
     */
    public void doGetLogout(HttpResponse respon) {
        ThreadPool.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                //创建一个Request对象
                Request request = new Request.Builder()
                        .url(URL_Constant.AUTH_LOGOUT_URL)
                        .get()
                        .build();
                sendRequest(mClient, request, respon);
            }
        });

    }

    /**
     * 用户注册
     *
     * @param userName
     * @param password
     * @param vCode
     * @param respon
     */
    public void doPostRegister(String userName, String password, String vCode, HttpResponse respon) {
        ThreadPool.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                //创建RequestBody对象
                RequestBody formBody = new FormBody.Builder()
                        .add("userName", userName)
                        .add("passWord", password)
                        .add("vCode", vCode)
                        .build();
                //创建一个Request对象
                Request request = new Request.Builder()
                        .url(URL_Constant.AUTH_REGISTER_URL)
                        .post(formBody)
                        .build();
                sendRequest(mClient, request, respon);
            }
        });

    }

    /**
     * 发送短息验证码
     *
     * @param phoneNumber
     * @param respon
     */
    public void doPostSendMobileCode(String phoneNumber, HttpResponse respon) {
        ThreadPool.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                //创建RequestBody对象
                RequestBody formBody = new FormBody.Builder()
                        .add("mobile", phoneNumber)
                        .build();
                //创建一个Request对象
                Request request = new Request.Builder()
                        .url(URL_Constant.AUTH_SEND_MOBILE_CODE_URL)
                        .post(formBody)
                        .build();
                sendRequest(mClient, request, respon);
            }
        });
    }

    /**
     * 重置密码
     *
     * @param respon
     */
    public void doPostResetPassword(String phoneNumber, String password, String vCode, HttpResponse respon) {
        ThreadPool.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                //创建RequestBody对象
                RequestBody formBody = new FormBody.Builder()
                        .add("mobile", phoneNumber)
                        .add("passWord", password)
                        .add("vCode", vCode)
                        .build();
                //创建一个Request对象
                Request request = new Request.Builder()
                        .url(URL_Constant.AUTH_RESET_PWD_URL)
                        .post(formBody)
                        .build();
                sendRequest(mClient, request, respon);
            }
        });
    }





}