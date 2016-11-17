package com.annie.gaode_medo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RailwayStationItem;
import com.annie.gaode_medo.R;
import com.annie.gaode_medo.been.SchemeBusStep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class BusRouteDetailAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SchemeBusStep> busStepList = new ArrayList<>();

    public BusRouteDetailAdapter(Context context,List<BusStep> list) {
        this.mContext = context;
        //开始封装数据
        SchemeBusStep start = new SchemeBusStep(null);
        start.setStart(true);
        busStepList.add(start);
        for (BusStep busStep : list) {
            // 判断有没有步行, 公交, 铁路等路径信息, 如果有通过创建SchemeBusStep 对象,把相应的路径信息设置给BusStep保存起来
            // 同时为其打上是否有路径信息的标记
            if (busStep.getWalk() != null && busStep.getWalk().getDistance() > 0) {
                SchemeBusStep walk = new SchemeBusStep(busStep);
                walk.setWalk(true);
                busStepList.add(walk);
            }
            if (busStep.getBusLine() != null) {
                SchemeBusStep bus = new SchemeBusStep(busStep);
                bus.setBus(true);
                busStepList.add(bus);
            }
            if (busStep.getRailway() != null) {
                SchemeBusStep railway = new SchemeBusStep(busStep);
                railway.setRailway(true);
                busStepList.add(railway);
            }
            if (busStep.getTaxi() != null) {
                SchemeBusStep taxi = new SchemeBusStep(busStep);
                taxi.setTaxi(true);
                busStepList.add(taxi);
            }
        }
        SchemeBusStep end = new SchemeBusStep(null);
        end.setEnd(true);
        busStepList.add(end);
    }

    @Override
    public int getCount() {
        return busStepList.size();
    }

    @Override
    public Object getItem(int i) {
        return busStepList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        SchemeBusStep item = busStepList.get(position);  //获取条目数据

        viewHolder holder = null;
        if (convertView == null) {
            holder = new viewHolder();
            convertView = View.inflate(mContext, R.layout.route_detail_item,null);
            holder.expand_content = (LinearLayout) convertView.findViewById(R.id.expand_content);
            holder.extend_pic = (ImageView) convertView.findViewById(R.id.extend_pic);
            holder.item_bottom_line = (ImageView) convertView.findViewById(R.id.item_bottom_line);
            holder.item_info = (TextView) convertView.findViewById(R.id.item_info);
            holder.pic_down_line = (ImageView) convertView.findViewById(R.id.pic_down_line);
            holder.pic_up_line = (ImageView) convertView.findViewById(R.id.pic_up_line);
            holder.route_type_pic = (ImageView) convertView.findViewById(R.id.route_type_pic);
            holder.station_num = (TextView) convertView.findViewById(R.id.station_num);
            holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);

            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.route_type_pic.setImageResource(R.mipmap.dir_start);
            holder.item_info.setText("出发");
            holder.pic_up_line.setVisibility(View.INVISIBLE);
            holder.pic_down_line.setVisibility(View.VISIBLE);
            holder.item_bottom_line.setVisibility(View.GONE);
            holder.station_num.setVisibility(View.GONE);
            holder.extend_pic.setVisibility(View.GONE);
            //holder.expand_content.setVisibility(View.GONE);

            return convertView;
        }else if (position == busStepList.size() - 1) {
            holder.route_type_pic.setImageResource(R.mipmap.dir_end);
            holder.item_info.setText("到达终点");
            holder.pic_up_line.setVisibility(View.VISIBLE);
            holder.pic_down_line.setVisibility(View.INVISIBLE);
            holder.item_bottom_line.setVisibility(View.VISIBLE);
            holder.station_num.setVisibility(View.GONE);
            holder.extend_pic.setVisibility(View.GONE);
            //holder.expand_content.setVisibility(View.GONE);

            return convertView;
        }else {
            if (item.isWalk() && item.getWalk() != null && item.getWalk().getDistance() > 0) {
                holder.route_type_pic.setImageResource(R.mipmap.dir13);
                holder.item_info.setText("步行"+ (int) item.getWalk().getDistance() +"米");
                holder.pic_up_line.setVisibility(View.VISIBLE);
                holder.pic_down_line.setVisibility(View.VISIBLE);
                holder.item_bottom_line.setVisibility(View.VISIBLE);
                holder.station_num.setVisibility(View.GONE);
                holder.extend_pic.setVisibility(View.GONE);
                //holder.expand_content.setVisibility(View.GONE);

                return convertView;
            }else if (item.isBus() && item.getBusLines().size() > 0) {
                holder.route_type_pic.setImageResource(R.mipmap.dir14);
                holder.item_info.setText(item.getBusLines().get(0).getBusLineName());
                holder.pic_up_line.setVisibility(View.VISIBLE);
                holder.pic_down_line.setVisibility(View.VISIBLE);
                holder.item_bottom_line.setVisibility(View.VISIBLE);
                holder.station_num.setText((item.getBusLines().get(0).getPassStationNum() + 1) +"站");
                holder.extend_pic.setVisibility(View.VISIBLE);

                // 为条目打标记,设监听
                arrowClick arrowClick = new arrowClick(holder,item);
                holder.rl_item.setTag(position);
                holder.rl_item.setOnClickListener(arrowClick);
                //holder.expand_content.setVisibility(View.GONE);

                return convertView;
            }else if (item.israilway() && item.getRailway() != null) {
                holder.route_type_pic.setImageResource(R.mipmap.dir16);
                holder.item_info.setText(item.getRailway().getName());
                holder.pic_up_line.setVisibility(View.VISIBLE);
                holder.pic_down_line.setVisibility(View.VISIBLE);
                holder.item_bottom_line.setVisibility(View.VISIBLE);
                holder.station_num.setText((item.getRailway().getViastops().size() + 1) +"站");
                holder.extend_pic.setVisibility(View.VISIBLE);

                // 为条目打标记,设监听
                arrowClick arrowClick = new arrowClick(holder,item);
                holder.rl_item.setTag(position);
                holder.rl_item.setOnClickListener(arrowClick);
                //.expand_content.setVisibility(View.GONE);

                return convertView;
            }else if (item.istaxi() && item.getTaxi() != null) {
                holder.route_type_pic.setImageResource(R.mipmap.dir14);
                holder.item_info.setText("打车到终点");
                holder.pic_up_line.setVisibility(View.VISIBLE);
                holder.pic_down_line.setVisibility(View.VISIBLE);
                holder.item_bottom_line.setVisibility(View.VISIBLE);
                holder.station_num.setVisibility(View.GONE);
                holder.extend_pic.setVisibility(View.GONE);
                //.expand_content.setVisibility(View.GONE);

                return convertView;
            }
        }
        return convertView;
    }

    public class viewHolder{
        ImageView item_bottom_line;
        ImageView route_type_pic;
        ImageView pic_up_line;
        ImageView pic_down_line;
        ImageView extend_pic;
        TextView station_num;
        TextView item_info;
        LinearLayout expand_content;
        boolean arrowExpend = false;
        RelativeLayout rl_item;
    }

    //扩展箭头的点击事件
    public class arrowClick implements View.OnClickListener {
        private viewHolder mHolder;
        private SchemeBusStep mItem;

        public arrowClick(viewHolder mHolder, SchemeBusStep mItem) {
            this.mHolder = mHolder;
            this.mItem = mItem;
        }

        @Override
        public void onClick(View view) {
            int position = Integer.parseInt(String.valueOf(view.getTag()));
            mItem = busStepList.get(position); //获取需要扩展的条目

            if (mItem.isBus()) {
                if (mHolder.arrowExpend == false) {
                    mHolder.arrowExpend = true;
                    mHolder.extend_pic.setImageResource(R.mipmap.up);
                    addBusStation(mItem.getBusLine().getDepartureBusStation());  // 起点
                    System.out.println("公交当前起点: "+ mItem.getBusLine().getDepartureBusStation());
                    for (BusStationItem station : mItem.getBusLine()  //中间经过的站
                            .getPassStations()) {
                        addBusStation(station);
                    }
                    addBusStation(mItem.getBusLine().getArrivalBusStation());  //终点站
                    System.out.println("公交当前终点: "+ mItem.getBusLine().getArrivalBusStation());
                }else {
                    mHolder.arrowExpend = false;
                    mHolder.extend_pic
                            .setImageResource(R.mipmap.down);
                    mHolder.expand_content.removeAllViews();  //移除扩展信息
                }

            }else if (mItem.israilway()){
                if (mHolder.arrowExpend == false) {
                    mHolder.arrowExpend = true;
                    mHolder.extend_pic.setImageResource(R.mipmap.up);
                    addRailwayStation(mItem.getRailway().getDeparturestop());
                    System.out.println("地铁当前起点: "+ mItem.getRailway().getDeparturestop());
                    for (RailwayStationItem station : mItem.getRailway().getViastops()) {
                        addRailwayStation(station);
                    }
                    addRailwayStation(mItem.getRailway().getArrivalstop());
                    System.out.println("地铁当前终点: "+ mItem.getRailway().getArrivalstop());
                }else {
                    mHolder.arrowExpend = false;
                    mHolder.extend_pic
                            .setImageResource(R.mipmap.down);
                    mHolder.expand_content.removeAllViews();  //移除扩展信息
                }
            }
        }

        //添加公交的扩展条目
        private void addBusStation(BusStationItem station) {
            LinearLayout ll = (LinearLayout) View.inflate(mContext,
                    R.layout.item_bus_segment_ex, null);
            TextView tv = (TextView) ll
                    .findViewById(R.id.bus_line_station_name);
            tv.setText(station.getBusStationName());
            mHolder.expand_content.addView(ll);
        }

        //添加地铁的扩展条目
        private void addRailwayStation(RailwayStationItem station) {
            LinearLayout ll = (LinearLayout) View.inflate(mContext,
                    R.layout.item_bus_segment_ex, null);
            TextView tv = (TextView) ll
                    .findViewById(R.id.bus_line_station_name);
            tv.setText(station.getName()+ " "+getRailwayTime(station.getTime()));
            System.out.println(getRailwayTime(station.getTime()));
            mHolder.expand_content.addView(ll);
        }
    }

    public static String getRailwayTime(String time) {
        return time.substring(0, 2) + ":" + time.substring(2, time.length());
    }

}
