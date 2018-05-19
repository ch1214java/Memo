package com.example.administrator.memo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2018/4/18.
 */

public class LinedEditText extends EditText {
    private Rect mRect;
    private Paint mPaint;
    //构造函数
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context,attrs);
        mRect = new Rect();
        //设置颜料颜色
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);

    }
    //生成视图

    @Override
    protected void onDraw(Canvas canvas) {
        int count = getLineCount();
        Rect r = mRect;
        Paint paint = mPaint;
        //设置每一行格式
        for (int i = 0;i < count;i++){
            //取得每一行的基准线Y坐标，并将每一行的界限值填写到r中
            int baseline = getLineBounds(i,r);
            //设置每一行的文字带下划线
            canvas.drawLine(r.left,baseline + 5,r.right,baseline + 5,paint);
        }
        super.onDraw(canvas);
    }
}
