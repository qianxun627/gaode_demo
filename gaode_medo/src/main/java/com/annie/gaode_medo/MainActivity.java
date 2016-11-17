package com.annie.gaode_medo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.NaviLatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private AMapNaviView aMapNaviView;
    private AMapNavi aMapNavi;
    ArrayList<NaviLatLng> list = new ArrayList<>();
    ArrayList<NaviLatLng> list1 = new ArrayList<>();
    // 起点终点坐标
    private NaviLatLng mNaviStart = new NaviLatLng(39.989614, 116.481763);
    private NaviLatLng mNaviEnd = new NaviLatLng(39.983456, 116.3154950);
    private MapView mapView;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*aMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        aMapNaviView.onCreate(savedInstanceState);
        aMapNaviView.setAMapNaviViewListener(this);

        aMapNavi = AMapNavi.getInstance(getApplicationContext());
        aMapNavi.addAMapNaviListener(this);
        list.add(mNaviStart);
        list1.add(mNaviEnd);*/

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //aMapNaviView.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //aMapNaviView.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*aMapNaviView.onDestroy();

        aMapNavi.stopNavi();
        aMapNavi.destroy();*/
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
        // aMapNaviView.onSaveInstanceState(outState);

    }
}
