package com.example.amrsakr.movies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.widget.CursorAdapter;
import com.squareup.picasso.Picasso;

public class TrailerAdapter extends CursorAdapter {
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();


    public TrailerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_trailer_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String trailer_name = cursor.getString(DetailFragment.COL_TRAILER_NAME);
        final String source = cursor.getString(DetailFragment.COL_TRAILER_SOURCE);
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.trailer.setText(trailer_name);
        final String BASE_URL = "http://img.youtube.com/vi/";
        final String url = BASE_URL + source + "/0.jpg";
        Picasso
                .with(context)
                .load(url)
                .fit()
                .centerCrop()
                .into(viewHolder.trailerImg);
        viewHolder.trailerImg.setAdjustViewBounds(true);

        final String trailerUrl = "https://www.youtube.com/watch?v=" + source;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                context.startActivity(intent);
            }
        });
    }

    public static class ViewHolder {

        public final TextView trailer;
        public final ImageView trailerImg;

        public ViewHolder(View view) {
            trailer = (TextView) view.findViewById(R.id.trailer_name);
            trailerImg = (ImageView) view.findViewById(R.id.youtubeImg);
        }
    }
}