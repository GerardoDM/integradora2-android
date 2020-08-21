package com.example.pruebanavdrawer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pruebanavdrawer.MainActivity;
import com.example.pruebanavdrawer.R;

public class PermissionsActivity extends AppCompatActivity {

    final int REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        askPermissions();


    }


    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionsActivity.this,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY) ) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(
                        PermissionsActivity.this
                );

                builder.setTitle("Grant permissions");
                builder.setMessage("ACCESS_NOTIFICATIONS_POLICY");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{
                                Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 123
                        );

                        Intent intent = new Intent(PermissionsActivity.this, MainActivity.class);

                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            } else {

                ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{
                        Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 123
                );

            }


        } else {

            Toast.makeText(PermissionsActivity.this, "Permission already granted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(PermissionsActivity.this, MainActivity.class);

            startActivity(intent);


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if ((grantResults.length > 0) && (grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(PermissionsActivity.this, "Permission granted", Toast.LENGTH_LONG).show();

            }

            else {
                Toast.makeText(PermissionsActivity.this,"Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}






