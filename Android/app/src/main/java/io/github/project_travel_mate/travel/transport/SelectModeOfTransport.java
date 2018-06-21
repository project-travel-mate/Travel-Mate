package io.github.project_travel_mate.travel.transport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapters.CardViewOptionsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import utils.CardItemEntity;

public class SelectModeOfTransport extends AppCompatActivity implements CardViewOptionsAdapter.OnItemClickListener {

    @BindView(R.id.transport_mode_options_recycle_view)
    RecyclerView mTransportModeOptionsRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode_of_transport);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        List<CardItemEntity> cardEntities = new ArrayList<>();
        cardEntities.add(
                new CardItemEntity(
                        getResources().getDrawable(R.drawable.train),
                        getResources().getString(R.string.text_train)));
        cardEntities.add(
                new CardItemEntity(
                        getResources().getDrawable(R.drawable.car),
                        getResources().getString(R.string.text_cart)));

        CardViewOptionsAdapter cardViewOptionsAdapter = new CardViewOptionsAdapter(this, cardEntities);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mTransportModeOptionsRecycleView.setLayoutManager(mLayoutManager);
        mTransportModeOptionsRecycleView.setItemAnimator(new DefaultItemAnimator());
        mTransportModeOptionsRecycleView.setAdapter(cardViewOptionsAdapter);


        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Select Mode of Transport");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Intent i;
        switch (position) {
            case 0: i = new Intent(SelectModeOfTransport.this, TrainList.class);
                startActivity(i);
                break;
            case 1: i = new Intent(SelectModeOfTransport.this, CarDirections.class);
                startActivity(i);
                break;
        }

    }
}
