package fr.antoinebaudot.lab1mad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by s254741 on 06/04/2018.
 */

public class User  {
    private String name ;
    private String email ;
    private String shortBio ;
    private String profilePicture ;

    public User(){};

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getShortBio() {
        return shortBio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }


}
