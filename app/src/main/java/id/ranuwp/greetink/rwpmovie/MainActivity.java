package id.ranuwp.greetink.rwpmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
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

public class MainActivity extends AppCompatActivity implements OnMovieClickListener{

    private RecyclerView movieRecyclerview;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies;
    private RequestQueue requestQueue;
    private int orderBy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(getResources().getString(R.string.popular));
        setupView();
    }

    private void setupView(){
        movieRecyclerview = (RecyclerView) findViewById(R.id.movie_recyclerview);
        movies = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        movieAdapter = new MovieAdapter(this,movies,this);
        movieRecyclerview.setLayoutManager(staggeredGridLayoutManager);
        movieRecyclerview.setAdapter(movieAdapter);
        requestQueue = Volley.newRequestQueue(this);
        loadPopular(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        movies.clear();
        switch (item.getItemId()){
            case R.id.order_by :
                int page;
                if(orderBy == 0){
                    page = 1;
                    getSupportActionBar().setTitle("Top Rated");
                    item.setIcon(R.drawable.ic_thumb_up);
                    item.setTitle("Top Rated");
                    orderBy = 1;
                    loadTopRated(page);
                }else{
                    page = 1;
                    getSupportActionBar().setTitle("Popular");
                    item.setIcon(R.drawable.ic_stars);
                    item.setTitle("Popular");
                    orderBy = 0;
                    loadPopular(1);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPopular(int page){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constant.getPopularMovie(page),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for(int i = 0; i < jsonArray.length();i++){
                                Movie movie = new Movie();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                movie.setId(jsonObject.getString("id"));
                                movie.setPosterUrl(Constant.IMAGE_REQUEST_URL +jsonObject.getString("poster_path"));
                                movie.setBackdropUrl(Constant.IMAGE_REQUEST_URL+jsonObject.getString("backdrop_path"));
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
                    public void onErrorResponse(VolleyError error) {Toast.makeText(MainActivity.this,"Can't Connect To Server",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void loadTopRated(int page){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constant.getTopRated(page),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for(int i = 0; i < jsonArray.length();i++){
                                Movie movie = new Movie();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                movie.setId(jsonObject.getString("id"));
                                movie.setPosterUrl(Constant.IMAGE_REQUEST_URL +jsonObject.getString("poster_path"));
                                movie.setBackdropUrl(Constant.IMAGE_REQUEST_URL+jsonObject.getString("backdrop_path"));
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
                    public void onErrorResponse(VolleyError error) {Toast.makeText(MainActivity.this,"Can't Connect To Server",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private int numberOfColumns(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int widyh = displayMetrics.widthPixels;
        int nColumna = widyh/widthDivider;
        if(nColumna<2)return 2;
        return nColumna;
    }

    @Override
    public void onClick(Movie movie) {
        DetailMovieActivity.toActivity(this,movie);
    }
}
