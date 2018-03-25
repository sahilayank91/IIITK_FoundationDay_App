package sahil.iiitk_foundationday_app.model;
// Made by Tanuj
import org.json.JSONException;
import org.json.JSONObject;

public class AdminIDs {

    private String id,name;

    public AdminIDs() {
    }
    public AdminIDs(JSONObject object) throws JSONException{
        if (object.has("id")) this.id=object.getString("id");
        if (object.has("name")) this.name=object.getString("name");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
