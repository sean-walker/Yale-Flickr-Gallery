package sean_walker.yaleflickrgallery.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sean_walker.yaleflickrgallery.R;
import sean_walker.yaleflickrgallery.activity.PhotoActivity;

/**
 * Created by seanwalker on 12/26/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context mContext;
    private List<Photo> photoList;

    public GalleryAdapter(Context mContext, List<Photo> photoList) {
        this.mContext = mContext;
        this.photoList = photoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.photo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Photo item = photoList.get(position);
        // zoom in on individual photos when they are clicked
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoActivity.class);
                intent.putExtra("item", item);
                mContext.startActivity(intent);
            }
        });
        if (item.getURL() != null) {
            Log.d("BIND", item.getURL());
            Picasso.with(mContext)
                    .load(item.getURL())
                    .fit()
                    .centerInside()
                    .into(holder.mImageView);
        } else {
            Log.d("UNBIND", item.getURL());
            holder.mImageView.setImageDrawable(null);
            holder.mImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    /*
    * add the list of photos received from the JSON request to the photo list
    * */
    public void addAll(List<Photo> newList) {
        photoList.addAll(newList);
    }
}
