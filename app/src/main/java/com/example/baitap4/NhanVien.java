package com.example.baitap4;

import com.google.gson.annotations.SerializedName;

public class NhanVien {
    @SerializedName("id")
    private String id;

    @SerializedName("employee_name")
    private String ten;

    @SerializedName("employee_age")
    private int tuoi;

    @SerializedName("employee_salary")
    private int luong;

    @SerializedName("profile_image")
    private String anhDaiDien;

    // Constructor cho việc tạo nhân viên mới
    public NhanVien(String ten, int tuoi, int luong, String anhDaiDien) {
        this.ten = ten;
        this.tuoi = tuoi;
        this.luong = luong;
        this.anhDaiDien = anhDaiDien;
    }

    // Getters
    public String getId() { return id; }
    public String getTen() { return ten; }
    public int getTuoi() { return tuoi; }
    public int getLuong() { return luong; }
    public String getAnhDaiDien() { return anhDaiDien; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTen(String ten) { this.ten = ten; }
    public void setTuoi(int tuoi) { this.tuoi = tuoi; }
    public void setLuong(int luong) { this.luong = luong; }
    public void setAnhDaiDien(String anhDaiDien) { this.anhDaiDien = anhDaiDien; }
}