package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.project_travel_mate.R;
import objects.CurrencyName;
import utils.CurrencyConverterGlobal;

public class CurrencyConverterAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<CurrencyName> mListCurencyNames;
    public Activity activity;

    private int mResId;

    public CurrencyConverterAdapter(Activity a, ArrayList<CurrencyName> list) {

        this.activity = a;
        this.mListCurencyNames = list;
        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("my list size", "" + getCount());
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mListCurencyNames.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mListCurencyNames.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final NewViewHolder mHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_curency_converter, null);
            mHolder = new NewViewHolder();
            mHolder.mShortTitle = (TextView) convertView.findViewById(R.id.title);
            mHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (NewViewHolder) convertView.getTag();
        }

        if (android.os.Build.VERSION.SDK_INT >= 5.0) {
            mHolder.mShortTitle.setText(mListCurencyNames.get(position).shortName);
        } else {
            mHolder.mShortTitle.setText(mListCurencyNames.get(position).abrivation);
        }

        if (android.os.Build.VERSION.SDK_INT >= 5.0) {
            mResId = activity.getResources().getIdentifier(mListCurencyNames.get(position).abrivation.toLowerCase(),
                    "drawable", activity.getPackageName());
        } else {
            mResId = activity.getResources().getIdentifier(mListCurencyNames.get(position).shortName.toLowerCase(),
                    "drawable", activity.getPackageName());
        }
        Log.d("Resouce resouce id", "" + mListCurencyNames.get(position).shortName);

        if (mResId == 0) {
            mResId = activity.getResources().getIdentifier("tryk", "drawable", activity.getPackageName());
            mHolder.mImageView.setImageResource(mResId);
        }
        mHolder.mImageView.setImageResource(mResId);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= 5.0) {
                    CurrencyConverterGlobal.global_image_id =
                            activity.getResources().getIdentifier(
                                    mListCurencyNames.get(position).abrivation.toLowerCase(),
                            "drawable", activity.getPackageName());
                    CurrencyConverterGlobal.global_country_name = mListCurencyNames.get(position).shortName;
                    CurrencyConverterGlobal.country_id = mListCurencyNames.get(position).abrivation;

                    if (CurrencyConverterGlobal.country_id.contains("TRY")) {
                        CurrencyConverterGlobal.global_image_id = activity.getResources().getIdentifier("tryk",
                                "drawable", activity.getPackageName());

                    }
                    if (CurrencyConverterGlobal.country_id.contains("TMM")) {
                        CurrencyConverterGlobal.global_image_id = activity.getResources().getIdentifier("tryk",
                                "drawable", activity.getPackageName());
                    }
                } else {
                    CurrencyConverterGlobal.global_image_id =
                            activity.getResources().getIdentifier(
                                    mListCurencyNames.get(position).shortName.toLowerCase(),
                            "drawable", activity.getPackageName());
                    CurrencyConverterGlobal.global_country_name = mListCurencyNames.get(position).abrivation;
                    CurrencyConverterGlobal.country_id = mListCurencyNames.get(position).shortName;
                    if (CurrencyConverterGlobal.country_id.contains("TRY")) {
                        CurrencyConverterGlobal.global_image_id = activity.getResources().getIdentifier("tryk",
                                "drawable", activity.getPackageName());
                    }
                    if (CurrencyConverterGlobal.country_id.contains("TMM")) {
                        CurrencyConverterGlobal.global_image_id = activity.getResources().getIdentifier("tryk",
                                "drawable", activity.getPackageName());
                    }
                }
                ((Activity) activity).finish();
            }
        });
        return convertView;
    }

    static class NewViewHolder {
        private TextView mShortTitle;
        private ImageView mImageView;
    }
}
