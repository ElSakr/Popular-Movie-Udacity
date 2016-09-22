package com.example.amrsakr.movies;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrsakr.movies.data.MovieContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String SELECTED_KEY = "selected_position";
    private static final int MOVIE_LOADER = 0;
    private static final String[] MOVIE_COLUMNS = {

            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies._ID,
            MovieContract.Movies.PAGE,
            MovieContract.Movies.POSTER_PATH,
            MovieContract.Movies.ADULT,
            MovieContract.Movies.OVERVIEW,
            MovieContract.Movies.RELEASE_DATE,
            MovieContract.Movies.MOVIE_ID,
            MovieContract.Movies.ORIGINAL_TITLE,
            MovieContract.Movies.ORIGINAL_LANGUAGE,
            MovieContract.Movies.TITLE,
            MovieContract.Movies.BACKDROP_PATH,
            MovieContract.Movies.POPULARITY,
            MovieContract.Movies.VOTE_COUNT,
            MovieContract.Movies.VOTE_AVERAGE,
            MovieContract.Movies.FAVOURED
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
            MovieContract.Favourites.FAVOURED
    };
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
    private MovieAdapter movieListAdapter;
    private int mPosition = ListView.INVALID_POSITION;
    private GridView gridView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int PAGE_LOADED = 1;
    private View rootView;

    public MainFragment() {

    }

    private void updateMovieList() {
        FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        String sortingOrder = Utility.getPreferredSorting(getActivity());
        if (!sortingOrder.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
            {
                movieTask.execute(sortingOrder, String.valueOf(PAGE_LOADED));
                PAGE_LOADED++;
                //Log.v("PAGE_LOADED",String.valueOf(PAGE_LOADED));
            }
        } else if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        PAGE_LOADED = 1;
        if (!Utility.isOnline(getActivity())) {
            Toast.makeText(getContext(), "Network Not Available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movieListAdapter =
                new MovieAdapter(
                        getActivity(), null, 0);

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.image_grid);
        gridView.setAdapter(movieListAdapter);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                final int size = gridView.getWidth();
                int numCol = (int) Math.round((double) size / (double) getResources().getDimensionPixelSize(R.dimen.poster_width));
                gridView.setNumColumns(numCol);
                //Log.d("MainActivityFrag", "Value: " +size+" "+numCol+" "+getResources().getDimensionPixelSize(R.dimen.poster_width)+" "+test);
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mPosition = firstVisibleItem;
                if(firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount>0 && !swipeRefreshLayout.isRefreshing()){
                    // End has been reached

                    //PAGE_LOADED++;
                    swipeRefreshLayout.setRefreshing(true);
                    updateMovieList();
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.Movies.buildMoviesUriWithMovieId(
                                     cursor.getString(COL_MOVIE_ID)
                            ));
                }
                mPosition = position;
            }
        });
        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.main_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utility.isOnline(getActivity())) {
                    if (!Utility.getPreferredSorting(getActivity()).equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))){
                        PAGE_LOADED = 1;
                        getActivity().getContentResolver().delete(MovieContract.Movies.CONTENT_URI, null, null);
                        Toast.makeText(getActivity(), "delete database and update new data", Toast.LENGTH_SHORT).show();
                        updateMovieList();
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }else {
                    Toast.makeText(getContext(), "Network Not Available!", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    // since we read the new soring order when we create the loader, all we need to do is restart things
    void onSortingChanged() {
        PAGE_LOADED = 1;
        updateMovieList();
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String sortOrder = MovieContract.Movies._ID + " ASC";
        Uri movie = MovieContract.Movies.buildMovieUri();
        Uri fav = MovieContract.Favourites.buildMovieUri();
        String sorting = Utility.getPreferredSorting(getActivity());
        if (sorting.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
            return new CursorLoader(getActivity(),
                    fav,
                    FAVOURITE_MOVIE_COLUMNS,
                    MovieContract.Favourites.FAVOURED + " = ?",
                    new String[]{"1"},
                    sortOrder);
        }
        return new CursorLoader(getActivity(),
                movie,
                MOVIE_COLUMNS,
               MovieContract.Movies.SORT_BY + " = ?",
                new String[]{sorting},
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        movieListAdapter.swapCursor(cursor);
        swipeRefreshLayout.setRefreshing(false);
        if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            gridView.smoothScrollToPosition(mPosition);
        }
        try {
            TextView info = (TextView) rootView.findViewById(R.id.empty);
            if (movieListAdapter.getCount() == 0) {
                String sorting = Utility.getPreferredSorting(getActivity());
                if (sorting.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
                    info.setText("Favourite List is Empty!");
                }
                info.setVisibility(View.VISIBLE);
            } else {
                info.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mPosition=0;
        PAGE_LOADED = 1;
        movieListAdapter.swapCursor(null);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         *
         * @param dateUri
         */
        void onItemSelected(Uri dateUri);
    }
}
