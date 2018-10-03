package objects;

import com.google.gson.annotations.SerializedName;

public class Contributor {
    @SerializedName("avatar_url")
    private String mAvatarUrl;
    @SerializedName("contributions")
    private Integer mContributions;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("repository_name")
    private String mRepositoryName;

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public Integer getContributions() {
        return mContributions;
    }

    public void setContributions(Integer contributions) {
        this.mContributions = contributions;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getRepositoryName() {
        return mRepositoryName;
    }

    public void setRepositoryName(String mRepositoryName) {
        this.mRepositoryName = mRepositoryName;
    }

}
