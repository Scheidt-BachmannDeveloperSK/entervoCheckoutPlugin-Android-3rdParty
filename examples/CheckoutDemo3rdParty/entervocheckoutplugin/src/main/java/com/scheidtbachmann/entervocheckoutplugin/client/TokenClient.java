package com.scheidtbachmann.entervocheckoutplugin.client;

import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;
import com.scheidtbachmann.entervocheckoutplugin.dto.TokenResult;
import com.scheidtbachmann.entervocheckoutplugin.localization.Text;
import com.scheidtbachmann.entervocheckoutplugin.util.Network;
import com.scheidtbachmann.entervocheckoutplugin.view.ViewController;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;

interface TokenService {

    @GET
    Call<TokenResult> getToken(
            @Header( "X-SampB-SelfCheckout-App") String magic,
            @Url String url,
            @Query(ClientConst.PARAM_APPID) String appId,
            @Query(ClientConst.PARAM_CUSTOMERID) String customerId);
}

public class TokenClient {

    public void retrieveClientToken( final String customerId) {

        if (!Network.isConnectedToNetwork()) {
            // no network connection
            Plugin.getInstance().cancelFlow( Text.get(Text.NO_NETWORK));
            return;
        }

        Thread separate = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit ret = new Retrofit.Builder()
                        .baseUrl(Plugin.getInstance().getConfig().getBaseUrl())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                TokenService service = ret.create(TokenService.class);
                Call<TokenResult> cfg = service.getToken("SampB-SelfCheckout-2015",
                        Plugin.getInstance().getConfig().getBaseUrl() + ClientConst.CLIENTTOKEN_URI,
                        String.valueOf(Plugin.getInstance().getConfig().getAppId()), customerId);
                try {
                    Response<TokenResult> result = cfg.execute();
                    TokenResult token= result.body();
                    if ( token != null) {
                        Plugin.getInstance().getClassificationResult().setNonce(token.getClient_token());
                        Plugin.getInstance().setClient_token( token.getClient_token());
                        Plugin.getInstance().preparationComplete( Plugin.getInstance().getClassificationResult(), token.getClient_token(), true, "");
                    }
                } catch ( IOException io) {
                    io.printStackTrace();
                    Plugin.getInstance().preparationComplete( Plugin.getInstance().getClassificationResult(), "", false, "");
                }
            }

        });
        separate.start();

    }
}