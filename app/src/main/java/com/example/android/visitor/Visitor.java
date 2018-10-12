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
    private String ttd_masuk;
    private Map waktu_masuk;

    public Visitor() {
    }

    public Visitor(String id_visitor, String nama, String instansi, String keperluan, String ttd_masuk, Map waktu_masuk) {
        this.id_visitor = id_visitor;
        this.nama = nama;
        this.instansi = instansi;
        this.keperluan = keperluan;
        this.ttd_masuk = ttd_masuk;
        this.waktu_masuk = waktu_masuk;
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

    public String getTtd_masuk() {
        return ttd_masuk;
    }

    public void setTtd_masuk(String ttd_masuk) {
        this.ttd_masuk = ttd_masuk;
    }

    public Map getWaktu_masuk() {
        return waktu_masuk;
    }

    public void setWaktu_masuk(Map waktu_masuk) {
        this.waktu_masuk = waktu_masuk;
    }
}
