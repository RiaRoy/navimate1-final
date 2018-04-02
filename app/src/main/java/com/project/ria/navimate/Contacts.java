package com.project.ria.navimate;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by skynet on 3/4/18.
 */

@IgnoreExtraProperties
public class Contacts {

    public String name;
    public String id;
    public String phone;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Contacts() {
    }

    public Contacts(String name, String phone,String uid) {
        this.name = name;
        this.phone = phone;
        this.id = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}