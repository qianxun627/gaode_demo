package com.annie.mywork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

public class MainActivity extends AppCompatActivity {

    private CircleView myCircleview;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCircleview = (CircleView) findViewById(R.id.myCircle);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radar_light_point_ico);
        myCircleview.setRadiation(true, bitmap);
        myCircleview.addPoint();
        myCircleview.addPoint();
        myCircleview.addPoint();

       /* ScaleAnimation sa = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(1000);

        AlphaAnimation aa = new AlphaAnimation(0,1f);
        sa.setDuration(1000);

        AnimationSet as = new AnimationSet(true);
        as.addAnimation(sa);
        as.addAnimation(aa);
        as.setDuration(1000);
        as.setFillAfter(true);
        as.setInterpolator(new AccelerateInterpolator());
        myCircleview.startAnimation(as);*/
    }

    /*Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myCircleview.flash();
        }
    };*/
    public void press(View view) {
        //myCircleview.invalidate();
       // handler.sendEmptyMessageDelayed(0,1200);
        /*myCircleview.setRadiation(true,bitmap);
        myCircleview.invalidate();*/
        //myCircleview.postInvalidate();
        //myCircleview.addPoint();

    }
}
