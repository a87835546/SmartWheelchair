package com.roy.www.smartwheelchair.mvp.v.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolygonOptions;
import com.blankj.utilcode.util.ToastUtils;
import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.api.URL_Constant;
import com.roy.www.smartwheelchair.mvp.base.BaseActivity;
import com.roy.www.smartwheelchair.mvp.base.WheelchairApplication;
import com.roy.www.smartwheelchair.mvp.bean.FenceInfoBean;
import com.roy.www.smartwheelchair.mvp.bean.GeoFenceBean;
import com.roy.www.smartwheelchair.mvp.base.BaseContract;
import com.roy.www.smartwheelchair.mvp.p.GeoFencePresenter;
import com.roy.www.smartwheelchair.utils.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ??????????????????
 *
 * @author hongming.wang
 * @since 3.2.0
 */
public class GeoFenceRoundActivity extends BaseActivity<GeoFencePresenter>
implements
        BaseContract.View,
        GeoFenceListener,
        OnMapClickListener,
        LocationSource,
        AMapLocationListener,
        OnCheckedChangeListener {

    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.tv_top_titel)
    TextView tvTopTitel;
    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.iv_top_set)
    ImageView ivTopSet;
    /**
     * ???????????????????????????
     * <p>
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * </p>
     */
    private AMapLocationClient mlocationClient;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;


    private AMapLocation mAMapLocation;
    private AMap mAMap;

    // ???????????????
    private LatLng centerLatLng = null;
    private LatLng setLatLng = null;

    String customId = "1";
    String radiusStr = "10";
    FenceInfoBean mInfoBean;

    // ?????????marker
    private Marker centerMarker;
    private BitmapDescriptor ICON_YELLOW = BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
    private BitmapDescriptor ICON_RED = BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_RED);
    private MarkerOptions markerOption = null;
    private List<Marker> markerList = new ArrayList<Marker>();
    // ???????????????????????????????????????????????????????????????????????????
    private LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

    // ?????????????????????
    private GeoFenceClient fenceClient = null;
    // ????????????????????????
    private float fenceRadius = 0.0F;
    // ???????????????????????????????????????????????????
    private int activatesAction = GeoFenceClient.GEOFENCE_IN;
    // ?????????????????????action
    private static final String GEOFENCE_BROADCAST_ACTION = "com.example.geofence.round";

    // ?????????????????????????????????
    private HashMap<String, GeoFence> fenceMap = new HashMap<String, GeoFence>();


    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_geofence_new);
        ButterKnife.bind(this);
        tvTopTitel.setText(R.string.home_local);

        // TODO ??????????????????????????????

        // ?????????????????????
        fenceClient = new GeoFenceClient(getApplicationContext());

        mMapView.onCreate(savedInstanceState);
        markerOption = new MarkerOptions().draggable(true);
        initAMap();

        getGeoFenceInfo();
    }




    @Override
    protected GeoFencePresenter createPresenter() {
        return new GeoFencePresenter();
    }

    void initAMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.getUiSettings().setRotateGesturesEnabled(false);
            mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));
            setUpMap();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        registerReceiver(mGeoFenceReceiver, filter);

        /**
         * ??????pendingIntent
         */
        fenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        fenceClient.setGeoFenceListener(this);
        /**
         * ?????????????????????????????????,???????????????
         */
        fenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN);
    }

    /**
     * ????????????amap?????????
     */
    private void setUpMap() {
        mAMap.setOnMapClickListener(this);
        mAMap.setLocationSource(this);// ??????????????????
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// ????????????????????????????????????
        // ???????????????????????????
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // ???????????????????????????
        myLocationStyle.myLocationIcon(
                BitmapDescriptorFactory.fromResource(R.mipmap.gps_point));
        // ??????????????????????????????????????????
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // ??????????????????????????????????????????
        myLocationStyle.strokeWidth(0);
        // ???????????????????????????
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        // ??????????????? myLocationStyle ????????????????????????
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setMyLocationEnabled(true);// ?????????true??????????????????????????????????????????false??????????????????????????????????????????????????????false
        // ???????????????????????????????????? ??????????????????????????????????????????????????????????????????
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        try {
            unregisterReceiver(mGeoFenceReceiver);
        } catch (Throwable e) {
        }

        if (null != fenceClient) {
            fenceClient.removeGeoFence();
        }
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }




    private void drawFence(GeoFence fence) {
        switch (fence.getType()) {
            case GeoFence.TYPE_ROUND:
            case GeoFence.TYPE_AMAPPOI:
                drawCircle(fence);
                break;
            case GeoFence.TYPE_POLYGON:
            case GeoFence.TYPE_DISTRICT:
                drawPolygon(fence);
                break;
            default:
                break;
        }

        // // ????????????maker????????????????????????????????????
         LatLngBounds bounds = boundsBuilder.build();
         mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

        removeMarkers();
    }

    private void drawCircle(GeoFence fence) {
        LatLng center = new LatLng(fence.getCenter().getLatitude(),
                fence.getCenter().getLongitude());
        // ??????????????????
        mAMap.addCircle(new CircleOptions().center(center)
                .radius(fence.getRadius()).strokeColor(Const.STROKE_COLOR)
                .fillColor(Const.FILL_COLOR).strokeWidth(Const.STROKE_WIDTH));
        boundsBuilder.include(center);
    }

    private void drawPolygon(GeoFence fence) {
        final List<List<DPoint>> pointList = fence.getPointList();
        if (null == pointList || pointList.isEmpty()) {
            return;
        }
        for (List<DPoint> subList : pointList) {
            List<LatLng> lst = new ArrayList<LatLng>();

            PolygonOptions polygonOption = new PolygonOptions();
            for (DPoint point : subList) {
                lst.add(new LatLng(point.getLatitude(), point.getLongitude()));
                boundsBuilder.include(
                        new LatLng(point.getLatitude(), point.getLongitude()));
            }
            polygonOption.addAll(lst);

            polygonOption.strokeColor(Const.STROKE_COLOR)
                    .fillColor(Const.FILL_COLOR).strokeWidth(Const.STROKE_WIDTH);
            mAMap.addPolygon(polygonOption);
        }
    }

    Object lock = new Object();

    void drawFence2Map() {
        new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        if (null == fenceList || fenceList.isEmpty()) {
                            return;
                        }
                        for (GeoFence fence : fenceList) {
                            if (fenceMap.containsKey(fence.getFenceId())) {
                                continue;
                            }
                            drawFence(fence);
                            fenceMap.put(fence.getFenceId(), fence);
                        }
                    }
                } catch (Throwable e) {

                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    StringBuffer sb = new StringBuffer();
                    sb.append("??????????????????");
                    String customId = (String) msg.obj;
                    if (!TextUtils.isEmpty(customId)) {
                        sb.append("customId: ").append(customId);
                    }
                    Toast.makeText(getApplicationContext(), sb.toString(),
                            Toast.LENGTH_SHORT).show();
                    drawFence2Map();
                    break;
                case 1:
                    int errorCode = msg.arg1;
                    Toast.makeText(getApplicationContext(),
                            "?????????????????? " + errorCode, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    String statusStr = (String) msg.obj;
                    break;
                default:
                    break;
            }
        }
    };

    List<GeoFence> fenceList = new ArrayList<GeoFence>();

    @Override
    public void onGeoFenceCreateFinished(final List<GeoFence> geoFenceList,
                                         int errorCode, String customId) {
        Message msg = Message.obtain();
        if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
            fenceList.addAll(geoFenceList);
            drawFence2Map();
            msg.obj = customId;
            msg.what = 0;
        } else {
            msg.arg1 = errorCode;
            msg.what = 1;
        }
//        handler.sendMessage(msg);
    }

    /**
     * ??????????????????????????????,??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ?????????????????????????????????????????????,???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // ????????????
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                Bundle bundle = intent.getExtras();
                String customId = bundle
                        .getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                //status??????????????????????????????????????????????????????
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                StringBuffer sb = new StringBuffer();
                switch (status) {
                    case GeoFence.STATUS_LOCFAIL:
                        sb.append("????????????");
                        break;
                    case GeoFence.STATUS_IN:
                        sb.append("???????????? ");
                        break;
                    case GeoFence.STATUS_OUT:
                        sb.append("???????????? ");
                        break;
                    case GeoFence.STATUS_STAYED:
                        sb.append("?????????????????? ");
                        break;
                    default:
                        break;
                }
                if (status != GeoFence.STATUS_LOCFAIL) {
                    if (!TextUtils.isEmpty(customId)) {
                        sb.append(" customId: " + customId);
                    }
                    sb.append(" fenceId: " + fenceId);
                }
                String str = sb.toString();
                Message msg = Message.obtain();
                msg.obj = str;
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }
    };

    @Override
    public void onMapClick(LatLng latLng) {

        // TODO ??????????????????
//        LatLng l = new LatLng()


        markerOption.icon(ICON_YELLOW);
        centerLatLng = latLng;
        addCenterMarker(centerLatLng);

//        addFence();
    }


    /**
     * ??????????????????
     * @param latitude
     * @param longitude
     */
    private void showRoundGeoFece(double latitude,double longitude ){
        markerOption.icon(ICON_YELLOW);
        centerLatLng = new LatLng(latitude,longitude);
        addCenterMarker(centerLatLng);

        addFence();
    }



    /**
     * ???????????????????????????
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                Log.w("onLocationChanged", "????????????");
                mAMapLocation = amapLocation;
                // TODO 1:????????????--->??????????????????????????????????????????????????????????????????


                if (mInfoBean != null){
                    Log.w("onLocationChanged", "mInfoBean != null");
                    amapLocation.setLatitude(mInfoBean.getData().getLat());
                    amapLocation.setLongitude(mInfoBean.getData().getLng());
                }else {
                    Log.w("onLocationChanged", "mInfoBean == null");
                }


                mListener.onLocationChanged(amapLocation);// ?????????????????????
            } else {
                String errText = "????????????," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);

            }
        }
    }

    /**
     * ????????????
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // ??????????????????
            mlocationClient.setLocationListener(this);
            // ??????????????????????????????
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            // ????????????????????????????????????????????????????????????
            mLocationOption.setOnceLocation(true);
            // ??????????????????
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    /**
     * ????????????
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private void addCenterMarker(LatLng latlng) {
        if (null == centerMarker) {
            centerMarker = mAMap.addMarker(markerOption);
        }
        centerMarker.setPosition(latlng);
        markerList.add(centerMarker);
    }

    private void removeMarkers() {
        if (null != centerMarker) {
            centerMarker.remove();
            centerMarker = null;
        }
        if (null != markerList && markerList.size() > 0) {
            for (Marker marker : markerList) {
                marker.remove();
            }
            markerList.clear();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_alertIn:
                if (isChecked) {
                    activatesAction |= GeoFenceClient.GEOFENCE_IN;
                } else {
                    activatesAction = activatesAction
                            & (GeoFenceClient.GEOFENCE_OUT
                            | GeoFenceClient.GEOFENCE_STAYED);
                }
                break;
            case R.id.cb_alertOut:
                if (isChecked) {
                    activatesAction |= GeoFenceClient.GEOFENCE_OUT;
                } else {
                    activatesAction = activatesAction
                            & (GeoFenceClient.GEOFENCE_IN
                            | GeoFenceClient.GEOFENCE_STAYED);
                }
                break;
            case R.id.cb_alertStated:
                if (isChecked) {
                    activatesAction |= GeoFenceClient.GEOFENCE_STAYED;
                } else {
                    activatesAction = activatesAction
                            & (GeoFenceClient.GEOFENCE_IN
                            | GeoFenceClient.GEOFENCE_OUT);
                }
                break;
            default:
                break;
        }
        if (null != fenceClient) {
            fenceClient.setActivateAction(activatesAction);
        }
    }


    /**
     * ????????????
     *
     * @author hongming.wang
     * @since 3.2.0
     */
    private void addFence() {

        addRoundFence();
    }

    /**
     * ??????????????????
     *
     * @author hongming.wang
     * @since 3.2.0
     */
    private void addRoundFence() {
        if (null == centerLatLng
                || TextUtils.isEmpty(radiusStr)) {
            Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        DPoint centerPoint = new DPoint(centerLatLng.latitude,
                centerLatLng.longitude);
        fenceRadius = Float.parseFloat(radiusStr);
        fenceClient.addGeoFence(centerPoint, fenceRadius, customId);
    }

    @OnClick({R.id.iv_top_back, R.id.iv_top_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_back:
                finish();
                break;
            case R.id.iv_top_set:

                setFenceConfig(centerLatLng);
                break;
        }
    }

    private void setFenceConfig(LatLng latLng) {
        setLatLng = latLng;
        // TODO ??????????????????
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", WheelchairApplication.mmkv.decodeString("DEVICE_ID"));
        map.put("fenceLng", latLng.longitude);
        map.put("fenceLat", latLng.latitude);
        map.put("radius", 100);
        mPresenter.doPost(URL_Constant.SAVE_GEO_FENCE_INFO_URL,
                WheelchairApplication.mmkv.decodeString("token"),
                map,GeoFenceBean.class);
    }

    private void getGeoFenceInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", WheelchairApplication.mmkv.decodeString("DEVICE_ID"));
        mPresenter.doPost(URL_Constant.GET_DEVICE_FENCE_INFO_URL,
                WheelchairApplication.mmkv.decodeString("token"),
                map,FenceInfoBean.class);
    }

    @Override
    public void showArticleSuccess(Object o) {

        Log.w("showArticleSuccess : ","===");
        if (o instanceof GeoFenceBean){
            GeoFenceBean bean = (GeoFenceBean) o;
            if (bean != null){
                Log.w("GeoFenceBean : ","======");
                if (bean.getStatusCode() == 200){
                    ToastUtils.showLong(bean.getData());

                    showRoundGeoFece(setLatLng.latitude,setLatLng.longitude);

                }else {
                    ToastUtils.showLong(bean.getMessage());
                }
            }
        }

        if (o instanceof FenceInfoBean){
            FenceInfoBean infoBean = (FenceInfoBean) o;
            if (infoBean != null){
                Log.w("GeoFenceBean : ","======");
                if (infoBean.getStatusCode() == 200){
                    mInfoBean = infoBean;
                    fenceRadius = (float) mInfoBean.getData().getRadius();
                    showRoundGeoFece(mInfoBean.getData().getFenceLat(),mInfoBean.getData().getFenceLng());

                }else {
                    ToastUtils.showLong(infoBean.getMessage());
                }
            }
        }


    }

    @Override
    public void showArticleFail(String errorMsg) {
        ToastUtils.showLong(errorMsg);
    }
}
