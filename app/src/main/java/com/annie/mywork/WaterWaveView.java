package com.annie.mywork;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/18.
 */
public class WaterWaveView extends View {

    private int circlePadding = 21; //同心圆的间距, 后期可外接传入
    private int color;  //同心圆颜色
    private int circleRadius = 12; // 圆形半径,初始值为6
    private ArrayList<CircleWave> circleList = new ArrayList<>(); //用于存放CircleWave对象
    private int parentViewWidth; //自定义view的宽度
    private int parentViewHeight; //自定义view的高度
    //private int circleRadius = 6; //初始半径

    private Paint paint; //画笔
    private int viewCenterX;
    private int viewCenterY;

    Canvas canvas = new Canvas();

    public WaterWaveView(Context context) {
        super(context);
        init();
    }

    public WaterWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint = initPaint(6);
        //executeAnimation();
    }

    //初始化画笔
    public Paint initPaint(int width ) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(width);
        paint.setColor(Color.RED); //设置画笔颜色
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        parentViewWidth = MeasureSpec.getSize(widthMeasureSpec); //获取该自定义view的宽度
        parentViewHeight = MeasureSpec.getSize(heightMeasureSpec); //获取该自定义view的高度
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //获取中心点的坐标
        viewCenterX = parentViewHeight / 2;
        viewCenterY = parentViewHeight / 2;

        //将各个圆环对象放到circleList集合中
        for (int i=0; i<2; i++) {
            CircleWave circleWave = new CircleWave();
            circleWave.setRadius(circleRadius);
            circleList.add(circleWave);
            circleRadius = circleRadius + circlePadding; //获取下一个圆的半径
        }

        //invalidate();
        //executeAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*//绘制所有圆环
        for (int i=0; i<circleList.size(); i++) {
            CircleWave circle = circleList.get(i);
            canvas.drawCircle(viewCenterX,viewCenterY,circle.getRadius(),paint);
        }*/
        executeAnimation(canvas);

    }

    //执行动画
    public void executeAnimation(final Canvas canvas) {
        ValueAnimator animator = ValueAnimator.ofInt(viewCenterX,0);
        animator.setDuration(2100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int curCircle = (int) valueAnimator.getAnimatedValue();

                System.out.println(curCircle +"");
                //如果当前值为半径, 那么画出这个圆
                //Canvas canvas = new Canvas();
                canvas.drawCircle(viewCenterX,viewCenterY,circleList.get(1).getRadius(),paint);
            }
        });
        animator.start();
    }

    /**
     * 获取控件宽高
     */
    private int resolveMeasured(int measureSpec, int desired) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }


    class CircleWave {
        private int radius;
        private int width;
        private int color;

        //重置

        // set, get方法
        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

}
