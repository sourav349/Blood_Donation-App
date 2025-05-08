package com.example.blooddonation.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.blooddonation.models.User;

public class AuthManager {
    private static final String PREF_NAME = "AuthPrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_AGE = "user_age";
    private static final String KEY_USER_BLOOD_GROUP = "user_blood_group";
    private static final String KEY_USER_LOCATION = "user_location";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_GENDER = "user_gender";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private static AuthManager instance;
    private final SharedPreferences preferences;
    private final Context context;

    private AuthManager(Context context) {
        this.context = context.getApplicationContext();
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context);
        }
        return instance;
    }

    public void createUserWithEmailAndPassword(String email, String password, User userData, OnAuthCompleteListener listener) {
        // In a real app, you would hash the password and store it securely
        // For this example, we'll just store the user data
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER_ID, email); // Using email as user ID
        editor.putString(KEY_USER_NAME, userData.getName());
        editor.putInt(KEY_USER_AGE, userData.getAge());
        editor.putString(KEY_USER_BLOOD_GROUP, userData.getBloodGroup());
        editor.putString(KEY_USER_LOCATION, userData.getLocation());
        editor.putString(KEY_USER_PHONE, userData.getPhoneNumber());
        editor.putString(KEY_USER_GENDER, userData.getGender());
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();

        listener.onSuccess(null); // No FirebaseUser in local auth
    }

    public void signInWithEmailAndPassword(String email, String password, OnAuthCompleteListener listener) {
        // In a real app, you would verify the credentials
        // For this example, we'll just check if the user exists
        String storedUserId = preferences.getString(KEY_USER_ID, "");
        if (storedUserId.equals(email)) {
            preferences.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply();
            listener.onSuccess(null);
        } else {
            listener.onError(new Exception("Invalid credentials"));
        }
    }

    public void signOut() {
        preferences.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public User getCurrentUser() {
        if (!isLoggedIn()) {
            return null;
        }

        return new User(
                preferences.getString(KEY_USER_ID, ""),
                preferences.getString(KEY_USER_NAME, ""),
                preferences.getInt(KEY_USER_AGE, 0),
                preferences.getString(KEY_USER_BLOOD_GROUP, ""),
                preferences.getString(KEY_USER_LOCATION, ""),
                preferences.getString(KEY_USER_PHONE, ""),
                preferences.getString(KEY_USER_GENDER, "")
        );
    }

    public void updateUserData(User user, OnAuthCompleteListener listener) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putInt(KEY_USER_AGE, user.getAge());
        editor.putString(KEY_USER_BLOOD_GROUP, user.getBloodGroup());
        editor.putString(KEY_USER_LOCATION, user.getLocation());
        editor.putString(KEY_USER_PHONE, user.getPhoneNumber());
        editor.putString(KEY_USER_GENDER, user.getGender());
        editor.apply();

        listener.onSuccess(null);
    }

    public interface OnAuthCompleteListener {
        void onSuccess(Object user); // Changed from FirebaseUser to Object
        void onError(Exception e);
    }
} 