package com.scheidtbachmann.entervocheckoutplugin.client;

import android.view.View;

import com.scheidtbachmann.entervocheckoutplugin.core.FlowControl;
import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;
import com.scheidtbachmann.entervocheckoutplugin.delegation.IdentificationType;
import com.scheidtbachmann.entervocheckoutplugin.delegation.LogLevel;
import com.scheidtbachmann.entervocheckoutplugin.dto.ClassificationResult;
import com.scheidtbachmann.entervocheckoutplugin.dto.SBCheckOutTransaction;
import com.scheidtbachmann.entervocheckoutplugin.localization.Text;
import com.scheidtbachmann.entervocheckoutplugin.util.Network;
import com.scheidtbachmann.entervocheckoutplugin.view.ViewController;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;

interface ClassificationService {

    @GET
    Call<ClassificationResult> classify(
            @Header( "X-SampB-SelfCheckout-App") String magic,
            @Url String url,
            @Query(ClientConst.PARAM_APPID) String appId,
            @Query(ClientConst.PARAM_CODE) String code);
}

public class ClassificationClient {

    static final int HTTP_OK = 200;

    public ClassificationClient() {
    }

    public void classify(final String id, IdentificationType type) {

        if (!Network.isConnectedToNetwork()) {
            // no network connection
            Plugin.getInstance().cancelFlow( Text.get(Text.NO_NETWORK));
            return;
        }
        ViewController.showSpinner(Text.get( Text.CHECKING_DATA));

        Thread separate = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit ret = new Retrofit.Builder()
                        .baseUrl(Plugin.getInstance().getConfig().getBaseUrl())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ClassificationService service = ret.create(ClassificationService.class);
                Call<ClassificationResult> cfg = service.classify("SampB-SelfCheckout-2015",
                        Plugin.getInstance().getConfig().getBaseUrl() + ClientConst.CALCULATION_URI,
                        String.valueOf(Plugin.getInstance().getConfig().getAppId()), id);

                try {
                    FlowControl.log( LogLevel.TRACE, "starting classification...");
                    Response<ClassificationResult> result = cfg.execute();
                    if ( result.code() == HTTP_OK) {

                        ClassificationResult classification = result.body();
                        if ( classification != null) {
                            Plugin.getInstance().setClassificationResult( classification);
                            Plugin.getInstance().setSale(SBCheckOutTransaction.fromClassificationResult(classification));
                            FlowControl.log(LogLevel.TRACE, "received classification result");
                            Plugin.getInstance().classificationComplete( classification, true, "");
                        }
                    } else {
                        FlowControl.log( LogLevel.ERROR, "classification failed with http status code " + String.valueOf( result.code()));
                        FlowControl.raise( "classification failed with http status code " + String.valueOf( result.code()));
                        Plugin.getInstance().classificationComplete( null, false, "");
                    }

                } catch ( IOException io) {

                    FlowControl.log( LogLevel.ERROR, "classification failed with I/O error");
                    FlowControl.raise( "classification failed with I/O error");
                    Plugin.getInstance().classificationComplete( null, false, "");
                }
                ViewController.hideSpinner();

            }
        });
        separate.start();

    }
}