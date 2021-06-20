package ru.geekbrains.lesson6_notepad;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Note implements Parcelable {
    private String title;
    private String content;
    private Calendar creationDate;
    //private int color;

    public Note(String title, String content, Calendar creationDate/*, int color*/) {
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        //this.color = color;
    }

    protected Note(Parcel in) {
        title = in.readString();
        content = in.readString();
        creationDate = (Calendar) in.readSerializable();
        //color = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeSerializable(creationDate);
        //dest.writeInt(color);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public static Creator<Note> getCREATOR() {
        return CREATOR;
    }

    /*public int getColor() {
        return color;
    }*/
}
