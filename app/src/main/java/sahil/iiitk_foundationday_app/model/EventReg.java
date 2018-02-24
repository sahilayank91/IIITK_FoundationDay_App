package sahil.iiitk_foundationday_app.model;
//Made by Tanuj

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EventReg {

    private String reg_time,team_name,email,phone;
    private List<String> FFIDs;
    private JSONArray jsonArray;

    public EventReg() {
    }
    public EventReg(Context context){
    }
    public EventReg(JSONObject object) throws JSONException {
        if (object.has("reg_time")) this.reg_time=object.getString("reg_time");
        if (object.has("team_name")) this.team_name=object.getString("team_name");
        if (object.has("ffids")) this.jsonArray=object.getJSONArray("ffids");
        if (object.has("email")) this.email=object.getString("email");
        if (object.has("phone")) this.phone=object.getString("phone");

        for(int i = 0; i < jsonArray.length(); i++){
            FFIDs.add(jsonArray.optString(i));
        }
    }

    public String getReg_time() {
        return reg_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public List<String> getFFIDs() {
        return FFIDs;
    }

    public void setFFIDs(List<String> FFIDs) {
        this.FFIDs = FFIDs;
    }

}
