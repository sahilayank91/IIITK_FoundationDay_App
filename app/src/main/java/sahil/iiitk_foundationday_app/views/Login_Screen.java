package sahil.iiitk_foundationday_app.views;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import sahil.iiitk_foundationday_app.R;

public class Login_Screen extends AppCompatActivity
{
    ImageView inb;
    Button submit;
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
    String personPhone;
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
                        //Intent i = new Intent(getApplicationContext(),Phone_Activity.class);
                        //startActivity(i);
                        callPhoneLogInDialog();
                    }
                }
        );

        ff_login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        //Intent i = new Intent(getApplicationContext(),FF_ID.class);
                        //startActivity(i);
                        callLogInDialog();
                    }
                }
        );

    }
    private void callLogInDialog()
    {
        final Dialog myDialog =  new Dialog(this);
        myDialog.setContentView(R.layout.id_k);
        myDialog.setCancelable(true);
        myDialog.setTitle("FFID");
        final Button proceed = (Button) myDialog.findViewById(R.id.btnFF1);
        final EditText id = (EditText) myDialog.findViewById(R.id.ffid1);
        //myDialog.show();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().equals(""))
                {
                    id.setError("Invalid FFID");
                }
            }
        });

        myDialog.show();
    }

    private void callPhoneLogInDialog()
    {
        final Dialog myDialog =  new Dialog(this);
        myDialog.setContentView(R.layout.phone_verify);
        myDialog.setCancelable(true);
        myDialog.setTitle("VERIFY PHONE NUMBER");
        verify = (Button) myDialog.findViewById(R.id.phoneButton2);
        phoneField = (EditText) myDialog.findViewById(R.id.phoneField2);
        //myDialog.show();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                mVerificationInProgress=false;
                Toast.makeText(getApplicationContext(),"OTP Verified",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),forwarded.class);
                Bundle  extras=new Bundle();
                extras.putString("phone",personPhone);
                intent.putExtras(extras);
                startActivity(intent);
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


    // handle phone
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
            verify.setEnabled(false);
            verify.setText("Waiting for OTP ...");
        }
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                15,                 // Timeout duration
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
                Intent intent=new Intent(this,forwarded.class);
                Bundle extras=new Bundle();
                extras.putString("name",personName);
                extras.putString("email",personEmail);
                intent.putExtras(extras);
                startActivity(intent);
                //finish();    //so that user cannot go to login screen by pressing back button
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "Exception " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
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