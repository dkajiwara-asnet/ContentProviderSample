package com.example.android.activityscenetransitionbasic;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyContentProvider extends ContentProvider {
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int CONTENTS = 1;
    private static final int CONTENT = 2;
    static {
        sURIMatcher.addURI(MyContract.AUTHORITY, "contents", CONTENTS);
        sURIMatcher.addURI(MyContract.AUTHORITY, "contents/#", CONTENT);
    }

    private static final Map<String, String> ProjectionMap;
    static {
        Map<String, String> tmpMap = new HashMap<>();
        tmpMap.put(MyContract.MyColumns._ID, MyContract.MyColumns._ID);
        tmpMap.put(MyContract.MyColumns.TITLE, MyContract.MyColumns.TITLE);
        tmpMap.put(MyContract.MyColumns.AUTHOR, MyContract.MyColumns.AUTHOR);
        tmpMap.put(MyContract.MyColumns.THUMBNAIL, MyContract.MyColumns.THUMBNAIL);
        tmpMap.put(MyContract.MyColumns.PHOTO, MyContract.MyColumns.PHOTO);
        tmpMap.put(MyContract.MyColumns.FAV, MyContract.MyColumns.FAV);
        ProjectionMap = Collections.unmodifiableMap(tmpMap);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        ExSQLiteHelper.newInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        ExSQLiteHelper openHelper = ExSQLiteHelper.newInstance(getContext());
        SQLiteDatabase db = openHelper.getReadableDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setProjectionMap(ProjectionMap);
        Cursor cursor;
        switch (sURIMatcher.match(uri)) {
            case CONTENTS:
                qb.setTables(ExSQLiteHelper.TABLE);
                cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case CONTENT:
                qb.setTables(ExSQLiteHelper.TABLE);
                qb.appendWhere(MyContract.MyColumns._ID + " = " + uri.getLastPathSegment());
                cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                new IllegalArgumentException("unknown uri : " + uri);
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        ExSQLiteHelper openHelper = ExSQLiteHelper.newInstance(getContext());
        SQLiteDatabase db = openHelper.getWritableDatabase();
        int ret = -1;
        switch (sURIMatcher.match(uri)) {
            case CONTENT:
                ret = db.update(ExSQLiteHelper.TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(MyContract.CONTENTS_URI, null);
            default:
                new IllegalArgumentException("unknown uri : " + uri);
        }
        return ret;
    }

    public static class ExSQLiteHelper extends SQLiteOpenHelper {
        private static final String SQLITE_FILENAME = "contents.db";
        private static final String TABLE = "table1";
        private static ExSQLiteHelper sInstance;
        private static List<Item> items = new ArrayList<>();
        static {
            items.add(new Item("Flying in the Light", "Romain Guy", "flying_in_the_light.jpg", false));
            items.add(new Item("Caterpillar", "Romain Guy", "caterpillar.jpg", false));
            items.add(new Item("Look Me in the Eye", "Romain Guy", "look_me_in_the_eye.jpg", false));
            items.add(new Item("Flamingo", "Romain Guy", "flamingo.jpg", false));
            items.add(new Item("Rainbow", "Romain Guy", "rainbow.jpg", false));
            items.add(new Item("Over there", "Romain Guy", "over_there.jpg", false));
            items.add(new Item("Jwelly Fish 2", "Romain Guy", "jelly_fish_2.jpg", false));
            items.add(new Item("Lone Pine Sunset", "Romain Guy", "lone_pine_sunset.jpg", false));
        }

        private ExSQLiteHelper(Context context, int version) {
            super(context, SQLITE_FILENAME, null, version);
        }

        public static ExSQLiteHelper newInstance(Context context) {
            synchronized (ExSQLiteHelper.class) {
                if (sInstance == null) {
                    try {
                        sInstance = new ExSQLiteHelper(
                                context,
                                context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sInstance;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE + " ("+ MyContract.MyColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                + MyContract.MyColumns.TITLE + " TEXT, "
                                                + MyContract.MyColumns.AUTHOR + " TEXT, "
                                                + MyContract.MyColumns.THUMBNAIL + " TEXT, "
                                                + MyContract.MyColumns.PHOTO + " TEXT, "
                                                + MyContract.MyColumns.FAV + " INTEGER)");
            for(Item item : items) {
                db.insert(TABLE, null, item.toContentValues());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
