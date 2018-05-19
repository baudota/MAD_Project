package fr.antoinebaudot.lab1mad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Antoine on 12/05/2018.
 */

public class Book implements Parcelable {

    private  String uid ;
    private String isbn ;
    private String author ;
    private String title ;
    private String subtitle;
    private String description ;
    private String coverUrl ;


    public Book(String uid, String isbn, String author, String title, String subtitle, String description, String coverUrl) {
        this.uid = uid;
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.coverUrl = coverUrl;
    }

    public Book(Parcel in){
        String[] data = new String[7];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.uid = data[0];
        this.isbn = data[1];
        this.author = data[2];
        this.title = data[3];
        this.subtitle = data[4];
        this.description = data[5];
        this.coverUrl = data[6];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                this.uid,
                this.isbn,
                this.author,
                this.title,
                this.subtitle,
                this.description,
                this.coverUrl
        });


    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
