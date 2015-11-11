package info.xijiao.dof;

/**
 * Created by xijiao on 2015/10/31.
 *
 * Calculate and store the data for display.
 */

public class DepthOfFieldCalculator {
    final float MAX_DISTANCE = 100.0f;
    final int MAX_DISTANCE_PROGRESS = 10000;
    final float APETURE_AVS[] = {-0.15f, 0.0f, 0.3f, 0.6f, 1.0f, 1.3f, 1.6f, 2.0f, 2.3f, 2.6f,
            3.0f, 3.3f, 3.6f, 4.0f, 4.3f, 4.6f, 5.0f, 5.3f, 5.6f, 6.0f, 7.3f, 7.6f, 8.0f,
            8.3f, 8.6f, 9.0f, 9.3f, 9.6f, 10.0f, 10.3f, 10.6f, 11.0f};
    final float CIRCLE_OF_CONFUSION[] = {0.011f, 0.015f, 0.018f, 0.018f, 0.019f, 0.023f, 0.029f,
            0.047f, 0.053f, 0.059f, 0.067f, 0.083f, 0.12f, 0.11f, 0.15f, 0.22f}; // in millimeter

    float DISTANCE_UNIT_LENGTH[] = {
            1.0f,
            0.01f,
            0.3048f,
            0.0254f,
    };
    int DISTANCE_UNIT_NAME[] = {
            R.string.meter,
            R.string.centimeter,
            R.string.feet,
            R.string.inch,
    };

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
