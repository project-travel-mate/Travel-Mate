package tie.hackathon.travelguide;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CaptureImage extends AppCompatActivity {


    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;


    Uri imageUri                      = null;
    static TextView imageDetails      = null;
    public  static ImageView showImg  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);
    }
}
