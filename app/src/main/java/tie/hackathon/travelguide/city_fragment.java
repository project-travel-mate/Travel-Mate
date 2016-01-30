package tie.hackathon.travelguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Util.Utils;
import adapters.Cities_adapter;
import objects.Friend;
import views.FontTextView;

import flipviewpager.adapter.BaseFlipAdapter;
import flipviewpager.utils.FlipSettings;

public class city_fragment extends Fragment {


    List<String> id = new ArrayList<>();
    List<String> names = new ArrayList<>();
    static Activity activity;
    ProgressBar pb;

    public city_fragment() {
        // Required empty public constructor
    }
    ListView lv;
    LinearLayout vehicle, acc, shop, city, check;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.content_citylist, container, false);

        lv = (ListView) v.findViewById(R.id.music_list);
        pb = (ProgressBar) v.findViewById(R.id.pb);

        try {
            new getcitytask().execute();

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
            alertDialog.setTitle("Can't connect.");
            alertDialog.setMessage("We cannot connect to the internet right now. Please try again later. Exception e: " + e.toString());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            Log.e("YouTube:", "Cannot fetch " + e.toString());
        }




        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
    public class getcitytask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            //this is where you should write your authentication code
            // or call external service
            // following try-catch just simulates network access


            try {
                Log.e("fvdbd","yoloooo");
                String uri = "http://csinsit.org/prabhakar/tie/all-cities.php";
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());
                Log.e("here",readStream+" ");
                return readStream;

//                return readStream;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }




        }

        @Override
        protected void onPostExecute(String result) {


            if(result == null){
                Toast.makeText(activity, "No result", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject ob  = new JSONObject(result);
                JSONArray ar = ob.getJSONArray("cities");
                pb.setVisibility(View.GONE);
                FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();



        //        lv.setAdapter(new Cities_adapter(activity,ar));

                List<Friend> friends = new ArrayList<>();
                for(int i=0;i<ar.length();i++){


                    double color = Math.random();
                    int c = (int)(color*100)%8;
                    int d = (int)(color*100)%2;


                    int draw;
                    if(d==1)
                        draw = R.drawable.delhi;
                    else
                    draw = R.drawable.goa;

                    int colo;
                    switch (c){
                        case 0 : colo = R.color.sienna;break;
                        case 1 : colo = R.color.saffron;break;
                        case 2 : colo = R.color.green;break;
                        case 3 : colo = R.color.pink;break;
                        case 4 : colo = R.color.orange;break;
                        case 5 : colo = R.color.saffron;break;
                        case 6 : colo = R.color.purple;break;
                        case 7 : colo = R.color.blue;break;
                        default:  colo = R.color.blue;break;
                    }

                    String dr = ar.getJSONObject(i).optString("image", "yolo");

                    friends.add(new Friend(

                            ar.getJSONObject(i).getString("id"),
                            dr,
                            ar.getJSONObject(i).getString("name"), colo,
                            ar.getJSONObject(i).getString("lat"),
                            ar.getJSONObject(i).getString("lng"),

                            "Know More", "View on Map", "Fun Facts", "View Website"));

                }


                lv.setAdapter(new FriendsAdapter(activity, friends, settings));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Friend f = (Friend) lv.getAdapter().getItem(position);

                        Toast.makeText(activity, f.getNickname(), Toast.LENGTH_SHORT).show();
                    }
                });


            } catch (JSONException e1) {
                e1.printStackTrace();
                Log.e("heer",e1.getMessage()+" ");
            }
        }

    }


    class FriendsAdapter extends BaseFlipAdapter<Friend> {

        private final int PAGES = 3;
        private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4};

        public FriendsAdapter(Context context, List<Friend> items, FlipSettings settings) {
            super(context, items, settings);
        }

        @Override
        public View getPage(int position, View convertView, ViewGroup parent, final Friend friend1, final Friend friend2) {
            final FriendsHolder holder;

            if (convertView == null) {
                holder = new FriendsHolder();
                convertView = activity.getLayoutInflater().inflate(R.layout.friends_merge_page, parent, false);
                holder.leftAvatar = (ImageView) convertView.findViewById(R.id.first);
                holder.rightAvatar = (ImageView) convertView.findViewById(R.id.second);
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
                holder = (FriendsHolder) convertView.getTag();
            }

            switch (position) {
                // Merged page with 2 friends
                case 1:
                    Picasso.with(getActivity()).load(friend1.getAvatar()).placeholder(R.drawable.delhi).into(holder.leftAvatar);

                    if (friend2 != null) {


                        Picasso.with(getActivity()).load(friend2.getAvatar()).placeholder(R.drawable.goa).into(holder.rightAvatar);
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

        private void fillHolder(FriendsHolder holder, final Friend friend) {
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
                    Intent i = new Intent(activity, CityInfo.class);
                    i.putExtra("id_", friend.getId());
                    i.putExtra("name_", friend.getNickname());
                    activity.startActivity(i);

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

        class FriendsHolder {
            ImageView leftAvatar;
            ImageView rightAvatar;
            View infoPage;
            TextView fv1,fv2,fv3,fv4;

            List<TextView> interests = new ArrayList<>();
            TextView nickName;
        }
    }


}
