package sahil.iiitk_foundationday_app.views;
// Game made by Tanuj
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    String cur_question,cur_option1,cur_option2,cur_option3,cur_option4,ffid;
    long cur_correct,cur_id,lives,correct;
    float progress;
    Button button1,button2,button3,button4;
    TextView question_view;
    ProgressBar progressBar;
    DataSnapshot userSnapshot;
    User user;
    CountDownTimer timer;
    final long range=10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        button1=findViewById(R.id.quiz_option1);
        button2=findViewById(R.id.quiz_option2);
        button3=findViewById(R.id.quiz_option3);
        button4=findViewById(R.id.quiz_option4);
        question_view=findViewById(R.id.quiz_question);
        progressBar=findViewById(R.id.timer);
        //show a dialog until data is downloaded
        dialog=new ProgressDialog(this);
        dialog.setMessage("Hang up a bit...We are loading...");
        dialog.setCancelable(false);
        dialog.show();

        //download data from firebase
        SharedPreferences pref=getSharedPreferences("userInfo",MODE_PRIVATE);
        ffid=pref.getString("FFID","");
        db=FirebaseDatabase.getInstance();
        //download list of done questions of user
        ref=db.getReference().child("Users");
        Query query=ref.orderByChild("user_id").equalTo(ffid);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot!=null){
                    userSnapshot=dataSnapshot;
                    user=dataSnapshot.getValue(User.class);
                    lives=user.getQuiz_lives();
                    correct=user.getQuiz_correct();
                    done_question_ids=user.getDone_questions();
                    if (done_question_ids==null){
                        done_question_ids=new ArrayList<>();
                    }
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
        DatabaseReference ques_ref=db.getReference().child("New_Questions");
        ques_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.e("quiz","Questions data exists.");
                    collectQuestions((Map<String,Object>) dataSnapshot.getValue());
                }else{
                    dialog.dismiss();
                    //No questions are there on firebase
                    Log.e("quiz","Questions data empty!");
                    //do not let user play this game
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                    builder.setTitle("Oops! No questions.");
                    builder.setMessage("Looks like no questions have been added for quiz.\n" +
                            "Best of luck for other events.\n"+
                            "Wait for the questions of the Quiz to be added.");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            //go back to home page
                            //Intent intent=new Intent(QuizActivity.this,MainActivity.class);
                            //QuizActivity.this.startActivity(intent);
                            QuizActivity.this.finish();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
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
        //check if user is left with lives or not
        Log.e("quiz","Lives left: "+lives);
        if (lives==-1){
            //do not let user play this game
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
            builder.setTitle("Oops! All lives exhausted!");
            builder.setMessage("You have used all your lives and are not allowed to play further!\n" +
                    "Best of luck for other events.\n"+
                    "Wait for the results of the Quiz to be announced.");
            builder.setCancelable(false);
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //go back to home page
                    //Intent intent=new Intent(QuizActivity.this,MainActivity.class);
                    //QuizActivity.this.startActivity(intent);
                    QuizActivity.this.finish();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }else{
            //delete questions which have been attempted
            if (done_question_ids!=null){
                for (int i=0;i<done_question_ids.size();i++){
                    all_questions.remove(done_question_ids.get(i));
                    int x=all_question_ids.indexOf(done_question_ids.get(i));
                    if(x>=0){
                        all_question_ids.remove(x);
                    }
                }
            }
            Log.e("quiz","Questions to show: "+all_question_ids.toString());
            //show a dialog to continue or not
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
            builder.setTitle("Would you like to start...");
            builder.setMessage("Click Yes to continue with Game\n" +
                    "OR\n" +
                    "No for going back to Home.");
            builder.setCancelable(false);
            builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //go back to home page
                    //Intent intent=new Intent(QuizActivity.this,MainActivity.class);
                    //QuizActivity.this.startActivity(intent);
                    QuizActivity.this.finish();
                }
            });
            builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //start with the questions
                    showNextQuestion();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }
    }

    public void showNextQuestion(){
        //show a random next question from the list
        Random random=new Random();
        int x;
        if (all_question_ids.size()>0){
            x=random.nextInt(all_question_ids.size());

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
            if (x>=0) all_question_ids.remove(x);
            if (y>=0) all_questions.remove(y);

            //add this question's ID to the attempted list
            done_question_ids.add(y);
            Log.e("quiz","Updated Done list: "+done_question_ids.toString());
            //updating done questions list on firebase
            user.setDone_questions(done_question_ids);
            userSnapshot.getRef().setValue(user);

            //start a timer to control the time of 10 seconds
            timer=new CountDownTimer(range, 500) {

                public void onTick(long millisUntilFinished) {
                    Log.e("quiz","onTick: "+(range-millisUntilFinished));
                    long elapsed=range-millisUntilFinished;
                    progress=(float) elapsed/range;
                    int max=progressBar.getMax();
                    progressBar.setProgress((int)(progress*max));
                }
                public void onFinish() {
                    Log.e("quiz","Timer completed!");
                    lives--;
                    Log.e("quiz","Lives left: "+lives);
                    //update the number of correct answers  to the firebase
                    user.setDone_questions(done_question_ids);
                    user.setQuiz_correct(correct);
                    user.setQuiz_lives(lives);
                    userSnapshot.getRef().setValue(user);
                    //show results to the user
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                    builder.setTitle("Time Out!");
                    String str="";
                    if (lives>=0){
                        str="You have lost one life.\n" +
                                "Lives Left: "+lives;
                    }
                    builder.setMessage("Your didn't answer anything!\n"+str);
                    builder.setNegativeButton("Close Game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            //go back to home page
                            //Intent intent=new Intent(QuizActivity.this,MainActivity.class);
                            //QuizActivity.this.startActivity(intent);
                            QuizActivity.this.finish();
                        }
                    });
                    if (lives>=0){
                        builder.setPositiveButton("Next Question", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                showNextQuestion();
                            }
                        });
                    }
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }

            };
            timer.start();
            Log.e("quiz","timer started!");
        }else{
            Log.e("quiz","All questions over!");
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
            builder.setTitle("All Questions Over");
            builder.setMessage("Congratulations! You have reached the end of Quiz.\n" +
                    "This is not what everyone can do.\n Be Proud. Best of Luck.\n" +
                    "Wait for the results of the Quiz to be announced.");
            builder.setCancelable(false);
            builder.setNegativeButton("Close Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //go back to home page
                    //Intent intent=new Intent(QuizActivity.this,MainActivity.class);
                    //QuizActivity.this.startActivity(intent);
                    QuizActivity.this.finish();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }

    }

    public  void check(View view){
        Log.e("quiz","Checking question!");
        timer.cancel();
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
            correct++;
            Log.e("quiz","Correct: "+correct);
            //update the number of correct answers  to the firebase
            user.setDone_questions(done_question_ids);
            user.setQuiz_correct(correct);
            user.setQuiz_lives(lives);
            userSnapshot.getRef().setValue(user);
            //show results to the user
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
            builder.setTitle("Congratulations!");
            builder.setMessage("Your answer is Correct!");
            builder.setCancelable(false);
            builder.setNegativeButton("Close Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //go back to home page
                    //Intent intent=new Intent(QuizActivity.this,MainActivity.class);
                    //QuizActivity.this.startActivity(intent);
                    QuizActivity.this.finish();
                }
            });
            builder.setPositiveButton("Next Question", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    showNextQuestion();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }else{
            lives--;
            Log.e("quiz","Lives left: "+lives);
            //update the number of correct answers  to the firebase
            user.setDone_questions(done_question_ids);
            user.setQuiz_correct(correct);
            user.setQuiz_lives(lives);
            userSnapshot.getRef().setValue(user);
            //show results to the user
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
            builder.setTitle("Sorry!");
            builder.setCancelable(false);
            String str="";
            if (lives>=0){
                str="You have lost one life.\n" +
                        "Lives Left: "+lives;
            }
            builder.setMessage("Your answer is Incorrect!\n"+str);
            builder.setNegativeButton("Close Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //go back to home page
                    //Intent intent=new Intent(QuizActivity.this,MainActivity.class);
                    //QuizActivity.this.startActivity(intent);
                    QuizActivity.this.finish();
                }
            });
            if (lives>=0){
                builder.setPositiveButton("Next Question", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        showNextQuestion();
                    }
                });
            }
            AlertDialog dialog=builder.create();
            dialog.show();
        }

    }

}
