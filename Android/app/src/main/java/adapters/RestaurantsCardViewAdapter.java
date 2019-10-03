package adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    /**
     * Initializes new CardViewoptions adapter
     *
     * @param mOnItemClickListener the onclick listener for the cardview items
     * @param mOptionsEntityList   the list of entities to be populated in view
     */
    public RestaurantsCardViewAdapter(OnItemClickListener mOnItemClickListener,
                                  List<RestaurantItemEntity> mOptionsEntityList) {
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOptionsEntityList = mOptionsEntityList;
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
        Context context = holder.optionImage.getContext();

        RestaurantItemEntity currentItem = mOptionsEntityList.get(position);

        if (!TextUtils.isEmpty(currentItem.getImage())) {
            Picasso.with(context).load(currentItem
                    .getImage()).fit().centerCrop().into(holder.optionImage);
        }
        holder.optionName.setText(currentItem.getName());
        holder.optionAddress.setText(currentItem.getAddress());
        holder.optionRatings.setText(String.valueOf(currentItem.getRatings()));
        holder.optionAvgCost.setText(context.getString(R.string.currency) +
                String.valueOf(currentItem.getAvgCost())
                + context.getString(R.string.approx_cost));
        holder.optionVotes.setText(currentItem.getVotes() + " "
                + context.getString(R.string.votes));

        holder.content.setOnClickListener(view -> mOnItemClickListener.onItemClick(currentItem));
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
        void onItemClick(RestaurantItemEntity item);
    }

    /**
     * Viewholder for the cardView item
     */
    public class UtilityOptionsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_content)
        LinearLayout content;

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
        }
    }
}
