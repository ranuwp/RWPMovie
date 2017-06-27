package id.ranuwp.greetink.rwpmovie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.ranuwp.greetink.rwpmovie.adapter.ReviewAdapter;
import id.ranuwp.greetink.rwpmovie.adapter.TrailerAdapter;
import id.ranuwp.greetink.rwpmovie.model.Constant;
import id.ranuwp.greetink.rwpmovie.model.Movie;
import id.ranuwp.greetink.rwpmovie.model.Review;
import id.ranuwp.greetink.rwpmovie.model.Trailer;
import id.ranuwp.greetink.rwpmovie.moviecontentprovider.MovieContract;

public class DetailMovieActivity extends AppCompatActivity {

    private ImageView backdropImageview;
    private TextView titleTextview;
    private TextView overviewTextview;
    private TextView releaseDateTextview;
    private TextView ratingTextview;
    private AlertDialog alertDialog;
    private Movie movie;

    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;

    //Request
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail_movie);
        if(getIntent() == null || !getIntent().hasExtra("movie")){
            finish();
            Toast.makeText(this,getResources().getString(R.string.null_intent_error),Toast.LENGTH_SHORT).show();
            return;
        }
        requestQueue = Volley.newRequestQueue(this);
        movie = getIntent().getParcelableExtra("movie");
        Cursor cursor = getContentResolver().query(MovieContract.MovieTable.CONTENT_URI,
                null,
                MovieContract.MovieTable._ID+"=?",
                new String[]{movie.getId()},
                null);
        if(cursor.getCount() == 1){
            movie.setFavorites(true);
        }
        setupView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        if(movie.isFavorites()){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_stars_enable));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite :
                if(movie.isFavorites()){
                    unfavorite();
                    item.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_stars_disable));
                    movie.setFavorites(false);
                }else{
                    favorite();
                    item.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_stars_enable));
                    movie.setFavorites(true);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(LayoutInflater.from(this).inflate(R.layout.loading_layout,null));
        alertDialog = dialog.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        backdropImageview = (ImageView) findViewById(R.id.backdrop_imageview);
        titleTextview = (TextView) findViewById(R.id.title_textview);
        overviewTextview = (TextView) findViewById(R.id.overview_textview);
        releaseDateTextview = (TextView) findViewById(R.id.release_date_textview);
        ratingTextview = (TextView) findViewById(R.id.rating_textview);
        getSupportActionBar().setTitle(movie.getTitle());
        Glide.with(DetailMovieActivity.this).asBitmap().load(movie.getBackdropUrl()).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                alertDialog.dismiss();
                return false;
            }
        }).into(backdropImageview);
        titleTextview.setText(movie.getTitle());
        overviewTextview.setText(movie.getOverview());
        releaseDateTextview.setText(movie.getReleaseDate());
        ratingTextview.setText(String.valueOf(movie.getVoteAverage()));
        //ListView
        trailers = new ArrayList<>();
        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recyclerview);
        trailerAdapter = new TrailerAdapter(this,trailers);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        trailerRecyclerView.setAdapter(trailerAdapter);
        JsonObjectRequest trailerRequest = new JsonObjectRequest(Request.Method.GET,
                Constant.getTrailerMovieURLById(movie.getId()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for(int i = 0; i< jsonArray.length();i++){
                                Trailer trailer = new Trailer();
                                trailer.setKey(jsonArray.getJSONObject(i).getString("key"));
                                trailer.setName(jsonArray.getJSONObject(i).getString("name"));
                                trailers.add(trailer);
                            }
                            trailerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(trailerRequest);

        reviews = new ArrayList<>();
        reviewRecyclerView = (RecyclerView) findViewById(R.id.review_recyclerview);
        reviewAdapter = new ReviewAdapter(this, reviews);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setAdapter(reviewAdapter);
        JsonObjectRequest reviewRequest = new JsonObjectRequest(Request.Method.GET,
                Constant.getReviewMovieURLById(movie.getId()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for(int i = 0; i< jsonArray.length();i++){
                                Review review = new Review();
                                review.setAuthor(jsonArray.getJSONObject(i).getString("author"));
                                review.setContent(jsonArray.getJSONObject(i).getString("content"));
                                reviews.add(review);
                            }
                            reviewAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(reviewRequest);
    }

    public static void toActivity(Context context, Movie movie){
        context.startActivity(new Intent(context,DetailMovieActivity.class).putExtra("movie",movie));
    }

    public void favorite(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieTable._ID,movie.getId());
        contentValues.put(MovieContract.MovieTable.COLUMN_BACKDROP_URL,movie.getBackdropUrl());
        contentValues.put(MovieContract.MovieTable.COLUMN_OVERVIEW,movie.getOverview());
        contentValues.put(MovieContract.MovieTable.COLUMN_POSTER_URL,movie.getPosterUrl());
        contentValues.put(MovieContract.MovieTable.COLUMN_RELEASE_DATE,movie.getReleaseDate());
        contentValues.put(MovieContract.MovieTable.COLUMN_TITLE,movie.getTitle());
        contentValues.put(MovieContract.MovieTable.COLUMN_VOTE_AVERAGE,movie.getVoteAverage());
        Uri uri = getContentResolver().insert(MovieContract.MovieTable.CONTENT_URI,contentValues);
        if(uri == null){
            Log.d(getClass().getSimpleName(), "insertMovie: ");
        }
    }

    public void unfavorite(){
        getContentResolver().delete(MovieContract.MovieTable.CONTENT_URI,
                MovieContract.MovieTable._ID+"=?",
                new String[]{movie.getId()});
    }
}
