/**
 * Created by xijiao on 2015/10/31.
 *
 * Calculate and store the data for display.
 */

package info.xijiao.dof.model;

import java.util.ArrayList;

import info.xijiao.dof.activity.MainActivity;
import info.xijiao.dof.Constants;

public class DepthOfFieldCalculator implements Constants {
    private Float[] mDistanceList;
    private int mMinFocal;
    private int mMaxFocal;
    private int mCurFocal;
    private int mMinApertureStep;
    private int mMaxApertureStep;
    private int mCurApertureStep;
    private float mCurDistance;
    private int mDistanceUnitIndex;
    private int mCircleOfConfusionIndex;

    public DepthOfFieldCalculator(int minFocal, int maxFocal) {
        mDistanceList = new Float[3];
        mDistanceList[0] = 0.1f;
        mDistanceList[1] = 0.2f;
        mDistanceList[2] = 0.5f;
        mMinFocal = minFocal;
        mMaxFocal = maxFocal;
        mCurFocal = minFocal;
        mMinApertureStep = 0;
        mMaxApertureStep = APETURE_AVS.length - 1;
        mCurApertureStep = 0;
        mCurDistance = 0.0f;
        mDistanceUnitIndex = 0;
        mCircleOfConfusionIndex = 0;
    }

    public int getFocalBarMax() {
        return mMaxFocal - mMinFocal;
    }
    public int getFocalBarProgress() {
        return mCurFocal - mMinFocal;
    }
    public void setFocalBarProgress(int progress) {
        mCurFocal = mMinFocal + progress;
    }
    public float getCurFocal() {
        return mCurFocal;
    }
    public String getCurFocalText() {
        return String.format("%dmm", mCurFocal);
    }

    public int getApertureBarMax() {
        return mMaxApertureStep - mMinApertureStep;
    }
    public void setApertureBarProgress(int progress) {
        mCurApertureStep = Math.min(mMinApertureStep + progress, mMaxApertureStep);
    }
    public float getCurAperture() {
        return (float)Math.pow(Math.pow(2.0f, 0.5f), APETURE_AVS[mCurApertureStep]);
    }
    public String getCurApertureText() {
        return String.format("F/%.2g", Math.pow(Math.pow(2.0f, 0.5f), APETURE_AVS[mCurApertureStep]));
    }

    public int getApertureBarProgress() {
        return mCurApertureStep - mMinApertureStep;
    }
    public float getDistanceUnit() {
        return DISTANCE_UNIT_LENGTH[mDistanceUnitIndex];
    }
    public String getDistanceUnitName() {
        return MainActivity.activity.getString(DISTANCE_UNIT_NAME[mDistanceUnitIndex]);
    }
    public int getDistanceCount() {
        return mDistanceList.length;
    }
    public float getDistanceAtIndex(int position) {
        return mDistanceList[position];
    }
    public void setDistanceList(Float distantList[]) {
        mDistanceList = distantList;
    }
    public int getDistanceUnitIndex() {
        return mDistanceUnitIndex;
    }
    public void setDistanceUnitIndex(int value) {
        mDistanceUnitIndex = value;
    }
    public float getCurDistance() {
        return mCurDistance;
    }
    public void setDistancePosition(int position, float offset) {
        if (position >= mDistanceList.length - 1) {
            mCurDistance = mDistanceList[mDistanceList.length - 1];
            return;
        }
        mCurDistance = mDistanceList[position];
    }
    public float getCircleOfConfusion() {
        return CIRCLE_OF_CONFUSION[mCircleOfConfusionIndex];
    }
    public int getCircleOfConfusionIndex() {
        return mCircleOfConfusionIndex;
    }
    public void setCircleOfConfusionIndex(int value) {
        mCircleOfConfusionIndex = value;
    }

    public float getDepthOfField() {
        float F = getCurAperture();
        float f = getCurFocal() / 1000.0f; // mm->m
        float L = getCurDistance();
        float c = getCircleOfConfusion() / 1000.0f;
        float denominator = f * f * f * f - F * F * c * c * L * L;
        if (denominator <= 0.0f) {
            return Float.POSITIVE_INFINITY;
        }
        else {
            return 2 * f * f * F * c * L * L / denominator;
        }
    }
    public float getNearDepthOfField() {
        float F = getCurAperture();
        float f = getCurFocal() / 1000.0f;
        float L = getCurDistance();
        float c = getCircleOfConfusion() / 1000.0f;
        return F * c * L * L /
                (f * f + F * c * L);
    }
    public float getFarDepthOfField() {
        float F = getCurAperture();
        float f = getCurFocal() / 1000.0f;
        float L = getCurDistance();
        float c = getCircleOfConfusion() / 1000.0f;
        float denominator = f * f - F * c * L;
        if (denominator <= 0.0f) {
            return Float.POSITIVE_INFINITY;
        }
        else {
            return F * c * L * L / denominator;
        }
    }
}
