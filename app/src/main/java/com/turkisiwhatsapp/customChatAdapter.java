package com.turkisiwhatsapp;

/**
 * Created by Windows10 on 11.10.2017.
 */

public class customChatAdapter {
    private  String s_mesaj;
    private String s_kullanici;

    public customChatAdapter(String s_mesaj, String s_kullanici) {

        this.s_mesaj = s_mesaj;
        this.s_kullanici = s_kullanici;
    }

    public String getS_mesaj() {
        return s_mesaj;
    }

    public void setS_mesaj(String s_mesaj) {
        this.s_mesaj = s_mesaj;
    }

    public String getS_kullanici() {
        return s_kullanici;
    }

    public void setS_kullanici(String s_kullanici) {
        this.s_kullanici = s_kullanici;
    }




}
