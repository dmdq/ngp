package com.ngnsoft.ngp.model;

import com.ngnsoft.ngp.util.JSONUtil;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class UserProfile extends BaseModel {
    
    public static final int GENDER_NA = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    
    private Long userId;
    
    private String country;//ISO 3366-1 alpha-2
    
    private Integer age;
    
    private Integer gender;
    
    private String avatar;
    
    private String email;
    
    private String mobile;
    
    private String jsonAll;//json string of all, include nick and name in User
    
    private UserAccount userAccount;
    
	public UserProfile() {
        
    }
    
    public UserProfile(Long uid) {
        this.userId = uid;
    }
    
    public UserProfile(Long uid, String country, int age, int gender, String avatar, String email, String mobile, String jsonAll) {
        this.userId = uid;
        this.country = country;
        this.age = age;
        this.gender = gender;
        this.avatar = avatar;
        this.email = email;
        this.mobile = mobile;
        this.jsonAll = jsonAll;
    }

    public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
    
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJsonAll() {
        return jsonAll;
    }

    public void setJsonAll(String jsonAll) {
        this.jsonAll = jsonAll;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long uid) {
        this.userId = uid;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
    
    @Override
    public Serializable getPrimaryKey() {
        return userId;
    }

    @Override
    public void setPrimaryKey(Object o) {
        userId = (Long)o;
    }
    
    @Override
    protected JSONObject doJSONObject() {
        JSONObject jo = super.doJSONObject();
        jo.remove("userId");
        jo.remove("jsonAll");
        return jo;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jo = null;
        try {
            jo = new JSONObject(jsonAll);
        } catch (Exception ex) {
        }
        return jo;
    }
    
    @Override
    public void prepareForSave() {
        super.prepareForSave();
        if (jsonAll == null) {
            JSONObject jo = doJSONObject();
            jsonAll = jo.toString();
        }
    }

    @Override
    public void prepareForUpdate() {
        super.prepareForUpdate();
        try {
            JSONObject old =  null;
            if (jsonAll != null) {
                old =  new JSONObject(jsonAll);
            }
            JSONObject up = doJSONObject();
            JSONObject jo = JSONUtil.mergeAndUpdate(old, up);
            jsonAll = jo.toString();
        } catch (JSONException ex) {
        }
    }
}
