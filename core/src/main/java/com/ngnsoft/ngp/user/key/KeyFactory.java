package com.ngnsoft.ngp.user.key;

/**
 *
 * @author fcy
 */
public class KeyFactory {
    
    public static SnsKey getSnsKey(String keyType){
        if (keyType.equals(ContactKey.TYPE)) {
            return new ContactKey();
        } else if (keyType.equals(DeviceKey.TYPE)) {
            return new DeviceKey();
        } else if (keyType.equals(EmailKey.TYPE)) {
            return new EmailKey();
        } else if (keyType.equals(FacebookKey.TYPE)) {
            return new FacebookKey();
        } else if (keyType.equals(GamecenterKey.TYPE)) {
            return new GamecenterKey();
        } else if (keyType.equals(GoogleplayKey.TYPE)) {
            return new GoogleplayKey();
        } else if (keyType.equals(WeiboKey.TYPE)) {
            return new WeiboKey();
        } else if (keyType.equals(NameKey.TYPE)) {
            return new NameKey();
        } else {
            return new DefaultKey();
        }
    }
    
}
