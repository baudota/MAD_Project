package fr.antoinebaudot.lab1mad;

import java.util.Date;

/**
 * Created by Antoine on 04/06/2018.
 */

public class BookRequest {

    private RequestState state ;
    private String ownerID ;
    private String userID ;
    private String start ;
    private String end ;
    private String bookId ;

    public BookRequest() {
        this.state = RequestState.SENT;
    }

    public BookRequest(RequestState state, String ownerID, String userID, String start, String end, String bookId) {
        this.state = state;
        this.ownerID = ownerID;
        this.userID = userID;
        this.start = start;
        this.end = end;
        this.bookId = bookId;
    }

    public void setState(RequestState state) {
        this.state = state;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
