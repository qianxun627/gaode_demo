package com.annie.gaode_medo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.annie.gaode_medo.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 关键字搜索
 */

public class KeywordSearchActivity extends FragmentActivity implements
        AMap.OnMapClickListener,PoiSearch.OnPoiSearchListener,View.OnClickListener,AMapLocationListener
        ,AMap.InfoWindowAdapter,AMap.OnMarkerClickListener,RouteSearch.OnRouteSearchListener{

    private AMap amap;
    private EditText editText;
    private Button searchBtn;
    public AMapLocationClient locationClient = null;
    public AMapLocationClientOption locationOption = null;
    private String cityCode;
    private int locationTime; //定位的间隔时间
    private ProgressDialog progressDialog; //搜索进度条
    private String keyword;private PoiSearch poiSearch;
    private PoiResult poiResult; // poi返回的结果
    private PoiSearch.Query query;
    private int currentPage; //当前页面
    private double latitude;
    private double longitude;
    private LatLng endPoint; // 路径终点
    private LatLng startPoint; //路径起点
    private LatLonPoint mEndPoint;
    private LatLonPoint mStartPoint;
    private final int ROUTE_TYPE_BUS = 1;
    private RouteSearch routeSearch;
    private BusRouteResult busRouteResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_search);

        editText = (EditText) findViewById(R.id.search);
        editText.setCursorVisible(false); //设置隐藏editText编辑框的光标
        searchBtn = (Button) findViewById(R.id.button);

        //初始化AMap对象,显示地图
        init();
    }

    //初始化AMap对象,显示地图, 界面相关设置
    private void init() {
        if (amap == null) {
            amap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            //定位设置
            locationClient = new AMapLocationClient(getApplicationContext());
            //初始化定位参数,用来设置发起定位的模式和相关参数。
            locationOption = new AMapLocationClientOption();
            //设置定位监听
            locationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            locationOption.setInterval(1000);
            //locationOption.setLocationCacheEnable(false);
            //locationOption.setGpsFirst(true);
            //强定位参数设置给locationClient对象
            locationClient.setLocationOption(locationOption);
            //启动定位
            locationClient.startLocation();

            //为界面各个控件设置监听
            setListener();
        }

        //路径规划相关
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
    }

    //设置路径起点和终点 (在marker的点击事件中别调用)
    private void setFromAndToMarker() {
        amap.addMarker(new MarkerOptions()
                       .position(startPoint)
                       .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start)));
        amap.addMarker(new MarkerOptions()
                       .position(endPoint)
                       .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)));
    }

    //为界面各个控件设置监听
    private void setListener() {
        //搜索框的监听事件
        editText.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        //地图的监听事件
        amap.setOnMapClickListener(this);
        //添加marker的监听事件
        amap.setOnMarkerClickListener(this);
        //添加infoWindow的监听事件
        amap.setInfoWindowAdapter(this);
    }

    //地图监听
    @Override
    public void onMapClick(LatLng latLng) {
        // 点击地图上的点获取经纬度信息
        System.out.println(String.valueOf("纬度: "+ latLng.latitude));
        System.out.println(String.valueOf("经度: "+ latLng.longitude));
    }

    //根据关键字进行搜索
    private void searchQuery() {
        showProgressDialog(); //显示进度框
        currentPage = 0;
        //参1: 搜索的字符串  参2: POI搜索类型(1/2 选填其一就可)   参3: 搜索区域(城市编码/名称, ""表示在全国范围内搜)
        query = new PoiSearch.Query(keyword,"",cityCode);
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);
        //query.setCityLimit(true); //设置搜索城市限制

        // 构造poiSearch对象, 并设置监听
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);

        //发送搜索请求
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult Result, int rCode) {
        //隐藏进度框
        dismissProgressDialog();
        if (rCode == 1000) {
            if (Result != null && Result.getQuery() !=null) {
                if (Result.getQuery().equals(query)) { //判断是否是同一条搜索信息
                    poiResult = Result;
                    ArrayList<PoiItem> poiItems = poiResult.getPois(); //获取到PoiItem列表
                    ArrayList<SuggestionCity> suggestionCities = (ArrayList<SuggestionCity>) poiResult.getSearchSuggestionCitys();

                    if (poiItems != null && poiItems.size() > 0) {
                        //通过listview来显示搜索结果
                      /*  MyAdapter myAdapter = new MyAdapter(poiItems,this);
                        listView.setAdapter(myAdapter);*/
                        amap.clear();  //清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(amap,poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

                    }else if (suggestionCities != null && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    }else {
                        Toast.makeText(this,"对不起,没有搜索到相关的数据",Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(this,"对不起,没有搜索到相关数据",Toast.LENGTH_SHORT).show();
            }

        }else {
            ToastUtil.showerror(this,rCode);
        }


    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    //poi没有搜索到数据，返回一些推荐城市的信息
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(this,infomation,Toast.LENGTH_SHORT).show();
    }

    //显示进度框
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //进度条样式设为圆形
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false); //设置ProgressDialog 是否可以按返回键取消；
        progressDialog.setMessage("正在搜索:\n" + keyword);
        progressDialog.show();
    }

    //隐藏进度框
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    //点点击搜索框的时候跳转到搜索界面
    @Override
    public void onClick(View view) {

        //在这可以设置一下,如果当前没有定位成功不让跳转或延时跳转
        Intent intent = new Intent(this,SearchListActivity.class);
        intent.putExtra("citycode",cityCode);
        startActivityForResult(intent,1221);
    }

    //searchlistactivity的返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1221 && resultCode == 1222) {
            keyword = data.getStringExtra("keyword");
            System.out.println("回传的数据: "+ keyword);

            searchQuery();

        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                int locationType = aMapLocation.getLocationType();//获取当前定位结果来源,详见定位类型表,1:gps;5:wifi:6"基站
                //获取纬度
                latitude = aMapLocation.getLatitude();
                //获取经度
                longitude = aMapLocation.getLongitude();
                float accuracy = aMapLocation.getAccuracy();//获取精度信息
                String address = aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                String country = aMapLocation.getCountry();//国家信息
                String province = aMapLocation.getProvince();//省信息
                String city = aMapLocation.getCity();//城市
                String aoiName = aMapLocation.getAoiName();
                //城市编码
                cityCode = aMapLocation.getCityCode();
                String district = aMapLocation.getDistrict();//城区信息

                //System.out.println(aoiName +" --fffffffffffffffff");
                System.out.println(locationType+" - "+ latitude +" - "+ longitude + " - "+ accuracy +" - "+ address);

                startPoint = new LatLng(latitude,longitude);
                mStartPoint = new LatLonPoint(latitude,longitude);
                if (mStartPoint == null) {
                    System.out.println("kong kong kong");
                }

                //如果定位成功,则取消继续定位
                if (!TextUtils.isEmpty(cityCode)) {
                    locationClient.stopLocation();
                    locationClient.onDestroy();
                }
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                System.out.println("AmapError,location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stopLocation();
        locationClient.onDestroy();
    }

    //在地图上显示搜索到的marker相关
    @Override
    public View getInfoWindow(final Marker marker) {
        View view = View.inflate(this,R.layout.keywordsearch_marker,null);
        TextView markerTitle = (TextView) view.findViewById(R.id.marker_title);
        TextView markerInfo = (TextView) view.findViewById(R.id.marker_info);
        markerTitle.setText(marker.getTitle());
        markerInfo.setText(marker.getSnippet());
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                endPoint = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
                mEndPoint = new LatLonPoint(marker.getPosition().latitude,marker.getPosition().longitude);
                System.out.println("marker的纬度: "+ marker.getPosition().latitude +" ,marke的经度: "+ marker.getPosition().longitude);

                //setFromAndToMarker(); //设置路径起点终点

                // 开始搜索路径规划方案
                startSearchRoute(ROUTE_TYPE_BUS,RouteSearch.BusDefault);

            }
        });
        return view;
    }

    //开始搜索路径规划方案
    public void startSearchRoute(int routeType, int mode) {
        if (mStartPoint == null) {
            Toast.makeText(this,"定位中,请稍后..",Toast.LENGTH_SHORT).show();
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(this,"请指定终点位置",Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressDialog(); //显示进度框
        LatLonPoint latLonPoint = new LatLonPoint(latitude,longitude);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint,mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {
            // fromAndTo包含路径规划的起点和终点，RouteSearch.BusLeaseWalk表示公交查询模式
            // 第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算,1表示计算
            RouteSearch.BusRouteQuery busRouteQuery = new RouteSearch.BusRouteQuery(fromAndTo,mode,cityCode,1);
            routeSearch.calculateBusRouteAsyn(busRouteQuery); // 异步路径规划 公交模式查询
        }
    }

    //在地图上显示搜索到的marker相关
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    // start 路径规划相关
    @Override
    public void onBusRouteSearched(BusRouteResult result, int rCode) {
        dismissProgressDialog(); //隐藏进度条
        amap.clear(); //清理地图上的所有覆盖物
        if (rCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    busRouteResult = result;
                    //将搜索到的路径规划方案传到RouteShowActivity进行展示
                    Intent intent = new Intent(KeywordSearchActivity.this,RouteShowActivity.class);
                    intent.putExtra("busresult",busRouteResult);
                    intent.putExtra("startpoint",startPoint);
                    intent.putExtra("endpoint",endPoint);
                    startActivity(intent);

                }else if (result != null && result.getPaths() == null) {
                    Toast.makeText(this,"抱歉,没有搜索到相关数据",Toast.LENGTH_SHORT);
                }
            }else {
                Toast.makeText(this,"抱歉,没有搜索到相关数据",Toast.LENGTH_SHORT);
            }
        }else {
            ToastUtil.showerror(this.getApplicationContext(), rCode);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    //end
}
