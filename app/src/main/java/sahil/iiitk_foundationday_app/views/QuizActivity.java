package sahil.iiitk_foundationday_app.views;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.model.Question;
import sahil.iiitk_foundationday_app.model.User;

public class QuizActivity extends AppCompatActivity {

    List<Long> all_question_ids =new ArrayList<>();
    List<Long> done_question_ids =new ArrayList<>();
    Map<Long,Question> all_questions=new HashMap<>();
    ProgressDialog dialog;
    FirebaseDatabase db;
    DatabaseReference ref;
    String cur_question,cur_option1,cur_option2,cur_option3,cur_option4;
    long cur_correct,cur_id;
    Button button1,button2,button3,button4;
    TextView question_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        button1=findViewById(R.id.quiz_option1);
        button2=findViewById(R.id.quiz_option2);
        button3=findViewById(R.id.quiz_option3);
        button4=findViewById(R.id.quiz_option4);
        question_view=findViewById(R.id.quiz_question);
        //show a dialog until data is downloaded
        dialog=new ProgressDialog(this);
        dialog.setMessage("Hang up a bit...We are loading...");

        //download data from firebase
        SharedPreferences pref=getSharedPreferences("userInfo",MODE_PRIVATE);
        String ffid=pref.getString("FFID","");
        db=FirebaseDatabase.getInstance();
        //download list of done questions of user
        ref=db.getReference().child("Users");
        Query query=ref.orderByChild("user_id").equalTo(ffid);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot!=null){
                    User fetch=dataSnapshot.getValue(User.class);
                    done_question_ids=fetch.getDone_questions();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //download list of all questions
        DatabaseReference ques_ref=db.getReference().child("Questions");
        ques_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.e("quiz","Questions data exists.");
                    collectQuestions((Map<String,Object>) dataSnapshot.getValue());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public void collectQuestions(Map<String,Object> data){
        Log.e("quiz","Collecting questions");
        for (Map.Entry<String, Object> entry : data.entrySet()){
            //Get questions
            Map singleQuestion = (Map) entry.getValue();
            all_question_ids.add((Long)singleQuestion.get("id"));
            Question q=new Question();
            q.setId((Long) singleQuestion.get("id"));
            q.setCorrect_answer((Long)singleQuestion.get("correct_answer"));
            q.setQuestion((String) singleQuestion.get("question"));
            q.setOptions((List<String>)singleQuestion.get("options"));
            all_questions.put((Long)singleQuestion.get("id"),q);
        }
        dialog.dismiss();
        Log.e("quiz",all_question_ids.toString());
        if (done_question_ids!=null) Log.e("quiz",done_question_ids.toString());
        //delete questions which have been attempted
        if (done_question_ids!=null){
            for (int i=0;i<done_question_ids.size();i++){
                all_questions.remove(done_question_ids.get(i));
                int x=all_question_ids.indexOf(done_question_ids.get(i));
                all_question_ids.remove(x);
            }
        }
        showNextQuestion();
    }

    public void showNextQuestion(){
        //show a random next question from the list
        Random random=new Random();
        int x;
        if (all_question_ids.size()>0){
            x=random.nextInt(all_question_ids.size());
        }else{
            Toast.makeText(this,"All questions over!",Toast.LENGTH_SHORT).show();
            return;
        }
        long y=all_question_ids.get(x);
        Question a=all_questions.get(y);
        cur_question=a.getQuestion();
        cur_correct=a.getCorrect_answer();
        cur_id=a.getId();
        question_view.setText(cur_question);
        List<String> b=a.getOptions();
        button1.setText(b.get(0));
        button2.setText(b.get(1));
        button3.setText(b.get(2));
        button4.setText(b.get(3));
        //delete this question from the list
        all_question_ids.remove(x);
        all_questions.remove(y);
    }

    public  void check(View view){
        long a=0;
        if (view.getId()==R.id.quiz_option1){
            a=1;
        }else if (view.getId()==R.id.quiz_option2){
            a=2;
        }else if (view.getId()==R.id.quiz_option3){
            a=3;
        }else if (view.getId()==R.id.quiz_option4){
            a=4;
        }
        if (a==cur_correct){
            Toast.makeText(this,"Correct Answer!",Toast.LENGTH_SHORT).show();
            showNextQuestion();
        }else{
            Toast.makeText(this,"Wrong Answer! You can't play more.",Toast.LENGTH_SHORT).show();
        }

    }

}
