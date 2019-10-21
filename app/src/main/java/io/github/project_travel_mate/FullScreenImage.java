package io.github.project_travel_mate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.TouchImageView;

import static utils.Constants.EXTRA_MESSAGE_IMAGE_URI;
import static utils.Constants.EXTRA_MESSAGE_USER_FULLNAME;

public class FullScreenImage extends AppCompatActivity {

    @BindView(R.id.full_profile_image)
    TouchImageView fullProfileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_profile_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String imageUri = (String) intent.getSerializableExtra(EXTRA_MESSAGE_IMAGE_URI);
        String title = (String) intent.getSerializableExtra(EXTRA_MESSAGE_USER_FULLNAME);
        Picasso.with(this).load(imageUri).placeholder(R.drawable.default_user_icon)
                .error(R.drawable.default_user_icon)
                .into(fullProfileImage);

        setTitle(title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getStartIntent(Context context, String uri, String title) {
        Intent intent = new Intent(context, FullScreenImage.class);
        intent.putExtra(EXTRA_MESSAGE_IMAGE_URI, uri);
        intent.putExtra(EXTRA_MESSAGE_USER_FULLNAME, title);
        return intent;
    }
}
