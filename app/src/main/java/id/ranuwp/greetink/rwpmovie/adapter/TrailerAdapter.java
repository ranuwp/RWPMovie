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
import id.ranuwp.greetink.rwpmovie.model.Trailer;
import id.ranuwp.greetink.rwpmovie.viewholder.MovieViewHolder;
import id.ranuwp.greetink.rwpmovie.viewholder.TrailerViewHolder;

/**
 * Created by ranuwp on 6/16/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    private Context context;
    private ArrayList<Trailer> trailers;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_layout,parent,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(trailers.get(position),context);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}
