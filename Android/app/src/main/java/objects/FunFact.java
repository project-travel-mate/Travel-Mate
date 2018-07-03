package objects;

import java.io.Serializable;
import java.util.ArrayList;

public class FunFact implements Serializable {

    private String mTitle;
    private String mImage;
    private String mText;
    private String mSource;
    private String mSourceURL;

    /***
     * Initialize funfact object
     * @param title fact title
     * @param image fact image
     * @param text  face text
     * @param source fact source
     * @param sourceURL url of fact source
     */
    public FunFact(String title, String image, String text, String source, String sourceURL) {
        this.mTitle = title;
        this.mImage = image;
        this.mText = text;
        this.mSource = source;
        this.mSourceURL = sourceURL;
    }

    public static ArrayList<FunFact> createFunFactList(ArrayList<String> facts, ArrayList<String> images,
                                                       ArrayList<String> sources, ArrayList<String> sourceURLs) {
        ArrayList<FunFact> factsArray = new ArrayList<>();
        if (facts.size() > 0) {
            String fact, source, sourceURL;
            for (int i = 0; i < facts.size(); i++) {
                fact = facts.get(i);
                source = sources.get(i);
                sourceURL = sourceURLs.get(i);
                double random = Math.random();
                String randomImage = null;
                if (images.size() > 0) {
                    int randomNum = (int) (random * 100) % (images.size());
                    randomImage = images.get(randomNum);
                }
                FunFact fact1 = new FunFact(null, randomImage, fact, source, sourceURL);
                factsArray.add(fact1);
            }
        }
        return factsArray;
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

    public String getSource() {
        return mSource;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public String getSourceURL() {
        return mSourceURL;
    }

    public void setSourceURL(String mSourceURL) {
        this.mSourceURL = mSourceURL;
    }

}
