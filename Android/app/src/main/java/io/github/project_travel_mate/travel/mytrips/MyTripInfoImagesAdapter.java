package io.github.project_travel_mate.travel.mytrips;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.gun0912.tedpicker.ImagePickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.project_travel_mate.R;
import utils.Constants;

class MyTripInfoImagesAdapter extends ArrayAdapter<File> {
    private final Activity mContext;
    private final List<File> mName;

    MyTripInfoImagesAdapter(Activity context, List<File> name) {
        super(context, R.layout.trip_listitem, name);
        this.mContext = context;
        this.mName = name;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = Objects.requireNonNull(mInflater).inflate(R.layout.image_listitem, parent, false);
            holder = new ViewHolder();
            holder.iv = view.findViewById(R.id.iv);

            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        if (position == mName.size() - 1) {
            holder.iv.setImageResource(R.drawable.add_image);
            holder.iv.setOnClickListener(v -> {
                Intent intent1 = new Intent(mContext, ImagePickerActivity.class);
                mContext.startActivityForResult(intent1, MyTripInfoActivity.INTENT_REQUEST_GET_IMAGES);
            });
        } else {
            holder.iv.setImageDrawable(Drawable.createFromPath(mName.get(position).getAbsolutePath()));
            holder.iv.setOnClickListener(v -> {
                Intent i = TripImageActivity.getStartIntent(mContext);
                ArrayList<String> a = new ArrayList<>();
                a.add(mName.get(position).getAbsolutePath());

                i.putExtra(Constants.EVENT_IMG, a);
                i.putExtra(Constants.EVENT_NAME, "Image");
                mContext.startActivity(i);
            });
        }
        return view;
    }

    private class ViewHolder {
        ImageView iv;
    }
}
