/**
 * Created by xijiao on 2015/10/31.
 *
 * Calculate and store the data for display.
 */

package info.xijiao.dof.model;

import info.xijiao.dof.activity.MainActivity;
import info.xijiao.dof.Constants;

public class DepthOfFieldCalculator implements Constants {
    int mMinFocal;
    int mMaxFocal;
    int mCurFocal;
    int mMinApertureStep;
    int mMaxApertureStep;
    int mCurApertureStep;
    float mCurDistance;
    int mDistanceUnitIndex;
    int mCircleOfConfusionIndex;

    public DepthOfFieldCalculator(int minFocal, int maxFocal) {
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
    public int getDistanceUnitIndex() {
        return mDistanceUnitIndex;
    }
    public void setDistanceUnitIndex(int value) {
        mDistanceUnitIndex = value;
    }
    public int getDistanceBarMax() {
        return MAX_DISTANCE_PROGRESS;
    }
    public int getDistanceBarProgress() {
        return (int)(mCurDistance / MAX_DISTANCE * MAX_DISTANCE_PROGRESS);
    }
    public void setDistanceBarProgress(int progress) {
        mCurDistance = (float)progress / MAX_DISTANCE_PROGRESS * MAX_DISTANCE;
    }
    public float getCurDistance() {
        return mCurDistance;
    }
    public String getCurDistanceText() {
        return String.format("%.1f%s", mCurDistance / getDistanceUnit(), getDistanceUnitName());
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
