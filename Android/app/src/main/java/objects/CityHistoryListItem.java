package objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CityHistoryListItem {

    private String mHeading;
    private String mText;
    private Boolean mTrimText;

    public CityHistoryListItem(String mHeading, String mText, Boolean mTrimText) {
        this.mHeading = mHeading;
        this.mText = mText;
        this.mTrimText = mTrimText;
    }

    public String getHeading() {
        return mHeading;
    }

    public String getText() {
        StringBuilder currText = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(mText);
            Iterator<?> keys = jsonObject.keys();
            int cnt = 0;
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String nextText = jsonObject.get(key).toString();

                if (cnt == 0 || nextText.equals(""))
                    currText.append(nextText);
                else currText.append(nextText).append("\n");

                cnt++;
            }
        } catch (JSONException e) {
            currText = new StringBuilder(mText + "\n");
        }
        return currText.toString();
    }

    public Boolean getExpanded() {
        return mTrimText;
    }

    public void setExpanded(Boolean expanded) {
        mTrimText = expanded;
    }
}
