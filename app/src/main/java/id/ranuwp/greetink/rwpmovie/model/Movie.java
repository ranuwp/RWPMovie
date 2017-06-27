package id.ranuwp.greetink.rwpmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ranuwp on 6/16/2017.
 */

public class Movie implements Parcelable{
    private String id;
    private String title;
    private String posterUrl;
    private String backdropUrl;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private boolean favorites;

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

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }

    public Movie(Parcel parcel){
        String[] data = new String[7];
        parcel.readStringArray(data);
        id = data[0];
        title = data[1];
        posterUrl = data[2];
        backdropUrl = data[3];
        overview = data[4];
        voteAverage = Double.valueOf(data[5]);
        releaseDate = data[6];
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
                this.posterUrl,
                this.backdropUrl,
                this.overview,
                String.valueOf(this.voteAverage),
                this.releaseDate
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
