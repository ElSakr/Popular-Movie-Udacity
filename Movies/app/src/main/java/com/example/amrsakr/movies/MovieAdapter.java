package com.example.amrsakr.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends CursorAdapter {
    //private static final String LOG_TAG = MovieAdapter.class.getSimpleName();


    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        final String url = cursor.getString(MainFragment.COL_POSTER_PATH);
        Picasso
                .with(context)
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .into(viewHolder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso
                                .with(context)
                                .load(url)
                                .error(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(viewHolder.imageView);
                    }
                });
        viewHolder.imageView.setAdjustViewBounds(true);
    }

    public static class ViewHolder {

        final ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.poster_image_view);
        }
    }
}