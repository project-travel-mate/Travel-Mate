package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Util.Constants;
import flipviewpager.adapter.BaseFlipAdapter;
import flipviewpager.utils.FlipSettings;
import objects.City;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import views.FontTextView;

public class CityFragment extends Fragment {


    AutoCompleteTextView cityname;
    String nameyet;
    List<String> id = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    Activity activity;
    ProgressBar pb;
    String cityid;
    Typeface tex;
    ListView lv;
    private Handler mHandler;

    public CityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_citylist, container, false);

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);

        mHandler = new Handler(Looper.getMainLooper());
        cityname = (AutoCompleteTextView) v.findViewById(R.id.cityname);
        lv = (ListView) v.findViewById(R.id.music_list);
        pb = (ProgressBar) v.findViewById(R.id.pb);
        tex = Typeface.createFromAsset(activity.getAssets(), "fonts/texgyreadventor-bold.otf");
        cityname.setThreshold(1);
        cityname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameyet = cityname.getText().toString();
                if (!nameyet.contains(" ")) {
                    Log.e("name", nameyet + " ");
                    tripAutoComplete();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        getCity();

        return v;
    }


    public void tripAutoComplete() {

        // to fetch city names
        String uri = Constants.apilink +
                "city/autocomplete.php?search=" + nameyet.trim();
        Log.e("executing", uri + " ");


        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray arr;
                        final ArrayList list, list1;
                        try {
                            arr = new JSONArray(response.body().string());
                            Log.e("erro", arr + " ");

                            list = new ArrayList<String>();
                            list1 = new ArrayList<String>();
                            list2 = new ArrayList<String>();
                            for (int i = 0; i < arr.length(); i++) {
                                try {
                                    list.add(arr.getJSONObject(i).getString("name"));
                                    list1.add(arr.getJSONObject(i).getString("id"));
                                    list2.add(arr.getJSONObject(i).optString("image", "http://i.ndtvimg.com/i/2015-12/delhi-pollution-traffic-cars-afp_650x400_71451565121.jpg"));
                                    Log.e("adding", "aff");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("error ", " " + e.getMessage());
                                }
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (activity.getApplicationContext(), R.layout.spinner_layout, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cityname.setThreshold(1);
                            cityname.setAdapter(dataAdapter);
                            cityname.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    // TODO Auto-generated method stub
                                    Log.e("jkjb", "uihgiug" + arg2);

                                    cityid = list1.get(arg2).toString();
                                    Intent i = new Intent(activity, FinalCityInfo.class);
                                    i.putExtra("id_", cityid);
                                    i.putExtra("name_", list.get(arg2).toString());
                                    i.putExtra("image_", list2.get(arg2));
                                    startActivity(i);

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("erro", e.getMessage() + " ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }


    public void getCity() {

        // to fetch city names
        String uri = Constants.apilink +
                "all-cities.php";
        Log.e("executing", uri + " ");


        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(response.body().string());
                            JSONArray ar = ob.getJSONArray("cities");
                            pb.setVisibility(View.GONE);
                            FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();
                            List<City> friends = new ArrayList<>();
                            for (int i = 0; i < ar.length(); i++) {


                                double color = Math.random();
                                int c = (int) (color * 100) % 8;


                                int colo;
                                switch (c) {
                                    case 0:
                                        colo = R.color.sienna;
                                        break;
                                    case 1:
                                        colo = R.color.saffron;
                                        break;
                                    case 2:
                                        colo = R.color.green;
                                        break;
                                    case 3:
                                        colo = R.color.pink;
                                        break;
                                    case 4:
                                        colo = R.color.orange;
                                        break;
                                    case 5:
                                        colo = R.color.saffron;
                                        break;
                                    case 6:
                                        colo = R.color.purple;
                                        break;
                                    case 7:
                                        colo = R.color.blue;
                                        break;
                                    default:
                                        colo = R.color.blue;
                                        break;
                                }

                                friends.add(new City(

                                        ar.getJSONObject(i).getString("id"),
                                        ar.getJSONObject(i).optString("image", "yolo"),
                                        ar.getJSONObject(i).getString("name"),
                                        colo,
                                        ar.getJSONObject(i).getString("lat"),
                                        ar.getJSONObject(i).getString("lng"),

                                        "Know More", "View on Map", "Fun Facts", "View Website"));


                            }


                            lv.setAdapter(new CityAdapter(activity, friends, settings));
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    City f = (City) lv.getAdapter().getItem(position);
                                    Toast.makeText(activity, f.getNickname(), Toast.LENGTH_SHORT).show();


                                    Intent i = new Intent(activity, FinalCityInfo.class);
                                    i.putExtra("id_", f.getId());
                                    i.putExtra("name_", f.getNickname());
                                    i.putExtra("image_", f.getAvatar());
                                    startActivity(i);


                                }
                            });


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Log.e("heer", e1.getMessage() + " ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = (Activity) activity;
    }


    class CityAdapter extends BaseFlipAdapter<City> {

        private final int PAGES = 3;
        private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4};

        public CityAdapter(Context context, List<City> items, FlipSettings settings) {
            super(context, items, settings);
        }

        @Override
        public View getPage(int position, View convertView, ViewGroup parent, final City friend1, final City friend2) {
            final CitiesHolder holder;

            if (convertView == null) {
                holder = new CitiesHolder();
                convertView = activity.getLayoutInflater().inflate(R.layout.friends_merge_page, parent, false);
                holder.leftAvatar = (ImageView) convertView.findViewById(R.id.first);
                holder.rightAvatar = (ImageView) convertView.findViewById(R.id.second);
                holder.left = (TextView) convertView.findViewById(R.id.name1);
                holder.right = (TextView) convertView.findViewById(R.id.name2);
                holder.infoPage = activity.getLayoutInflater().inflate(R.layout.friends_info, parent, false);
                holder.nickName = (TextView) holder.infoPage.findViewById(R.id.nickname);
                holder.fv1 = (FontTextView) holder.infoPage.findViewById(R.id.interest_1);
                holder.fv2 = (FontTextView) holder.infoPage.findViewById(R.id.interest_2);
                holder.fv3 = (FontTextView) holder.infoPage.findViewById(R.id.interest_3);
                holder.fv4 = (FontTextView) holder.infoPage.findViewById(R.id.interest_4);

                for (int id : IDS_INTEREST)
                    holder.interests.add((TextView) holder.infoPage.findViewById(id));

                convertView.setTag(holder);
            } else {
                holder = (CitiesHolder) convertView.getTag();
            }

            switch (position) {
                case 1:
                    Picasso.with(getActivity()).load(friend1.getAvatar()).placeholder(R.drawable.delhi).into(holder.leftAvatar);
                    holder.left.setTypeface(tex);
                    holder.left.setText(friend1.getNickname());


                    if (friend2 != null) {

                        holder.right.setText(friend2.getNickname());
                        holder.right.setTypeface(tex);
                        Picasso.with(getActivity()).load(friend2.getAvatar()).placeholder(R.drawable.delhi).into(holder.rightAvatar);
                    }
                    break;
                default:
                    fillHolder(holder, position == 0 ? friend1 : friend2);
                    holder.infoPage.setTag(holder);
                    return holder.infoPage;
            }


            return convertView;
        }


        @Override
        public int getPagesCount() {
            return PAGES;
        }

        private void fillHolder(CitiesHolder holder, final City friend) {
            if (friend == null)
                return;
            Iterator<TextView> iViews = holder.interests.iterator();
            Iterator<String> iInterests = friend.getInterests().iterator();
            while (iViews.hasNext() && iInterests.hasNext())
                iViews.next().setText(iInterests.next());
            holder.infoPage.setBackgroundColor(getResources().getColor(friend.getBackground()));
            holder.nickName.setText(friend.getNickname());

            holder.nickName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("fsgb", "clikc");
                }
            });


            holder.fv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, FinalCityInfo.class);
                    i.putExtra("id_", friend.getId());
                    i.putExtra("name_", friend.getNickname());
                    i.putExtra("image_", friend.getAvatar());
                    startActivity(i);

                }
            });


            holder.fv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, FunFacts.class);
                    i.putExtra("id_", friend.getId());
                    i.putExtra("name_", friend.getNickname());
                    activity.startActivity(i);

                }
            });


            holder.fv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/?ie=UTF8&hq=&ll=" +
                            friend.getLa() +
                            "," +
                            friend.getLo() +
                            "&z=13"));
                    activity.startActivity(browserIntent);

                }
            });


            holder.fv4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                    activity.startActivity(browserIntent);

                }
            });


        }

        class CitiesHolder {
            ImageView leftAvatar;
            ImageView rightAvatar;
            View infoPage;
            TextView fv1, fv2, fv3, fv4;
            TextView left, right;
            List<TextView> interests = new ArrayList<>();
            TextView nickName;
        }
    }


}
