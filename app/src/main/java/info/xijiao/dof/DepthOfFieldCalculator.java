package info.xijiao.dof;

/**
 * Created by xijiao on 2015/10/31.
 */
public class DepthOfFieldCalculator {
    final float MAX_DISTANCE = 100.0f;
    final float APETURE_AVS[] = {-0.15f, 0.0f, 0.3f, 0.6f, 1.0f, 1.3f, 1.6f, 2.0f, 2.3f, 2.6f,
            3.0f, 3.3f, 3.6f, 4.0f, 4.3f, 4.6f, 5.0f, 5.3f, 5.6f, 6.0f, 7.3f, 7.6f, 8.0f};
    int minFocal_;
    int maxFocal_;
    int curFocal_;
    int minApertureStep_;
    int maxApertureStep_;
    int curApertureStep_;
    float curDistance_;
    float distanceUnit_;

    public DepthOfFieldCalculator(int minFocal, int maxFocal) {
        minFocal_ = minFocal;
        maxFocal_ = maxFocal;
        curFocal_ = minFocal;
        minApertureStep_ = 0;
        maxApertureStep_ = APETURE_AVS.length - 1;
        curApertureStep_ = 0;
        curDistance_ = 0.0f;
        distanceUnit_ = 0.01f;
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
    public String getCurFocalText() {
        return String.format("%dmm", curFocal_);
    }

    public int getApertureBarMax() {
        return maxApertureStep_ - minApertureStep_;
    }
    public int getApetureBarProgress() {
        return curApertureStep_ - minApertureStep_;
    }
    public void setApertureBarProgress(int progress) {
        curApertureStep_ = Math.min(minApertureStep_ + progress, maxApertureStep_);
    }
    public String getCurApertureText() {
        return String.format("F/%.2g", Math.pow(Math.pow(2.0f, 0.5f), APETURE_AVS[curApertureStep_]));
    }

    public int getApertureBarProgress() {
        return curApertureStep_ - minApertureStep_;
    }
    public int getDistanceBarMax() {
        return (int)(MAX_DISTANCE / distanceUnit_);
    }
    public int getDistanceBarProgress() {
        return (int)(curDistance_ / distanceUnit_);
    }
    public void setDistanceBarProgress(int progress) {
        curDistance_ = progress * distanceUnit_;
    }
    public String getCurDistanceText() {
        return String.format("%gcm", curDistance_);
    }
}
