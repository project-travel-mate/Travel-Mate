package adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by swati on 1/1/16.
 */
public class ImageAdapter extends PagerAdapter {
    Context context;
    private ArrayList<String> GalImages ;
    public ImageAdapter(Context context,ArrayList<String> x){
        GalImages = x;
        this.context=context;
    }
    @Override
    public int getCount() {
        if(GalImages == null)
            return 0;
        return GalImages.size();
    }





    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((TouchImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imageView = new TouchImageView(context);

        Picasso.with(context).load(GalImages.get(position)).into(imageView);

        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}