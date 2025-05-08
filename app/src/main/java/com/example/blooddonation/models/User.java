package com.example.blooddonation.models;

public class User {
    private String uid;
    private String name;
    private int age;
    private String bloodGroup;
    private String location;
    private String phoneNumber;
    private String gender;
    private String profileImagePath;

    // Empty constructor required for Firestore
    public User() {}

    public User(String uid, String name, int age, String bloodGroup, String location, String phoneNumber, String gender) {
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }
} 