package sahil.iiitk_foundationday_app.model;
// Made by tanuj
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class Notif {

    private long notif_id;
    private String details,time,which_club;

    public Notif() {
    }
    public Notif(Context context){
    }
    public Notif(JSONObject object) throws JSONException{
        if (object.has("notif_id")) this.notif_id=object.getLong("notif_id");
        if (object.has("details")) this.details=object.getString("details");
        if (object.has("time")) this.time=object.getString("time");
        if (object.has("which_club")) this.which_club=object.getString("which_club");
    }

    @Override
    public boolean equals(Object obj) {
        Notif notif=(Notif) obj;
        if (notif.getNotif_id()==this.notif_id)
            return true;
        else return false;
    }

    public long getNotif_id() {
        return notif_id;
    }

    public void setNotif_id(long notif_id) {
        this.notif_id = notif_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWhich_club() {
        return which_club;
    }

    public void setWhich_club(String which_club) {
        this.which_club = which_club;
    }
}
