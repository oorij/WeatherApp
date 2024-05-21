package bsu.bsit3d.weathermap;

import static bsu.bsit3d.weathermap.LogIn.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Edit extends AppCompatActivity {

    TextView txtInvalid;
    EditText etxtUser, etxtEmail, etxtCurrentpass, etxtNewpass, etxtConfirmnewpass;
    Button done, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        etxtUser = findViewById(R.id.txtUsername);
        etxtEmail = findViewById(R.id.txtEmail);
        etxtCurrentpass = findViewById(R.id.txtCurrentpassword);
        etxtNewpass = findViewById(R.id.txtNewpassword);
        etxtConfirmnewpass = findViewById(R.id.txtConfirmnewpassword);
        done = findViewById(R.id.btnEdit);
        cancel = findViewById(R.id.btnCancel);
        txtInvalid = findViewById(R.id.txtInvalid);

        Intent intent = getIntent();
        String loggedUsername = intent.getStringExtra("username");
        String loggedEmail = intent.getStringExtra("email");

        etxtUser.setText(loggedUsername);
        etxtEmail.setText(loggedEmail);

        done.setOnClickListener(v -> editAccount());
        cancel.setOnClickListener(v -> navigateToMenu());
    }

    private void navigateToMenu() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
    public void editAccount() {
        String user = etxtUser.getText().toString();
        String email = etxtEmail.getText().toString();
        String newpass = etxtNewpass.getText().toString();
        String confirmnewpass = etxtConfirmnewpass.getText().toString();
        String currentpass = etxtCurrentpass.getText().toString();
        boolean verified = false;

        if (currentpass.isEmpty() || newpass.isEmpty() || confirmnewpass.isEmpty()) {
            txtInvalid.setText("Please fill-up all of the password fields.");
        } else if (!currentpass.equals(loggedPassword)) {
            txtInvalid.setText("Wrong Password.");
        } else if (!newpass.equals(confirmnewpass)) {
            txtInvalid.setText("Passwords do not match.");
        } else {
            if (!user.isEmpty() || !email.isEmpty()) {
                if (!email.equals(loggedEmail) && emailList.contains(email)) {
                    txtInvalid.setText("An account is already registered with the given email.");
                    return;
                }
                if (!user.equals(loggedUsername) && userList.contains(user)) {
                    txtInvalid.setText("Username is already taken.");
                    return;
                }
                userList.set(accNumber, user);
                emailList.set(accNumber, email);
                loggedUsername = user;
                loggedEmail = email;
            }
            passwordList.set(accNumber, newpass);
            loggedPassword = newpass;
            verified = true;
        }

        if (verified) {
            Menu();
        }
    }

    public void Menu() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}