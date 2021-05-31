
package com.simpad.pathaknotebook;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simpad.pathaknotebook.NetwokSyncs.CheckInternetConnection;
import com.simpad.pathaknotebook.NetwokSyncs.UserSession;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 2;
    MaterialButton signInGoogle,signInFacebook ;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mRef;
    private DatabaseReference dbRef;
    private UserSession session;
    private LottieAnimationView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();


        signInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInFacebookLogin();
            }
        });
        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signInFacebookLogin() {
        FancyAlertDialog.Builder info = new FancyAlertDialog.Builder(this)
                .setBackgroundColor(R.color.myTheme)
                .setimageResource(R.drawable.sad)
                .setTextTitle("We will update this Feature")
                .setTitleColor(R.color.white)
                .setTextSubTitle("Sorry for the inconvenience we will try to build this feature by the next update")
                .setSubtitleColor(R.color.white)
                .setBody("Try Signing in with Google which is preferred.")
                .setPositiveButtonText("OK")
                .setPositiveColor(R.color.white)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                            dialog.dismiss();
                    }
                })
                .setBodyGravity(FancyAlertDialog.TextGravity.CENTER)
                .setTitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setSubtitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setCancelable(true)
                .build();
        info.show();
    }


    private void initialize(){

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        new CheckInternetConnection(this).checkConnection();

        session = new UserSession(getApplicationContext());

        signInGoogle = findViewById(R.id.googleLogin);
        progress = findViewById(R.id.progressLottie);
        signInFacebook = findViewById(R.id.facebookLogin);
        progress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mRef = FirebaseDatabase.getInstance();
        dbRef = mRef.getReference("user");

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            account = handleSignInResult(task);
            assert account != null;
            progress.setVisibility(View.VISIBLE);
            firebaseAuthWithGoogle(account.getIdToken());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("YOO", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            session.createLoginSession(account.getDisplayName(),account.getEmail());
                            Log.d("TAG", "onComplete: "+account.getEmail());

                            HashMap<String, String> userDetails = session.getUserDetails();
                            dbRef.child(user.getUid()).setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progress.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, e.toString()+e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.d("TAG", "onFailure: "+e.toString());
                                    Log.d("TAG","On Failure :"+e.getMessage());
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("YOO", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private GoogleSignInAccount handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            return task.getResult(ApiException.class);
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        new CheckInternetConnection(this).checkConnection();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}