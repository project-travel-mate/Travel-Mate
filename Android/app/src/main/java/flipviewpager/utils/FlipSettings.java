package flipviewpager.utils;

import android.util.SparseIntArray;

/**
 * @author Yalantis
 */
public class FlipSettings {
    private final int mDefaultPage;

    private final SparseIntArray mPages = new SparseIntArray();

    private FlipSettings(int defaultPage) {
        this.mDefaultPage = defaultPage;
    }

    public void savePageState(int position, int page) {
        mPages.put(position, page);
    }

    public Integer getPageForPosition(int position) {
        return mPages.get(position, mDefaultPage);
    }

    public int getDefaultPage() {
        return mDefaultPage;
    }

    public static class Builder {

        private int mDefaultPage = 1;

        public Builder defaultPage() {
            this.mDefaultPage = 1;
            return this;
        }

        public FlipSettings build() {
            return new FlipSettings(mDefaultPage);
        }
    }
}
