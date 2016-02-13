package acroos.yardsale.models;

import java.security.SecureRandom;

/**
 * Created by austinroos on 1/20/16.
 */
public class ForSaleItem {

    private int mId;
    private String mItemName;
    private String mShortDescription;
    private String mDescription;
    private float mPrice;

    public ForSaleItem() {

    }

    public int getId() {
        return mId;
    }

    public ForSaleItem setId(int id) {
        this.mId = id;
        return this;
    }

    public String getItemName() {
        return mItemName;
    }

    public ForSaleItem setItemName(String mItemName) {
        this.mItemName = mItemName;
        return this;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public ForSaleItem setShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
        return this;
    }

    public String getDescription() {
        return mDescription;
    }

    public ForSaleItem setDescription(String mDescription) {
        this.mDescription = mDescription;
        return this;
    }

    public float getPrice() {
        return mPrice;
    }

    public ForSaleItem setPrice(float mPrice) {
        this.mPrice = mPrice;
        return this;
    }
}
