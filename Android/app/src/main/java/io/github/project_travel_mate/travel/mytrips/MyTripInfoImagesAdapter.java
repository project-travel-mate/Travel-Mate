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
    private final Activity context;
    private final List<File> name;

    MyTripInfoImagesAdapter(Activity context, List<File> name) {
        super(context, R.layout.trip_listitem, name);
        this.context = context;
        this.name = name;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = Objects.requireNonNull(mInflater).inflate(R.layout.image_listitem, parent, false);
            holder = new ViewHolder();
            holder.iv = view.findViewById(R.id.iv);

            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        if (position == name.size() - 1) {
            holder.iv.setImageResource(R.drawable.add_image);
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(context, ImagePickerActivity.class);
                    context.startActivityForResult(intent1, MyTripInfo.INTENT_REQUEST_GET_IMAGES);
                }
            });
        } else {
            holder.iv.setImageDrawable(Drawable.createFromPath(name.get(position).getAbsolutePath()));
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, TripImage.class);
                    ArrayList<String> a = new ArrayList<>();
                    a.add(name.get(position).getAbsolutePath());

                    i.putExtra(Constants.EVENT_IMG, a);
                    i.putExtra(Constants.EVENT_NAME, "Image");
                    context.startActivity(i);
                }
            });
        }
        return view;
    }

    private class ViewHolder {
        ImageView iv;
    }
}
