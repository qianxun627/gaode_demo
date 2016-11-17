package com.annie.mywork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2016/10/18.
 */
public class CircleView extends View {

    private int parentViewWidth; //自定义view的宽度
    private int parentViewHeight; //自定义view的高度
    private int viewCenterX;  //中心点X坐标
    private int viewCenterY;  //中心点Y坐标
    private int outCirclePadding; //外圆的宽度,即外圆与次外圆之间的距离
    private int inCircleRadius;  //內圆的半径
    private int outCircleRadius;  //外圆的半径
    private Paint paint; //画笔
    private float outRadiation1 = 0.1f; //用于辐射扫描进度
    private float outRadiation2 = 0.1f; //用于辐射扫描进度
    private float outRadiation3 = 0.1f; //用于辐射扫描进度
    private float outRadiation4 = 0.1f; //用于辐射扫描进度
    private boolean isRadiation = false; //用于判断是否开启辐射扫描
    private Bitmap lightPointBmp;  // z测试用的圆点
    private Context mContext;
    private int pointCount = 0;  // 圆点总数
    private ArrayList<Points> pointList = new ArrayList<>();  //用于存放点对象
    private Random random = new Random();
    private boolean isRadiating = false;
    Paint paint1;
    private int free = 170;
    //设置扫描渲染的shader
    private Shader scanShader;

    public CircleView(Context context) {
        super(context);
        init(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        this.mContext = context;
        paint = new Paint();
        paint1 = new Paint();
       // this.lightPointBmp = Bitmap.createBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.radar_light_point_ico));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取view的最小宽高
        int minViewWidth = getSuggestedMinimumWidth();
        int minViewHeight = getSuggestedMinimumHeight();

        //获取控件宽高
        parentViewWidth = getMeasured(widthMeasureSpec,minViewWidth);
        parentViewHeight = getMeasured(heightMeasureSpec,heightMeasureSpec);

        //获取中心点坐标
        viewCenterX = parentViewWidth / 2;
        viewCenterY = parentViewHeight / 2;

        //获取外圆的宽度
        outCirclePadding = parentViewWidth / 10;

        //获取内外圆的半径
        outCircleRadius = parentViewWidth / 2;
        inCircleRadius = (parentViewWidth - outCirclePadding)/4/2; // 4表示有4层, 各层內圆的半径=inCircleRadius*层数
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画笔属性
        paint.setAntiAlias(true);  //设置抗锯齿
        paint.setStyle(Paint.Style.FILL);  //设置填充样式为实心
        paint.setColor(Color.BLACK);  //设置画笔颜色为黑色
        paint.setStrokeWidth(3);

        // 1.开始绘制圆
        //绘制外圆
        canvas.drawCircle(viewCenterX, viewCenterY, outCircleRadius, paint);

        //绘制第4层圆
        paint.setStyle(Paint.Style.STROKE); //只画圆的边界
        paint.setColor(0x66EEeEee);  //设置画笔颜色为白色
        canvas.drawCircle(viewCenterX, viewCenterY, inCircleRadius*4, paint);

        //绘制第3层圆
        canvas.drawCircle(viewCenterX, viewCenterY, inCircleRadius*3, paint);

        //绘制第2层圆
        canvas.drawCircle(viewCenterX, viewCenterY, inCircleRadius*2, paint);

        //绘制第1层圆
        canvas.drawCircle(viewCenterX, viewCenterY, inCircleRadius*1, paint);

        // 2.开始绘制对角线
        canvas.drawLine(0, viewCenterY, parentViewWidth, viewCenterY, paint); //水平方向
        canvas.drawLine(viewCenterX, 0, viewCenterX, parentViewHeight, paint);

        int startX, startY, endX, endY;  // x,y 坐标的初始, 结束位置
        double radians;  //转换后得到的弧度

        //绘制45°方向的线
        radians = Math.toRadians((double)45);
        // (inCircleRadius*4 + outCirclePadding * Math.cos(radians): 45°方向线对应的x轴坐标
        startX = (int) (((inCircleRadius*4 + outCirclePadding) * Math.cos(radians)) + viewCenterX);
        startY = (int) (((inCircleRadius*4 + outCirclePadding) * Math.sin(radians)) + viewCenterY);

        radians = Math.toRadians((double)(45 + 180));
        endX = (int) (((inCircleRadius*4 + outCirclePadding) * Math.cos(radians)) + viewCenterX);
        endY = (int) (((inCircleRadius*4 + outCirclePadding) * Math.sin(radians)) + viewCenterY);
        canvas.drawLine(startX,startY,endX,endY,paint); //画出45°方向的对角线

        //绘制135°方向的对角线
        radians = Math.toRadians((double)135);
        startX = (int) (((inCircleRadius*4 + outCirclePadding) * Math.cos(radians)) + viewCenterX);
        startY = (int) (((inCircleRadius*4 + outCirclePadding) * Math.sin(radians)) + viewCenterY);
        radians = Math.toRadians((double)(135 + 180));
        endX = (int) (((inCircleRadius*4 + outCirclePadding) * Math.cos(radians)) + viewCenterX);
        endY = (int) (((inCircleRadius*4 + outCirclePadding) * Math.sin(radians)) + viewCenterY);
        canvas.drawLine(startX,startY,endX,endY,paint);  //画出135°方向的对角线

        // 3.开始绘制辐射效果
        canvas.save();  // 用来保存Canvas的状态.save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作.
        if (isRadiation) {  //判断是否开启辐射扫描
            // 缩放画布, 参1参2: 缩放的比例   参3参4: 缩放中心点的坐标
            // 第一条环形线
            //Paint paint1 = new Paint();
           // isRadiating = false;
            paint1.setAntiAlias(true);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setAlpha(free);
            //paint1.setColor(0xffEEFEFF);
            paint1.setARGB(free,236,253,255);
            paint1.setStrokeWidth(9);
            //paint1.setShadowLayer(32,9,9,0xff31C922);
            scanShader = new RadialGradient(viewCenterX,viewCenterY,outCircleRadius,new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")},null, Shader.TileMode.REPEAT);
            paint1.setShader(scanShader);
            canvas.scale(outRadiation1, outRadiation1, viewCenterX, viewCenterY);
            //canvas.alp
            canvas.drawCircle(viewCenterX, viewCenterY, outCircleRadius, paint1);

            //第2条环形线
            canvas.save();
            canvas.scale(outRadiation2, outRadiation2, viewCenterX, viewCenterY);
            canvas.drawCircle(viewCenterX, viewCenterY, outCircleRadius, paint1);
            canvas.restore();

            //第3条环形线
            canvas.save();
            canvas.scale(outRadiation3, outRadiation3, viewCenterX, viewCenterY);
            canvas.drawCircle(viewCenterX, viewCenterY, outCircleRadius, paint1);
            canvas.restore();



            //第4条环形线
            canvas.save();
            canvas.scale(outRadiation4, outRadiation4, viewCenterX, viewCenterY);
            canvas.drawCircle(viewCenterX, viewCenterY, outCircleRadius, paint1);
            canvas.restore();

            if (outRadiation4 < 1.2f) {
                outRadiation1 += 0.0148f;
                outRadiation2 += 0.0142f;
                outRadiation3 += 0.014f;
                outRadiation4 += 0.008f;
                if (outRadiation4 >= 0.45f) {
                    isRadiating = true;
                }
            }
            if (free >= 90) {
                free -= 10;
            }
           /* if (outRadiation2 < 1.2f) {
                outRadiation2 += 0.0162f;
            }
            if (outRadiation3 < 1.2f) {
                outRadiation3 += 0.01f;
            }
            if (outRadiation4 < 1.2f) {
                outRadiation4 += 0.008f;
            }*/



        }
        canvas.restore();  // 用来恢复Canvas之前保存的状态.防止save后对Canvas执行的操作对后续的绘制有影响.

        //绘制动态点
        if (pointCount > 0 && isRadiating) {
            if (pointCount > pointList.size()) {
                Points points = new Points();
                int pointX = inCircleRadius + random.nextInt(inCircleRadius*3) +outCirclePadding;
                int pointY = inCircleRadius + random.nextInt(inCircleRadius*3) +outCirclePadding;
                points.setPointX(pointX);
                points.setPointY(pointY);
                pointList.add(points);
            }
            Points point = new Points();
            if (pointList.size() > 0 ) {
                for (int i=0; i<pointList.size(); i++) {
                    point = pointList.get(i);
                    canvas.drawBitmap(lightPointBmp, point.getPointX(), point.getPointY(),null);
                }
                //pointCount = 0;
                //pointList.clear();
            }
        }

        if (isRadiation) {
            this.invalidate();
        }



    }

    //重写触摸方法, 想要重新引起重绘,需要初始化辐射扫描进度,并清空存放点对象的集合
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("gggggggggggggggg");
                isRadiating = false;
                //pointCount = 0;
                pointList.clear();
                outRadiation1 = 0.1f;
                outRadiation2 = 0.1f;
                outRadiation3 = 0.1f;
                outRadiation4 = 0.1f;
                pointList.clear();
                this.invalidate();
                return true;

        }
        //this.invalidate();
        //return true;
       return super.onTouchEvent(event);

    }

    /**
     * 设置扫描状态(外界调用时使用)
     */
    public void setRadiation(boolean status,Bitmap bitmap) {
        this.isRadiation = status;
        this.lightPointBmp = bitmap;
        this.invalidate();
    }

    public void flash() {
        isRadiation = true;
        this.invalidate();
    }
    /**
     * 统计添加点的个数
     */
    public void addPoint() {
        pointCount++;
        this.invalidate();
    }

    /**
     * 获取控件宽高
     */
    private int getMeasured(int measureSpec, int desired) {
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

}
