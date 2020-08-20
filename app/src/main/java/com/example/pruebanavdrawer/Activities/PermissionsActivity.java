package com.example.pruebanavdrawer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pruebanavdrawer.R;

public class PermissionsActivity extends AppCompatActivity {

    private final int PERMISSION_REQUEST_ACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        if(askPermissions()){
            begin();
        }
    }

    private void begin(){

        Toast.makeText(this, "Me concediste los permisos.", Toast.LENGTH_SHORT).show();

    }

    private boolean askPermissions(){

        if (
                !(
                        ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                                == PackageManager.PERMISSION_GRANTED
                )
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_ACTIVITY);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_ACTIVITY) {
            if(!(grantResults.length >0 && (grantResults[0]) == PackageManager.PERMISSION_GRANTED))
            {
                new AlertDialog.Builder(this)
                        .setTitle("Permiso necesario.")
                        .setMessage("Se necesitan autorizar los permisos para utilizar la aplicacion.")
                        .setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finishAffinity();
                                    }
                                }
                        ).create().show();
                return;
            }
            begin();
        }
    }
}
