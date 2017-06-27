package id.ranuwp.greetink.rwpmovie.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import id.ranuwp.greetink.rwpmovie.R;
import id.ranuwp.greetink.rwpmovie.listener.OnMovieClickListener;
import id.ranuwp.greetink.rwpmovie.model.Movie;
import id.ranuwp.greetink.rwpmovie.model.Review;

/**
 * Created by ranuwp on 6/16/2017.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    private TextView authorTextView;
    private TextView contentTextView;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        authorTextView = (TextView) itemView.findViewById(R.id.author_textview);
        contentTextView = (TextView) itemView.findViewById(R.id.content_textview);
    }

    public void bind(Review review){
        authorTextView.setText(review.getAuthor());
        contentTextView.setText(review.getContent());
    }

}
