/**
 * Created by xijiao on 15/11/12.
 * A Widget for accurately and quickly adjusting for a large scale.
 */

package info.xijiao.dof.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import info.xijiao.dof.R;

public class Wheel extends View
        implements GestureDetector.OnGestureListener {
    public interface WheelAdaptor {
        int getSize();
        String getDescription(int position);
    };

    private WheelAdaptor mAdaptor;
    GestureDetector mGestureDetector;
    private float mFirstOffset = 0.f;
    private float mNodeWidth;
    private float mTextSize;
    private Paint mPainter = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Wheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Wheel,
                0, 0);
        try {
            mNodeWidth = a.getDimension(R.styleable.Wheel_nodeWidth, 60);
            mTextSize = a.getDimension(R.styleable.Wheel_nodeTextSize, 20);
        } finally {
            a.recycle();
        }

        mPainter.setTextAlign(Paint.Align.CENTER);
        mPainter.setTextSize(mTextSize);

        mGestureDetector = new GestureDetector( context, this );
        mGestureDetector.setIsLongpressEnabled( false );
        setFocusable( true );
        setFocusableInTouchMode( true );
    }

    @Override
    public void onDraw(Canvas canvas) {
        float mLeftBoundary = - getWidth() / 2.f;
        float mRightBoundary = getWidth() / 2.f;

        int size = 10; // TODO
        int firstShowPosition = (int)Math.floor((mLeftBoundary - mFirstOffset) / mNodeWidth - 0.5f);
        int lastShowPosition = (int)Math.ceil((mRightBoundary - mFirstOffset) / mNodeWidth + 0.5f);
        firstShowPosition = Math.max(0, firstShowPosition);
        lastShowPosition = Math.min(size - 1, lastShowPosition);

        for (int i = firstShowPosition; i <= lastShowPosition; i++) {
            String text = String.format("%d", i);
            canvas.drawText(text,
                    mFirstOffset + i * mNodeWidth + getWidth() / 2.f,
                    getHeight() / 2.f + mTextSize / 2.f,
                    mPainter);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    @Override
    protected void onLayout( boolean changed, int left, int top, int right, int bottom ) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        return;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mFirstOffset -= distanceX;

        invalidate();
        return true;
    }
    @Override
    public void onShowPress(MotionEvent e) {

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    int getPosition() {
        int value = (int)Math.rint(-mFirstOffset / mNodeWidth);
        value = Math.max(0, value);
        if (mAdaptor != null) {
            value = Math.min(mAdaptor.getSize() - 1, value);
        }
        return value;
    }

    // Return the offset of cur node, [-0.5, 0.5]
    float getOffset() {
        double value = Math.rint(-mFirstOffset / mNodeWidth);
        return (float)(value - Math.rint(value));
    }


    public void setAdaptor(WheelAdaptor adaptor) {
        mAdaptor = adaptor;
        invalidate();
    }
}
