package info.androidhive.sqlite.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Loc {
    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
