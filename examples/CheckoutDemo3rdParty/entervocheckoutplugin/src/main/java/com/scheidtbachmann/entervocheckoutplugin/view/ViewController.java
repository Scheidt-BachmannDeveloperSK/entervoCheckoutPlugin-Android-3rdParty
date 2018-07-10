package com.scheidtbachmann.entervocheckoutplugin.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;

import com.scheidtbachmann.entervocheckoutplugin.assets.TemplateEngine;
import com.scheidtbachmann.entervocheckoutplugin.client.ClientUtil;
import com.scheidtbachmann.entervocheckoutplugin.client.TokenClient;
import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;
import com.scheidtbachmann.entervocheckoutplugin.core.SBCheckOut;
import com.scheidtbachmann.entervocheckoutplugin.delegation.SBCheckOutStatus;
import com.scheidtbachmann.entervocheckoutplugin.dto.ClassificationResult;
import com.scheidtbachmann.entervocheckoutplugin.localization.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class ViewController {

    public static final String HAPPY_SMILEY = "happy_smiley.png";
    public static final String SAD_SMILEY = "sad_smiley.png";
    public static final String STATUS_PAGE = "status";
    public static final String PRICE_PAGE = "price";

    public static void showSpinner( final String prompt) {

        Context context = Plugin.getInstance().getContext();

        if ( context != null) {

            final Activity act = (Activity) context;
            act.runOnUiThread( new Runnable() { public void run(){Plugin.getInstance().setSpinner(ProgressDialog.show(act, "",
                    prompt, true));}});

        }

    }

    public static void showSpinner() {
        ViewController.showSpinner( "");
    }

    public static void hideSpinner() {


        if ( Plugin.getInstance().getSpinner() != null) {
            Plugin.getInstance().getSpinner().dismiss();
        }

    }

    public static void showPrice() {

        Plugin.switchStatus(SBCheckOutStatus.PRICE_DISPLAY_STARTED);
        Plugin.switchStatus(SBCheckOutStatus.PREPARATION_STARTED);
        String customerId = ClientUtil.extendedCustomerId();
        Plugin.getInstance().getClassificationResult().setCustomer_id( customerId);
        new TokenClient().retrieveClientToken( customerId);

        String price = String.valueOf( Plugin.getInstance().getClassificationResult().getAmount());
        String[] data = { "price", price};
        TemplateEngine engine = new TemplateEngine( PRICE_PAGE, data);
        String contents = engine.merge();
        contents = engine.merge( contents, Plugin.getInstance().getConfig().asDictionary());
        contents = engine.merge( contents, classificationAsDictionary());
        final WebView pluginView = Plugin.getInstance().getPluginView();
        final String displayData = contents;

        if ( pluginView != null) {
            pluginView.post(new Runnable() {
                public void run() {
                    pluginView.loadDataWithBaseURL(
                            getBaserUrl(),
                            displayData,
                            "text/html",
                            "utf-8",
                            null);
                }
            });
        }
    }


    public static void showStatus( String status, boolean smile) {

        Plugin.switchStatus(SBCheckOutStatus.STATUS_DISPLAY_STARTED);
        String[] data = {"status", status, "OK", Text.get( Text.OK)};
        TemplateEngine engine = new TemplateEngine(STATUS_PAGE, data);
        String contents = engine.merge();
        contents = engine.merge(contents, "smiley", smile ? HAPPY_SMILEY : SAD_SMILEY);
        contents = engine.merge(contents, "smileytitle", Text.get( smile ? Text.THANK_YOU : Text.OOPS));
        contents = engine.merge(contents, Plugin.getInstance().getConfig().asDictionary());
        final String page = contents;
        Plugin.getInstance().getPluginView()
                .post(new Runnable() {  public void run() {
                    Plugin.getInstance().getPluginView().loadDataWithBaseURL(
                            ViewController.getBaserUrl(), page, "text/html", "utf-8", null);
                }});
    }

    public static void removePluginView() {

        View view = Plugin.getInstance().getPluginView();
        int anim = Plugin.getInstance().getExitAnimation();
        FragmentActivity activity = (FragmentActivity) (view.getContext());
        if ( activity != null) {
            Fragment frag = activity.getSupportFragmentManager().findFragmentByTag(SBCheckOut.PLUGIN_FRAGEMENT_TAG);
            if ( frag != null) {

                if ( anim != -1) {

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(anim, anim)
                            .detach(frag)
                            .remove(frag)
                            .commit();
                } else {

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .detach(frag)
                            .remove(frag)
                            .commit();
                }
            }
            Plugin.getInstance().setContext(null);
        }
    }

    private static String getBaserUrl() {
        String baseUrl = "file://" + Plugin.getInstance().getFileManager().documentRootUrl() + File.separator;
        return baseUrl;
    }

    private static HashMap<String,String> classificationAsDictionary() {

        HashMap<String,String> dict = new HashMap<String,String>();
        ClassificationResult data = Plugin.getInstance().getClassificationResult();
        dict.put( "facility", data.getFacility());
        dict.put( "CURRENT_DATE", niceDateTime());
        dict.put( "LABEL_PARKING_DURATION", Text.get( Text.LABEL_PARKING_DURATION));
        dict.put( "PARKING_DURATION", formattedDuration( data.getDuration()));
        dict.put( "LABEL_AMOUNT_DUE", Text.get( Text.LABEL_AMOUNT_DUE));
        dict.put( "AMOUNT_DUE", String.valueOf( data.getAmount()) + " " + data.getCurrency());
        dict.put( "CANCEL", Text.get( Text.CANCEL));
        dict.put( "PAY_NOW", Text.get( Text.PAY_NOW));
        return dict;
    }

    private static String niceDateTime() {

        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format( now);
    }

    private static String formattedDuration( String duration) {
        String days     = duration.substring( 0, 2);
        String hours    = duration.substring( 3, 5);
        String minutes  = duration.substring( 6, 8);
        return days + " " + Text.get( Text.FMT_DAYS) + " " + hours + ":" + minutes + Text.get( Text.FMT_HOURS);
    }
}

