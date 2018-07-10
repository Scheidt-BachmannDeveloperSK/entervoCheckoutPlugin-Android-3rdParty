package com.scheidtbachmann.entervocheckoutplugin.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.scheidtbachmann.entervocheckoutplugin.client.ClassificationClient;
import com.scheidtbachmann.entervocheckoutplugin.client.ClientUtil;
import com.scheidtbachmann.entervocheckoutplugin.client.ConfigClient;
import com.scheidtbachmann.entervocheckoutplugin.client.SaleClient;
import com.scheidtbachmann.entervocheckoutplugin.client.TokenClient;
import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;
import com.scheidtbachmann.entervocheckoutplugin.core.SBCheckOut;
import com.scheidtbachmann.entervocheckoutplugin.delegation.IdentificationType;
import com.scheidtbachmann.entervocheckoutplugin.delegation.SBCheckOutStatus;
import com.scheidtbachmann.entervocheckoutplugin.dto.PluginConfig;

import java.io.File;

public class entervoCheckoutWebViewClient extends WebViewClient {

    public boolean I_WILL_HANDLE_THAT = true;
    public boolean WEBVIEW_WILL_HANDLE_THAT = false;

    @Override
    public void onPageFinished(WebView view, String url) {
        if ( Plugin.getInstance().getContext() != null) {
            Plugin.getInstance().startPendingClassification();
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        String command = getLastPathSegment( url);
        if ( command == null) {
            return WEBVIEW_WILL_HANDLE_THAT;
        }
        if ( command.equalsIgnoreCase("token")) {
            String customerId = ClientUtil.extendedCustomerId();
            Plugin.getInstance().getClassificationResult().setCustomer_id( customerId);
            new TokenClient().retrieveClientToken( customerId);
            return I_WILL_HANDLE_THAT;
        }
        if ( command.equalsIgnoreCase("pay")) {
            Plugin.getInstance().startPayment(
                    Plugin.getInstance().getClassificationResult(), Plugin.getInstance().getClassificationResult().getNonce());
            return I_WILL_HANDLE_THAT;
        }
        if ( command.equalsIgnoreCase("ok")) {
            Plugin.switchStatus(SBCheckOutStatus.STATUS_DISPLAY_FINISHED);
            ViewController.removePluginView();
            return I_WILL_HANDLE_THAT;
        }
        if ( command.equalsIgnoreCase("cancel")) {
            Plugin.switchStatus(SBCheckOutStatus.PRICE_DISPLAY_CANCELLED);
            ViewController.removePluginView();
            Plugin.switchStatus(SBCheckOutStatus.FLOW_FINISHED);
            Plugin.switchStatus(SBCheckOutStatus.IDLE);
            return I_WILL_HANDLE_THAT;
        }
        return WEBVIEW_WILL_HANDLE_THAT;
    }


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        //return super.shouldInterceptRequest( view, url);
        return null;
    }


    private String getLastPathSegment( String url) {
        String[] parts = url.split(File.separator);
        return parts[ parts.length - 1];
    }
}
