package acroos.yardsale.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import acroos.yardsale.models.ForSaleItem;
import acroos.yardsale.models.YardSaleUser;

/**
 * Created by austinroos on 1/21/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YardSale.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD_HASH = "password_hash";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_PROFILE_PICTURE = "profile_picture_url";

    private static final String TABLE_ITEMS = "items";
    private static final String COLUMN_ITEM_ID = "item_id";
    private static final String COLUMN_ITEM_TITLE = "title";
    private static final String COLUMN_ITEM_DESC = "description";
    private static final String COLUMN_ITEM_PRICE = "price";

    private static DatabaseHandler mInstance = null;

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void init(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHandler(context);
        }
    }

    public static DatabaseHandler getInstance() {
        return mInstance;
    }

    public void addUser(YardSaleUser user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD_HASH, user.getPasswordHash());
        values.put(COLUMN_FULL_NAME, user.getFullName());
        values.put(COLUMN_PROFILE_PICTURE, user.getProfilePicturePath());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public void addItem(ForSaleItem item, int userId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_TITLE, item.getItemName());
        values.put(COLUMN_ITEM_DESC, item.getDescription());
        values.put(COLUMN_ITEM_PRICE, item.getPrice());
        values.put(COLUMN_USER_ID, userId);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    @Nullable
    public YardSaleUser getUser(String username) {
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = \"" + username + "\"";
        SQLiteDatabase db = getWritableDatabase();

        YardSaleUser user = new YardSaleUser(username);

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            c.moveToFirst();
            user.setId(c.getInt(0))
                    .setPasswordHash(c.getString(2))
                    .setFullName(c.getString(3))
                    .setProfilePicturePath(c.getString(4));
            c.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }

    @Nullable
    public ArrayList<ForSaleItem> getItemsForUser(int userId) {
        String query = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_USER_ID + "=" + userId;
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery(query, null);
        int count = c.getCount();
        ArrayList<ForSaleItem> items = new ArrayList<>();
        for(int i=0; i<count; i++) {
            c.moveToPosition(i);
            ForSaleItem item = new ForSaleItem()
                    .setId(c.getInt(0))
                    .setItemName(c.getString(1))
                    .setDescription(c.getString(2))
                    .setPrice(c.getFloat(3));
            items.add(item);
        }
        c.close();
        db.close();
        return items;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String path = db.getPath();
        String createUsers = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" +
                COLUMN_USER_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                COLUMN_USERNAME + " VARCHAR(32) NOT NULL, " +
                COLUMN_PASSWORD_HASH + " CHAR(102) NOT NULL, " +
                COLUMN_FULL_NAME + " TEXT, " +
                COLUMN_PROFILE_PICTURE + " TEXT" +
                ");";
        db.execSQL(createUsers);
        String createItems = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + "(" +
                COLUMN_ITEM_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                COLUMN_ITEM_TITLE + " VARCHAR(100) NOT NULL, " +
                COLUMN_ITEM_DESC + " TEXT, " +
                COLUMN_ITEM_PRICE + " REAL, " +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ");";
        db.execSQL(createItems);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS + ";");
        onCreate(db);
    }
}
