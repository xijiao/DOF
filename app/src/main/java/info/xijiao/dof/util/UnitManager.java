/**
 * Created by xijiao on 15/11/8.
 *
 * Handles unit converstion。
 * You can choose between metric and imperial.
 * The class chooses proper unit depend on the magnitude.
 */

package info.xijiao.dof.util;

import android.content.Context;

import info.xijiao.dof.R;
import info.xijiao.dof.Constants;

public class UnitManager implements Constants {
    private static UnitManager instance = new UnitManager();

    public static UNIT_SYSTEM mUnitType = UNIT_SYSTEM.METRIC;

    private UnitManager() {}

    public static UnitManager getInstance() {
        return instance;
    }

    public void setUnitSystem(UNIT_SYSTEM unitType) {
        mUnitType = unitType;
    }

    public float getDistanceUnitLength(DISTANCE_UNIT distanceUnit) {
        return DISTANCE_UNIT_LENGTH[distanceUnit.ordinal()];
    }
    public String getDistanceUnitName(Context context, DISTANCE_UNIT distanceUnit) {
        return context.getString(DISTANCE_UNIT_NAME[distanceUnit.ordinal()]);
    }

    public String getDistanceText(Context context, float distance) {
        if (distance == Float.POSITIVE_INFINITY) {
            return context.getString(R.string.infinity);
        }
        else if (distance == Float.NEGATIVE_INFINITY) {
            return context.getString(R.string.negative_infinity);
        }
        else if (distance == Float.NaN) {
            return context.getString(R.string.no_a_number);
        }

        if (mUnitType == UNIT_SYSTEM.METRIC) {
            if (distance >= getDistanceUnitLength(DISTANCE_UNIT.METERS)) {
                return String.format("%.2f%s",
                        distance / getDistanceUnitLength(DISTANCE_UNIT.METERS),
                        getDistanceUnitName(context, DISTANCE_UNIT.METERS));
            }
            else {
                return String.format("%.3g%s",
                        distance / getDistanceUnitLength(DISTANCE_UNIT.CENTIMETERS),
                        getDistanceUnitName(context, DISTANCE_UNIT.CENTIMETERS));

            }
        }
        else {
            if (distance >= getDistanceUnitLength(DISTANCE_UNIT.FEET)) {
                return String.format("%.2f%s",
                        distance / getDistanceUnitLength(DISTANCE_UNIT.FEET),
                        getDistanceUnitName(context, DISTANCE_UNIT.FEET));
            }
            else {
                return String.format("%.3g%s",
                        distance / getDistanceUnitLength(DISTANCE_UNIT.INCHES),
                        getDistanceUnitName(context, DISTANCE_UNIT.INCHES));

            }
        }
    }

    public String getCompatDistanceText(Context context, float v) {
        if (v < 0.01f) {
            return String.format("%.0f%s", v * 1000.0f, context.getResources().getString(R.string.millimeter));
        } else if (v < 1.0f) {
            return String.format("%.0f%s", v * 100.0f, context.getResources().getString(R.string.centimeter));
        } else if (v < 1000f) {
            return String.format("%.0f%s", v, context.getResources().getString(R.string.meter));
        } else {
            return String.format("%.0f%s", v / 1000.0f, context.getResources().getString(R.string.kilometer));
        }
    }
}
