package com.annie.gaode_medo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.annie.gaode_medo.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

//应该是在拿到搜索框的内容之后,才在地图上请求搜索,也就是说当点击listview条目后将该条目的数据返回过来,返回的数据就是搜索用的keyword
//也就是说这个界面没有必要去实现OnPoiSearchListener, OnPoiSearchListener的实现应该放在KeywordSearchActivity中
public class SearchListActivity extends AppCompatActivity implements
        PoiSearch.OnPoiSearchListener,View.OnClickListener,Inputtips.InputtipsListener,TextWatcher,AdapterView.OnItemClickListener{

    private EditText editText;
    private Button searchBtn;
    private String keyWord;  //需要传递的搜索框的内容
    private String cityCode; //城市编码
    private int currentPage; //当前页面
    //private ProgressDialog progressDialog; //搜索进度条
    private PoiSearch poiSearch;
    private PoiResult poiResult; // poi返回的结果
    private PoiSearch.Query query;
    private ListView listView;
    private String searchedCity;  //要搜索的城市的名字, 需要通过定位来获取当前所在的城市,默认为当前所在地
    private boolean isSearchEmpty = false; //判断搜索框内是否有内容, 若无内容, 清空listview中的数据
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        //初始化操作
        init();
    }

    //初始化操作
    private void init() {
        Intent intent = getIntent();
        searchedCity = intent.getStringExtra("citycode");  //获取传递过来的城市编码
        System.out.println(searchedCity + "aaaaaaaaaaaaaaaaaa");
        //搜索框
        editText = (EditText) findViewById(R.id.search);
        searchBtn = (Button) findViewById(R.id.button);
        editText.addTextChangedListener(this);  //设置搜索框的监听事件
        searchBtn.setOnClickListener(this);  //设置搜索按钮的点击事件

        //listview
        listView = (ListView) findViewById(R.id.listview);
    }

    //搜索框点击事件
    @Override
    public void onClick(View view) {
        keyWord = editText.getText().toString().trim(); //获取搜索框中的内容
        if (TextUtils.isEmpty(keyWord)) {
            Toast.makeText(this,"请输入搜索内容",Toast.LENGTH_SHORT).show();
        }else {
           Intent intent = new Intent(this,KeywordSearchActivity.class);
            intent.putExtra("keyword",keyWord);
            //startActivity(intent);
            setResult(1222,intent);  //回传数据
            finish();
        }

    }

   /* //根据关键字进行搜索
    private void searchQuery() {
        showProgressDialog(); //显示进度框
        currentPage = 0;
        //参1: 搜索的字符串  参2: POI搜索类型(1/2 选填其一就可)   参3: 搜索区域(城市编码/名称, ""表示在全国范围内搜)
        query = new PoiSearch.Query(keyWord,"",cityCode);
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);
        //query.setCityLimit(true); //设置搜索城市限制

        // 构造poiSearch对象, 并设置监听
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);

        //发送搜索请求
        poiSearch.searchPOIAsyn();
    }*/

    @Override
    public void onPoiSearched(PoiResult Result, int rCode) {
        /*//隐藏进度框
        dismissProgressDialog();
        if (rCode == 1000) {
            if (Result != null && Result.getQuery() !=null) {
                if (Result.getQuery().equals(query)) { //判断是否是同一条搜索信息
                    poiResult = Result;
                    ArrayList<PoiItem> poiItems = poiResult.getPois(); //获取到PoiItem列表
                    ArrayList<SuggestionCity> suggestionCities = (ArrayList<SuggestionCity>) poiResult.getSearchSuggestionCitys();

                    if (poiItems != null && poiItems.size() > 0) {
                        //通过listview来显示搜索结果
                      *//*  MyAdapter myAdapter = new MyAdapter(poiItems,this);
                        listView.setAdapter(myAdapter);*//*

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
*/
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int rCode) {

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

    /*//显示进度框
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //进度条样式设为圆形
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false); //设置ProgressDialog 是否可以按返回键取消；
        progressDialog.setMessage("正在搜索:\n" + keyWord);
        progressDialog.show();
    }

    //隐藏进度框
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }*/

    //将搜索框中的内容给listview进行展示
    @Override
    public void onGetInputtips(List<Tip> list, int code) {
        if (code == 1000) {
            ArrayList<String> infoList = new ArrayList<>();
            for (int i=0; i<list.size(); i++) {
                infoList.add(list.get(i).getName());  //这里还可以拿到address, poiId等信息
            }
            if (isSearchEmpty) {
                infoList.clear();
                /*myAdapter = new MyAdapter(infoList,this);
                myAdapter.notifyDataSetChanged();*/
            }else {
                myAdapter = new MyAdapter(infoList,this);
                listView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                listView.setOnItemClickListener(this); //设置listview的条目点击事件
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view,  int position, long id) {
        //String itemInfo = (String) listView.getSelectedItem();
        String itemInfo = (String) listView.getItemAtPosition(position);  //获取listview选中的条目信息
        System.out.println(itemInfo +" -aaaaaaaaaaaaaaaaa");
        editText.setText(itemInfo);
        editText.setSelection(editText.getText().length());  //将输入框的光标移到文本的末尾处
    }

    //textWatcher, 用于监听editText, 当有输入的时候会调用该监听的相应方法,进行搜索,进而将搜索的相关内容展示在listview上
    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                  int after) {

    }

    //textWatcher, 用于监听editText, 当有输入的时候会调用该监听的相应方法,进行搜索,进而将搜索的相关内容展示在listview上
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        String newText = charSequence.toString().trim();
        if (!TextUtils.isEmpty(newText)) {
            // 参1: 要搜索的关键字   参2:要搜索的城市,默认为当前所在地
            if (TextUtils.isEmpty(searchedCity)) {
                searchedCity = "北京市";
            }
            isSearchEmpty = false;
            InputtipsQuery inputtipsQuery = new InputtipsQuery(newText,searchedCity);
            Inputtips inputtips = new Inputtips(this,inputtipsQuery);
            inputtips.setInputtipsListener(this);
            inputtips.requestInputtipsAsyn();  //发送搜索请求
        }else {
            isSearchEmpty = true;
        }
    }

    //textWatcher
    @Override
    public void afterTextChanged(Editable editable) {
        if (isSearchEmpty) {
            listView.setVisibility(View.INVISIBLE);
        }else {
            listView.setVisibility(View.VISIBLE);
        }
    }


}
