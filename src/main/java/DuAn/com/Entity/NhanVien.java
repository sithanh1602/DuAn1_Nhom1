/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DuAn.com.Entity;

/**
 *
 * @author NITRO 5
 */
import java.io.Serializable;
import java.util.Date;

public class NhanVien implements Serializable {

    String idNv;

    String tenNv;

    String chucVu;

    String gioiTinh;

    Date ngayVaoLam;

    Integer luong;

    String matKhau;

    // Constructors
    public NhanVien() {
    }

    public NhanVien(String idNv, String tenNv, String chucVu, String gioiTinh, Date ngayVaoLam, Integer luong, String matKhau) {
        this.idNv = idNv;
        this.tenNv = tenNv;
        this.chucVu = chucVu;
        this.gioiTinh = gioiTinh;
        this.ngayVaoLam = ngayVaoLam;
        this.luong = luong;
        this.matKhau = matKhau;
    }

    // Getters and Setters
    public String getIdNv() {
        return idNv;
    }

    public void setIdNv(String idNv) {
        this.idNv = idNv;
    }

    public String getTenNv() {
        return tenNv;
    }

    public void setTenNv(String tenNv) {
        this.tenNv = tenNv;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public Integer getLuong() {
        return luong;
    }

    public void setLuong(Integer luong) {
        this.luong = luong;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    @Override
    public String toString() {
        return "NhanVien{"
                + "idNv='" + idNv + '\''
                + ", tenNv='" + tenNv + '\''
                + ", chucVu='" + chucVu + '\''
                + ", gioiTinh='" + gioiTinh + '\''
                + ", ngayVaoLam=" + ngayVaoLam
                + ", luong=" + luong
                + ", matKhau='" + matKhau + '\''
                + '}';
    }
}
