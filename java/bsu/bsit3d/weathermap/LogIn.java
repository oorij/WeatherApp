package bsu.bsit3d.weathermap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;

public class LogIn extends AppCompatActivity {

    EditText etxtUser, etxtPass;
    Button btnLogin;
    CheckBox chkRemember;
    TextView txtSignup, txtInvalid;
    ImageButton btnGoogle;

    static String loggedUsername, loggedPassword, loggedEmail;
    static int accNumber;
    static ArrayList<String> userList = new ArrayList<>();
    static ArrayList<String> passwordList = new ArrayList<>();
    static ArrayList<String> emailList = new ArrayList<>();

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        etxtUser = findViewById(R.id.txtUser);
        etxtPass = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);
        chkRemember = findViewById(R.id.checkRemember);
        txtSignup = findViewById(R.id.txtSignup);
        txtInvalid = findViewById(R.id.txtInvalid);
        btnGoogle = findViewById(R.id.btnGoogle);
        FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnLogin.setOnClickListener(v -> checkAccount());

        txtSignup.setOnClickListener(v -> signupAccount());

        btnGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("LogIn", "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            loggedUsername = user.getDisplayName();
                            loggedEmail = user.getEmail();
                            loggedAccount();
                        }
                    } else {
                        Log.w("LogIn", "signInWithCredential:failure", task.getException());
                        Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loggedUsername = currentUser.getDisplayName();
            loggedEmail = currentUser.getEmail();
            loggedAccount();
        }
    }

    public void checkAccount() {
        String username, password;
        boolean userfound = false;
        username = etxtUser.getText().toString();
        password = etxtPass.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            txtInvalid.setText("Please enter both username and password.");
            return;
        }
        for (int i = 0; i<userList.size(); i++) {
            if (userList.get(i).equals(username) || emailList.get(i).equals(username)) {
                if (passwordList.get(i).equals(password)) {
                    String user = userList.get(i);
                    String pass = passwordList.get(i);
                    String email = emailList.get(i);
                    setUsername(user);
                    setEmail(email);
                    setPassword(pass);
                    userfound = true;
                    txtInvalid.setText("");
                    accNumber = i ;
                    loggedAccount();
                } else {
                    txtInvalid.setText("Username or password is incorrect.");
                    break;
                }
            }
        }
        if (!userfound){
            txtInvalid.setText("Username or password is incorrect.");
        }
    }

    public void loggedAccount() {
        Intent intent = new Intent(this, bsu.bsit3d.weathermap.Menu.class);
        startActivity(intent);
    }

    public void signupAccount() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public static String getUsername(){
        return loggedUsername;
    }
    public void setUsername(String username){
        loggedUsername = username;
    }
    public static String getPassword(){
        return loggedPassword;
    }
    public void setPassword(String password){
        loggedPassword = password;
    }
    public static String getEmail(){
        return loggedEmail;
    }
    public void setEmail(String email){
        loggedEmail = email;
    }
}