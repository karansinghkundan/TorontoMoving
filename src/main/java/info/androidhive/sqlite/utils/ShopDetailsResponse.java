package info.androidhive.sqlite.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhi on 20-05-2016.
 */
public class ShopDetailsResponse {
    @SerializedName("result")
    @Expose
    private ShopDetails result;

    @SerializedName("status")
    @Expose
    private String status;

    public ShopDetails getResult() {
        return result;
    }

    public void setResult(ShopDetails result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
