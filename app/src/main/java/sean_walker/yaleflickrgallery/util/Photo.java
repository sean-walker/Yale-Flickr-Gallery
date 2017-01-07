package sean_walker.yaleflickrgallery.util;

import java.io.Serializable;

/**
 * Created by seanwalker on 12/26/16.
 */

public class Photo implements Serializable {

    private String id;
    private String secret;
    private String server;
    private String farm;

    public Photo(String id, String secret, String server, String farm) {
        this.id = id;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
    }

    public String getId() {
        return id;
    }

    public String getURL() {
        // returns Flickr URL for a given photo's metadata
        return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }
}
