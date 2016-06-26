package com.noqueue.logic;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Joel on 6/16/2016.
 */
public class Business {
    @SerializedName("id")
    public int id;
    @SerializedName("busnx_name")
    public String busnx_name;
    @SerializedName("busnx_address")
    public String busnx_address;
    @SerializedName("busnx_type")
    public String busnx_type;
    @SerializedName("location")
    public String location;
    @SerializedName("imageUrl")
    public String imageUrl;
}
