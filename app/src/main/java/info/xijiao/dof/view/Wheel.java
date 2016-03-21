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
    public interface WheelAdapter {
        int getCount();
        String getItemText(int position);
    }
    public interface OnScrollListener {
        void onWheelScroll(int position, float offset);
    }

    class DummyAdapter implements WheelAdapter {
        @Override
        public int getCount() {
            return 10;
        }
        @Override
        public String getItemText(int position) {
            return String.format("%d", position);
        }
    }

    private WheelAdapter mAdapter = new DummyAdapter();
    private OnScrollListener mScrollListener;
    private GestureDetector mGestureDetector;
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

        mPainter.setTextAlign(Paint.Align.LEFT);
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

        canvas.drawLine(getWidth() / 2.f, 0.f, getWidth() / 2.f, (getHeight() - mTextSize) / 4.f,
                mPainter);

        int firstShowPosition = (int)Math.floor((mLeftBoundary - mFirstOffset) / mNodeWidth - 0.5f);
        int lastShowPosition = (int)Math.ceil((mRightBoundary - mFirstOffset) / mNodeWidth + 0.5f);
        firstShowPosition = Math.max(0, firstShowPosition);
        lastShowPosition = Math.min(mAdapter.getCount() - 1, lastShowPosition);

        for (int i = firstShowPosition; i <= lastShowPosition; i++) {
            String text = mAdapter.getItemText(i);
            float x = mFirstOffset + i * mNodeWidth + getWidth() / 2.f;
            canvas.drawText(text,
                    x,
                    getHeight() / 2.f + mTextSize / 2.f,
                    mPainter);
            canvas.drawLine(x, 0, x, getHeight(), mPainter);
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
        if (mFirstOffset > 0) mFirstOffset = 0;
        int maxOffset = (int)((mAdapter.getCount() - 1) * mNodeWidth);
        if (-mFirstOffset > maxOffset) {
            mFirstOffset = -maxOffset;
        }

        if (mScrollListener != null) {
            mScrollListener.onWheelScroll(getPosition(), getOffset());
        }

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
        int pos = (int)Math.floor(-mFirstOffset / mNodeWidth);
        return pos;
    }

    // Return the offset of cur node, [0.0, 1.0]
    float getOffset() {
        double value = -mFirstOffset / mNodeWidth;
        return (float)(value - Math.floor(value));
    }

    public void setAdapter(WheelAdapter adapter) {
        mAdapter = adapter;
        if (mAdapter == null) {
            mAdapter = new DummyAdapter();
        }
        invalidate();
    }

    public void setScrollListener(OnScrollListener listener) {
        mScrollListener = listener;
    }
}
