package com.rameshracharla.httpmethods.interfaces;


import com.rameshracharla.httpmethods.model.Data;
import com.rameshracharla.httpmethods.model.ResponseData;
import com.rameshracharla.httpmethods.model.ResponseModifyData;
import com.rameshracharla.httpmethods.model.ResponseSingleData;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("employees")
    Single<ResponseData> getRecords();

    @GET("employee/{id}")
    Single<ResponseSingleData> getSingleRecord(@Path("id") String id);

    @POST("create")
    Single<ResponseSingleData> createNewRecord(@Body Data data);

    @PUT("update/{id}")
    Single<ResponseModifyData> updateRecord(@Path("id") String id);

    @DELETE("delete/{id}")
    Single<ResponseModifyData> deleteRecord(@Path("id") String id);
}
