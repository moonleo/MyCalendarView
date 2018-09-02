package priv.yh.mycalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class MyDayView extends AppCompatTextView {

    private boolean mIsToday;
    private Paint mPaint = new Paint();
    private PaintFlagsDrawFilter mPfd = new PaintFlagsDrawFilter(
            0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);

    public MyDayView(Context context) {
        super(context);
        initPaint();
    }

    public MyDayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MyDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public boolean isToday() {
        return this.mIsToday;
    }

    public void setToday(boolean today) {
        this.mIsToday = today;
    }

    private void initPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(getResources().getColor(R.color.color_cur_day_circle));
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mPfd);
        if(mIsToday)  {
            canvas.drawCircle(getWidth()/2, getHeight()/2,
                    Math.max(getWidth(), getHeight())/4, mPaint);
        }
    }
}
