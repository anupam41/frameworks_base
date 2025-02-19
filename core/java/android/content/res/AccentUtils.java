package android.content.res;

import android.app.ActivityThread;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/** @hide */
public class AccentUtils {
    private static final String TAG = "AccentUtils";

    private static ArrayList<String> accentResources = new ArrayList<>(
            Arrays.asList("accent_device_default",
                    "accent_device_default_light",
                    "accent_device_default_dark",
                    "accent_material_light",
                    "accent_material_dark",
                    "biometric_dialog_accent",
                    "colorAccent",
                    "system_notification_accent_color",
                    "alert_dialog_color_accent_light",
                    "alert_dialog_color_accent_dark",
                    "lockscreen_clock_accent_color",
                    "dismiss_all_icon_color",
                    "material_pixel_blue_dark",
                    "material_pixel_blue_bright",
                    "omni_color5",
                    "omni_color4",
                    "oneplus_accent_color",
                    "oneplus_accent_text_color",
                    "dialer_theme_color",
                    "dialer_theme_color_dark",
                    "dialer_theme_color_20pct",
                    "gradient_start"));

    private static ArrayList<String> gradientResources = new ArrayList<>(
            Arrays.asList("settings_accent_color",
                    "settingsHeaderColor",
                    "bootleg_accent_gradient_end_color",
                    "gradient_end"));

    private static final String ACCENT_COLOR_PROP = "persist.sys.theme.accentcolor";
    private static final String GRADIENT_COLOR_PROP = "persist.sys.theme.gradientcolor";

    private static final String UNIVERSAL_DISCO = "universal_disco";

    static boolean isResourceAccent(String resName) {
        for (String ar : accentResources)
            if (resName.contains(ar))
                return true;
        return false;
    }

    static boolean isResourceGradient(String resName) {
        for (String gr : gradientResources)
            if (resName.contains(gr))
                return true;
        return false;
    }

    public static int getNewAccentColor(int defaultColor) {
        return getAccentColor(defaultColor, ACCENT_COLOR_PROP);
    }

    public static int getNewGradientColor(int defaultColor) {
        return getAccentColor(defaultColor, GRADIENT_COLOR_PROP);
    }

    private static int getAccentColor(int defaultColor, String property) {
        final Context context = ActivityThread.currentApplication();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (cal.get(Calendar.MONTH) == 3 && cal.get(Calendar.DAY_OF_MONTH) == 1 ||
            Settings.Secure.getInt(context.getContentResolver(), UNIVERSAL_DISCO, 0) == 1) {
            return ColorUtils.genRandomAccentColor(property == ACCENT_COLOR_PROP);
        }
        try {
            String colorValue = SystemProperties.get(property, "-1");
            return "-1".equals(colorValue)
                    ? defaultColor
                    : Color.parseColor("#" + colorValue);
        } catch (Exception e) {
            Log.e(TAG, "Failed to set accent: " + e.getMessage() +
                    "\nSetting default: " + defaultColor);
            return defaultColor;
        }
    }
}
