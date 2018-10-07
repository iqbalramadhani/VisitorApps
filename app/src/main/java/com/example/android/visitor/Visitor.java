package com.example.android.visitor;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Bale-PC on 07/10/2018.
 */

public class Visitor {
    private String id_visitor;
    private String nama;
    private String instansi;
    private String keperluan;
    private Map waktu_masuk,waktu_keluar;

    public Visitor() {
    }

    public Visitor(String id_visitor, String nama, String instansi, String keperluan, Map waktu_masuk, Map waktu_keluar) {
        this.id_visitor = id_visitor;
        this.nama = nama;
        this.instansi = instansi;
        this.keperluan = keperluan;
        this.waktu_masuk = waktu_masuk;
        this.waktu_keluar = waktu_keluar;
    }

    public String getId_visitor() {
        return id_visitor;
    }

    public void setId_visitor(String id_visitor) {
        this.id_visitor = id_visitor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getInstansi() {
        return instansi;
    }

    public void setInstansi(String instansi) {
        this.instansi = instansi;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }

    public Map getWaktu_masuk() {
        return waktu_masuk;
    }

    public void setWaktu_masuk(Map waktu_masuk) {
        this.waktu_masuk = waktu_masuk;
    }

    public Map getWaktu_keluar() {
        return waktu_keluar;
    }

    public void setWaktu_keluar(Map waktu_keluar) {
        this.waktu_keluar = waktu_keluar;
    }
}
