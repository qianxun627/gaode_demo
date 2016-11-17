package com.annie.gaode_medo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.annie.gaode_medo.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/21.
 */
public class MyAdapter extends BaseAdapter {
    private ArrayList<PoiItem> poiItems;
    private ArrayList<String> infoList;
    private Context context;
    private ArrayList<SuggestionCity> suggestionCities;

    public MyAdapter(ArrayList<String> list,Context context) {
        //this.poiItems = list;
        this.infoList = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int i) {
        return infoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.listview_item,null);
        }
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(infoList.get(i));
        return view;
    }
}
