package bsu.bsit3d.weathermap;

import static bsu.bsit3d.weathermap.LogIn.accNumber;
import static bsu.bsit3d.weathermap.LogIn.emailList;
import static bsu.bsit3d.weathermap.LogIn.passwordList;
import static bsu.bsit3d.weathermap.LogIn.userList;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class fragAccount extends Fragment {
    TextView txtUsername, txtPassword, txtEmail;
    Button btnEdit, btnDelete, btnLogout;
    ImageView imgLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragaccount, container, false);

        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPassword = view.findViewById(R.id.txtPassword);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnLogout = view.findViewById(R.id.btnLogout);

        String username = LogIn.getUsername();
        txtUsername.setText(username);
        String email = LogIn.getEmail();
        txtEmail.setText(email);
        String password = LogIn.getPassword();
        txtPassword.setText(password);

        btnEdit.setOnClickListener(v -> editAccount());
        btnDelete.setOnClickListener(v -> deleteAccount());
        btnLogout.setOnClickListener(v -> showLogout());

        return view;
    }

    private void navigateToLogIn() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LogIn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void editAccount() {
        Intent intent = new Intent(getActivity(), Edit.class);
        startActivity(intent);
    }

    private void showLogout() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("Are you sure?")
                .setPositiveButton("Logout", (dialog, which) -> navigateToLogIn())
                .setNegativeButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void deleteAccount() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    userList.remove(accNumber);
                    passwordList.remove(accNumber);
                    emailList.remove(accNumber);
                    navigateToLogIn();
                })
                .setNegativeButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }
}
