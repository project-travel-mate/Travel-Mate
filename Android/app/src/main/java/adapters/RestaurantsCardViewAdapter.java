package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import utils.RestaurantItemEntity;

public class RestaurantsCardViewAdapter
        extends RecyclerView.Adapter<RestaurantsCardViewAdapter.UtilityOptionsViewHolder> {

    private final OnItemClickListener mOnItemClickListener;
    private final List<RestaurantItemEntity> mOptionsEntityList;
    Context mContext;
    /**
     * Initializes new CardViewoptions adapter
     *
     * @param mOnItemClickListener the onclick listener for the cardview items
     * @param mOptionsEntityList   the list of entities to be populated in view
     */
    public RestaurantsCardViewAdapter(OnItemClickListener mOnItemClickListener,
                                  List<RestaurantItemEntity> mOptionsEntityList, Context mContext) {
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOptionsEntityList = mOptionsEntityList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UtilityOptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurants_card_view, parent, false);
        return new UtilityOptionsViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull UtilityOptionsViewHolder holder, int position) {
        Picasso.with(holder.optionImage.getContext()).load(mOptionsEntityList.get(position)
                .getImage()).fit().centerCrop().into(holder.optionImage);
        holder.optionName.setText(mOptionsEntityList.get(position).getName());
        holder.optionAddress.setText(mOptionsEntityList.get(position).getAddress());
        holder.optionRatings.setText(mOptionsEntityList.get(position).getRatings());
        holder.optionAvgCost.setText(mContext.getString(R.string.currency) +
                String.valueOf(mOptionsEntityList.get(position).getAvgCost())
                + mContext.getString(R.string.approx_cost));
        holder.optionVotes.setText(mOptionsEntityList.get(position).getVotes() + " "
                + mContext.getString(R.string.votes));
    }

    /**
     * Get items count in the adapter
     *
     * @return item count
     */
    @Override
    public int getItemCount() {
        return mOptionsEntityList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * Viewholder for the cardView item
     */
    public class UtilityOptionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView optionImage;

        @BindView(R.id.restaurant_name)
        TextView optionName;

        @BindView(R.id.restaurant_address)
        TextView optionAddress;

        @BindView(R.id.ratings)
        TextView optionRatings;

        @BindView(R.id.avg_cost)
        TextView optionAvgCost;

        @BindView(R.id.votes)
        TextView optionVotes;

        UtilityOptionsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
