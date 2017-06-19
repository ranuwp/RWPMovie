package id.ranuwp.greetink.rwpmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ranuwp on 6/16/2017.
 */

public class Movie implements Parcelable{
    private String id;
    private String title;
    private String poster_url;
    private String backdrop_url;
    private String overview;
    private double vote_average;
    private String release_date;

    public Movie() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getBackdrop_url() {
        return backdrop_url;
    }

    public void setBackdrop_url(String backdrop_url) {
        this.backdrop_url = backdrop_url;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Movie(Parcel parcel){
        String[] data = new String[7];
        parcel.readStringArray(data);
        id = data[0];
        title = data[1];
        poster_url = data[2];
        backdrop_url = data[3];
        overview = data[4];
        vote_average = Double.valueOf(data[5]);
        release_date = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                this.id,
                this.title,
                this.poster_url,
                this.backdrop_url,
                this.overview,
                String.valueOf(this.vote_average),
                this.release_date
        });
    }

    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>(){

                @Override
                public Movie createFromParcel(Parcel parcel) {
                    return new Movie(parcel);
                }

                @Override
                public Movie[] newArray(int i) {
                    return new Movie[i];
                }
            };
}
