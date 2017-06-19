package id.ranuwp.greetink.rwpmovie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import id.ranuwp.greetink.rwpmovie.model.Movie;

public class DetailMovieActivity extends AppCompatActivity {

    private ImageView backdropImageview;
    private TextView titleTextview;
    private TextView overviewTextview;
    private TextView releaseDateTextview;
    private TextView ratingTextview;
    private AlertDialog alertDialog;
    private Movie movie;

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
        movie = getIntent().getParcelableExtra("movie");
        setupView();
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
    }

    public static void toActivity(Context context, Movie movie){
        context.startActivity(new Intent(context,DetailMovieActivity.class).putExtra("movie",movie));
    }
}
