package tie.hackathon.travelguide;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import utils.Constants;
import utils.Services;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareContact extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.create) Button create;
    @BindView(R.id.scan) Button scan;
    private static final int ACTIVITY_CREATE = 0, ACTIVITY_SCAN = 1;
    private SharedPreferences s;
    private SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_contact);

        ButterKnife.bind(this);

        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();

        create.setOnClickListener(this);
        scan.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ACTIVITY_SCAN == requestCode && null != data && data.getExtras() != null) {
            //Read result from QR Droid (it's stored in la.droid.qr.result)
            String result = data.getExtras().getString(Services.RESULT);
            //Just set result to EditText to be able to view it

            String x[] = result.split("---");
            String r = "My name is " + x[1] + ". My phone number : " + x[0];

            addContact(x[1], x[0]);

            TextView resultTxt = (TextView) findViewById(R.id.result);
            resultTxt.setText(r);
            resultTxt.setVisibility(View.VISIBLE);
        }
        if (ACTIVITY_CREATE == requestCode && null != data && data.getExtras() != null) {
            //Read result from QR Droid (it's stored in la.droid.qr.result)
            //Result is a string or a bitmap, according what was requested
            ImageView imgResult = (ImageView) findViewById(R.id.im);

            String qrCode = data.getExtras().getString(Services.RESULT);

            //If image path was not returned, it could not be saved. Check SD card is mounted and is writable
            if (null == qrCode || 0 == qrCode.trim().length()) {
                Toast.makeText(ShareContact.this, R.string.not_saved, Toast.LENGTH_LONG).show();
                return;
            }

            //Show success message
            Toast.makeText(ShareContact.this, getString(R.string.saved) + " " + qrCode, Toast.LENGTH_LONG).show();

            //Load QR code image from given path
            imgResult.setImageURI(Uri.parse(qrCode));

            imgResult.setVisibility(View.VISIBLE);
        }
    }

    private void addContact(String name, String phone) {
        ContentValues values = new ContentValues();
        values.put(Contacts.People.NUMBER, phone);
        values.put(Contacts.People.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        values.put(Contacts.People.LABEL, name);
        values.put(Contacts.People.NAME, name);
        Uri dataUri = getContentResolver().insert(Contacts.People.CONTENT_URI, values);
        Uri updateUri = Uri.withAppendedPath(dataUri, Contacts.People.Phones.CONTENT_DIRECTORY);
        values.clear();
        values.put(Contacts.People.Phones.TYPE, Contacts.People.TYPE_MOBILE);
        values.put(Contacts.People.NUMBER, phone);
        updateUri = getContentResolver().insert(updateUri, values);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        Intent qrDroid;
        switch (view.getId())
        {
            case R.id.scan :
                qrDroid = new Intent(Services.SCAN); //Set action "la.droid.qr.scan"

                //Check whether a complete or displayable result is needed
                qrDroid.putExtra(Services.COMPLETE, true);

                //Send intent and wait result
                try {
                    startActivityForResult(qrDroid, ACTIVITY_SCAN);
                } catch (ActivityNotFoundException activity) {
                    Toast.makeText(ShareContact.this, "can't be generated. Need to download QR services", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.create :
                //Create a new Intent to send to QR Droid
                qrDroid = new Intent(Services.ENCODE); //Set action "la.droid.qr.encode"

                String mPhoneNumber = s.getString(Constants.USER_NUMBER, "997112322");
                String name = s.getString(Constants.USER_NAME, "Swati Garg");

                qrDroid.putExtra(Services.CODE, mPhoneNumber + "---" + name);

                Log.e("here", "Hey, My contact number is :" + mPhoneNumber);

                //Check whether an URL or an imge is required
                //First item selected ("Get Bitmap")
                //Notify we want complete results (default is FALSE)
                qrDroid.putExtra(Services.IMAGE, true);
                //Optionally, set requested image size. 0 means "Fit Screen"
                qrDroid.putExtra(Services.SIZE, 0);


                //Send intent and wait result
                try {
                    startActivityForResult(qrDroid, ACTIVITY_CREATE);
                } catch (ActivityNotFoundException activity) {
                    Toast.makeText(ShareContact.this, "can't be generated. Need to download QR services", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
