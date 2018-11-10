package objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class QuoteGroup implements Serializable {

    @SerializedName("topic")
    @Expose
    private String mTopic;
    @SerializedName("quotes")
    @Expose
    private List<Quote> mQuotes = null;
    private static final long serialVersionUID = 3183946805251275841L;

    /**
     * No args constructor for use in serialization
     *
     */
    public QuoteGroup() {
    }

    /**
     *
     * @param mTopic
     * @param mQuotes
     */
    public QuoteGroup(String mTopic, List<Quote> mQuotes) {
        super();
        this.mTopic = mTopic;
        this.mQuotes = mQuotes;
    }

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String mTopic) {
        this.mTopic = mTopic;
    }

    public QuoteGroup withTopic(String topic) {
        this.mTopic = topic;
        return this;
    }

    public List<Quote> getQuotes() {
        return mQuotes;
    }

    public void setQuotes(List<Quote> mQuotes) {
        this.mQuotes = mQuotes;
    }

    public QuoteGroup withQuotes(List<Quote> quotes) {
        this.mQuotes = quotes;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("mTopic: " + mTopic)
                .append("mQuotes: " + mQuotes).toString();
    }

}