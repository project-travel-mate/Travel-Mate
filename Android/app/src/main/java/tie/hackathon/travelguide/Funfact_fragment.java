package tie.hackathon.travelguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by swati on 25/1/16.
 */
public class Funfact_fragment extends Fragment {
    File file;
    public static final String EXTRA_MESSAGE_IMAGE = "_image";
    public static final String EXTRA_MESSAGE_TEXT = "_text";
    public static final String EXTRA_MESSAGE_TITLE = "_title";
        public static final Funfact_fragment newInstance(String image,String text,String title)
        {
            Funfact_fragment f = new Funfact_fragment();
            Bundle bdl = new Bundle(1);
            bdl.putString(EXTRA_MESSAGE_IMAGE, image);
            bdl.putString(EXTRA_MESSAGE_TEXT, text);
            bdl.putString(EXTRA_MESSAGE_TITLE, title);
            f.setArguments(bdl);
            return f;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            String image = getArguments().getString(EXTRA_MESSAGE_IMAGE);
            String text = getArguments().getString(EXTRA_MESSAGE_TEXT);
            View v = inflater.inflate(R.layout.funfact_fragment, container, false);
            TextView tv = (TextView) v.findViewById(R.id.tv);
            tv.setText(text);

            tv = (TextView) v.findViewById(R.id.head);
            tv.setText(getArguments().getString(EXTRA_MESSAGE_TITLE));

            ImageView iv = (ImageView) v.findViewById(R.id.imag);
            Picasso.with(getContext()).load(image).error(R.drawable.delhi).placeholder(R.drawable.delhi).into(iv);

            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
                    Bitmap b = getScreenShot(rootView);
                    store(b, "myfile" + System.currentTimeMillis());
                    shareImage(file);
                }
            });



            //TextView messageTextView = (TextView)v.findViewById(R.id.textView);
            //messageTextView.setText(message);
            return v;
        }


    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }




    public  void store(Bitmap bm, String fileName){
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyScreenshots";
        File dr = new File(dir);
        if(!dr.exists())
            dr.mkdirs();
        file = new File(dr, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Screenshot"));
    }




}
