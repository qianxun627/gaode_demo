package com.annie.gaode_medo;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;

/**
 * 自动进行定位,并打印位置信息
 * 将定位得到的经纬度转换成屏幕坐标
 * 点击地图上的点获取经纬度, 并转换成屏幕坐标
 */

public class GetLocationDataActivity extends AppCompatActivity implements AMapLocationListener,AMap.OnMapClickListener{

    public AMapLocationClient locationClient = null;
    public AMapLocationClientOption locationOption = null;
    private CoordinateConverter converter;
    private LatLng latLng;
    private LatLng convert;
    private MapView mapView;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location_data);

        //显示地图
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            // 给地图设置点击事件
            aMap.setOnMapClickListener(this);
        }

        //定位设置
        locationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位参数,用来设置发起定位的模式和相关参数。
        locationOption = new AMapLocationClientOption();
        //设置定位监听
        locationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(9000);
        locationOption.setLocationCacheEnable(false);
        locationOption.setGpsFirst(true);
        //强定位参数设置给locationClient对象
        locationClient.setLocationOption(locationOption);
        //启动定位
        locationClient.startLocation();

        //将获取的经纬度转换为屏幕坐标
        converter = new CoordinateConverter();
        //设置带转换的坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);

        //根据关键字进行搜索



    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stopLocation();
        locationClient.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                int locationType = aMapLocation.getLocationType();//获取当前定位结果来源,详见定位类型表,1:gps;5:wifi:6"基站
                double latitude = aMapLocation.getLatitude();//获取纬度
                double latitude1 = aMapLocation.getLongitude();//获取经度
                float accuracy = aMapLocation.getAccuracy();//获取精度信息
                String address = aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                String country = aMapLocation.getCountry();//国家信息
                String province = aMapLocation.getProvince();//省信息
                String city = aMapLocation.getCity();//城市
                String cityCode = aMapLocation.getCityCode();//城市编码
                String district = aMapLocation.getDistrict();//城区信息

                //将经纬度转换成屏幕上的点的坐标只需要两行代码
                //转换点对象
                latLng = new LatLng(latitude,latitude1);
                //转换坐标点
                //converter.coord(latLng);
                //执行转换操作,转换为高德坐标体系
                //convert = converter.convert();
                //System.out.println("转换后的坐标为: "+ convert.toString());
                //将点转化为屏幕坐标
                Point point = aMap.getProjection().toScreenLocation(latLng);
                System.out.println("X坐标: " + String.valueOf(point.x) +" Y坐标: "+ String.valueOf(point.y));

                System.out.println(locationType+" - "+ latitude +" - "+ latitude1 + " - "+ accuracy +" - "+ address);
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                System.out.println("AmapError,location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        // 点击地图上的点获取经纬度信息
        System.out.println(String.valueOf("纬度: "+ latLng.latitude));
        System.out.println(String.valueOf("经度: "+ latLng.longitude));

        // 将获得的经纬度转换成屏幕坐标
        LatLng latLng1 = new LatLng(latLng.latitude,latLng.longitude);
        Point point1 = aMap.getProjection().toScreenLocation(latLng1);
        System.out.println("点击获取的X坐标: "+ point1.x +"点击获取的y坐标: "+ point1.y);

    }
}
