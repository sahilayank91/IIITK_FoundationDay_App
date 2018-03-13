package sahil.iiitk_foundationday_app.model;
// Made by Tanuj
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Question {

    private long id,correct_answer;
    private String question;
    private List<String> options;
    private JSONArray jsonArray;

    public Question() {
    }
    public Question(Context context){
    }
    public Question(JSONObject object) throws JSONException{
        if (object.has("id")) this.id=object.getLong("id");
        if (object.has("question")) this.question=object.getString("question");
        if (object.has("correct_answer")) this.correct_answer=object.getInt("correct_answer");
        if (object.has("options")) this.jsonArray=object.getJSONArray("options");

        for(int i = 0; i < jsonArray.length(); i++){
            options.add(jsonArray.optString(i));
        }
    }

    @Override
    public boolean equals(Object obj) {
         Question question=(Question) obj;
        if (question.getId()==this.id)
            return true;
        else return false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public long getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(long correct_answer) {
        this.correct_answer = correct_answer;
    }
}
