package id.ranuwp.greetink.rwpmovie.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import id.ranuwp.greetink.rwpmovie.MainActivity;
import id.ranuwp.greetink.rwpmovie.R;
import id.ranuwp.greetink.rwpmovie.listener.OnMovieClickListener;
import id.ranuwp.greetink.rwpmovie.model.Movie;

/**
 * Created by ranuwp on 6/16/2017.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    ImageView poster;
    View itemView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        poster = (ImageView) itemView.findViewById(R.id.poster_image);
        this.itemView = itemView;
    }

    public void bind(final Movie movie, Context context, final OnMovieClickListener onMovieClickListener){
        Glide.with(context).load(movie.getPoster_url()).into(poster);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMovieClickListener.onClick(movie);
            }
        });
    }

}
