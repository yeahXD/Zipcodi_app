package com.yeah13hz.zipcodi.gcm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yeah13hz.zipcodi.MyApplication;

/**
 * Created by dongja94 on 2016-05-16.
 */
public class PropertyManager {
    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }

    private static final String FIELD_EMAIL = "email";
    public void setEmail(String email) {
        mEditor.putString(FIELD_EMAIL, email);
        mEditor.commit();
    }
    public String getEmail() {
        return mPrefs.getString(FIELD_EMAIL,"");
    }

    private static final String FIELD_PASSWORD = "password";
    public void setPassword(String password) {
        mEditor.putString(FIELD_PASSWORD, password);
        mEditor.commit();
    }
    public String getPassword() {
        return mPrefs.getString(FIELD_PASSWORD, "");
    }


    public static final String FIELD_FACEBOOK_ID = "facebookid";
    public void setFacebookId(String facebookId) {
        mEditor.putString(FIELD_FACEBOOK_ID, facebookId);
        mEditor.commit();
    }
    public String getFacebookId() {
        return mPrefs.getString(FIELD_FACEBOOK_ID, "");
    }

    private boolean isLogin = false;
    public void setLogin(boolean login) {
        isLogin = login;
    }
    public boolean isLogin() {
        return isLogin;
    }

//    private User user = null;
//    public static final String USER_ID = "uid";
//    public static final String USER_NAME = "name";
//    public static final String USER_IMAGE = "image";
//    public void setUser(User user) {
//        mEditor.putString(USER_ID, user.getUid()+"");
//        mEditor.putString(USER_NAME, user.getName()+"");
//        mEditor.putString(USER_IMAGE, user.getProfile_image()+"");
//        mEditor.commit();
//        this.user = user;
//    }
//    public User getUser() {
//        user.setUid(Integer.parseInt(mPrefs.getString(USER_ID, "")));
//        user.setName(mPrefs.getString(USER_NAME, ""));
//        user.setProfile_image(mPrefs.getString(USER_IMAGE, ""));
//        return user;
//    }


    private static final String FIELD_REGISTRATION_ID = "regid";
    public void setRegistrationToken(String token) {
        mEditor.putString(FIELD_REGISTRATION_ID, token);
        mEditor.commit();
    }
    public String getRegistrationToken(){
        return mPrefs.getString(FIELD_REGISTRATION_ID, "");
    }


    private static final String FIELD_PUSH = "push";
    public void setPush(String push) {
        mEditor.putString(FIELD_PUSH, push);
        mEditor.commit();
    }
    public String getPush() {
        return mPrefs.getString(FIELD_PUSH, "");
    }
}
