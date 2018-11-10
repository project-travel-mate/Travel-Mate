package objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Quote implements Serializable {

    @SerializedName("quote")
    @Expose
    private String mQuote;
    @SerializedName("author")
    @Expose
    private String mAuthor;


    private static final long serialVersionUID = 1042998232026681227L;
    /**
     * No args constructor for use in serialization
     *
     */
    public Quote() {
    }

    /**
     *
     * @param mAuthor
     * @param mQuote
     */
    public Quote(String mQuote, String mAuthor) {
        super();
        this.mQuote = mQuote;
        this.mAuthor = mAuthor;
    }

    public String getQuote() {
        return mQuote;
    }

    public void setQuote(String mQuote) {
        this.mQuote = mQuote;
    }

    public Quote withQuote(String quote) {
        this.mQuote = quote;
        return this;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public Quote withAuthor(String author) {
        this.mAuthor = author;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("mQuote: " + mQuote)
                .append("mAuthor:" + mAuthor).toString();
    }

}