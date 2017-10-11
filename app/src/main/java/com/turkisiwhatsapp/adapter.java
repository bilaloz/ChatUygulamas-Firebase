package com.turkisiwhatsapp;

/**
 * Created by Windows10 on 7.10.2017.
 */

public class adapter {
    public String getS_FriendsName() {
        return s_FriendsName;
    }

    public void setS_FriendsName(String s_FriendsName) {
        this.s_FriendsName = s_FriendsName;
    }

    public String getS_FriendsImage() {
        return s_FriendsImage;
    }

    public void setS_FriendsImage(String s_FriendsImage) {
        this.s_FriendsImage = s_FriendsImage;
    }

    public adapter(String s_FriendsName, String s_FriendsImage) {

        this.s_FriendsName = s_FriendsName;
        this.s_FriendsImage = s_FriendsImage;
    }

    private String s_FriendsName,s_FriendsImage;
}
