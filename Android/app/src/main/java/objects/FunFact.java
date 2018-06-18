package objects;

import java.io.Serializable;

public class FunFact implements Serializable {

    private String mTitle;
    private String mImage;
    private String mText;

    /***
     * Initialize funfact object
     * @param title fact title
     * @param image fact image
     * @param text  face text
     */
    public FunFact(String title, String image, String text) {
        this.mTitle = title;
        this.mImage = image;
        this.mText = text;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }
}
