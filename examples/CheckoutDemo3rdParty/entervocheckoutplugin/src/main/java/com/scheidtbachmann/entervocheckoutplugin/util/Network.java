package com.scheidtbachmann.entervocheckoutplugin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;

public class Network {

    public static boolean isConnectedToNetwork() {

        return true;
        /*
        Context context = Plugin.getInstance().getContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        */
    }
}