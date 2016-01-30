package tie.hackathon.travelguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.CheckList_adapter;
import database.DBhelp_new;
import database.TableEntry_new;


public class Emergency_fragment extends Fragment implements View.OnClickListener {


    Button police,fire,ambulance, blood_bank,bomb,railways;

    Activity activity;

    public Emergency_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_emergency, container, false);

        police = (Button)  v.findViewById(R.id.police);
        fire = (Button)  v.findViewById(R.id.fire);
        ambulance = (Button)  v.findViewById(R.id.ambulance);
        blood_bank = (Button)  v.findViewById(R.id.blood_bank);
        bomb = (Button)  v.findViewById(R.id.bomb);
        railways = (Button)  v.findViewById(R.id.railways);

        police.setOnClickListener(this);
        fire.setOnClickListener(this);
        ambulance.setOnClickListener(this);
        blood_bank.setOnClickListener(this);
        bomb.setOnClickListener(this);
        railways.setOnClickListener(this);


        return v;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        switch (v.getId()){
            case R.id.police:
                intent.setData(Uri.parse("tel:100"));
                break;
            case R.id.fire:
                intent.setData(Uri.parse("tel:101"));
                break;
            case R.id.ambulance:
                intent.setData(Uri.parse("tel:102"));
                break;
            case R.id.blood_bank:
                intent.setData(Uri.parse("tel:25752924"));
                break;
            case R.id.bomb:
                intent.setData(Uri.parse("tel:22512201"));
                break;
            case R.id.railways:
                intent.setData(Uri.parse("tel:23366177"));
        }
        startActivity(intent);
    }
}
