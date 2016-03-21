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
