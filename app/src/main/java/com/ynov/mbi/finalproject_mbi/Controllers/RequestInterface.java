package com.ynov.mbi.finalproject_mbi.Controllers;

import android.util.Log;

import com.ynov.mbi.finalproject_mbi.Interfaces.BaseInterface;
import com.ynov.mbi.finalproject_mbi.Models.Credentials;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestInterface {
    public static <T> T getInterface(Class<T> controller){

        final String crendential = Credentials.getAuth_token();
        //Si nous avons un auth_token, on le rajoute en header a TOUTES les requêtes
        if(crendential == null){
            return new Retrofit.Builder()
                    .baseUrl(BaseInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(controller);
        }
        else {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("AUTHORIZATION", crendential)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });

            OkHttpClient client = httpClient.build();

            return new Retrofit.Builder().baseUrl(BaseInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build().create(controller);
        }



        
    }
}
