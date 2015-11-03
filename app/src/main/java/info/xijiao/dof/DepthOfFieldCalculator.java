package info.xijiao.dof;

/**
 * Created by xijiao on 2015/10/31.
 */

public class DepthOfFieldCalculator {
    final float MAX_DISTANCE = 100.0f;
    final float APETURE_AVS[] = {-0.15f, 0.0f, 0.3f, 0.6f, 1.0f, 1.3f, 1.6f, 2.0f, 2.3f, 2.6f,
            3.0f, 3.3f, 3.6f, 4.0f, 4.3f, 4.6f, 5.0f, 5.3f, 5.6f, 6.0f, 7.3f, 7.6f, 8.0f,
            8.3f, 8.6f, 9.0f, 9.3f, 9.6f, 10.0f, 10.3f, 10.6f, 11.0f};

    enum DISTANCE_UNIT_TYPE {
        METERS,
        CENTIMETERS,
        FEET,
        INCHES,
    }
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
    DISTANCE_UNIT_TYPE distanceUnit_;
    float circleOfConfusion_;

    public DepthOfFieldCalculator(int minFocal, int maxFocal) {
        minFocal_ = minFocal;
        maxFocal_ = maxFocal;
        curFocal_ = minFocal;
        minApertureStep_ = 0;
        maxApertureStep_ = APETURE_AVS.length - 1;
        curApertureStep_ = 0;
        curDistance_ = 0.0f;
        distanceUnit_ = DISTANCE_UNIT_TYPE.METERS;
        circleOfConfusion_ = 0.029f;
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
        return DISTANCE_UNIT_LENGTH[distanceUnit_.ordinal()];
    }
    public String getDistanceUnitName() {
        return MainActivity.activity.getString(DISTANCE_UNIT_NAME[distanceUnit_.ordinal()]);
    }
    public int getDistanceBarMax() {
        return (int)(MAX_DISTANCE / getDistanceUnit());
    }
    public int getDistanceBarProgress() {
        return (int)(curDistance_ / getDistanceUnit());
    }
    public void setDistanceBarProgress(int progress) {
        curDistance_ = progress * getDistanceUnit();
    }
    public float getCurDistance() {
        return curDistance_;
    }
    public String getCurDistanceText() {
        return String.format("%.1f%s", curDistance_ / getDistanceUnit(), getDistanceUnitName());
    }

    public float getDepthOfField() {
        float F = getCurAperture();
        float f = getCurFocal() / 1000.0f;
        float L = getCurDistance();
        float c = circleOfConfusion_ / 1000.0f;
        return 2 * f * f * F * c * L * L /
                (f * f * f * f - F * F * c * c * L * L);
    }
    public float getNearDepthOfField() {
        float F = getCurAperture();
        float f = getCurFocal() / 1000.0f;
        float L = getCurDistance();
        float c = circleOfConfusion_ / 1000.0f;
        return F * c * L * L /
                (f * f + F * c * L);
    }
    public float getFarDepthOfField() {
        float F = getCurAperture();
        float f = getCurFocal() / 1000.0f;
        float L = getCurDistance();
        float c = circleOfConfusion_ / 1000.0f;
        return F * c * L * L /
                (f * f - F * c * L);
    }

    public String getDepthOfFieldText() {
        return String.format("%f%s", getDepthOfField() / getDistanceUnit(), getDistanceUnitName());
    }
    public String getNearDepthOfFieldText() {
        return String.format("%f%s", getNearDepthOfField() / getDistanceUnit(), getDistanceUnitName());
    }
    public String getFarDepthOfFieldText() {
        return String.format("%f%s", getFarDepthOfField() / getDistanceUnit(), getDistanceUnitName());
    }
}
