package com.roy.www.alirtc.network;


import android.util.Log;


import com.roy.www.alirtc.bean.AliAuthInfoBean;
import com.roy.www.alirtc.bean.RTCAuthInfo;
import com.roy.www.alirtc.bean.RTCMeetingInfo;
import com.roy.www.alirtc.utils.AliRtcConstants;
import com.roy.www.alirtc.utils.ParserJsonUtils;
import com.roy.www.alirtc.utils.ThreadUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * alirtc服务器返回的包含加入频道信息的业务类
 */
public class RequestRTCAuthInfo {
    public static final String TAG = RequestRTCAuthInfo.class.getSimpleName();




    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient mClient = initClient();
    private static final String TOKEN = "4e272f6b-a637-e453-196c-39fe2cda97ed";
    private static final String DEVICE_ID = "1";

    private static OkHttpClient initClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        client.connectTimeoutMillis();
        return client;
    }

    /**
     * 发送请求
     *
     * @param client
     * @param request
     * @param respon
     */
    private static void sendRequest(OkHttpClient client, Request request, final HttpResponse respon) {
        // 发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
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

    public static void doPost(String url,String token, Map<String, Object> map, HttpResponse respon) {

        new Thread(new Runnable() {
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
        }).start();

    }


    public static void getAuthInfo(final String userName, final String channelId,OnRequestAuthInfoListener onRequestAuthInfoListener){
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", userName);
        hashMap.put("room", channelId);
        String baseUrl = AliRtcConstants.SERVER_URL;
        doPost(baseUrl, TOKEN, hashMap, new HttpResponse(AliAuthInfoBean.class) {
            @Override
            public void onSuccess(Object o) {
                AliAuthInfoBean infoBean = (AliAuthInfoBean) o;
                if (infoBean.getStatusCode() == 200){
                    final RTCAuthInfo rtcAuthInfo = new RTCAuthInfo();
                    rtcAuthInfo.setData(infoBean.getData());
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (rtcAuthInfo != null && onRequestAuthInfoListener != null) {
                                onRequestAuthInfoListener.onObtainAuthInfo(rtcAuthInfo);
                            }
                        }
                    });
                }else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ( onRequestAuthInfoListener != null) {
                                onRequestAuthInfoListener.onFailure(infoBean.getMessage());
                            }
                        }
                    });

                }

            }

            @Override
            public void onError(String msg) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( onRequestAuthInfoListener != null) {
                            onRequestAuthInfoListener.onFailure(msg);
                        }
                    }
                });
            }
        });
    }




    public static void getAuthInfo(final String token, final Map<String, Object> map,OnRequestAuthInfoListener onRequestAuthInfoListener){
        String baseUrl = AliRtcConstants.SERVER_URL;
        doPost(baseUrl, token, map, new HttpResponse(AliAuthInfoBean.class) {
            @Override
            public void onSuccess(Object o) {
                AliAuthInfoBean infoBean = (AliAuthInfoBean) o;
                if (infoBean.getStatusCode() == 200){
                    final RTCAuthInfo rtcAuthInfo = new RTCAuthInfo();
                    rtcAuthInfo.setData(infoBean.getData());
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (rtcAuthInfo != null && onRequestAuthInfoListener != null) {
                                onRequestAuthInfoListener.onObtainAuthInfo(rtcAuthInfo);
                            }
                        }
                    });
                }else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ( onRequestAuthInfoListener != null) {
                                onRequestAuthInfoListener.onFailure(infoBean.getMessage());
                            }
                        }
                    });

                }

            }

            @Override
            public void onError(String msg) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( onRequestAuthInfoListener != null) {
                            onRequestAuthInfoListener.onFailure(msg);
                        }
                    }
                });
            }
        });
    }














//    public static void getAuthInfo(final String userName, final String channelId,  final OnRequestAuthInfoListener onRequestAuthInfoListener) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("user", userName);
//        hashMap.put("room", channelId);
//        hashMap.put("passwd", "12345678");
//        String base = AliRtcConstants.SERVER_URL;
//        String url = getQueryUrl(base, hashMap);
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        okhttp3.Call call = client.newCall(request);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
//                final RTCAuthInfo rtcAuthInfo = ParserJsonUtils.parserLoginJson(response.body().string());
//                ThreadUtils.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (rtcAuthInfo != null && onRequestAuthInfoListener != null) {
//                            onRequestAuthInfoListener.onObtainAuthInfo(rtcAuthInfo);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
//                ThreadUtils.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (onRequestAuthInfoListener != null) {
//                            onRequestAuthInfoListener.onFailure("请求失败");
//                        }
//                    }
//                });
//            }
//        });
//
//    }

    public static void getMeetingInfo(final String userName, final OnRequestMeetingListener onRequestMeetingListener) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", userName);

        String base = AliRtcConstants.CHANNEL_ID_URL;
        String url = getQueryUrl(base, hashMap);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        okhttp3.Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                final RTCMeetingInfo rtcMeetingInfo = ParserJsonUtils.parserMeetingJson(response.body().string());
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rtcMeetingInfo != null && onRequestMeetingListener != null) {
                            onRequestMeetingListener.onObtainMeetingInfo(rtcMeetingInfo);
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (onRequestMeetingListener != null) {
                            onRequestMeetingListener.onFailure("请求失败");
                        }
                    }
                });
            }
        });

    }



    public interface OnRequestMeetingListener {
        void onObtainMeetingInfo(RTCMeetingInfo rtcMeetingInfo);

        void onFailure(String failure);
    }

    private OnRequestMeetingListener onRequestMeetingListener;

    public void setOnRequestMeetingListener(OnRequestAuthInfoListener onRequestAuthInfoListener) {
        this.mOnRequestAuthInfoListener = onRequestAuthInfoListener;
    }


    public interface OnRequestAuthInfoListener {
        void onObtainAuthInfo(RTCAuthInfo rtcAuthInfo);

        void onFailure(String failure);
    }
    private OnRequestAuthInfoListener mOnRequestAuthInfoListener;

    public void setOnRequestAuthInfoListener(OnRequestAuthInfoListener onRequestAuthInfoListener) {
        this.mOnRequestAuthInfoListener = onRequestAuthInfoListener;
    }

    /**
     * 拼接get数据
     *
     * @param url    地址
     * @param params get参数
     * @return
     */
    private static String getQueryUrl(String url, Map<String, String> params) {
        StringBuilder neoUrl = new StringBuilder(url);
        if (params != null) {
            neoUrl.append("?");
            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                neoUrl.append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("&");
            }
            neoUrl = new StringBuilder(neoUrl.substring(0, neoUrl.length() - 1));
        }
        Log.i(TAG, "getQueryUrl: " + neoUrl.toString());
        return neoUrl.toString();
    }
}
