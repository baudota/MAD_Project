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
    private String title ;

    public BookRequest() {
        this.state = RequestState.SENT;
    }

    public BookRequest(RequestState state, String ownerID, String userID, String start, String end, String bookId, String title) {
        this.state = state;
        this.ownerID = ownerID;
        this.userID = userID;
        this.start = start;
        this.end = end;
        this.bookId = bookId;
        this.title =title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RequestState getState() {

        return state;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getUserID() {
        return userID;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getBookId() {
        return bookId;
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
