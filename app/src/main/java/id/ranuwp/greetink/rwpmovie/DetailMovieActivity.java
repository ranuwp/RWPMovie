package id.ranuwp.greetink.rwpmovie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import id.ranuwp.greetink.rwpmovie.model.Constant;
import id.ranuwp.greetink.rwpmovie.model.Movie;

public class DetailMovieActivity extends AppCompatActivity {

    private ImageView backdrop_imageview;
    private TextView title_textview;
    private TextView overview_textview;
    private TextView release_date_textview;
    private TextView rating_textview;
    private AlertDialog alertDialog;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail_movie);
        String id = getIntent().getExtras().getString("id");
        movie = new Movie();
        movie.setId(id);
        setupView();
    }

    private void setupView(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(LayoutInflater.from(this).inflate(R.layout.loading_layout,null));
        alertDialog = dialog.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        backdrop_imageview = (ImageView) findViewById(R.id.backdrop_imageview);
        title_textview = (TextView) findViewById(R.id.title_textview);
        overview_textview = (TextView) findViewById(R.id.overview_textview);
        release_date_textview = (TextView) findViewById(R.id.release_date_textview);
        rating_textview = (TextView) findViewById(R.id.rating_textview);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constant.getMovieDetailURL(movie.getId()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            movie.setBackdrop_url(Constant.IMAGE_REQUEST_URL+response.getString("backdrop_path"));
                            movie.setOverview(response.getString("overview"));
                            movie.setTitle(response.getString("title"));
                            movie.setVote_average(response.getDouble("vote_average"));
                            movie.setRelease_date(response.getString("release_date"));
                            getSupportActionBar().setTitle(movie.getTitle());
                            Glide.with(DetailMovieActivity.this).asBitmap().load(movie.getBackdrop_url()).listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    alertDialog.dismiss();
                                    return false;
                                }
                            }).into(backdrop_imageview);
                            title_textview.setText(movie.getTitle());
                            overview_textview.setText(movie.getOverview());
                            release_date_textview.setText(movie.getRelease_date());
                            rating_textview.setText(String.valueOf(movie.getVote_average()));
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(DetailMovieActivity.this,"Format Exception",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(DetailMovieActivity.this,"Cannot connect to Server",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public static void toActivity(Context context, Movie movie){
        context.startActivity(new Intent(context,DetailMovieActivity.class).putExtra("id",movie.getId()));
    }
}
