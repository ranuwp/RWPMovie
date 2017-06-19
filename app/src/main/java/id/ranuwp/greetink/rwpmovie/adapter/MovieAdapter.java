package id.ranuwp.greetink.rwpmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import id.ranuwp.greetink.rwpmovie.R;
import id.ranuwp.greetink.rwpmovie.listener.OnMovieClickListener;
import id.ranuwp.greetink.rwpmovie.model.Movie;
import id.ranuwp.greetink.rwpmovie.viewholder.MovieViewHolder;

/**
 * Created by ranuwp on 6/16/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private Context context;
    private ArrayList<Movie> movies;
    private OnMovieClickListener onMovieClickListener;

    public MovieAdapter(Context context, ArrayList<Movie> movies, OnMovieClickListener onMovieClickListener) {
        this.context = context;
        this.movies = movies;
        this.onMovieClickListener = onMovieClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_viewholder,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position),context,onMovieClickListener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
