/**
 * Created by xijiao on 2015/10/31.
 *
 * Calculate and store the data for display.
 */

package info.xijiao.dof.model;

import info.xijiao.dof.Constants;

public class DepthOfFieldCalculator implements Constants {
    private Double[] mDistanceList;
    private Double[] mApertureList;
    private Double[] mFocalList;

    private double mCurDistance;
    private double mCurAperture;
    private double mCurFocal;

    private int mDistanceUnitIndex;
    private int mCircleOfConfusionIndex;

    public DepthOfFieldCalculator(Double[] distance_list, Double[] aperture_list,
                                  Double[] focal_list) {
        mDistanceList = distance_list;
        mApertureList = aperture_list;
        mFocalList = focal_list;
        mCurDistance = distance_list[0];
        mCurAperture = aperture_list[0];
        mCurFocal = focal_list[0];
        mDistanceUnitIndex = 0;
        mCircleOfConfusionIndex = 5;
    }

    public double getCurFocal() {
        return mCurFocal;
    }
    public double getFocalAtIndex(int position) { return mFocalList[position]; }
    public int getFocalCount() { return mFocalList.length; }

    public double getCurAperture() {
        return mCurAperture;
    }
    public double getApertureAtIndex(int position) { return mApertureList[position]; }
    public int getApertureCount() { return mApertureList.length; }

    public int getDistanceCount() {
        return mDistanceList.length;
    }
    public double getDistanceAtIndex(int position) {
        return mDistanceList[position];
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
    public void setAperturePosition(int position, double offset) {
        if (position >= mApertureList.length - 1) {
            mCurAperture = mApertureList[mApertureList.length - 1];
            return;
        }

        double lower = mApertureList[position];
        double upper = mApertureList[position + 1];
        double lower_derivative = position <= 0 ? 0.0f : lower - mApertureList[position - 1];
        double upper_derivative = upper - lower;

        mCurAperture = getSmoothedValue(lower, upper, lower_derivative, upper_derivative, offset);
    }
    public void setFocalPosition(int position, double offset) {
        if (position >= mFocalList.length - 1) {
            mCurFocal = mFocalList[mFocalList.length - 1];
            return;
        }

        double lower = mFocalList[position];
        double upper = mFocalList[position + 1];
        double lower_derivative = position <= 0 ? 0.0f : lower - mFocalList[position - 1];
        double upper_derivative = upper - lower;

        mCurFocal = getSmoothedValue(lower, upper, lower_derivative, upper_derivative, offset);
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
