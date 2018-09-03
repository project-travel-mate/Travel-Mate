package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import butterknife.BindView;
import io.github.project_travel_mate.R;
import objects.CurrencyName;

public class ActivityConversionListview extends Activity {

    @BindView(R.id.listView)
    ListView listview;
    CurrencyConverterAdapter adaptr_listview;
    String temp = null;

    public static ArrayList<CurrencyName> currences_names;
    String s_ids_names;
    private Context mContext;

    StringTokenizer stok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_listview);

        currences_names = new ArrayList<CurrencyName>();
        listview = (ListView) findViewById(R.id.listView);

        mContext = this;
        addCurrencies();
    }

    public void addCurrencies() {

        try {
            InputStream is = mContext.getAssets().open("currencies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            s_ids_names = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        s_ids_names = s_ids_names.replace("{", "");
        s_ids_names = s_ids_names.replace("}", "");
        s_ids_names = s_ids_names.replace("\"", "");
        stok = new StringTokenizer(s_ids_names, ",");

        while (stok.hasMoreElements()) {
            temp = stok.nextElement().toString();

            if (temp.indexOf("currencySymbol") != -1) {
                temp = stok.nextElement().toString();
            }

            String[] split = temp.split(":");
            temp = stok.nextElement().toString();
            if (temp.indexOf("currencySymbol") != -1) {
                temp = stok.nextElement().toString();
            }
            String[] split2 = temp.split(":");
            temp = null;
            currences_names.add(new CurrencyName(split[2], split2[1]));
        }

        Collections.sort(currences_names, new Comparator<CurrencyName>() {
            @Override
            public int compare(CurrencyName n1, CurrencyName n2) {
                return n1.shortName.compareTo(n2.shortName);
            }
        });

        adaptr_listview = new CurrencyConverterAdapter(ActivityConversionListview.this, currences_names);
        listview.setAdapter(adaptr_listview);
    }
}

