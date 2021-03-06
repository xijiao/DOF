/**
 * Created by xijiao on 15/11/8.
 *
 * The View to show calculated depth of field.
 */

package info.xijiao.dof.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import info.xijiao.dof.R;
import info.xijiao.dof.util.UnitManager;


public class DepthView extends View {
    private double mDepthOfField = 0.4f;
    private double mDistance = 1.f;
    private double mNearLimit = 0.8f;
    private double mFarLimit = 1.2f;
    private Paint mTextPainter;
    private Paint mLinePainter;
    private Drawable mCameraLogo;
    private float mTargetRatio;
    private float mFontSize;

    public DepthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DepthView,
                0, 0);
        try {
            mTargetRatio = a.getFloat(R.styleable.DepthView_targetPosition, 0.333f);
            mFontSize = a.getDimension(R.styleable.DepthView_textSize, 20);
        } finally {
            a.recycle();
        }

        mCameraLogo = ContextCompat.getDrawable(getContext(),
                R.drawable.camera_logo);
        mTextPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPainter.setTextSize(mFontSize);
        mTextPainter.setTextAlign(Paint.Align.CENTER);

        mLinePainter = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Where the camera is.
        mCameraLogo.draw(canvas);

        float beginLine = mCameraLogo.getBounds().right;
        float endLine = getWidth();

        float nearLine;
        float farLine;
        float targetLine;
        targetLine = (endLine - beginLine) * mTargetRatio + beginLine;
        nearLine = (float)(((targetLine - beginLine)* mNearLimit) / mDistance + beginLine);
        if (mFarLimit == Float.POSITIVE_INFINITY || mFarLimit > mDistance / mTargetRatio) {
            farLine = endLine;
        }
        else {
            farLine = (float)((targetLine - beginLine) * mFarLimit / mDistance  + beginLine);
        }

        // draw near line
        canvas.drawLine(nearLine, 0, nearLine, getHeight() - mFontSize, mLinePainter);
        String nearLimitText = UnitManager.getInstance().getCompatDistanceText(getContext(), mNearLimit);
        Rect nearLimitBounds = new Rect();
        mTextPainter.getTextBounds(nearLimitText, 0, nearLimitText.length(), nearLimitBounds);
        if (nearLine - beginLine < nearLimitBounds.width()) {
            mTextPainter.setTextAlign(Paint.Align.LEFT);
        } else {
            mTextPainter.setTextAlign(Paint.Align.RIGHT);
        }
        canvas.drawText(nearLimitText, nearLine, mFontSize, mTextPainter);


        // draw far line
        canvas.drawLine(farLine, 0, farLine, getHeight() - mFontSize, mLinePainter);
        String farLimitText = UnitManager.getInstance().getCompatDistanceText(getContext(), mFarLimit);
        Rect farLimitBounds = new Rect();
        mTextPainter.getTextBounds(farLimitText, 0, farLimitText.length(), farLimitBounds);
        if (endLine - farLine < farLimitBounds.width()) {
            mTextPainter.setTextAlign(Paint.Align.RIGHT);
        }
        else {
            mTextPainter.setTextAlign(Paint.Align.LEFT);
        }
        canvas.drawText(farLimitText, farLine, mFontSize, mTextPainter);

        // draw target position
        canvas.drawLine(targetLine, 0, targetLine, getHeight() - mFontSize, mLinePainter);

        // draw depth of view
        canvas.drawLine(nearLine, getHeight() - mFontSize,
                farLine, getHeight() - mFontSize, mLinePainter);
        mTextPainter.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(UnitManager.getInstance().getCompatDistanceText(getContext(), mDepthOfField),
                (nearLine + farLine) / 2.f, getHeight(), mTextPainter);
    }

    @Override
    public void onLayout( boolean changed, int left, int top, int right, int bottom ) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("Trace", String.format("left=%d, top=%d, right=%d, bottom=%d",
                left, top, right, bottom));
        Log.d("Trace", String.format("leftPadding=%d, rightPadding=%d, " +
                "topPadding=%d, bottomPadding=%d",
                getLeftPaddingOffset(), getRightPaddingOffset(),
                getTopPaddingOffset(), getBottomPaddingOffset()));


        int cameraSize = getHeight() / 4;
        mCameraLogo.setBounds(0, getHeight() / 2 - cameraSize / 2,
                cameraSize, getHeight() / 2 + cameraSize / 2);
    }

    public void setData(double depthOfView, double distance, double nearLimit, double farLimit) {
        mDepthOfField = depthOfView;
        mDistance = distance;
        mNearLimit = nearLimit;
        mFarLimit = farLimit;
        invalidate();
    }
}
