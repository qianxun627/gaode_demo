package com.annie.gaode_medo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.annie.gaode_medo.adapter.BusRouteDetailAdapter;
import com.annie.gaode_medo.util.AMapUtil;

public class RouteDetailActivity extends AppCompatActivity implements AMap.OnMapClickListener,View.OnClickListener {

    private MapView mapView;
    private BusPath busPath;
    private BusRouteResult busRouteResult;
    private AMap aMap;
    private TextView routeTitle;
    private TextView tv_map;
    private TextView firstLineInfo;
    private TextView secondLineInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //隐藏actionBar
        setContentView(R.layout.activity_route_detail);

        getIntentData();
        //初始化数据
        init(savedInstanceState);
    }

    //初始化数据
    public void init(Bundle savedInstanceState){
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        if (aMap ==null) {
            aMap = mapView.getMap();
        }

        //设置监听
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.tv_map).setOnClickListener(this);

        routeTitle = (TextView) findViewById(R.id.routeTitle);
        tv_map = (TextView) findViewById(R.id.tv_map);
        firstLineInfo = (TextView) findViewById(R.id.first_line_info);
        secondLineInfo = (TextView) findViewById(R.id.second_line_info);

        routeTitle.setText("公交线路详情");
        String dur = AMapUtil.getFriendlyTime((int) busPath.getDuration());  //返回路程所需时间
        String dis = AMapUtil.getFriendlyLength((int) busPath.getDistance());  //返回路程的距离
        firstLineInfo.setText(dur + "(" + dis + ")");
        int taxiCost = (int) busRouteResult.getTaxiCost();
        secondLineInfo.setText("打车约"+taxiCost+"元");

        // listview的处理
        ListView listview = (ListView) findViewById(R.id.listview);
        BusRouteDetailAdapter busRouteDetailAdapter = new BusRouteDetailAdapter(this.getApplicationContext(),busPath.getSteps());
        listview.setAdapter(busRouteDetailAdapter);

    }

    //获取intent传递过来的数据
    public void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            busPath = intent.getParcelableExtra("bus_path");
            busRouteResult = intent.getParcelableExtra("bus_result");
        }
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
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // 地图的触摸事件
    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_map:
                tv_mapListener();
                break;
            case R.id.back:
                backListener();
                break;
        }
    }

    private void backListener() {
        this.finish(); //销毁当前activity
    }

    private void tv_mapListener() {

    }
}
