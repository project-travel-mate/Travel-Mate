package io.github.project_travel_mate.searchcitydialog;

import ir.mirrajabi.searchdialog.core.Searchable;

/*
 * Items to be displayed in search list
 */
public class CitySearchModel implements Searchable {

    private String mName;
    private String mImageUrl;
    private String mId;

    public CitySearchModel(String name, String imageUrl, String id) {
        mName = name;
        mImageUrl = imageUrl;
        mId = id;
    }

    @Override
    public String getTitle() {
        return mName;
    }

    public String getName() {
        return mName;
    }

    public CitySearchModel setName(String name) {
        mName = name;
        return this;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getId() {
        return mId;
    }
}