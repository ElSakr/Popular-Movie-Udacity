package com.example.amrsakr.movies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrsakr.movies.data.MovieContract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    static final String DETAIL_URI = "URI";
    private Uri mUri;
    private static final String MOVIE_SHARE_HASHTAG = " #PopularMovieApp";
    private static final int DETAIL_LOADER = 0;
    private static final int TRAILER_LOADER = 1;
    private static final int REVIEW_LOADER = 2;
    private static final int FAVOURITE_LOADER = 3;
    private static final String[] MOVIE_COLUMNS = {

            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies._ID,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.PAGE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.POSTER_PATH,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ADULT,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.OVERVIEW,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.RELEASE_DATE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.MOVIE_ID,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ORIGINAL_TITLE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ORIGINAL_LANGUAGE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.TITLE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.BACKDROP_PATH,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.POPULARITY,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.VOTE_COUNT,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.VOTE_AVERAGE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.FAVOURED,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.SHOWED,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.DOWNLOADED,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.SORT_BY

    };
    private static final String[] TRAILER_COLUMNS = {

            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers._ID,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.NAME,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.SIZE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.SOURCE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.TYPE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.MOVIE_ID
    };
    private static final String[] REVIEW_COLUMNS = {

            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews._ID,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.PAGE,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.TOTAL_PAGE,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.TOTAL_RESULTS,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.ID_REVIEWS,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.AUTHOR,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.CONTENT,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.URL,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.MOVIE_ID

    };

    private static final String[] FAVOURITE_MOVIE_COLUMNS = {

            MovieContract.Favourites.TABLE_NAME + "." + MovieContract.Favourites._ID,
            MovieContract.Favourites.PAGE,
            MovieContract.Favourites.POSTER_PATH,
            MovieContract.Favourites.ADULT,
            MovieContract.Favourites.OVERVIEW,
            MovieContract.Favourites.RELEASE_DATE,
            MovieContract.Favourites.MOVIE_ID,
            MovieContract.Favourites.ORIGINAL_TITLE,
            MovieContract.Favourites.ORIGINAL_LANGUAGE,
            MovieContract.Favourites.TITLE,
            MovieContract.Favourites.BACKDROP_PATH,
            MovieContract.Favourites.POPULARITY,
            MovieContract.Favourites.VOTE_COUNT,
            MovieContract.Favourites.VOTE_AVERAGE,
            MovieContract.Favourites.FAVOURED,
            MovieContract.Favourites.SHOWED,
            MovieContract.Favourites.DOWNLOADED,
            MovieContract.Favourites.SORT_BY
    };
    public static String playTrailer = "https://www.youtube.com/watch?v=";
    public static int COL_ID = 0;
    public static int COL_PAGE = 1;
    public static int COL_POSTER_PATH = 2;
    public static int COL_ADULT = 3;
    public static int COL_OVERVIEW = 4;
    public static int COL_RELEASE_DATE = 5;
    public static int COL_MOVIE_ID = 6;
    public static int COL_ORIGINAL_TITLE = 7;
    public static int COL_ORIGINAL_LANG = 8;
    public static int COL_TITLE = 9;
    public static int COL_BACKDROP_PATH = 10;
    public static int COL_POPULARITY = 11;
    public static int COL_VOTE_COUNT = 12;
    public static int COL_VOTE_AVERAGE = 13;
    public static int COL_FAVOURED = 14;
    public static int COL_SHOWED = 15;
    public static int COL_DOWNLOADED = 16;
    public static int COL_SORT_BY = 17;
    public static int COL_TRAILER_ID = 0;
    public static int COL_TRAILER_NAME = 1;
    public static int COL_TRAILER_SIZE = 2;
    public static int COL_TRAILER_SOURCE = 3;
    public static int COL_TRAILER_TYPE = 4;
    public static int COL_TRAILER_MOVIE_ID = 5;
    public static int COL_REVIEW_ID = 0;
    public static int COL_REVIEW_PAGE = 1;
    public static int COL_REVIEW_TOTAL_PAGE = 2;
    public static int COL_REVIEW_TOTAL_RESULTS = 3;
    public static int COL_REVIEW_ID_REVIEWS = 4;
    public static int COL_REVIEW_AUTHOR = 5;
    public static int COL_REVIEW_CONTENT = 6;
    public static int COL_REVIEW_URL = 7;
    public static int COL_REVIEW_MOVIE_ID = 8;
    private static ContentValues movieValues;
    String orgLang;
    String orgTitle;
    String overview;
    String relDate;
    String postURL;
    String popularity;
    String votAvg;
    String movieId;
    String backdropURL;
    private View rootView;
    private TrailerAdapter trailerListAdapter;
    private ReviewAdapter reviewListAdapter;
    private ListView listViewTrailers;
    private ListView listViewReviews;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShareActionProvider mShareActionProvider;

    public DetailFragment() {
        setHasOptionsMenu(true);
        playTrailer = "https://www.youtube.com/watch?v=";
        movieValues = new ContentValues();

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (orgTitle != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }
    private Intent createShareMovieIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,
                orgTitle + "Watch : " + playTrailer + MOVIE_SHARE_HASHTAG);
        intent.setType("text/plain");
        return intent;
    }
    private void updateMovieList() {
        if (mUri!=null){
        FetchTrailReviewTask trailReviewTask = new FetchTrailReviewTask(getActivity());
        trailReviewTask.execute(MovieContract.Movies.getIDFromUri(mUri));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }
        updateMovieList();
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        trailerListAdapter = new TrailerAdapter(getActivity(), null, 0);
        listViewTrailers = (ListView) rootView.findViewById(R.id.listview_trailer);
        listViewTrailers.setAdapter(trailerListAdapter);

        reviewListAdapter = new ReviewAdapter(getActivity(), null, 0);
        listViewReviews = (ListView) rootView.findViewById(R.id.listview_review);
        listViewReviews.setAdapter(reviewListAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.detail_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utility.isOnline(getActivity())) {
                    if (mUri!=null){
                        getActivity().getContentResolver().delete(MovieContract.Trailers.buildMoviesUriWithMovieId(MovieContract.Movies.getIDFromUri(mUri)), null, null);
                        getActivity().getContentResolver().delete(MovieContract.Reviews.buildMoviesUriWithMovieId(MovieContract.Movies.getIDFromUri(mUri)), null, null);
                        updateMovieList();
                    }
                    else {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    //Log.v(LOG_TAG, "refreshed");
                } else{
                    Toast.makeText(getContext(), "Network Not Available!", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(FAVOURITE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void onSortingChanged() {
        if (mUri!=null) {
            String mI = MovieContract.Movies.getIDFromUri(mUri);
            if (null != mI) {
                getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
                getLoaderManager().restartLoader(TRAILER_LOADER, null, this);
                getLoaderManager().restartLoader(REVIEW_LOADER, null, this);
                getLoaderManager().restartLoader(FAVOURITE_LOADER, null, this);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("onCreateLoader", ""+ id);
        if (mUri!=null && null != MovieContract.Movies.getIDFromUri(mUri)) {
            switch (id) {
                case DETAIL_LOADER:
                    String sorting = Utility.getPreferredSorting(getActivity());
                    if (sorting.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
                        return new CursorLoader(getActivity(),
                                MovieContract.Favourites.buildMovieUri(),
                                FAVOURITE_MOVIE_COLUMNS,
                                MovieContract.Favourites.MOVIE_ID + " = ?",
                                new String[]{MovieContract.Movies.getIDFromUri(mUri)},
                                null);
                    }
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Movies.buildMoviesUriWithMovieId(MovieContract.Movies.getIDFromUri(mUri)),
                            MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                case FAVOURITE_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Favourites.buildMovieUri(),
                            FAVOURITE_MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                case TRAILER_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Trailers.buildMoviesUriWithMovieId(MovieContract.Movies.getIDFromUri(mUri)),
                            TRAILER_COLUMNS,
                            null,
                            null,
                            null
                    );
                case REVIEW_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Reviews.buildMoviesUriWithMovieId(MovieContract.Movies.getIDFromUri(mUri)),
                            REVIEW_COLUMNS,
                            null,
                            null,
                            null
                    );
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        swipeRefreshLayout.setRefreshing(false);
       // Log.v("In onLoadFinished" , " " + loader.getId());
        if (!data.moveToFirst() && loader.getId()!=FAVOURITE_LOADER) {
            return;
        }
        switch (loader.getId()) {
            case DETAIL_LOADER:
                orgLang = data.getString(COL_ORIGINAL_LANG);

                orgTitle = data.getString(COL_ORIGINAL_TITLE);
                ((TextView) rootView.findViewById(R.id.orgTitle))
                        .setText(orgTitle);

                overview = data.getString(COL_OVERVIEW);
                ((TextView) rootView.findViewById(R.id.overview))
                        .setText(overview);

                relDate = data.getString(COL_RELEASE_DATE);

                ((TextView) rootView.findViewById(R.id.relDate))
                        .setText("Release Date: " + relDate);

                postURL = data.getString(COL_POSTER_PATH);
                ImageView poster = (ImageView) rootView.findViewById(R.id.poster);
                Picasso
                        .with(getActivity())
                        .load(postURL)
                        .fit()
                        .into(poster);

                movieId = data.getString(COL_MOVIE_ID);
                popularity = data.getString(COL_POPULARITY);
                double pop = Double.parseDouble(popularity);
                popularity = String.valueOf((double) Math.round(pop * 10d) / 10d);

                ((TextView) rootView.findViewById(R.id.popularity))
                        .setText("Popularity : " + popularity);

                votAvg = data.getString(COL_VOTE_AVERAGE);
                double vote = Double.parseDouble(votAvg);
                votAvg = String.valueOf((double) Math.round(vote * 10d) / 10d);
                ((TextView) rootView.findViewById(R.id.vote))
                        .setText(votAvg);
                backdropURL = data.getString(COL_BACKDROP_PATH);

                final ImageView backdrop = (ImageView) rootView.findViewById(R.id.backdropImg);
                Picasso
                        .with(getActivity())
                        .load(backdropURL)
                        .fit()
                        .centerCrop()
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(backdrop, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                Picasso
                                        .with(getActivity())
                                        .load(backdropURL)
                                        .fit()
                                        .centerCrop()
                                        .error(R.mipmap.ic_launcher)
                                        .into(backdrop, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                            }

                                            @Override
                                            public void onError() {
                                                //Log.v("Error Loading Images", "'");
                                            }
                                        });
                            }
                        });
                backdrop.setAdjustViewBounds(true);
                rootView.findViewById(R.id.divisor).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ten).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.TrailerText).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.listview_trailer).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ReviewsText).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.listview_review).setVisibility(View.VISIBLE);

                if (movieValues.size() == 0) {
                    movieValues.put(MovieContract.Movies.PAGE, data.getString(COL_PAGE));
                    movieValues.put(MovieContract.Movies.POSTER_PATH, postURL);
                    movieValues.put(MovieContract.Movies.ADULT, data.getString(COL_ADULT));
                    movieValues.put(MovieContract.Movies.OVERVIEW, overview);
                    movieValues.put(MovieContract.Movies.RELEASE_DATE, relDate);
                    movieValues.put(MovieContract.Movies.MOVIE_ID, MovieContract.Movies.getIDFromUri(mUri));
                    movieValues.put(MovieContract.Movies.ORIGINAL_TITLE, orgTitle);
                    movieValues.put(MovieContract.Movies.ORIGINAL_LANGUAGE, orgLang);
                    movieValues.put(MovieContract.Movies.TITLE, data.getString(COL_TITLE));
                    movieValues.put(MovieContract.Movies.BACKDROP_PATH, backdropURL);
                    movieValues.put(MovieContract.Movies.POPULARITY, popularity);
                    movieValues.put(MovieContract.Movies.VOTE_COUNT, data.getString(COL_VOTE_COUNT));
                    movieValues.put(MovieContract.Movies.VOTE_AVERAGE, votAvg);
                    movieValues.put(MovieContract.Movies.FAVOURED, "1");
                    movieValues.put(MovieContract.Movies.SHOWED, data.getString(COL_SHOWED));
                    movieValues.put(MovieContract.Movies.DOWNLOADED, data.getString(COL_DOWNLOADED));
                    movieValues.put(MovieContract.Movies.SORT_BY, data.getString(COL_SORT_BY));
                }

                break;
            case FAVOURITE_LOADER:
                final Button fab = (Button) rootView.findViewById(R.id.btnFavourite);
                rootView.findViewById(R.id.btnFavourite).setVisibility(View.VISIBLE);
                boolean favoured = false;
                if (data.moveToFirst()) {
                    do {
                        if (data.getString(COL_MOVIE_ID).equalsIgnoreCase(MovieContract.Movies.getIDFromUri(mUri))) {
                            favoured = true;
                        }
                    }
                    while (data.moveToNext());
                }
                if (favoured) {
                    fab.setText("Remove From Favourite");
                } else {
                    fab.setText("Add To Favourite");
                }
                final boolean finalFavoured = favoured;
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalFavoured) {
                            getActivity().getContentResolver().delete(MovieContract.Favourites.buildMoviesUriWithMovieId(MovieContract.Movies.getIDFromUri(mUri)), null, null);
                            Toast.makeText(getContext(), "REMOVED FROM FAVOURITES!", Toast.LENGTH_SHORT).show();
                            fab.setText("Add To Favourite");
                        } else {
                            getActivity().getContentResolver().insert(MovieContract.Favourites.buildMovieUri(), movieValues);
                            Toast.makeText(getContext(), "ADDED TO FAVOURITES!", Toast.LENGTH_SHORT).show();
                            fab.setText("Remove From Favourite");
                        }
                    }
                });
                break;
            case TRAILER_LOADER:
                int iter = 0;
                if (data.moveToFirst()) {
                    do {
                        trailerListAdapter.swapCursor(data);
                        iter++;
                        if (iter == 1) {
                            playTrailer += data.getString(DetailFragment.COL_TRAILER_SOURCE);
                            if (mShareActionProvider != null) {
                                mShareActionProvider.setShareIntent(createShareMovieIntent());
                            }
                        }
                    }
                    while (data.moveToNext());
                }
                break;
            case REVIEW_LOADER:
                //Log.e(LOG_TAG, "Review");
                if (data.moveToFirst()) {
                    do {
                        reviewListAdapter.swapCursor(data);
                    }
                    while (data.moveToNext());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Loader");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        trailerListAdapter.swapCursor(null);
        reviewListAdapter.swapCursor(null);
    }
}
