package objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CityHistoryListItem {

    private String mHeading;
    private String mText;

    public CityHistoryListItem(String heading, String text) {
        mHeading = heading;
        mText = text;
    }

    public String getHeading() {
        return mHeading;
    }

    public String getText() {
        String currText = "";
        try {
            JSONObject jsonObject = new JSONObject(mText);
            Iterator<?> keys = jsonObject.keys();
            int cnt = 0;
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String nextText = jsonObject.get(key).toString();

                if (cnt == 0 || nextText.equals(""))
                    currText += nextText;
                else currText += nextText + "\n";

                cnt++;
            }
        } catch (JSONException e) {
            currText = mText + "\n";
        }
        return currText;
    }
}
