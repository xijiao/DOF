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
    private Double[] mDistanceList;
    private int mMinFocal;
    private int mMaxFocal;
    private int mCurFocal;
    private int mMinApertureStep;
    private int mMaxApertureStep;
    private int mCurApertureStep;
    private double mCurDistance;
    private int mDistanceUnitIndex;
    private int mCircleOfConfusionIndex;

    public DepthOfFieldCalculator(int minFocal, int maxFocal) {
        mDistanceList = new Double[1];
        mDistanceList[0] = 0.1;
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
    public double getDistanceAtIndex(int position) {
        return mDistanceList[position];
    }
    public void setDistanceList(Double distantList[]) {
        mDistanceList = distantList;
    }
    public int getDistanceUnitIndex() {
        return mDistanceUnitIndex;
    }
    public void setDistanceUnitIndex(int value) {
        mDistanceUnitIndex = value;
    }
    public double getCurDistance() {
        return mCurDistance;
    }
    public static double getSmoothedValue(double lower, double upper,
                                         double lower_derivative, double upper_derivative,
                                         double offset) {
        double a = lower;
        double b = lower_derivative;
        double c = upper * 3 - upper_derivative - lower * 3l - lower_derivative * 2;
        double d = (-upper * 2) + upper_derivative + (lower * 2) + lower_derivative;
        return (a + b * offset + c * offset * offset + d * offset * offset * offset);
    }
    public void setDistancePosition(int position, double offset) {
        if (position >= mDistanceList.length - 1) {
            mCurDistance = mDistanceList[mDistanceList.length - 1];
            return;
        }

        double lower = mDistanceList[position];
        double upper = mDistanceList[position + 1];
        double lower_derivative = position <= 0 ? 0.0f : lower - mDistanceList[position - 1];
        double upper_derivative = upper - lower;

        mCurDistance = getSmoothedValue(lower, upper, lower_derivative, upper_derivative, offset);
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

    public double getDepthOfField() {
        double F = getCurAperture();
        double f = getCurFocal() / 1000.0f; // mm->m
        double L = getCurDistance();
        double c = getCircleOfConfusion() / 1000.0f;
        double denominator = f * f * f * f - F * F * c * c * L * L;
        if (denominator <= 0.0f) {
            return Double.POSITIVE_INFINITY;
        }
        else {
            return 2 * f * f * F * c * L * L / denominator;
        }
    }
    public double getNearDepthOfField() {
        double F = getCurAperture();
        double f = getCurFocal() / 1000.0;
        double L = getCurDistance();
        double c = getCircleOfConfusion() / 1000.0;
        return F * c * L * L /
                (f * f + F * c * L);
    }
    public double getFarDepthOfField() {
        double F = getCurAperture();
        double f = getCurFocal() / 1000.0;
        double L = getCurDistance();
        double c = getCircleOfConfusion() / 1000.0;
        double denominator = f * f - F * c * L;
        if (denominator <= 0.0f) {
            return Double.POSITIVE_INFINITY;
        }
        else {
            return F * c * L * L / denominator;
        }
    }
}
