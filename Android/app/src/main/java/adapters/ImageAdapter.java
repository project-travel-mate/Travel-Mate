package adapters;

/**
 * Created by swati on 20/10/16.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import utils.TouchImageView;

public class ImageAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<String> galImages;

    /**
     * Initializes and adapter that adds particular image into imageView adapter
     *
     * @param context The context referring this class
     * @param images  Array list containing URLs of images
     */
    public ImageAdapter(Context context, ArrayList<String> images) {
        galImages = images;
        this.context = context;
    }

    /**
     * Returns count of number of images in array list
     *
     * @return number of images
     */
    @Override
    public int getCount() {
        if (galImages == null)
            return 0;
        return galImages.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /***
     * Display image at position in images Array in imageView
     *
     * @param container container view
     * @param position  position of item
     * @return object instantiated
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imageView = new TouchImageView(context);

        Picasso.with(context).load(galImages.get(position)).into(imageView);

        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}