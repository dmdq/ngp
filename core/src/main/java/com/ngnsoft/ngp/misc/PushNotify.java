package com.ngnsoft.ngp.misc;

/**
 *
 * @author fcy
 */
public class PushNotify {
    String message;
    int badge;
    String sound;
    String game;
    String appId;
    String deviceId;
    String token;
    String pass;
    String pushKey;
    String pushLink;
    boolean clear;
    boolean prod;
    
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPushLink() {
        return pushLink;
    }

    public void setPushLink(String pushLink) {
        this.pushLink = pushLink;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public boolean isProd() {
        return prod;
    }

    public void setProd(boolean prod) {
        this.prod = prod;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }


}
