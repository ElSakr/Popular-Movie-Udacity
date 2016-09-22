package com.example.amrsakr.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ReviewAdapter extends CursorAdapter {
    //private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    public ReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_review_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String author_name = cursor.getString(DetailFragment.COL_REVIEW_AUTHOR);
        final String content = cursor.getString(DetailFragment.COL_REVIEW_CONTENT);
        final String url = cursor.getString(DetailFragment.COL_REVIEW_URL);

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.author.setText("Author:  " + author_name);
        viewHolder.contentView.setText("Content:  " + content);
        viewHolder.urlView.setText("Look more at:  " + url);
    }

    public static class ViewHolder {

        public final TextView author;
        public final TextView contentView;
        public final TextView urlView;

        public ViewHolder(View view) {
            author = (TextView) view.findViewById(R.id.review_author);
            contentView = (TextView) view.findViewById(R.id.review_content);
            urlView = (TextView) view.findViewById(R.id.review_url);

        }
    }
}