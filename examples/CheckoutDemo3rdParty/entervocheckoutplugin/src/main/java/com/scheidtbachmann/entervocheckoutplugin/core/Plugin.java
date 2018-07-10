package com.scheidtbachmann.entervocheckoutplugin.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.webkit.WebView;

import com.scheidtbachmann.entervocheckoutplugin.BuildConfig;
import com.scheidtbachmann.entervocheckoutplugin.assets.AssetManager;
import com.scheidtbachmann.entervocheckoutplugin.assets.BundleFileManager;
import com.scheidtbachmann.entervocheckoutplugin.client.ClassificationClient;
import com.scheidtbachmann.entervocheckoutplugin.client.ClientUtil;
import com.scheidtbachmann.entervocheckoutplugin.client.SaleClient;
import com.scheidtbachmann.entervocheckoutplugin.client.TokenClient;
import com.scheidtbachmann.entervocheckoutplugin.delegation.Environment;
import com.scheidtbachmann.entervocheckoutplugin.delegation.IdentificationType;
import com.scheidtbachmann.entervocheckoutplugin.delegation.LogLevel;
import com.scheidtbachmann.entervocheckoutplugin.delegation.SBCheckOutDelegate;
import com.scheidtbachmann.entervocheckoutplugin.delegation.SBCheckOutStatus;
import com.scheidtbachmann.entervocheckoutplugin.dto.ClassificationResult;
import com.scheidtbachmann.entervocheckoutplugin.dto.PluginConfig;
import com.scheidtbachmann.entervocheckoutplugin.dto.SBCheckOutTransaction;
import com.scheidtbachmann.entervocheckoutplugin.localization.Text;
import com.scheidtbachmann.entervocheckoutplugin.view.ViewController;

import static com.scheidtbachmann.entervocheckoutplugin.delegation.Environment.SANDBOX;

public class Plugin {

    private static Plugin instance = new Plugin();
    private String apiKey;
    private PluginConfig config;
    private Context context;
    private ClassificationResult classificationResult;
    private String client_token;
    private SBCheckOutTransaction sale;
    private BundleFileManager fileManager;
    private WebView pluginView;
    private SBCheckOutDelegate delegate = null;
    private SBCheckOutStatus status = SBCheckOutStatus.UNINITIALIZED;
    private String pendingIdentification = null;
    private IdentificationType pendingIdentificationType;
    private ProgressDialog spinner;
    private Environment environment = SANDBOX;
    private int exitAnimation = -1;

    public static Plugin getInstance() {
        return instance;
    }

    private Plugin() {

    }

    public static void switchStatus( SBCheckOutStatus newStatus) {

        instance.status = newStatus;
        if ( instance.delegate != null) {
            instance.delegate.onMessage(LogLevel.INFO, "changing to new status " + newStatus.name());
            instance.delegate.onStatus(newStatus, instance.sale);
        }
    }

    public void setContext( Context context) {
        this.context = context;

        if ( context != null) {
            this.fileManager = new BundleFileManager(this.context);
            fileManager.createDocRootIfNotExists();
            AssetManager.applyCachedAssets();
            startPendingClassification();
        }
    }

    public void startPendingClassification() {
        if ( pendingIdentification != null && context != null) {
            startClassification( pendingIdentification, pendingIdentificationType);
        }
    }

    public static void startClassification(String identification, IdentificationType identificationType) {

        if ( instance.status == SBCheckOutStatus.IDLE) {
            instance.pendingIdentification = null;
            instance.sale = null;
            instance.switchStatus( SBCheckOutStatus.CLASSIFICATION_STARTED);
            new ClassificationClient().classify( identification, identificationType);

        } else {
            FlowControl.raise( "start not possible in status " + instance.status.name());
        }
    }

    public static void nothingToPay() {

        FlowControl.log( LogLevel.TRACE, "nothing to pay");
        Plugin.getInstance().switchStatus( SBCheckOutStatus.IDLE);
        ViewController.showStatus( Text.get( Text.NOTHING_TO_PAY), true);
    }

    public static void cancelFlow( String reason) {

        FlowControl.log( LogLevel.INFO, "flow cancelled with reason " + reason);
        switchStatus( SBCheckOutStatus.FLOW_FINISHED);
        switchStatus( SBCheckOutStatus.IDLE);
    }

    public static void preparePayment( ClassificationResult result) {

        String customerId = ClientUtil.extendedCustomerId();
        Plugin.getInstance().getClassificationResult().setCustomer_id( customerId);
        new TokenClient().retrieveClientToken( customerId);
    }

    public static void startPayment( ClassificationResult result, String authorization) {

        switchStatus( SBCheckOutStatus.PAYMENT_STARTED);
        if ( instance.delegate != null) {
            instance.delegate.onConductPayment( authorization);
        }

    }

    public static void preparationComplete( ClassificationResult result, String authorization, boolean success, String message) {

        switchStatus( SBCheckOutStatus.PREPARATION_FINISHED);
        if ( success) {

            //startPayment( result, authorization);

        } else {

            cancelFlow( Text.get( Text.TRY_AGAIN_LATER));

        }
    }

    public static void cancelPayment( boolean userCancellation) {

        switchStatus( SBCheckOutStatus.PAYMENT_CANCELLED);
        switchStatus( SBCheckOutStatus.IDLE);
        ViewController.showStatus( Text.get( Text.TRY_AGAIN_LATER), false);
    }

    public static void classificationComplete( ClassificationResult result, boolean success, String message) {

        switchStatus( SBCheckOutStatus.CLASSIFICATION_FINISHED);
        if ( success) {

            if ( result.getAmount() == 0.0) {
                instance.nothingToPay();
            } else {
                FlowControl.log( LogLevel.TRACE, "amount to pay is " + String.valueOf( result.getAmount()) + ", currency is " + result.getCurrency());
                ViewController.showPrice();
            }

        } else {

            cancelFlow( Text.get(Text.TRY_AGAIN_LATER));
            ViewController.showStatus( Text.get( Text.TRY_AGAIN_LATER), false);
        }
    }

    public static void bookPayment( String nonce) {
        Plugin.getInstance().getClassificationResult().setNonce( nonce);
        new SaleClient().postSale(Plugin.getInstance().getClassificationResult().getNonce());
    }

    public static String version() {
        return BuildConfig.VERSION_NAME;
    }

    public void setApiKey( String apiKey) {this.apiKey = apiKey;}
    public void setDelegate( SBCheckOutDelegate delegate) {this.delegate = delegate;}
    public void setPluginView( WebView pluginView) {this.pluginView = pluginView;}
    public void setSale( SBCheckOutTransaction sale) { this.sale = sale;}
    public void setClient_token( String client_token) { this.client_token = client_token;}
    public void setClassificationResult( ClassificationResult classificationResult) { this.classificationResult = classificationResult;}
    public void setConfig( PluginConfig config) { this.config = config;}
    public PluginConfig getConfig() { return config;}
    public Context getContext() {return context;}
    public ClassificationResult getClassificationResult() { return classificationResult;}
    public String getClient_token() { return client_token;}
    public SBCheckOutTransaction getSale() {return sale;}
    public WebView getPluginView() {return pluginView;}
    public BundleFileManager getFileManager() {return fileManager;}
    public SBCheckOutDelegate getDelegate() { return delegate;}
    public String getApiKey() { return apiKey;}
    public void setPendingIdentification( String pendingIdentification, IdentificationType pendingIdentificationType) {
        this.pendingIdentification = pendingIdentification;
        this.pendingIdentificationType = pendingIdentificationType;
    }
    public ProgressDialog getSpinner() {return spinner;}
    public void setSpinner( ProgressDialog spinner) {this.spinner = spinner;}

    public Environment getEnvironment() {
        return environment;
    }
    public void setEnvironment( Environment env) { this.environment = env;}
    public int getExitAnimation() { return exitAnimation;}
    public void setExitAnimation( int anim) {this.exitAnimation = anim;}
}
