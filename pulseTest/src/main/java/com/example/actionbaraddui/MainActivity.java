package com.example.actionbaraddui;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnClickListener{
	
//	SearchDevicesView search_device_view;
	Button cancle;
//	ListView list;
	ImageView imageview_01;
	AnimationSet animationSet;
	SeekBar seekBar1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		search_device_view = (SearchDevicesView) findViewById(R.id.search_device_view);
//		search_device_view.setWillNotDraw(false);
		cancle = (Button) findViewById(R.id.cancle);
//		list = (ListView) findViewById(R.id.device_list_view);
		imageview_01 = (ImageView) findViewById(R.id.imageview_01);
		seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
		seekBar1.setOnSeekBarChangeListener(onseeklistener);
		imageview_01.setOnClickListener(this);
		cancle.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.cancle:
			animation_01(10.0f);
//			search_device_view.setSearching(false);
			break;
		}
	}
	
	private void animation_01(float animation_size){
		//创建一个AnimationSet对象，参数为Boolean型，
        //true表示使用Animation的interpolator，false则是使用自己的
		animationSet = new AnimationSet(true);
        //参数1：x轴的初始值
        //参数2：x轴收缩后的值
        //参数3：y轴的初始值
        //参数4：y轴收缩后的值
        //参数5：确定x轴坐标的类型
        //参数6：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        //参数7：确定y轴坐标的类型
        //参数8：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        ScaleAnimation scaleAnimation = new ScaleAnimation(
               1, animation_size,1,animation_size,
               Animation.RELATIVE_TO_SELF,0.5f,
               Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(3000);
        animationSet.addAnimation(scaleAnimation);
		
		
        //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        //设置动画执行的时间
        alphaAnimation.setDuration(3000);
        //将alphaAnimation对象添加到AnimationSet当中
        animationSet.addAnimation(alphaAnimation);
        //使用ImageView的startAnimation方法执行动画
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				imageview_01.startAnimation(animationSet);
			}
		});
        imageview_01.startAnimation(animationSet);
	}
	
	OnSeekBarChangeListener onseeklistener = new OnSeekBarChangeListener() {
        
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        	animation_01(seekBar.getProgress()*10.0f);
        }
        
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
//            description.setText("开始拖动");
        }
        
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                boolean fromUser) {
//            description.setText("当前进度："+progress+"%");
        }
    };
	
}
