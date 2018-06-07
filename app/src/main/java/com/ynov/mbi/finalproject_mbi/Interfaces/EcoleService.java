package com.ynov.mbi.finalproject_mbi.Interfaces;

import android.content.res.Resources;

import com.google.gson.JsonObject;
import com.ynov.mbi.finalproject_mbi.Models.Credentials;
import com.ynov.mbi.finalproject_mbi.Models.Ecole;
import com.ynov.mbi.finalproject_mbi.Models.JsonResponseEcole;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EcoleService {

    //String BASE_URL = "http://" + Resources.getSystem().getString(R.string.ip) + ':' + Resources.getSystem().getString(R.string.port);

    @POST("/api/v1/users/sign_in")
    Call<JsonObject> authenticate(@Body Credentials credential);

    @GET("/api/v1/schools")
    Call<JsonResponseEcole> getEcole(@Query("status") String status);

    @POST("/api/v1/schools")
    Call<JsonResponseEcole> saveEcole(@Body Ecole school);

    @DELETE("/api/v1/schools/{id}")
    Call<ResponseBody> removeEcole(@Path("id") int id);

    @PATCH("/api/v1/schools/{id}")
    Call<JsonResponseEcole> updateEcole(@Path("id") int id, @Body Ecole school);

}
