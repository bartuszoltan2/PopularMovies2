package com.zobartus.android.movies.utils;


import com.zobartus.android.movies.BuildConfig;

public class Constants {

    public final static int GRID_NUM_OF_COLUMNS = 2;
    public final static int TOAST_DURATION = 400;
    public final static String OUT_OF_RATING_STRING = " / 10";
    public static String FAVOURED_STRING = "Favoured!";
    public static String ADD_TO_FAVORITES_STRING = "Add to Favorites";
    public static String FULL_DATE_FORMAT_STRING = "MMMM dd, yyyy";
    public static String UNFORMATTED_DATE_STRING = "yyyy-MM-dd";

    public final static String NO_TRAILERS = "Sorry, no trailers here.";

    final static String API_KEY = BuildConfig.API_KEY;
    final static String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie";
    public final static String MOVIE_DB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";
    public final static String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";
    public final static String YOUTUBE_APP_BASE = "vnd.youtube:";

    final static String API_KEY_QUERY_PARAM = "api_key";
    public final static String VIDEO_QUERY_PARAM = "videos";
    public final static String REVIEW_URL_QUERY_PARAM = "reviews";
    public final static String VIDEO_TRAILER_KEY_PARAM = "key";
    public final static String MOVIE_ID_QUERY_PARAM = "id";
    public final static String POPULAR_QUERY_PARAM = "popular";
    public final static String TOP_RATED_QUERY_PARAM = "top_rated";
    public final static String RESULTS_QUERY_PARAM = "results";
    public final static String ORIGINAL_TITLE_QUERY_PARAM = "original_title";
    public final static String POSTER_PATH_QUERY_PARAM = "poster_path";
    public final static String OVERVIEW_QUERY_PARAM = "overview";
    public final static String VOTER_AVERAGE_QUERY_PARAM = "vote_average";
    public final static String RELEASE_DATE_QUERY_PARAM = "release_date";
    public final static String REVIEW_AUTHOR_QUERY_PARAM = "author";
    public final static String REVIEW_QUERY_PARAM = "content";
    public final static String REVIEW_URL_PARAM = "url";

    public final static String INTERNET_CHECK_COMMAND = "/system/bin/ping -c 1 8.8.8.8";
}
