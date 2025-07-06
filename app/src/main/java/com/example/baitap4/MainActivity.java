package com.example.baitap4;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NhanVienAdapter adapter;
    private List<NhanVien> danhSachNhanVien;
    private ApiService apiService;
    private FloatingActionButton fabThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        khoiTaoView();
        thietLapRetrofit();
        taiDanhSachNhanVien();
    }

    private void khoiTaoView() {
        recyclerView = findViewById(R.id.recyclerView);
        fabThem = findViewById(R.id.fabThem);

        danhSachNhanVien = new ArrayList<>();
        adapter = new NhanVienAdapter(danhSachNhanVien, this::onNhanVienClick, this::onXoaClick);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabThem.setOnClickListener(v -> hienThiDialogThemNhanVien());
    }

    private void thietLapRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://blackntt.net:88")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    private void taiDanhSachNhanVien() {
        Call<List<NhanVien>> call = apiService.layTatCaNhanVien();
        call.enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachNhanVien.clear();
                    danhSachNhanVien.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Không thể tải danh sách nhân viên", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API", "Lỗi: " + t.getMessage());
            }
        });
    }

    private void onNhanVienClick(NhanVien nhanVien) {
        hienThiDialogChiTietNhanVien(nhanVien);
    }

    private void onXoaClick(NhanVien nhanVien) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa nhân viên")
                .setMessage("Bạn có chắc muốn xóa " + nhanVien.getTen() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> xoaNhanVien(nhanVien.getId()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void xoaNhanVien(String id) {
        Call<Void> call = apiService.xoaNhanVien(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Xóa nhân viên thành công", Toast.LENGTH_SHORT).show();
                    taiDanhSachNhanVien();
                } else {
                    Toast.makeText(MainActivity.this, "Xóa nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hienThiDialogThemNhanVien() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nhanvien, null);
        EditText etTen = dialogView.findViewById(R.id.etTen);
        EditText etTuoi = dialogView.findViewById(R.id.etTuoi);
        EditText etLuong = dialogView.findViewById(R.id.etLuong);
        EditText etAnhDaiDien = dialogView.findViewById(R.id.etAnhDaiDien);

        new AlertDialog.Builder(this)
                .setTitle("Thêm nhân viên mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String ten = etTen.getText().toString().trim();
                    String tuoiStr = etTuoi.getText().toString().trim();
                    String luongStr = etLuong.getText().toString().trim();
                    String anhDaiDien = etAnhDaiDien.getText().toString().trim();

                    if (ten.isEmpty() || tuoiStr.isEmpty() || luongStr.isEmpty()) {
                        Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int tuoi = Integer.parseInt(tuoiStr);
                        int luong = Integer.parseInt(luongStr);

                        NhanVien nhanVienMoi = new NhanVien(ten, tuoi, luong, anhDaiDien);
                        taoNhanVien(nhanVienMoi);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Vui lòng nhập số hợp lệ cho tuổi và lương", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void hienThiDialogChiTietNhanVien(NhanVien nhanVien) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nhanvien, null);
        EditText etTen = dialogView.findViewById(R.id.etTen);
        EditText etTuoi = dialogView.findViewById(R.id.etTuoi);
        EditText etLuong = dialogView.findViewById(R.id.etLuong);
        EditText etAnhDaiDien = dialogView.findViewById(R.id.etAnhDaiDien);

        etTen.setText(nhanVien.getTen());
        etTuoi.setText(String.valueOf(nhanVien.getTuoi()));
        etLuong.setText(String.valueOf(nhanVien.getLuong()));
        etAnhDaiDien.setText(nhanVien.getAnhDaiDien());

        new AlertDialog.Builder(this)
                .setTitle("Sửa thông tin nhân viên")
                .setView(dialogView)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String ten = etTen.getText().toString().trim();
                    String tuoiStr = etTuoi.getText().toString().trim();
                    String luongStr = etLuong.getText().toString().trim();
                    String anhDaiDien = etAnhDaiDien.getText().toString().trim();

                    if (ten.isEmpty() || tuoiStr.isEmpty() || luongStr.isEmpty()) {
                        Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int tuoi = Integer.parseInt(tuoiStr);
                        int luong = Integer.parseInt(luongStr);

                        NhanVien nhanVienCapNhat = new NhanVien(ten, tuoi, luong, anhDaiDien);
                        capNhatNhanVien(nhanVien.getId(), nhanVienCapNhat);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Vui lòng nhập số hợp lệ cho tuổi và lương", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void taoNhanVien(NhanVien nhanVien) {
        Call<NhanVien> call = apiService.taoNhanVien(nhanVien);
        call.enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                    taiDanhSachNhanVien();
                } else {
                    Toast.makeText(MainActivity.this, "Thêm nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NhanVien> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void capNhatNhanVien(String id, NhanVien nhanVien) {
        Call<NhanVien> call = apiService.capNhatNhanVien(id, nhanVien);
        call.enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Cập nhật nhân viên thành công", Toast.LENGTH_SHORT).show();
                    taiDanhSachNhanVien();
                } else {
                    Toast.makeText(MainActivity.this, "Cập nhật nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NhanVien> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}