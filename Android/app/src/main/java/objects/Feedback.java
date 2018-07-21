package objects;

public class Feedback {

    private String mType;
    private String mText;
    private String mDateCreated;

    public Feedback(String type, String text, String dateCreated) {
        mType = type;
        mText = text;
        mDateCreated = dateCreated;
    }

    public String getFeedbackType() {
        return mType;
    }

    public String getFeedbackText() {
        return mText;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

}
