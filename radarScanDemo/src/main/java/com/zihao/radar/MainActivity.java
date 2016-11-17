package com.zihao.radar;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.zihao.radar.view.RadarView;

public class MainActivity extends Activity {

	private RadarView mRadarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRadarView = (RadarView) findViewById(R.id.radar_view);
		mRadarView.setSearching(true);
		mRadarView.addPoint();
		mRadarView.addPoint();


	}

}