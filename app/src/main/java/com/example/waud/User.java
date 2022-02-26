package com.example.waud;

public class User {
    public String fullName;
    public String nickName;
    public String phoneNumber;
    public String profilePicture;
    public MyLocation location;
    public String fbUID;

    public User() {
    }

    public User(String fullName, String nickName, String phoneNumber, String profilePicture, MyLocation location, String fbUID) {
        this.fullName = fullName;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.location = location;
        this.fbUID = fbUID;
    }

    public String getFbUID() {
        return fbUID;
    }

    public void setFbUID(String fbUID) {
        this.fbUID = fbUID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public MyLocation getLocation() {
        return location;
    }

    public void setLocation(MyLocation location) {
        this.location = location;
    }
}
