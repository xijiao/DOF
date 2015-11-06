package info.xijiao.dof;

/**
 * Created by xijiao on 2015/10/31.
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

    int minFocal_;
    int maxFocal_;
    int curFocal_;
    int minApertureStep_;
    int maxApertureStep_;
    int curApertureStep_;
    float curDistance_;
    int distanceUnitIndex_;
    int circleOfConfusionIndex_;

    public DepthOfFieldCalculator(int minFocal, int maxFocal) {
        minFocal_ = minFocal;
        maxFocal_ = maxFocal;
        curFocal_ = minFocal;
        minApertureStep_ = 0;
        maxApertureStep_ = APETURE_AVS.length - 1;
        curApertureStep_ = 0;
        curDistance_ = 0.0f;
        distanceUnitIndex_ = 0;
        circleOfConfusionIndex_ = 0;
    }

    public int getFocalBarMax() {
        return maxFocal_ - minFocal_;
    }
    public int getFocalBarProgress() {
        return curFocal_ - minFocal_;
    }
    public void setFocalBarProgress(int progress) {
        curFocal_ = minFocal_ + progress;
    }
    public float getCurFocal() {
        return curFocal_;
    }
    public String getCurFocalText() {
        return String.format("%dmm", curFocal_);
    }

    public int getApertureBarMax() {
        return maxApertureStep_ - minApertureStep_;
    }
    public void setApertureBarProgress(int progress) {
        curApertureStep_ = Math.min(minApertureStep_ + progress, maxApertureStep_);
    }
    public float getCurAperture() {
        return (float)Math.pow(Math.pow(2.0f, 0.5f), APETURE_AVS[curApertureStep_]);
    }
    public String getCurApertureText() {
        return String.format("F/%.2g", Math.pow(Math.pow(2.0f, 0.5f), APETURE_AVS[curApertureStep_]));
    }

    public int getApertureBarProgress() {
        return curApertureStep_ - minApertureStep_;
    }
    public float getDistanceUnit() {
        return DISTANCE_UNIT_LENGTH[distanceUnitIndex_];
    }
    public String getDistanceUnitName() {
        return MainActivity.activity.getString(DISTANCE_UNIT_NAME[distanceUnitIndex_]);
    }
    public int getDistanceUnitIndex() {
        return distanceUnitIndex_;
    }
    public void setDistanceUnitIndex(int value) {
        distanceUnitIndex_ = value;
    }
    public int getDistanceBarMax() {
        return MAX_DISTANCE_PROGRESS;
    }
    public int getDistanceBarProgress() {
        return (int)(curDistance_ / MAX_DISTANCE * MAX_DISTANCE_PROGRESS);
    }
    public void setDistanceBarProgress(int progress) {
        curDistance_ = (float)progress / MAX_DISTANCE_PROGRESS * MAX_DISTANCE;
    }
    public float getCurDistance() {
        return curDistance_;
    }
    public String getCurDistanceText() {
        return String.format("%.1f%s", curDistance_ / getDistanceUnit(), getDistanceUnitName());
    }
    public float getCircleOfConfusion() {
        return CIRCLE_OF_CONFUSION[circleOfConfusionIndex_];
    }
    public int getCircleOfConfusionIndex() {
        return circleOfConfusionIndex_;
    }
    public void setCircleOfConfusionIndex(int value) {
        circleOfConfusionIndex_ = value;
    }

    public double getDepthOfField() {
        double F = getCurAperture();
        double f = getCurFocal() / 1000.0f; // mm->m
        double L = getCurDistance();
        double c = getCircleOfConfusion() / 1000.0f;
        double denominator = f * f * f * f - F * F * c * c * L * L;
        if (denominator <= 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        else {
            return 2 * f * f * F * c * L * L / denominator;
        }
    }
    public double getNearDepthOfField() {
        double F = getCurAperture();
        double f = getCurFocal() / 1000.0f;
        double L = getCurDistance();
        double c = getCircleOfConfusion() / 1000.0f;
        return F * c * L * L /
                (f * f + F * c * L);
    }
    public double getFarDepthOfField() {
        double F = getCurAperture();
        double f = getCurFocal() / 1000.0f;
        double L = getCurDistance();
        double c = getCircleOfConfusion() / 1000.0f;
        double denominator = f * f - F * c * L;
        if (denominator <= 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        else {
            return F * c * L * L / denominator;
        }
    }

    public String getDepthOfFieldText() {
        double dof = getDepthOfField();
        if (dof == Double.POSITIVE_INFINITY) {
            return MainActivity.activity.getString(R.string.infinity);
        }
        else {
            return String.format("%.3f%s", dof / getDistanceUnit(),
                    getDistanceUnitName());
        }
    }
    public String getNearDepthOfFieldText() {
        double ndof = getNearDepthOfField();
        if (ndof == Double.POSITIVE_INFINITY) {
            return MainActivity.activity.getString(R.string.infinity);
        }
        else {
            return String.format("%.3f%s", ndof / getDistanceUnit(),
                    getDistanceUnitName());
        }
    }
    public String getFarDepthOfFieldText() {
        double fdof = getFarDepthOfField();
        if (fdof == Double.POSITIVE_INFINITY) {
            return MainActivity.activity.getString(R.string.infinity);
        }
        else {
            return String.format("%.3f%s", fdof / getDistanceUnit(),
                    getDistanceUnitName());
        }
    }
}
