package tie.hackathon.travelguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import Util.Constants;

public class FinalCityInfo extends AppCompatActivity implements View.OnClickListener {

    Intent i;
    String id, tit, image;
    ImageView iv;
    TextView title;
    ExpandableTextView des;

    LinearLayout funfact, restau, hangout, monum, shopp, trend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_city_info);


        des = (ExpandableTextView) findViewById(R.id.expand_text_view);
        des.setText(getString(R.string.sample_string));
        iv = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.head);

        i = getIntent();
        tit = i.getStringExtra("name_");
        setTitle(tit);
        id = i.getStringExtra("id_");
        image = i.getStringExtra("image_");

        title.setText(tit);

        funfact = (LinearLayout) findViewById(R.id.funfact);
        restau = (LinearLayout) findViewById(R.id.restau);
        hangout = (LinearLayout) findViewById(R.id.hangout);
        monum = (LinearLayout) findViewById(R.id.monu);
        shopp = (LinearLayout) findViewById(R.id.shoppp);
        trend = (LinearLayout) findViewById(R.id.trends);


        funfact.setOnClickListener(this);
        restau.setOnClickListener(this);
        hangout.setOnClickListener(this);
        monum.setOnClickListener(this);
        shopp.setOnClickListener(this);
        trend.setOnClickListener(this);


        Picasso.with(this).load(image).into(iv);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {

            case R.id.funfact:
                i = new Intent(FinalCityInfo.this, FunFacts.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                startActivity(i);
                break;


            case R.id.restau:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("type_", "restaurant");
                startActivity(i);
                break;
            case R.id.hang:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("type_", "hangout");
                startActivity(i);
                break;
            case R.id.monu:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("type_", "monument");
                startActivity(i);
                break;
            case R.id.shoppp:

                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("type_", "shopping");
                startActivity(i);
                break;


        }

    }
}
