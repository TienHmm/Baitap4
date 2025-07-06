package com.example.baitap4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.NhanVienViewHolder> {
    private List<NhanVien> danhSachNhanVien;
    private OnNhanVienClickListener clickListener;
    private OnXoaClickListener xoaListener;

    public interface OnNhanVienClickListener {
        void onNhanVienClick(NhanVien nhanVien);
    }

    public interface OnXoaClickListener {
        void onXoaClick(NhanVien nhanVien);
    }

    public NhanVienAdapter(List<NhanVien> danhSachNhanVien, OnNhanVienClickListener clickListener, OnXoaClickListener xoaListener) {
        this.danhSachNhanVien = danhSachNhanVien;
        this.clickListener = clickListener;
        this.xoaListener = xoaListener;
    }

    @NonNull
    @Override
    public NhanVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhanvien, parent, false);
        return new NhanVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhanVienViewHolder holder, int position) {
        NhanVien nhanVien = danhSachNhanVien.get(position);
        holder.bind(nhanVien);
    }

    @Override
    public int getItemCount() {
        return danhSachNhanVien.size();
    }

    class NhanVienViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTen, tvTuoi, tvLuong;
        private ImageView ivAnhDaiDien;
        private Button btnXoa;

        public NhanVienViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvTuoi = itemView.findViewById(R.id.tvTuoi);
            tvLuong = itemView.findViewById(R.id.tvLuong);
            ivAnhDaiDien = itemView.findViewById(R.id.ivAnhDaiDien);
            btnXoa = itemView.findViewById(R.id.btnXoa);

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onNhanVienClick(danhSachNhanVien.get(getAdapterPosition()));
                }
            });

            btnXoa.setOnClickListener(v -> {
                if (xoaListener != null) {
                    xoaListener.onXoaClick(danhSachNhanVien.get(getAdapterPosition()));
                }
            });
        }

        public void bind(NhanVien nhanVien) {
            tvTen.setText(nhanVien.getTen());
            tvTuoi.setText("Tuổi: " + nhanVien.getTuoi());
            tvLuong.setText("Lương: " + String.format("%,d", nhanVien.getLuong()) + " VND");

            // Tải ảnh đại diện (nếu có Glide)
            if (nhanVien.getAnhDaiDien() != null && !nhanVien.getAnhDaiDien().isEmpty()) {
                // Uncomment nếu có Glide
                // Glide.with(itemView.getContext())
                //     .load(nhanVien.getAnhDaiDien())
                //     .placeholder(R.drawable.ic_person)
                //     .error(R.drawable.ic_person)
                //     .into(ivAnhDaiDien);

                // Tạm thời dùng icon mặc định
                ivAnhDaiDien.setImageResource(android.R.drawable.ic_menu_gallery);
            } else {
                ivAnhDaiDien.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }
}