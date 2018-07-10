package com.scheidtbachmann.entervocheckoutplugin.client;

import android.content.Context;
import android.provider.Settings;

import com.scheidtbachmann.entervocheckoutplugin.BuildConfig;
import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;
import com.scheidtbachmann.entervocheckoutplugin.util.Crypt;

public class ClientUtil {

    public static String extendedCustomerId() {

        Context context = Plugin.getInstance().getContext();
        String idForVendor  = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String appBundleId  = BuildConfig.PLUGIN_BUNDLE_ID;
        String epoch        = Long.toString( System.currentTimeMillis());
        return Crypt.MD5( idForVendor + appBundleId + epoch);
    }
}