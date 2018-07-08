package io.github.project_travel_mate.destinations.funfacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.FunFact;

import static utils.Constants.EXTRA_MESSAGE_FUNFACT_OBJECT;

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
     * @param fact FunFact object
     * @return fragment object
     */
    public static FunfactFragment newInstance(FunFact fact) {
        FunfactFragment fragment = new FunfactFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(EXTRA_MESSAGE_FUNFACT_OBJECT, fact);
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

        FunFact fact = (FunFact) getArguments().getSerializable(EXTRA_MESSAGE_FUNFACT_OBJECT);
        View view = inflater.inflate(R.layout.fragment_funfact, container, false);
        ViewHolder holder = new ViewHolder(view);
        if (fact != null) {
            (holder.desc).setText(fact.getText());
            (holder.title).setText(fact.getTitle());
            if (fact.getSource() != null && !fact.getSource().equals("null")) {
                (holder.text_source).setVisibility(View.VISIBLE);
                (holder.text_source).setOnClickListener(view1 -> openURL(fact.getSourceURL()));
                (holder.source).setText(fact.getSource());
                (holder.source).setOnClickListener(view1 -> openURL(fact.getSourceURL()));
                (holder.share).setOnClickListener(view1 -> shareClicked());
            }
            Picasso.with(getContext()).load(fact.getImage()).error(R.drawable.placeholder_image)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.image);
        }
        return view;
    }

    /**
     * To Share a mFile via mFile sharer
     *
     * @param file File location to be shared
     */
    private void shareImage(File file) {
        Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),
                "io.github.project_travel_mate.shareFile", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_fun_fact));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Screenshot"));
    }

    /**
     * To open a url in browser
     *
     * @param url URL to be opened
     */

    private void openURL(String url) {
        Uri sourceURI = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, sourceURI);
        if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content),
                    R.string.no_activity_for_browser,
                    Snackbar.LENGTH_LONG).show();
        }
    }

    void shareClicked() {
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

    class ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.imag)
        ImageView image;
        @BindView(R.id.source)
        TextView source;
        @BindView(R.id.text_source)
        TextView text_source;
        @BindView(R.id.fab)
        FloatingActionButton share;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
