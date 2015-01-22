/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.activityscenetransitionbasic;

import android.content.ContentValues;

/**
 * Represents an Item in our application. Each item has a name, id, full size image url and
 * thumbnail url.
 */
public class Item {
    private static final String LARGE_BASE_URL = "http://storage.googleapis.com/androiddevelopers/sample_data/activity_transition/large/";
    private static final String THUMB_BASE_URL = "http://storage.googleapis.com/androiddevelopers/sample_data/activity_transition/thumbs/";
    private final String mName;
    private final String mAuthor;
    private final String mFileName;
    private boolean mFav;

    Item (String name, String author, String fileName, boolean fav) {
        mName = name;
        mAuthor = author;
        mFileName = fileName;
        mFav = fav;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getName() {
        return mName;
    }

    public String getPhotoUrl() {
        return LARGE_BASE_URL + mFileName;
    }

    public String getThumbnailUrl() {
        return THUMB_BASE_URL + mFileName;
    }

    public boolean getFav() { return  mFav; }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MyContract.MyColumns.TITLE, getName());
        values.put(MyContract.MyColumns.AUTHOR, getAuthor());
        values.put(MyContract.MyColumns.PHOTO, getPhotoUrl());
        values.put(MyContract.MyColumns.THUMBNAIL, getThumbnailUrl());
        values.put(MyContract.MyColumns.FAV, getFav() ? 1 : 0);
        return values;
    }
}
