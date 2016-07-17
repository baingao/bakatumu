package com.bakatumu.bakatumu.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Order implements Serializable {
    String id, idCustomer, idAset, alamat, lat, lng;
    String pesan, antar, terima, keterangan, jumlah, harga;

    public Order () {
    }

    public Order(String id, String idCustomer, String idAset, String alamat, String lat, String lng,
                 String pesan, String antar, String terima, String keterangan, String jumlah,
                 String harga) {
        this.id = id;
        this.idCustomer = idCustomer;
        this.idAset = idAset;
        this.alamat = alamat;
        this.lat = lat;
        this.lng = lng;
        this.pesan = pesan;
        this.antar = antar;
        this.terima = terima;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdAset() {
        return idAset;
    }

    public void setIdAset(String idAset) {
        this.idAset = idAset;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getAntar() {
        return antar;
    }

    public void setAntar(String antar) {
        this.antar = antar;
    }

    public String getTerima() {
        return terima;
    }

    public void setTerima(String terima) {
        this.terima = terima;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }


}
