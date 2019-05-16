package com.example.wsapp1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.wsapp1.model.GetAutoFromUser;
import com.example.wsapp1.model.GetToken;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiHelper {
    String BASEURL = "http://q-arp.net:3030";


    @FormUrlEncoded
    @POST("/user/login")
    Observable<GetToken> getUserToken(@Field("login") String login, @Field("password") String password);


    @FormUrlEncoded
    @POST("/auto/add")
    Observable<ResponseBody> addAuto(@Header("Auth") String auth, @Field("vin") String vin, @Field("name") String name);


    @POST("/auto/getMy")
    Observable<GetAutoFromUser> getAutoForUser(@Query("token") String token);

    @POST ("/auto/addPhoto/{id}")
    @Multipart
    Observable<ResponseBody> addPhoto(@Path("id") int id, @Query("token") String token,  @Part("xfile") RequestBody file);


    class Creator {

        public static ApiHelper newApi() {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .callTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(ApiHelper.class);

        }

    }

}
