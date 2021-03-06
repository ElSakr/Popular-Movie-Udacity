package com.example.amrsakr.movies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.amrsakr.movies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Amr Sakr on 9/21/2016.
 */

public class FetchTrailReviewTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final Context mContext;
    private boolean DEBUG = true;

    public FetchTrailReviewTask(Context context) {
        mContext = context;
    }

    private void getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String NAME = "name";
        final String SIZE = "size";
        final String SOURCE = "source";
        final String TYPE = "type";
        final String REVIEW_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";
        final String TOTAL_PAGES_REVIEWS = "total_pages";
        final String TOTAL_RESULTS_REVIEWS = "total_results";
        final String PAGE = "page";
        final String MOVIE_ID = "id";

        final String RESULT1 = "trailers";
        final String RESULT2 = "reviews";
        final String YOUTUBE = "youtube";


        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArrayTrailer = movieJson.getJSONObject(RESULT1).getJSONArray(YOUTUBE);


            JSONObject movieArrayReviews = movieJson.getJSONObject(RESULT2);

            String movie_id = movieJson.getString(MOVIE_ID);

            Vector<ContentValues> cVVectorTrailer = new Vector<ContentValues>(movieArrayTrailer.length());

            for (int i = 0; i < movieArrayTrailer.length(); i++) {

                JSONObject trailerInfo = movieArrayTrailer.getJSONObject(i);


                String name;
                String size;
                String source;
                String type;


                name = trailerInfo.getString(NAME);
                size = trailerInfo.getString(SIZE);
                source = trailerInfo.getString(SOURCE);
                type = trailerInfo.getString(TYPE);

                ContentValues movieValues = new ContentValues();

                movieValues.put(MovieContract.Trailers.NAME, name);
                movieValues.put(MovieContract.Trailers.SIZE, size);
                movieValues.put(MovieContract.Trailers.SOURCE, source);
                movieValues.put(MovieContract.Trailers.TYPE, type);
                movieValues.put(MovieContract.Trailers.MOVIE_ID, movie_id);
                cVVectorTrailer.add(movieValues);

            }
            int inserted = 0;
            if (cVVectorTrailer.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVectorTrailer.size()];
                cVVectorTrailer.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.Trailers.CONTENT_URI, cvArray);
            }
            //Log.d(LOG_TAG, "FetchTrailer Task Complete. " + inserted + " Inserted");


            String page = movieArrayReviews.getString(PAGE);
            String total_page = movieArrayReviews.getString(TOTAL_PAGES_REVIEWS);
            String total_results = movieArrayReviews.getString(TOTAL_RESULTS_REVIEWS);

            JSONArray reviews = movieArrayReviews.getJSONArray("results");

            Vector<ContentValues> cVVectorReviews = new Vector<ContentValues>(reviews.length());

            for (int j = 0; j < reviews.length(); j++) {

                JSONObject reviewsInfo = reviews.getJSONObject(j);

                String id_reviews;
                String author;
                String content;
                String url;


                id_reviews = reviewsInfo.getString(REVIEW_ID);
                author = reviewsInfo.getString(AUTHOR);
                content = reviewsInfo.getString(CONTENT);
                url = reviewsInfo.getString(URL);

                ContentValues movieValues = new ContentValues();

                // Then add the data, along with the corresponding name of the data type,
                // so the content provider knows what kind of value is being inserted.

                movieValues.put(MovieContract.Reviews.PAGE, page);
                movieValues.put(MovieContract.Reviews.TOTAL_PAGE, total_page);
                movieValues.put(MovieContract.Reviews.TOTAL_RESULTS, total_results);
                movieValues.put(MovieContract.Reviews.ID_REVIEWS, id_reviews);
                movieValues.put(MovieContract.Reviews.AUTHOR, author);
                movieValues.put(MovieContract.Reviews.CONTENT, content);
                movieValues.put(MovieContract.Reviews.URL, url);
                movieValues.put(MovieContract.Reviews.MOVIE_ID, movie_id);

                cVVectorReviews.add(movieValues);

            }
            inserted = 0;
            // add to database
            if (cVVectorReviews.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVectorReviews.size()];
                cVVectorReviews.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.Reviews.CONTENT_URI, cvArray);
            }
            //Log.d(LOG_TAG, "FetchReview Task Complete. " + inserted + " Inserted");



        } catch (JSONException e) {
            //Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        try {
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";
            final String DATA = "append_to_response";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIES_API_KEY)
                    .appendQueryParameter(DATA, "trailers,reviews")
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr);
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            //Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    //Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }
}