package com.tsing.healthprogress.healthprogressdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2017/7/3.
 */

public class HealthProgressView extends View {

    private int screenWidth;

    public int progressHeight = 50;
    public int progressRectTopY = 100;
    public float maxBMI = 37.5f;
    public float minBMI = 15;
    //    public float marginLeftDP = 20;
    public int marginLeft;
    public int marginRight;
//    public float marginRightDP = 20;

    public float mTextSize = 14;
    private float currentValue = 22.2f;
    public int mTextColor = Color.WHITE;
    public List<Float> separateList = new ArrayList<>();//分割的值
    public List<Integer> colorList = new ArrayList<>();//区域色值
    public List<String> textList = new ArrayList<>();//区域色值


    public Paint mTextPaint;
    public Paint mRectPaint;
    public int numbTextColor = Color.GRAY;
    public int stringTextColor = Color.WHITE;


    public HealthProgressView(Context context) {
        this(context, null);
    }

    public HealthProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HealthProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        marginLeft = dp2px(20);
        marginRight = dp2px(20);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mTextColor = Color.WHITE;
        progressHeight = dp2px(18);
        mTextSize = dp2px(10);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);

        mRectPaint.setAntiAlias(true);
        colorList.add(Color.BLUE);
        colorList.add(Color.GREEN);
        colorList.add(Color.YELLOW);
        colorList.add(Color.RED);
        separateList.add(18.5f);
        separateList.add(25f);
        separateList.add(30f);
        textList.add("偏瘦");
        textList.add("正常");
        textList.add("超重");
        textList.add("肥胖");

    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制第一个左边圆角的矩形

        int width = screenWidth - marginLeft - marginRight;


        int leftRectWidth = (int) (width * (separateList.get(0) - minBMI) / (maxBMI - minBMI));
        int textY = progressRectTopY + progressHeight * 2 / 3;
        int separateWidth = dp2px(3);
        float[] outerL = new float[]{
                progressHeight / 2, progressHeight / 2//左上
                , 0, 0//右上
                , 0, 0//右下
                , progressHeight / 2, progressHeight / 2//左下
        };
        ShapeDrawable mDrawablesLeft = new ShapeDrawable(new RoundRectShape(outerL, null, null));
        mDrawablesLeft.getPaint().setColor(colorList.get(0));
        mDrawablesLeft.setBounds(marginLeft, progressRectTopY, marginLeft + leftRectWidth - separateWidth / 2, progressRectTopY + progressHeight);
        mDrawablesLeft.draw(canvas);
        mTextPaint.setColor(stringTextColor);
        canvas.drawText(textList.get(0)
                , marginLeft + leftRectWidth / 2
//                        - mTextPaint.measureText(textList.get(0)) / 2
                , textY
                , mTextPaint);

//        canvas.dra
        //绘制间隔矩形
//        mRectPaint.setColor(Color.BLACK);
//        canvas.drawRect(marginLeft + leftRectWidth, progressRectTopY, marginLeft + leftRectWidth + separateWidth, progressRectTopY + progressHeight, mRectPaint);
        //绘制中间的多个矩形
        float startWidth = marginLeft + leftRectWidth;
        for (int x = 0; x < colorList.size() - 1; x++) {
            //排除第一个
            if (x > 0) {
                //矩形
                float lastSeparate = separateList.get(0);
                if (x > 1) {
                    lastSeparate = separateList.get(x - 1);
                }
                float endWidth = startWidth + width * (separateList.get(x) - lastSeparate) / (maxBMI - minBMI);
                mRectPaint.setColor(colorList.get(x));
                canvas.drawRect(startWidth + separateWidth / 2, progressRectTopY, endWidth - separateWidth / 2, progressRectTopY + progressHeight, mRectPaint);
                mTextPaint.setColor(stringTextColor);
                canvas.drawText(textList.get(x)
                        , startWidth + width * (separateList.get(x) - lastSeparate) / (maxBMI - minBMI) / 2
//                                - mTextPaint.measureText(textList.get(x)) / 2
                        , textY
                        , mTextPaint);
                //间隔
//                mRectPaint.setColor(Color.BLACK);
//                canvas.drawRect(endWidth, progressRectTopY, endWidth + separateWidth, progressRectTopY + progressHeight, mRectPaint);
                startWidth = endWidth;
            }
        }
        //绘制右边的圆角矩形

        float[] outerR = new float[]{
                0, 0//左上
                , progressHeight / 2, progressHeight / 2//右上
                , progressHeight / 2, progressHeight / 2//右下
                , 0, 0//左下
        };
        ShapeDrawable mDrawablesRight = new ShapeDrawable(new RoundRectShape(outerR, null, null));
        mDrawablesRight.getPaint().setColor(colorList.get(colorList.size() - 1));
        mDrawablesRight.setBounds((int) startWidth + separateWidth / 2, progressRectTopY, (int) (startWidth + width * (maxBMI - separateList.get(separateList.size() - 1)) / (maxBMI - minBMI)), progressRectTopY + progressHeight);
        mDrawablesRight.draw(canvas);
        mTextPaint.setColor(stringTextColor);
        canvas.drawText(textList.get(textList.size() - 1)
                , (int) (startWidth + width * (maxBMI - separateList.get(separateList.size() - 1)) / (maxBMI - minBMI) / 2)
//                        - mTextPaint.measureText(textList.get(textList.size() - 1)) / 2
                , textY
                , mTextPaint);

        //绘制底部数值文本

        for (int x = 0; x < separateList.size(); x++) {
            float txtX = marginLeft + width * (separateList.get(x) - minBMI) / (maxBMI - minBMI);
            float txtY = progressRectTopY + progressHeight * 1.7f;
            mTextPaint.setColor(numbTextColor);
            canvas.drawText(String.valueOf(separateList.get(x)), txtX, txtY, mTextPaint);
        }

        //绘制三角指针
        float triAngeleBottomX = marginLeft + width * (currentValue - minBMI) / (maxBMI - minBMI);
        float triAngeleBottomY = progressRectTopY * 9 / 10;
        float offset = progressHeight / 3;

        Path path = new Path();
        path.moveTo(triAngeleBottomX, triAngeleBottomY);// 此点为多边形的起点
        path.lineTo(triAngeleBottomX + offset, triAngeleBottomY - offset * 1.15f);
        path.lineTo(triAngeleBottomX - offset, triAngeleBottomY - offset * 1.15f);
        path.close(); // 使这些点构成封闭的多边形
        int triangleColor = colorList.get(0);
        for (int x = 0; x < separateList.size(); x++) {
            if (currentValue < separateList.get(x)) {
                triangleColor = colorList.get(x);
                break;
            } else {
                if (x == separateList.size() - 1) {
                    triangleColor = colorList.get(x + 1);
                }
            }
        }
        mRectPaint.setColor(triangleColor);
        canvas.drawPath(path, mRectPaint);
    }

    public void setCurrentValue(float currentValue) {
        //防止显示到边界
        if (currentValue < minBMI + 0.6f) {
            currentValue = minBMI + 0.6f;
        }
        if (currentValue > maxBMI - 0.6f) {
            currentValue = maxBMI - 0.6f;
        }
        this.currentValue = currentValue;
        invalidate();
    }
}
