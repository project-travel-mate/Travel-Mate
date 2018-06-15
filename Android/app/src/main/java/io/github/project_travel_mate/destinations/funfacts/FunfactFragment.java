package io.github.project_travel_mate.destinations.funfacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;

import static utils.Constants.EXTRA_MESSAGE_IMAGE;
import static utils.Constants.EXTRA_MESSAGE_TEXT;
import static utils.Constants.EXTRA_MESSAGE_TITLE;

/**
 * Created by swati on 25/1/16.
 * <p>
 * to display fun facts about a city
 */
public class FunfactFragment extends Fragment {

    private File mFile;

    /**
     * instantiate funfact fragment
     *
     * @param image Image of fun fact
     * @param text  Fun fact text
     * @param title Title
     * @return fragment object
     */
    public static FunfactFragment newInstance(String image, String text, String title) {
        FunfactFragment fragment = new FunfactFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE_IMAGE, image);
        bdl.putString(EXTRA_MESSAGE_TEXT, text);
        bdl.putString(EXTRA_MESSAGE_TITLE, title);
        fragment.setArguments(bdl);
        return fragment;
    }

    /**
     * Takes screenshot of current screen
     *
     * @param view to be taken screenshot of
     * @return bitmap of the screenshot
     */
    private static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String image    = Objects.requireNonNull(getArguments()).getString(EXTRA_MESSAGE_IMAGE);
        String text     = getArguments().getString(EXTRA_MESSAGE_TEXT);
        View v          = inflater.inflate(R.layout.funfact_fragment, container, false);
        TextView tv     = v.findViewById(R.id.tv);
        tv.setText(text);
        tv              = v.findViewById(R.id.head);
        tv.setText(getArguments().getString(EXTRA_MESSAGE_TITLE));
        ImageView iv    = v.findViewById(R.id.imag);
        Picasso.with(getContext()).load(image).error(R.drawable.delhi).placeholder(R.drawable.delhi).into(iv);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.fab) void onClick() {
        View rootView = Objects.requireNonNull(getActivity())
                .getWindow()
                .getDecorView()
                .findViewById(android.R.id.content);
        Bitmap b = getScreenShot(rootView);
        store(b, "myfile" + System.currentTimeMillis() + ".png");
        shareImage(mFile);
    }

    /**
     * Store bitmap mFile in MyScreenshots directory
     *
     * @param bitmap   bitmap to be saved
     * @param fileName Name of bitmap mFile
     */
    private void store(Bitmap bitmap, String fileName) {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyScreenshots";
        File dr = new File(dir);
        if (!dr.exists())
            dr.mkdirs();
        mFile = new File(dr, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(mFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To Share a mFile via mFile sharer
     *
     * @param file File location to be shared
     */
    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Screenshot"));
    }

}
