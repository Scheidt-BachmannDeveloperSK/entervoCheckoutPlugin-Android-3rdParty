package com.scheidtbachmann.entervocheckoutplugin.client;

import android.content.Context;

import com.scheidtbachmann.entervocheckoutplugin.core.FlowControl;
import com.scheidtbachmann.entervocheckoutplugin.core.Plugin;
import com.scheidtbachmann.entervocheckoutplugin.delegation.Environment;
import com.scheidtbachmann.entervocheckoutplugin.delegation.LogLevel;
import com.scheidtbachmann.entervocheckoutplugin.delegation.SBCheckOutStatus;
import com.scheidtbachmann.entervocheckoutplugin.dto.PluginConfig;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

interface PluginConfigService {

    @GET
    Call<PluginConfig> getCfg(@Header( "X-SampB-SelfCheckout-App") String magic,
                              @Url String url, @Header( "X-API-KEY") String apiKey);
}

public class ConfigClient {

    public PluginConfig getPluginConfig(final String apiKey) {

        Context context = Plugin.getInstance().getContext();
        if ( context == null) {
            return null;
        }
        final String packageName = context.getPackageName();

        Thread separate = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit ret = new Retrofit.Builder()
                        .baseUrl(getBaseUrl())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                PluginConfigService service = ret.create(PluginConfigService.class);
                Call<PluginConfig> cfg = service.getCfg("SampB-SelfCheckout-2015",
                        ClientConst.CONFIG_URI + "/" + packageName, apiKey);
                try {
                    Response<PluginConfig> result = cfg.execute();
                    PluginConfig config = result.body();
                    if ( config != null) {
                        Plugin.getInstance().setConfig( config);
                        FlowControl.log(LogLevel.INFO, "plugin version " + Plugin.version());
                        Plugin.getInstance().switchStatus(SBCheckOutStatus.IDLE);
                        Plugin.getInstance().startPendingClassification();
                    }
                } catch ( IOException io) {
                    io.printStackTrace();
                }
            }
        });
        separate.start();
        return null;
    }

    private String getBaseUrl() {
        Environment env = Plugin.getInstance().getEnvironment();
        switch (env) {
            case LIVE: return ClientConst.CONFIG_URL_LIVE;
            case PRELIVE: return ClientConst.CONFIG_URL_PRELIVE;
            default: return ClientConst.CONFIG_URL_SANDBOX;
        }
    }
}