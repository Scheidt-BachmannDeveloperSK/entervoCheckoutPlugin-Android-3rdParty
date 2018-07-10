package com.scheidtbachmann.entervocheckoutplugin.client;

import com.scheidtbachmann.entervocheckoutplugin.core.FlowControl;
import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;
import com.scheidtbachmann.entervocheckoutplugin.delegation.LogLevel;
import com.scheidtbachmann.entervocheckoutplugin.delegation.SBCheckOutStatus;
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
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

interface SaleService {

    @POST( ClientConst.SALE_URI)
    Call<SBCheckOutTransaction> postSaleToBackend(
            @Header( "X-SampB-SelfCheckout-App") String magic,
            @Body ClassificationResult body);
}

public class SaleClient {

    public void postSale( String nonce) {

        if (!Network.isConnectedToNetwork()) {
            // no network connection
        }

        ViewController.showSpinner(Text.get( Text.SENDING_PAYMENT));

        Plugin.getInstance().getClassificationResult().setNonce(nonce);

        Thread separate = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit ret = new Retrofit.Builder()
                        .baseUrl(Plugin.getInstance().getConfig().getBaseUrl())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                SaleService service = ret.create(SaleService.class);
                Call<SBCheckOutTransaction> cfg = service.postSaleToBackend("SampB-SelfCheckout-2015",
                        Plugin.getInstance().getClassificationResult());
                try {
                    Response<SBCheckOutTransaction> result = cfg.execute();
                    SBCheckOutTransaction sale = result.body();
                    if ( sale != null) {
                        Plugin.getInstance().setSale( sale);
                        if ( sale.getSuccess()) {
                            Plugin.getInstance().switchStatus(SBCheckOutStatus.PAYMENT_FINISHED);
                            Plugin.getInstance().switchStatus(SBCheckOutStatus.FLOW_FINISHED);
                            Plugin.getInstance().switchStatus(SBCheckOutStatus.IDLE);
                            ViewController.showStatus( Text.get( Text.PAYMENT_RECEIVED), true);
                        } else {
                            ViewController.showStatus(Text.get( Text.TRY_AGAIN_LATER), false);
                        }
                    } else {
                        FlowControl.log( LogLevel.ERROR, "booking of payment failed with http status code " + String.valueOf( result.code()));
                        FlowControl.raise( "booking of payment failed with http status code " + String.valueOf( result.code()));
                        Plugin.getInstance().switchStatus(SBCheckOutStatus.PAYMENT_FINISHED);
                        Plugin.getInstance().switchStatus(SBCheckOutStatus.FLOW_FINISHED);
                        Plugin.getInstance().switchStatus(SBCheckOutStatus.IDLE);
                        ViewController.showStatus(Text.get( Text.TRY_AGAIN_LATER), false);
                    }
                } catch ( IOException io) {
                    io.printStackTrace();
                }
                ViewController.hideSpinner();

            }
        });
        separate.start();

    }
}
