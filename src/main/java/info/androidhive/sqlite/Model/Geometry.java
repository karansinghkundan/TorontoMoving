package info.androidhive.sqlite.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Geometry {
    @SerializedName("location")
    @Expose
    private Loc location;

    public Loc getLoc() {
        return location;
    }

    public void setLoc(Loc location) {
        this.location = location;
    }


}
