package acroos.yardsale.models;

import android.media.Image;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by austinroos on 1/20/16.
 */
public class YardSaleUser {

    private int mId;
    private String mUserName;
    private String mPasswordHash;
    private String mFullName;
    private String mProfilePicture;
    private ArrayList<ForSaleItem> mItemsForSale;

    public YardSaleUser(String userName) {
        mUserName = userName;
        mFullName = null;
        mProfilePicture = null;
        mItemsForSale = new ArrayList<>();
    }


    public int getId() {
        return mId;
    }

    public YardSaleUser setId(int id) {
        mId = id;
        return this;
    }

    public String getUsername() {
        return mUserName;
    }

    public YardSaleUser setUserName(String userName) {
        mUserName = userName;
        return this;
    }

    public String getPasswordHash() {
        return mPasswordHash;
    }

    public YardSaleUser setPasswordHash(String hash) {
        mPasswordHash = hash;
        return this;
    }

    public String getFullName() {
        return mFullName;
    }

    public YardSaleUser setFullName(String name) {
        mFullName = name;
        return this;
    }

    public String getProfilePicturePath() {
        return mProfilePicture;
    }

    public YardSaleUser setProfilePicturePath(String path) {
        mProfilePicture = path;
        return this;
    }

    public ArrayList<ForSaleItem> getItemsForSale() {
        return mItemsForSale;
    }

    public YardSaleUser addItem(ForSaleItem item) {
        if (mItemsForSale == null) {
            mItemsForSale = new ArrayList<>();
        }
        mItemsForSale.add(0, item);
        return this;
    }
}
