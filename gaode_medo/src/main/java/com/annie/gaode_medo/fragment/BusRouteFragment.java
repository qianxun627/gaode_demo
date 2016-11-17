package com.annie.gaode_medo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.route.BusRouteResult;
import com.annie.gaode_medo.R;
import com.annie.gaode_medo.adapter.BusRouteListAdapter;

/**
 * Created by Administrator on 2016/10/24.
 */
public class BusRouteFragment extends Fragment {

    private MapView mapView;
    private ListView listView;
    private AMap aMap;
    private BusRouteResult busRouteResult;  //公交路线规划结果
    private LatLng startPoint; //路线起点
    private LatLng endPoint; //路线终点

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_bus,null);

        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        listView = (ListView) view.findViewById(R.id.listview);

        //为listview设置adapter展示数据
        BusRouteListAdapter busRouteListAdapter = new BusRouteListAdapter(getActivity(),busRouteResult);
        listView.setAdapter(busRouteListAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    //初始化操作
    public void init() {

    }

    //获取上个activity界面传递的数据
    public void setData(BusRouteResult result, LatLng start, LatLng end) {
        this.busRouteResult = result;
        this.startPoint = start;
        this.endPoint = end;
    }

}
