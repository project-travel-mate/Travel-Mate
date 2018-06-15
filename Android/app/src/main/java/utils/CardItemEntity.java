package utils;

import android.graphics.drawable.Drawable;

public class CardItemEntity {
    private Drawable mImage;
    private String mName;

    public CardItemEntity(Drawable image, String name) {
        this.mImage = image;
        this.mName = name;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable image) {
        this.mImage = image;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
