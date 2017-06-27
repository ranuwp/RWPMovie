package id.ranuwp.greetink.rwpmovie.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import id.ranuwp.greetink.rwpmovie.R;
import id.ranuwp.greetink.rwpmovie.listener.OnMovieClickListener;
import id.ranuwp.greetink.rwpmovie.model.Constant;
import id.ranuwp.greetink.rwpmovie.model.Movie;
import id.ranuwp.greetink.rwpmovie.model.Trailer;

/**
 * Created by ranuwp on 6/16/2017.
 */

public class TrailerViewHolder extends RecyclerView.ViewHolder {

    private ImageView playImageView;
    private TextView nameTextView;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        playImageView = (ImageView) itemView.findViewById(R.id.play_imageview);
        nameTextView = (TextView) itemView.findViewById(R.id.name_textview);
    }

    public void bind(final Trailer trailer,final Context context){
        playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Constant.getYoutubeUriById(trailer.getKey()));
                context.startActivity(intent);
            }
        });
        nameTextView.setText(trailer.getName());
    }

}
