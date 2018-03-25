package sahil.iiitk_foundationday_app.views;
// Made by Tanuj
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.model.Question;

public class UploadQuestionActivity extends AppCompatActivity {

    EditText question,option1,option2,option3,option4;
    Spinner correct_option;
    List<String> upload_options=new ArrayList<>();
    Question upload_question=new Question();
    FirebaseDatabase database;
    String[] arraySpinner = new String[]{"1", "2", "3","4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_question);
        database = FirebaseDatabase.getInstance();

        question=findViewById(R.id.upload_question);
        option1=findViewById(R.id.upload_option1);
        option2=findViewById(R.id.upload_option2);
        option3=findViewById(R.id.upload_option3);
        option4=findViewById(R.id.upload_option4);
        correct_option=findViewById(R.id.upload_correct_option);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        correct_option.setAdapter(adapter);
    }

    public void uploadQuestion(View view){
        upload_options.clear();
        String ques=question.getText().toString();
        String a=option1.getText().toString();
        String b=option2.getText().toString();
        String c=option3.getText().toString();
        String d =option4.getText().toString();
        String e=arraySpinner[correct_option.getSelectedItemPosition()];
        if (ques.isEmpty()) {
            question.setError("Empty!");
            return;
        }
        if (a.isEmpty()){
            option1.setError("Empty!");
            return;
        }
        if (b.isEmpty()){
            option2.setError("Empty!");
            return;
        }
        if (c.isEmpty()){
            option3.setError("Empty!");
            return;
        }
        if (d.isEmpty()){
            option4.setError("Empty!");
            return;
        }
        upload_options.add(a);
        upload_options.add(b);
        upload_options.add(c);
        upload_options.add(d);
        upload_question.setQuestion(ques);
        upload_question.setOptions(upload_options);
        upload_question.setCorrect_answer(Integer.parseInt(e));
        generateQuestionID();
    }

    public  void generateQuestionID(){
        DatabaseReference myRef = database.getReference("last_question");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long id=(long)dataSnapshot.getValue();
                id=id+1;
                dataSnapshot.getRef().setValue(id);
                pushQuestion(id);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", "Failed to read value.", databaseError.toException());
            }

        });
    }

    public void pushQuestion(long id){
        upload_question.setId(id);
        DatabaseReference mRef = database.getReference().child("New_Questions");
        mRef.push().setValue(upload_question);
        Toast.makeText(this, "Question push Successfull!", Toast.LENGTH_SHORT).show();
        question.setText("");
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");
    }
}
