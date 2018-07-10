package com.scheidtbachmann.entervocheckoutplugin.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.scheidtbachmann.entervocheckoutplugin.R;
import com.scheidtbachmann.entervocheckoutplugin.assets.AssetManager;
import com.scheidtbachmann.entervocheckoutplugin.client.ConfigClient;
import com.scheidtbachmann.entervocheckoutplugin.delegation.AssetType;
import com.scheidtbachmann.entervocheckoutplugin.delegation.Environment;
import com.scheidtbachmann.entervocheckoutplugin.delegation.IdentificationType;
import com.scheidtbachmann.entervocheckoutplugin.delegation.LogLevel;
import com.scheidtbachmann.entervocheckoutplugin.delegation.SBCheckOutDelegate;
import com.scheidtbachmann.entervocheckoutplugin.delegation.SBCheckOutStatus;
import com.scheidtbachmann.entervocheckoutplugin.localization.Text;
import com.scheidtbachmann.entervocheckoutplugin.view.entervoCheckoutWebViewClient;

public class SBCheckOut extends Fragment {

    public static final String PLUGIN_FRAGEMENT_TAG = "entervo";
    View view;

    public static SBCheckOut newInstance( String apiKey) {

        Plugin.getInstance().setApiKey( apiKey);
        return new SBCheckOut();
    }

    public static SBCheckOut newInstance(String apiKey, Environment environment) {

        Plugin.getInstance().setApiKey( apiKey);
        Plugin.getInstance().setEnvironment(environment);
        return new SBCheckOut();
    }

    @Override
    public void onAttach( Context context) {
        super.onAttach(context);
        Plugin.getInstance().setContext( getContext());
        new ConfigClient().getPluginConfig( Plugin.getInstance().getApiKey());
        Plugin.getInstance().startPendingClassification();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.checkout_fragment, container, false);
        WebView me = (WebView) view.findViewById(R.id.plugin_webview);
        Plugin.getInstance().setPluginView(me);
        me.getSettings().setJavaScriptEnabled(true);
        me.getSettings().setDomStorageEnabled(true);
        me.setWebViewClient( new entervoCheckoutWebViewClient());
        me.loadData( "", "text/html", "utf-8");
        return view;
    }

    public void setDelegate(SBCheckOutDelegate delegate) {
        Plugin.getInstance().setDelegate( delegate);
    }

    public void setAsset(Drawable asset, AssetType assetType) {
        AssetManager.setAsset( asset, assetType);
    }

    public void setAsset( String contents, AssetType assetType) {
        AssetManager.setAsset( contents, assetType);
    }

    public void setLogLevel( LogLevel level) {

    }

    public boolean setLanguage( String lang) {
        return Text.getInstance().setLanguage( lang);
    }

    public void bookPayment( String nonce) {
        Plugin.getInstance().bookPayment( nonce);
    }

    public void cancelPayment() {
        Plugin.getInstance().cancelPayment( true);
    }

    public void start(String identification, IdentificationType identificationType) {

        Plugin.switchStatus(SBCheckOutStatus.FLOW_STARTED);

        if ( Plugin.getInstance().getContext() == null) {

            Plugin.getInstance().setPendingIdentification( identification, identificationType);

        } else {
            Plugin.getInstance().startClassification(identification, identificationType);
        }
    }

    public String version() {
        return Plugin.version();
    }

    public void setOutroAnimation( int anim) {
        Plugin.getInstance().setExitAnimation(anim);
    }
}
