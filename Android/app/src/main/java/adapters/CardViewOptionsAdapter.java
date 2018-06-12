package adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import utils.CardItemEntity;

public class CardViewOptionsAdapter
        extends RecyclerView.Adapter<CardViewOptionsAdapter.UtilityOptionsViewHolder> {

    private OnItemClickListner mOnItemClickListner;
    private List<CardItemEntity> mUtilityOptionsEntityList;

    public CardViewOptionsAdapter(OnItemClickListner mOnItemClickListner,
                                  List<CardItemEntity> mUtilityOptionsEntityList) {
        this.mOnItemClickListner = mOnItemClickListner;
        this.mUtilityOptionsEntityList = mUtilityOptionsEntityList;
    }

    @NonNull
    @Override
    public UtilityOptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_card_view, parent, false);
        return new UtilityOptionsViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull UtilityOptionsViewHolder holder, int position) {
        holder.optionImage.setImageDrawable(mUtilityOptionsEntityList.get(position).getImage());
        holder.optionName.setText(mUtilityOptionsEntityList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mUtilityOptionsEntityList.size();
    }

    public class UtilityOptionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView optionImage;

        @BindView(R.id.text)
        TextView optionName;

        UtilityOptionsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListner.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListner {
        void onItemClick(int position);
    }
}
