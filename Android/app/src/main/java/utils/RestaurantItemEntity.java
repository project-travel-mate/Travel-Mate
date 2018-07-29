package utils;

public class RestaurantItemEntity {
    private String mImageUrl;
    private String mRestaurantName;
    private String mRestaurantAddress;
    private String mRatings;
    private String mVotes;
    private String mURL;
    private int mAvgCost;

    public RestaurantItemEntity(String imageUrl, String name, String address,
                                String ratings, String votes, int avgCost, String restaurantURL) {
        this.mImageUrl = imageUrl;
        this.mRestaurantName = name;
        this.mRestaurantAddress = address;
        this.mRatings = ratings;
        this.mVotes = votes;
        this.mAvgCost = avgCost;
        this.mURL = restaurantURL;
    }

    public String getImage() {
        return mImageUrl;
    }

    public String getName() {
        return mRestaurantName;
    }

    public String getAddress() {
        return mRestaurantAddress;
    }

    public String getRatings() {
        return mRatings;
    }

    public String getVotes() {
        return mVotes;
    }

    public int getAvgCost() {
        return mAvgCost;
    }

    public String getURL() {
        return mURL;
    }

}
