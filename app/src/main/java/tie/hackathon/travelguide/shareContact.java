package tie.hackathon.travelguide;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Util.Services;

public class shareContact extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0, ACTIVITY_SCAN = 1;
    private boolean image = false;
    Button create, scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_contact);

        create = (Button) findViewById(R.id.create);
        scan = (Button) findViewById(R.id.scan);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a new Intent to send to QR Droid
                Intent qrDroid = new Intent(Services.ENCODE); //Set action "la.droid.qr.encode"
                TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                //Set text to encode
                if(mPhoneNumber == null)
                    mPhoneNumber="9971604989";
                qrDroid.putExtra(Services.CODE, "Hey, My contact number is :" + mPhoneNumber);

                Log.e("here", "Hey, My contact number is :" + mPhoneNumber);

                //Check whether an URL or an imge is required
                //First item selected ("Get Bitmap")
                //Notify we want complete results (default is FALSE)
                image = true;
                qrDroid.putExtra(Services.IMAGE, true);
                //Optionally, set requested image size. 0 means "Fit Screen"
                qrDroid.putExtra(Services.SIZE, 0);


                //Send intent and wait result
                try {
                    startActivityForResult(qrDroid, ACTIVITY_CREATE);
                } catch (ActivityNotFoundException activity) {
                    Toast.makeText(shareContact.this, "can't be generated. Need to download QR services", Toast.LENGTH_SHORT).show();
                }
            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qrDroid = new Intent(Services.SCAN); //Set action "la.droid.qr.scan"

                //Check whether a complete or displayable result is needed
                qrDroid.putExtra(Services.COMPLETE, true);


                //Send intent and wait result
                try {
                    startActivityForResult(qrDroid, ACTIVITY_SCAN);
                } catch (ActivityNotFoundException activity) {
                    Toast.makeText(shareContact.this, "can't be generated. Need to download QR services", Toast.LENGTH_SHORT).show();
                }

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ACTIVITY_SCAN == requestCode && null != data && data.getExtras() != null) {
            //Read result from QR Droid (it's stored in la.droid.qr.result)
            String result = data.getExtras().getString(Services.RESULT);
            //Just set result to EditText to be able to view it
            TextView resultTxt = (TextView) findViewById(R.id.result);
            resultTxt.setText(result);
            resultTxt.setVisibility(View.VISIBLE);
        }
        if (ACTIVITY_CREATE == requestCode && null != data && data.getExtras() != null) {
            //Read result from QR Droid (it's stored in la.droid.qr.result)
            //Result is a string or a bitmap, according what was requested
            ImageView imgResult = (ImageView) findViewById(R.id.im);

            String qrCode = data.getExtras().getString(Services.RESULT);

            //If image path was not returned, it could not be saved. Check SD card is mounted and is writable
            if (null == qrCode || 0 == qrCode.trim().length()) {
                Toast.makeText(shareContact.this, R.string.not_saved, Toast.LENGTH_LONG).show();
                return;
            }

            //Show success message
            Toast.makeText(shareContact.this, getString(R.string.saved) + " " + qrCode, Toast.LENGTH_LONG).show();

            //Load QR code image from given path
            imgResult.setImageURI(Uri.parse(qrCode));

            imgResult.setVisibility(View.VISIBLE);

            //TODO: After using this QR code, you should move it to a permanent location, or delete it

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
