package com.android.systemui.nezuko;

import android.annotation.NonNull;
import android.app.WallpaperManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.palette.graphics.Palette;
import androidx.core.graphics.ColorUtils;
import android.provider.Settings;
import android.util.Log;
import android.content.om.IOverlayManager;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import com.legion.support.monet.colorgiber;

public class MonetWatcher {

    private WallpaperManager mWallManager;
    private static int fallbackColor = 0xFFFF4081;
    private IOverlayManager mOverlayManager;
    private static final String ACCENT_COLOR_PROP = "persist.sys.theme.accentcolor";
    colorgiber cg = new colorgiber();

    private boolean isMonetEnabled(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.MONET_TOGGLE, 1) == 1;
    }

    public MonetWatcher(@NonNull Context context) {
        WallpaperManager wm = WallpaperManager.getInstance(context);
        wm.addOnColorsChangedListener((colors, which) -> update(context),
            new Handler(context.getMainLooper()), UserHandle.USER_CURRENT);
        if(isMonetEnabled(context)){
            update(context);
        }
    }

    private void update(Context context) {
        String hexColorXD = String.format("%08x", (0xFFFFFFFF & cg.noSysPriviledgeMoment(1, 5, context)));
        int wallColor = cg.noSysPriviledgeMoment(1, 5, context);
        System.putIntForUser(context.getContentResolver(),System.ACCENT_COLOR, wallColor, UserHandle.USER_CURRENT);
        setAccentColor(hexColorXD, context);
    }

    private void setAccentColor(String colorHex, Context context) {
        String accentVal = SystemProperties.get(ACCENT_COLOR_PROP);
        if (!accentVal.equals(colorHex)) {
            SystemProperties.set(ACCENT_COLOR_PROP, colorHex);
            try {
                mOverlayManager.reloadAndroidAssets(UserHandle.USER_CURRENT);
                mOverlayManager.reloadAssets("com.android.settings", UserHandle.USER_CURRENT);
                mOverlayManager.reloadAssets("com.android.systemui", UserHandle.USER_CURRENT);
            }
            catch (Exception e) { }
        }
    }
}
