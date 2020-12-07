package info.androidhive.sqlite.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhi on 20-05-2016.
 */
public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    public Boolean openNow;

    @SerializedName("weekday_text")
    @Expose
    public List<String> weekdayText = new ArrayList<String>();

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }
}
