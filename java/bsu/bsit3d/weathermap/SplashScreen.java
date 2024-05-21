package bsu.bsit3d.weathermap;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setOnCompletionListener(mp -> checkInternetConnection());
        videoView.start();
    }

    private void checkInternetConnection() {
        ConnectivityManager check = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = check.getAllNetworks();
        NetworkInfo networkInfo;

        boolean wifiConnected = false;

        for(Network network: networks){
            networkInfo = check.getNetworkInfo(network);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                    networkInfo.getState().equals(NetworkInfo.State.CONNECTED)){
                wifiConnected = true;
                break;
            }
        }

        if (wifiConnected){
            Toast.makeText(context, "Connected to Wi-Fi", Toast.LENGTH_LONG).show();
            login();
        } else {
            Toast.makeText(context, "No Wi-Fi Network Connected", Toast.LENGTH_LONG).show();
            AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
            alert.setMessage("No Wi-Fi Network Connected")
                    .setCancelable(false)
                    .setPositiveButton("Try again", (dialog, which) -> restart());
            AlertDialog alert1 = alert.create();
            alert1.show();
        }
    }
    public void login() {
        Intent intent = new Intent(this, bsu.bsit3d.weathermap.LogIn.class);
        startActivity(intent);
    }
    public void restart() {
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
    }
}