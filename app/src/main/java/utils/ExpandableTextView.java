package utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import io.github.project_travel_mate.R;

public class ExpandableTextView extends android.support.v7.widget.AppCompatTextView {

    private static final int DEFAULT_TRIM_LENGTH = 200;
    private static final String ELLIPSIS = "...";
    private CharSequence mOriginalText;
    private CharSequence mTrimmedText;
    private BufferType mBufferType;
    private boolean mTrim = true;
    private int mTrimLength;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.mTrimLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
        typedArray.recycle();
    }

    public void handleExpansion(boolean isExpandClicked) {
        mTrim = isExpandClicked;
        setText();
    }

    public void setText() {
        super.setText(getDisplayableText(), mBufferType);
    }

    private CharSequence getDisplayableText() {
        return mTrim ? mTrimmedText : mOriginalText;
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        mOriginalText = text;
        mTrimmedText = getTrimmedText(text);
        mBufferType = type;
        setText();
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (mOriginalText != null && mOriginalText.length() > mTrimLength) {
            return new SpannableStringBuilder(mOriginalText, 0, mTrimLength + 1).append(ELLIPSIS);
        } else {
            return mOriginalText;
        }
    }

    public CharSequence getOriginalText() {
        return mOriginalText;
    }

    public void setTrimLength(int trimLength) {
        this.mTrimLength = trimLength;
        mTrimmedText = getTrimmedText(mOriginalText);
        setText();
    }

    public int getTrimLength() {
        return mTrimLength;
    }
}
