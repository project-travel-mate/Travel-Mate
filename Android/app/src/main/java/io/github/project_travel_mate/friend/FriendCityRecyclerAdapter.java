package io.github.project_travel_mate.friend;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import io.github.project_travel_mate.R;
import objects.FriendCity;

public class FriendCityRecyclerAdapter extends RecyclerView.Adapter<FriendCityRecyclerAdapter.FriendCityViewHolder> {

    private ArrayList<FriendCity> mCityList;
    private Context mContext;

    public FriendCityRecyclerAdapter(Context context, ArrayList<FriendCity> list) {
        mCityList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public FriendCityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendCityViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.friend_city_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendCityViewHolder holder, int position) {
        holder.friendCityTextView.setText(mCityList.get(position).getCityName());
        holder.friendCityTextView.setBackgroundColor(getRandomColor());
    }

    private int getRandomColor() {
        int[] colors = new int[]{Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED, Color.YELLOW};
        Random random = new Random();
        return colors[random.nextInt(colors.length - 1)];
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    public class FriendCityViewHolder extends RecyclerView.ViewHolder {
        public TextView friendCityTextView;

        public FriendCityViewHolder(View itemView) {
            super(itemView);
            friendCityTextView = itemView.findViewById(R.id.city_name_text_view);
        }
    }

}
