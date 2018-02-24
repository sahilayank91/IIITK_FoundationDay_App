package sahil.iiitk_foundationday_app.views;
// Made by tanuj
//all of the main login process was coded by tanuj,
// and final touch to some parts  by Gaurav
import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.roger.match.library.MatchTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.concurrent.TimeUnit;

import sahil.iiitk_foundationday_app.R;
import sahil.iiitk_foundationday_app.model.User;

public class Login_Screen extends AppCompatActivity
{
    TextView reg_later;
    ImageView inb;
    EditText id;
    Button ff_login_button;
    EditText ff_number_input;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "PhoneAuthActivity";
    SignInButton signInButton;
    Button phoneButton;
    EditText phoneField;
    int RC_SIGN_IN=1;
    Button verify;
    String personName;
    String personEmail;
    MatchTextView mtv;
    String personPhone;
    AVLoadingIndicatorView avi;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean backPressedToExitOnce = false;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status));
        }
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        //initialising the google signin process
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        inb=(ImageView) findViewById(R.id.imageView2);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        inb.startAnimation(myanim);
        ff_login_button=(Button) findViewById(R.id.button4);
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        signInButton=(SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        phoneButton=(Button)findViewById(R.id.phonebutton);
        phoneButton.setEnabled(true);
        signInButton.setEnabled(true);
        ff_login_button.setEnabled(true);
        reg_later=(TextView)findViewById(R.id.reg_later);
        avi = (AVLoadingIndicatorView) findViewById(R.id.AVLoadingIndicatorView);
        mtv = (MatchTextView) findViewById(R.id.hello);
        mtv.setText("IIIT KOTA Presents");
        mtv.setTextSize(35);
        mtv.setTextColor(Color.WHITE);
        mtv.setProgress(0.5f);
        signInButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gSignIn();
                    }
                }
        );

        phoneButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        callPhoneLogInDialog();
                        phoneButton.setVisibility(View.VISIBLE);
                        ff_login_button.setVisibility(View.VISIBLE);
                        signInButton.setVisibility(View.VISIBLE);
                    }
                }
        );

        ff_login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        callLogInDialog();
                        phoneButton.setVisibility(View.VISIBLE);
                        ff_login_button.setVisibility(View.VISIBLE);
                        signInButton.setVisibility(View.VISIBLE);
                    }
                }
        );
        //request internet permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},124);
           }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==124 && grantResults[0]==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},124);
        }
    }
    private void callLogInDialog()
    {
        phoneButton.setVisibility(View.GONE);
        ff_login_button.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);
        final Dialog myDialog =  new Dialog(this);
        myDialog.setContentView(R.layout.id_k);
        myDialog.setCancelable(true);
        myDialog.setTitle("FFID");
        final Button proceed = (Button) myDialog.findViewById(R.id.btnFF1);
        final EditText id = (EditText) myDialog.findViewById(R.id.ffid1);
        final EditText email=(EditText) myDialog.findViewById(R.id.ffid_email);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().equals(""))
                {
                    id.setError("Empty FFID");
                }
                else if (email.getText().toString().equals("")){
                    email.setError("Empty Email");
                }
                else
                {
                    checkFFID(id.getText().toString(),email.getText().toString());
                    myDialog.dismiss();
                }
            }
        });

        myDialog.show();
    }

    private void callPhoneLogInDialog()
    {
        phoneButton.setVisibility(View.GONE);
        ff_login_button.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);
        final Dialog myDialog =  new Dialog(this);
        myDialog.setContentView(R.layout.phone_verify);
        myDialog.setCancelable(true);
        myDialog.setTitle("VERIFY PHONE NUMBER");
        verify = (Button) myDialog.findViewById(R.id.phoneButton2);
        phoneField = (EditText) myDialog.findViewById(R.id.phoneField2);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mVerificationInProgress=false;
                Toast.makeText(getApplicationContext(),"OTP Verified",Toast.LENGTH_SHORT).show();
                checkPhone(personPhone);
                myDialog.dismiss();    //so that user cannot go to login screen by pressing back button

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(getApplicationContext(),"Verification Failed: "+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                verify.setEnabled(false);
                Toast.makeText(getApplicationContext(),"OTP sent",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(getApplicationContext(),"OTP detection timeout. Try resending OTP",Toast.LENGTH_SHORT).show();
                verify.setText("Resend OTP");
                verify.setEnabled(true);
            }
        };

        myDialog.show();
    }

    //this method checks if a registered account exists for the given phone number in
//firebase. Appropriate action to be done inside if-else statements to get required usage.
//this will be used on the onCreate() method of login screen
    public void checkPhone(final String a){
        phoneButton.setEnabled(false);
        signInButton.setEnabled(false);
        ff_login_button.setEnabled(false);
        avi.show();
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference().child("Users");
        Query query=ref.orderByChild("phone").equalTo(a);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    downloadUserDetailsAfterPhone(a);
                }else{
                    //do appropriate action here when account with this phone number does not exist
                    Intent i = new Intent(getApplicationContext(),Register.class);
                    Bundle extra = new Bundle();
                    extra.putString("phone",personPhone);
                    i.putExtras(extra);
                    avi.hide();
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void downloadUserDetailsAfterPhone(final String a){
        //retrieve details of user from database
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference().child("Users");
        Query query=ref.orderByChild("phone").equalTo(a);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User fetch=dataSnapshot.getValue(User.class);
                SharedPreferences userdetails = getSharedPreferences("userInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor=userdetails.edit();
                editor.putString("name",fetch.getName());
                editor.putString("department",fetch.getDepartment());
                editor.putString("phone",fetch.getPhone());
                editor.putString("email",fetch.getEmail());
                editor.putString("college",fetch.getCollege());
                editor.putString("collegeid",fetch.getCollegeid());
                editor.putString("gender", fetch.getGender());
                editor.putString("Year", fetch.getYear());
                editor.putString("MOS",fetch.getMos());
                editor.putString("FFID",fetch.getUser_id());
                editor.putString("status","true");
                editor.apply();

                //do appropriate action here when account with this phone number exists
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                avi.hide();
                finish();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mVerificationInProgress && validatePhoneNumber(phoneField.getText().toString())) {
            startPhoneNumberVerification(phoneField.getText().toString());
        }
    }
    private boolean validatePhoneNumber(String num) {
        if (TextUtils.isEmpty(num) || num.length()<10) {
            phoneField.setError("Invalid phone number");
            return false;
        }
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }
    //to handle login by otp verification
    public void phoneLogin(View view){
        personPhone=phoneField.getText().toString();
        if (validatePhoneNumber(personPhone)){
            startPhoneNumberVerification(personPhone);
            verify.setText("Waiting for OTP ...");
        }
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                45,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallback
        mVerificationInProgress = true;
    }



    //to handle signin by otp verification
    public void gSignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                personName = account.getDisplayName();
                personEmail = account.getEmail();
                // Launching landing activity for registration
                checkEmail(personEmail);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "Exception " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    //this method checks if a registered account exists for the given email id in
//firebase. Appropriate action to be done inside if-else statements to get required usage.
//this will be used on the onCreate() method of login screen
    public void checkEmail(final String a){
        phoneButton.setEnabled(false);
        signInButton.setEnabled(false);
        ff_login_button.setEnabled(false);
        avi.show();
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference().child("Users");
        Query q=ref.orderByChild("email").equalTo(a);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    downloadUserDetailsAfterEmail(a);
                }else{
                    //do appropriate action here when account with this email does not exist
                    Intent i =  new Intent(getApplicationContext(),Register.class);
                    Bundle extra = new Bundle();
                    extra.putString("name",personName);
                    extra.putString("email",personEmail);
                    i.putExtras(extra);
                    avi.hide();
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void downloadUserDetailsAfterEmail(final String a ){
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference().child("Users");
        Query query=ref.orderByChild("email").equalTo(a);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //do appropriate action here when account with this email exists
                    //retrieve user details from database
                    User fetch=dataSnapshot.getValue(User.class);
                    Log.e("datasnapshot",fetch.getEmail()+","+fetch.getName()+", "+fetch.getUser_id());
                    SharedPreferences userdetails = getSharedPreferences("userInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor=userdetails.edit();
                    editor.putString("name",fetch.getName());
                    editor.putString("department",fetch.getDepartment());
                    editor.putString("phone",fetch.getPhone());
                    editor.putString("email",fetch.getEmail());
                    editor.putString("college",fetch.getCollege());
                    editor.putString("collegeid",fetch.getCollegeid());
                    editor.putString("gender", fetch.getGender());
                    editor.putString("Year", fetch.getYear());
                    editor.putString("MOS",fetch.getMos());
                    editor.putString("FFID", fetch.getUser_id());
                    editor.putString("status","true");
                    editor.apply();

                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    avi.hide();
                    finish();
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
    }

    //this method checks if a registered account exists for the given user_id/FFID in
//firebase. Appropriate action to be done inside if-else statements to get required usage.
//this will be used on the onCreate() method of login screen
    public void checkFFID(final String a,final String b){
        phoneButton.setEnabled(false);
        signInButton.setEnabled(false);
        ff_login_button.setEnabled(false);
        avi.show();
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference("Users");
        Query q=ref.orderByChild("user_id").equalTo(a);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    authFFIDwithEmail(a,b);
                }else{
                    Toast.makeText(getApplicationContext(),"No account exists with given FFID!",Toast.LENGTH_SHORT).show();
                    avi.hide();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void authFFIDwithEmail(String a,final String b){
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference("Users");
        Query query=ref.orderByChild("user_id").equalTo(a);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("datasnapshot","exists");

                //do appropriate action here when account with this user_id/FFID number exists
                //retrieve user details from database
                User fetch=(User)dataSnapshot.getValue(User.class);
                Log.e("datasnapshot",fetch.getPhone()+","+fetch.getName()+", "+fetch.getUser_id());
                if (fetch.getEmail().equals(b)){
                    SharedPreferences userdetails = getSharedPreferences("userInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor=userdetails.edit();
                    editor.putString("name",fetch.getName());
                    editor.putString("department",fetch.getDepartment());
                    editor.putString("phone",fetch.getPhone());
                    editor.putString("email",fetch.getEmail());
                    editor.putString("college",fetch.getCollege());
                    editor.putString("collegeid",fetch.getCollegeid());
                    editor.putString("gender", fetch.getGender());
                    editor.putString("Year", fetch.getYear());
                    editor.putString("MOS",fetch.getMos());
                    editor.putString("FFID",fetch.getUser_id());
                    editor.putString("status","true");
                    editor.apply();

                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    avi.hide();
                    finish();
                }else{
                    Log.e("datasnapshot","email does not match with FFID!");
                    //do appropriate action here when account with this user_id/FFID does not exist
                    phoneButton.setEnabled(true);
                    signInButton.setEnabled(true);
                    ff_login_button.setEnabled(true);
                    avi.hide();
                    Toast.makeText(getApplicationContext(),"FFID and email do not match!",Toast.LENGTH_SHORT).show();
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
    }

    // OnBackPress Logic
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
        } else {
            this.backPressedToExitOnce = true;
            showToast("Press again to exit");
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }
    private void showToast(String message) {
        if (this.toast == null) {
            // Create toast if found null, it would he the case of first call only
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else if (this.toast.getView() == null) {
            // Toast not showing, so create new one
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else {
            // Updating toast message is showing
            this.toast.setText(message);
        }

        // Showing toast finally
        this.toast.show();
    }
    private void killToast() {
        if (this.toast != null) {
            this.toast.cancel();
        }
    }
    @Override
    protected void onPause() {
        killToast();
        super.onPause();
    }
}