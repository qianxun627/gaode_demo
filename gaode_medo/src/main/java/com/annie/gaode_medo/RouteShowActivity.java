package com.annie.gaode_medo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.route.BusRouteResult;
import com.annie.gaode_medo.fragment.BusRouteFragment;
import com.annie.gaode_medo.fragment.DriveRouteFragment;
import com.annie.gaode_medo.fragment.WalkRouteFragment;

import java.util.ArrayList;

public class RouteShowActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList<Fragment> list = new ArrayList<>();
    private BusRouteResult busRouteResult;
    private LatLng startPoint;
    private LatLng endPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //隐藏actionBar
        setContentView(R.layout.activity_route_show);

        // 获取intent传递过来的数据
        getIntentData();

        tabLayout = (TabLayout) findViewById(R.id.routeType);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // a.准备数据,即要展示的界面
        list.add(new DriveRouteFragment());
        BusRouteFragment busRouteFragment = new BusRouteFragment();
        busRouteFragment.setData(busRouteResult,startPoint,endPoint);
        list.add(busRouteFragment);
        list.add(new WalkRouteFragment());

        // b.为viewpager设置适配器
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        // c.绑定TabLayout和ViewPager
        tabLayout.setupWithViewPager(viewPager);

        //设置默认选中公交
        viewPager.setCurrentItem(1);

    }

    // 获取intent传递过来的数据
    public void getIntentData() {
        Intent intent = getIntent();
        busRouteResult = intent.getParcelableExtra("busresult");
        startPoint = intent.getParcelableExtra("startpoint");
        endPoint = intent.getParcelableExtra("endpoint");
    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] str = new String[] {"驾车","公交","步行"};
            return str[position];
        }
    }


}
