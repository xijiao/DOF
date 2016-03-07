/**
 * Created by xijiao on 15/11/8.
 *
 * Handles unit converstion。
 * You can choose between metric and imperial.
 * The class chooses proper unit depend on the magnitude.
 */

package info.xijiao.dof.util;

import android.content.Context;

import java.text.DecimalFormat;

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

    public String getCompatDistanceText(Context context, double v) {
        if (v == Double.POSITIVE_INFINITY) {
            return context.getString(R.string.infinity);
        }
        else if (v == Double.NEGATIVE_INFINITY) {
            return context.getString(R.string.negative_infinity);
        }
        else if (v == Double.NaN) {
            return context.getString(R.string.no_a_number);
        }

        DecimalFormat formater = new DecimalFormat("#.##");
        if (v < 0.01f) {
            return String.format("%s%s", formater.format(v * 1000.0),
                    context.getResources().getString(R.string.millimeter));
        } else if (v < 1.0f) {
            return String.format("%s%s", formater.format(v * 100.0),
                    context.getResources().getString(R.string.centimeter));
        } else if (v < 1000f) {
            return String.format("%s%s", formater.format(v),
                    context.getResources().getString(R.string.meter));
        } else {
            return String.format("%s%s", formater.format(v / 1000.0),
                    context.getResources().getString(R.string.kilometer));
        }
    }
}
