package bsu.bsit3d.weathermap;

import static bsu.bsit3d.weathermap.LogIn.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    TextView txtInvalid, txtLogin;
    EditText etxtUser, etxtEmail, etxtPass, etxtConfirmpass;
    CheckBox chkAgreeterms;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        etxtUser = findViewById(R.id.txtUser);
        etxtEmail = findViewById(R.id.txtEmail);
        etxtPass = findViewById(R.id.txtPass);
        etxtConfirmpass = findViewById(R.id.txtConfirmpass);
        btnSignup = findViewById(R.id.btnSignin);
        chkAgreeterms = findViewById(R.id.chkAgreeTerms);
        txtLogin = findViewById(R.id.txtLogin);
        txtInvalid = findViewById(R.id.txtInvalid);

        btnSignup.setOnClickListener(v -> signupAccount());
        txtLogin.setOnClickListener(v -> loginAccount());
    }
    public void loginAccount() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
    public void signupAccount(){
        String user = etxtUser.getText().toString();
        String email = etxtEmail.getText().toString();
        String pass = etxtPass.getText().toString();
        String confirmpass = etxtConfirmpass.getText().toString();
        boolean verified = false;

        if (user.isEmpty() || pass.isEmpty() || email.isEmpty() || confirmpass.isEmpty()) {
            txtInvalid.setText("Please fill-up all of the information.");
        } else if (!pass.equals(confirmpass)) {
            txtInvalid.setText("Password does not match.");
        } else if (emailList.contains(email)) {
            txtInvalid.setText("An account is already registered with the given email.");
        } else if (userList.contains(user)) {
            txtInvalid.setText("Username is already taken.");
        } else if (!chkAgreeterms.isChecked()) {
            txtInvalid.setText("Terms and Conditions is required to be checked.");
        } else {
            verified = true;
        }

        if (verified){
            userList.add(user);
            passwordList.add(pass);
            emailList.add(email);
            finish();
        }
    }
}