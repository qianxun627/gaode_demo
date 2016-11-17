package com.annie.gaode_medo.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/10/24.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }



    // 这里需要重写这个构造,这个构造函数是布局文件中调用的
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 这个是在代码中调用的
	/*
	 * public NoScrollViewPager(Context context) { super(context); }
	 */

    //这里是覆盖了父类Viewpager的onTouchevent方法,表示不执行父类的ontouchevent方法(包含父类处理事件的默认行为,如移动等),
    //干掉父类处理事件的默认行为
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 返回true表示自己消耗掉该事件
        return false;
    }

    //不让拦截孩子事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
