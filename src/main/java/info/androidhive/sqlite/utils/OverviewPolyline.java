package info.androidhive.sqlite.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhi on 22-05-2016.
 */
public class OverviewPolyline {
    @SerializedName("points")
    @Expose
    public String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
