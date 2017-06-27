package id.ranuwp.greetink.rwpmovie;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.ranuwp.greetink.rwpmovie.adapter.MovieAdapter;
import id.ranuwp.greetink.rwpmovie.listener.OnMovieClickListener;
import id.ranuwp.greetink.rwpmovie.model.Constant;
import id.ranuwp.greetink.rwpmovie.model.Movie;
import id.ranuwp.greetink.rwpmovie.moviecontentprovider.MovieContract;

public class MainActivity extends AppCompatActivity implements OnMovieClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView movieRecyclerview;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    private static final int POPULAR_TAG = 0;
    private static final int TOP_RATED_TAG = 1;
    private static final int FAVORITES_TAG = 2;
    //Loader
    private static final int MOVIE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(savedInstanceState != null){
            switch (sharedPreferences.getInt("menu",0)){
                case POPULAR_TAG :
                    getSupportActionBar().setTitle(getResources().getString(R.string.popular));
                    break;
                case TOP_RATED_TAG :
                    getSupportActionBar().setTitle(getResources().getString(R.string.top_rated));
                    break;
                case FAVORITES_TAG :
                    getSupportActionBar().setTitle(getResources().getString(R.string.favorites));
                    break;
            }
            ArrayList<Movie> temp = savedInstanceState.getParcelableArrayList("movies");
            for(Movie movie : temp){
                movies.add(movie);
            }
            movieAdapter.notifyDataSetChanged();
        }else{
            switch (sharedPreferences.getInt("menu",0)){
                case POPULAR_TAG :
                    getSupportActionBar().setTitle(getResources().getString(R.string.popular));
                    loadPopular(1);
                    break;
                case TOP_RATED_TAG :
                    getSupportActionBar().setTitle(getResources().getString(R.string.top_rated));
                    loadTopRated(1);
                    break;
                case FAVORITES_TAG :
                    getSupportActionBar().setTitle(getResources().getString(R.string.favorites));
                    loadFavorites();
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies",movies);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupView() {
        movieRecyclerview = (RecyclerView) findViewById(R.id.movie_recyclerview);
        movies = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns(), StaggeredGridLayoutManager.VERTICAL);
        movieAdapter = new MovieAdapter(this, movies, this);
        movieRecyclerview.setLayoutManager(staggeredGridLayoutManager);
        movieRecyclerview.setAdapter(movieAdapter);
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        movies.clear();
        movieAdapter.notifyDataSetChanged();
        getSupportActionBar().setTitle(item.getTitle());
        switch (item.getItemId()) {
            case R.id.popular:
                sharedPreferences.edit().putInt("menu",POPULAR_TAG).apply();
                loadPopular(1);
                break;
            case R.id.top_rated:
                sharedPreferences.edit().putInt("menu",TOP_RATED_TAG).apply();
                loadTopRated(1);
                break;
            case R.id.favorites:
                sharedPreferences.edit().putInt("menu",FAVORITES_TAG).apply();
                loadFavorites();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavorites() {
        movies.clear();
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    private void loadPopular(int page) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constant.getPopularMovie(page),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Movie movie = new Movie();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                movie.setId(jsonObject.getString("id"));
                                movie.setPosterUrl(Constant.IMAGE_REQUEST_URL + jsonObject.getString("poster_path"));
                                movie.setBackdropUrl(Constant.IMAGE_REQUEST_URL + jsonObject.getString("backdrop_path"));
                                movie.setOverview(jsonObject.getString("overview"));
                                movie.setTitle(jsonObject.getString("title"));
                                movie.setVoteAverage(jsonObject.getDouble("vote_average"));
                                movie.setReleaseDate(jsonObject.getString("release_date"));
                                movies.add(movie);
                            }
                            movieAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Can't Connect To Server", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void loadTopRated(int page) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constant.getTopRated(page),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Movie movie = new Movie();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                movie.setId(jsonObject.getString("id"));
                                movie.setPosterUrl(Constant.IMAGE_REQUEST_URL + jsonObject.getString("poster_path"));
                                movie.setBackdropUrl(Constant.IMAGE_REQUEST_URL + jsonObject.getString("backdrop_path"));
                                movie.setOverview(jsonObject.getString("overview"));
                                movie.setTitle(jsonObject.getString("title"));
                                movie.setVoteAverage(jsonObject.getDouble("vote_average"));
                                movie.setReleaseDate(jsonObject.getString("release_date"));
                                movies.add(movie);
                            }
                            movieAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Can't Connect To Server", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int widyh = displayMetrics.widthPixels;
        int nColumna = widyh / widthDivider;
        if (nColumna < 2) return 2;
        return nColumna;
    }

    @Override
    public void onClick(Movie movie) {
        DetailMovieActivity.toActivity(this, movie);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVIE_LOADER_ID:
                return new AsyncTaskLoader<Cursor>(this) {

                    Cursor movieData = null;

                    @Override
                    protected void onStartLoading() {
                        if (movieData != null) {
                            deliveryResult(movieData);
                        } else {
                            forceLoad();
                        }
                    }

                    @Override
                    public Cursor loadInBackground() {

                        try {
                            Cursor cursor = getContentResolver().query(MovieContract.MovieTable.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    MovieContract.MovieTable.COLUMN_VOTE_AVERAGE);
                            return cursor;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    public void deliveryResult(Cursor data) {
                        movieData = data;
                        super.deliverResult(data);
                    }
                };
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while (data.moveToNext()) {
            Movie movie = new Movie();
            movie.setTitle(data.getString(data.getColumnIndex(MovieContract.MovieTable.COLUMN_TITLE)));
            movie.setOverview(data.getString(data.getColumnIndex(MovieContract.MovieTable.COLUMN_OVERVIEW)));
            movie.setId(data.getString(data.getColumnIndex(MovieContract.MovieTable._ID)));
            movie.setBackdropUrl(data.getString(data.getColumnIndex(MovieContract.MovieTable.COLUMN_BACKDROP_URL)));
            movie.setPosterUrl(data.getString(data.getColumnIndex(MovieContract.MovieTable.COLUMN_POSTER_URL)));
            movie.setReleaseDate(data.getString(data.getColumnIndex(MovieContract.MovieTable.COLUMN_RELEASE_DATE)));
            movie.setVoteAverage(data.getDouble(data.getColumnIndex(MovieContract.MovieTable.COLUMN_VOTE_AVERAGE)));
            Log.d("test", "test");
            movies.add(movie);
        }
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("Loader Reset", "Reset");
    }
}
