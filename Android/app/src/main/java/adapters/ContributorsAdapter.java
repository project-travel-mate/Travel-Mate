package adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Contributor;
import utils.CircleImageView;

/**
 * @author amoraitis
 */
public class ContributorsAdapter extends BaseAdapter {
    private final Context mContext;
    private List<Contributor> mContributors;

    public ContributorsAdapter(Context context, List<Contributor> contributors) {
        mContext = context;
        mContributors = contributors;
    }

    @Override
    public int getCount() {
        return mContributors.size();
    }

    @Override
    public Object getItem(int i) {
        return mContributors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Contributor contributor = mContributors.get(position);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.contributors_gridview_item, null);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);

        String contributorAvatarUrl = contributor.getAvatarUrl();
        Picasso.with(mContext).load(contributorAvatarUrl).centerCrop()
                .resize(250, 250)
                .into(viewHolder.contributor_avatarImageView);
        final String unameFormatted = "@" + contributor.getUsername();
        viewHolder.contributor_unameTextView.setText(unameFormatted);
        viewHolder.contributor_contributionsTextView.setText(String.valueOf(contributor.getContributions()));
        return convertView;
    }

    /**
     * Replaces dataset with the argument's dataset
     * ans notifies the UI about those changes
     *
     * @param newContributors - new data to be set to list
     */
    public void update(ArrayList<Contributor> newContributors) {
        mContributors.clear();
        this.mContributors = newContributors;
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.contributor_image)
        CircleImageView contributor_avatarImageView;

        @BindView(R.id.contributor_name)
        TextView contributor_unameTextView;

        @BindView(R.id.contributor_contributions)
        AppCompatTextView contributor_contributionsTextView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
