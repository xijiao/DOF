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

    public static UNIT_SYSTEM sUnitType = UNIT_SYSTEM.METRIC;

    public static void setUnitSystem(UNIT_SYSTEM unitType) {
        sUnitType = unitType;
    }

    public static float getDistanceUnitLength(DISTANCE_UNIT distanceUnit) {
        return DISTANCE_UNIT_LENGTH[distanceUnit.ordinal()];
    }
    public static String getDistanceUnitName(Context context, DISTANCE_UNIT distanceUnit) {
        return context.getString(DISTANCE_UNIT_NAME[distanceUnit.ordinal()]);
    }

    public static String getDistanceText(Context context, float distance) {
        if (distance == Float.POSITIVE_INFINITY) {
            return context.getString(R.string.infinity);
        }
        else if (distance == Float.NEGATIVE_INFINITY) {
            return context.getString(R.string.negative_infinity);
        }
        else if (distance == Float.NaN) {
            return context.getString(R.string.no_a_number);
        }

        if (sUnitType == UNIT_SYSTEM.METRIC) {
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
}
