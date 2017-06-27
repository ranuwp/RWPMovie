package id.ranuwp.greetink.rwpmovie.model;

import android.net.Uri;

import id.ranuwp.greetink.rwpmovie.BuildConfig;

/**
 * Created by ranuwp on 6/16/2017.
 */

public class Constant {
    private static final String TMDB_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key="+BuildConfig.TMDB_API_KEY;
    private static final String TMDB_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key="+BuildConfig.TMDB_API_KEY;
    public static final String IMAGE_REQUEST_URL = "http://image.tmdb.org/t/p/original/";


    public static String getMovieDetailURL(String id){
        return "http://api.themoviedb.org/3/movie/"+id+"?api_key="+BuildConfig.TMDB_API_KEY;
    }

    public static String getTrailerMovieURLById(String id){
        return "http://api.themoviedb.org/3/movie/"+id+"/videos?api_key="+BuildConfig.TMDB_API_KEY;
    }

    public static String getReviewMovieURLById(String id){
        return "http://api.themoviedb.org/3/movie/"+id+"/reviews?api_key="+BuildConfig.TMDB_API_KEY;
    }

    public static Uri getYoutubeUriById(String id){
        return Uri.parse("http://www.youtube.com/watch?v="+id);
    }

    public static String getPopularMovie(int page){
        return TMDB_POPULAR+"&page="+page;
    }

    public static String getTopRated(int page){
        return TMDB_TOP_RATED+"&page="+page;
    }
}
