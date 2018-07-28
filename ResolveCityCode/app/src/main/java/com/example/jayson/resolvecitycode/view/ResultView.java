package com.example.jayson.resolvecitycode.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by shaoheng on 2018/3/9.
 */

public class ResultView extends View {
    private Paint mpaint;
    private Path mpath;
    private int mdefaultSize = 2000;

    private int mviewSize;



    public ResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mpaint = new Paint();
        mpaint.setColor(Color.rgb(199,199,205));
        mpaint.setAntiAlias(true);
        mpaint.setStrokeWidth(2);
        mpaint.setStyle(Paint.Style.STROKE);
        mpath = new Path();
        mdefaultSize = dip2px(getContext(),200);
        //C7C7CD
    }

    private int dip2px(Context context, float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(mdefaultSize,widthMeasureSpec);
        int width = measureSize(mdefaultSize,widthMeasureSpec);
        int min = Math.min(width,height);
        setMeasuredDimension(min,min);
        mviewSize = min;

    }

    private int measureSize(int defaultSize,int measureSpec){
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            result = Math.min(result,specSize);
        }else if (specMode == MeasureSpec.AT_MOST){
            result = Math.min(result,specSize);
        }
        return result;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = Bitmap.createBitmap(mviewSize,mviewSize, Bitmap.Config.ARGB_8888);
//        Canvas canvas1 = new Canvas(bitmap);
        canvas.drawPath(getPath(),mpaint);


//        canvas.drawBitmap(bitmap,0,0,null);
    }

    private Path getPath(){
        mpath.reset();
        mpath.moveTo(0,0);
        mpath.lineTo(0,mviewSize);
        mpath.lineTo(mviewSize * 2/3,mviewSize);
        mpath.moveTo(0,0);
        mpath.close();
        return mpath;
    }
}
