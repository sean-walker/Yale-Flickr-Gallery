package sean_walker.yaleflickrgallery.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sean_walker.yaleflickrgallery.R;
import sean_walker.yaleflickrgallery.util.GalleryAdapter;
import sean_walker.yaleflickrgallery.util.Photo;
import sean_walker.yaleflickrgallery.util.URLManager;

/**
 * Created by seanwalker on 12/25/16.
 */

public class GalleryFragment extends Fragment {

    public static final int COLUMN_NUMBER = 3;
    public static final int PER_PAGE = 20;
    public static final String REQ_TAG = "imgRequest";


    private RequestQueue mRq;
    private GridLayoutManager mLayoutManager;
    private GalleryAdapter mAdapter;
    private ProgressDialog progressDialog;

    private boolean mLoading = false;
    private boolean mHasMore = true;

    public GalleryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // load the gallery fragment view
        View view = inflater.inflate(R.layout.gallery_fragment, container);
        // instantiate a new volley request queue for the gallery activity
        mRq = Volley.newRequestQueue(getActivity());
        // get reference to our recyclerview
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // set a layoutmanager for the recyclerview
        mLayoutManager = new GridLayoutManager(getActivity(), COLUMN_NUMBER);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // set an adapter for the recyclerview
        mAdapter = new GalleryAdapter(getActivity(), new ArrayList<Photo>());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItem = mLayoutManager.getItemCount();
                int lastItemPos = mLayoutManager.findLastVisibleItemPosition();
                // at the bottom of page, if there are more photos, load another 10
                if (mHasMore && !mLoading && totalItem - 1 == lastItemPos) {
                    startLoading();
                }
            }
        });

        startLoading();
        return view;
    }

    private void startLoading() {

        mLoading = true;

        int totalItem = mLayoutManager.getItemCount();
        // refers to the page (abstract variable that flickr keeps: some amount of items per page)
        final int page = totalItem / PER_PAGE + 1;

        String url = URLManager.getInstance().getItemURL(page);

        progressDialog.setMessage("Loading images, please wait...");
        progressDialog.show();

        // get the JSON object returned by Flickr's API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Photo> result = new ArrayList<>();
                        progressDialog.hide();
                        try {
                            JSONObject photos = response.getJSONObject("photos");
                            // if there are no more pages than the one we are currently accessing, don't load any more
                            if (!(photos.getInt("pages") > page)) {
                                mHasMore = false;
                            }
                            JSONArray photoArr = photos.getJSONArray("photo");
                            // iterate through the JSON result, adding each photo to our own list of photos
                            for (int i = 0; i < photoArr.length(); i++) {
                                JSONObject itemObj = photoArr.getJSONObject(i);
                                Photo item = new Photo(
                                        itemObj.getString("id"),
                                        itemObj.getString("secret"),
                                        itemObj.getString("server"),
                                        itemObj.getString("farm")
                                );
                                result.add(item);
                            }
                        } catch (JSONException e) {
                            Log.e("JSON REQ", "JSON parsing error: " + e.getMessage());
                        }
                        mAdapter.addAll(result);
                        mAdapter.notifyDataSetChanged();
                        mLoading = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY_ERROR", error.toString());
                    }
                }
        );
        request.setTag(REQ_TAG);
        mRq.add(request);
    }

    private void stopLoading() {
        if (mRq != null) {
            mRq.cancelAll(REQ_TAG);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLoading();
    }
}
