package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.project_travel_mate.R;

public class CityImageSliderAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mImagesArray;
    CityImageSliderAdapter(Context context, ArrayList<String> imagesArray) {
        this.mContext = context;
        this.mImagesArray = imagesArray;
    }
    @Override
    public int getCount() {
        return mImagesArray.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        //display default city image if no image is present in array
        if (mImagesArray.size() == 0) {
            Picasso.with(mContext).load(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image).fit().centerCrop().into(imageView);
        }
        Picasso.with(mContext).load(mImagesArray.get(position))
                .error(R.drawable.placeholder_image).fit().centerCrop().into(imageView);
        container.addView(imageView);
        return imageView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
