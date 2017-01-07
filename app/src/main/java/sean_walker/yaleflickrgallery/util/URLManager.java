package sean_walker.yaleflickrgallery.util;

import android.net.Uri;

import sean_walker.yaleflickrgallery.fragment.GalleryFragment;

/**
 * Created by seanwalker on 12/26/16.
 */

public class URLManager {

    // constants
    private static final String API_KEY = "Insert API key here";
    private static final String API_ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String REQUEST_METHOD = "flickr.people.getPublicPhotos";
    private static final String YALE_USER_ID = "12208415@N08";

    private static volatile URLManager instance = null;
    private URLManager() {
    }

    public static URLManager getInstance() {
        if (instance == null) {
            synchronized (URLManager.class) {
                if (instance == null) {
                    instance = new URLManager();
                }
            }
        }
        return instance;
    }

    public static String getItemURL(int page) {
        String url;
        // build a request string to fit Flickr API REST format, as per https://www.flickr.com/services/api/explore/flickr.people.getPublicPhotos
        url = Uri.parse(API_ENDPOINT).buildUpon()
                .appendQueryParameter("per_page", String.valueOf(GalleryFragment.PER_PAGE))
                .appendQueryParameter("method", REQUEST_METHOD)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("user_id", YALE_USER_ID)
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
        return url;
    }
}
