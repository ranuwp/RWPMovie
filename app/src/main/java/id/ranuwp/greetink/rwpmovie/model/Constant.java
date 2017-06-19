package id.ranuwp.greetink.rwpmovie.model;

import android.os.Build;

import id.ranuwp.greetink.rwpmovie.BuildConfig;
import id.ranuwp.greetink.rwpmovie.R;

/**
 * Created by ranuwp on 6/16/2017.
 */

public class Constant {
    public static final String TMDB_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key="+BuildConfig.TMDB_API_KEY;
    public static final String TMDB_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key="+BuildConfig.TMDB_API_KEY;
    public static final String IMAGE_REQUEST_URL = "http://image.tmdb.org/t/p/original/";

    public static String getMovieDetailURL(String id){
        return "http://api.themoviedb.org/3/movie/"+id+"?api_key=901dc762a8face9c6aa6be831c58a3e9";
    }

    public static String getPopularMovie(int page){
        return TMDB_POPULAR+"&page="+page;
    }

    public static String getTopRated(int page){
        return TMDB_TOP_RATED+"&page="+page;
    }
}
