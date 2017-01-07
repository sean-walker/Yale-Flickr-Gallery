package sean_walker.yaleflickrgallery.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import sean_walker.yaleflickrgallery.R;
import sean_walker.yaleflickrgallery.util.Photo;

/**
 * Created by seanwalker on 12/26/16.
 */

public class PhotoFragment extends Fragment {

    private Photo mItem;
    private ImageView mPhoto;

    public PhotoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.photo_fragment, container, false);
        mItem = (Photo) getActivity().getIntent().getSerializableExtra("item");
        mPhoto = (ImageView) view.findViewById(R.id.photo);
        Picasso.with(getContext())
                .load(mItem.getURL())
                .into(mPhoto);
        return view;
    }
}
