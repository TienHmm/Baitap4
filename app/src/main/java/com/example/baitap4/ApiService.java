package com.example.baitap4;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/api/v1/employees")
    Call<List<NhanVien>> layTatCaNhanVien();

    @GET("/api/v1/employee/{id}")
    Call<NhanVien> layNhanVien(@Path("id") String id);

    @POST("/api/v1/create")
    Call<NhanVien> taoNhanVien(@Body NhanVien nhanVien);

    @PUT("/api/v1/update/{id}")
    Call<NhanVien> capNhatNhanVien(@Path("id") String id, @Body NhanVien nhanVien);

    @DELETE("/api/v1/delete/{id}")
    Call<Void> xoaNhanVien(@Path("id") String id);
}
