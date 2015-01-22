package com.example.android.activityscenetransitionbasic;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author dkajiwara
 */
public class MyContract {
    public static final String AUTHORITY = "com.example.android";
    public static final Uri CONTENTS_URI = Uri.parse("content://" + AUTHORITY + "/contents");

    public static class MyColumns implements BaseColumns {
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String THUMBNAIL = "thumbnail";
        public static final String PHOTO = "photo";
        public static final String FAV = "fav";
    }
}