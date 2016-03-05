package info.xijiao.dof;

/**
 * Put constant definitions here.
 *
 * Created by xijiao on 3/5/16.
 */
public interface Constants {
    enum UNIT_SYSTEM {
        METRIC,
        IMPERIAL
    }

    enum DISTANCE_UNIT {
        METERS,
        CENTIMETERS,
        FEET,
        INCHES,
    }

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

}
