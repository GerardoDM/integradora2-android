package com.example.pruebanavdrawer.ui.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.pruebanavdrawer.R;

public class Permissions extends Fragment {

    private final int PERMISSION_REQUEST_FRAGMENT = 2;
    private Switch swWriteExternal, swCamera, swMicrophone;
    private String[] permissions;

    public Permissions() {
        // Required empty public constructor
        permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swWriteExternal = view.findViewById(R.id.swWriteExt);
        swWriteExternal.setOnClickListener(v -> {
            swWriteExternal.setEnabled(false);
            activatePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        });

        swCamera = view.findViewById(R.id.swCamera);
        swCamera.setOnClickListener(v -> {
            swCamera.setEnabled(false);
            activatePermission(Manifest.permission.CAMERA);
        });

        swMicrophone = view.findViewById(R.id.swMicrophone);
        swMicrophone.setOnClickListener(v -> {
            swMicrophone.setEnabled(false);
            activatePermission(Manifest.permission.RECORD_AUDIO);
        });

        checkPermissions();

    }

    private void activatePermission(String permission){

        if (
                !(
                        ContextCompat.checkSelfPermission(getActivity(),permission)
                                == PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(new String[]{
                    permission
            }, PERMISSION_REQUEST_FRAGMENT);
        }
    }

    private void checkPermissions(){
        for (String permission : permissions) {
            if ((ContextCompat.checkSelfPermission(getActivity(), permission)) == PackageManager.PERMISSION_GRANTED) {
                manipulateSwitch(permission,true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode != PERMISSION_REQUEST_FRAGMENT){
            return;
        }

        if(!(grantResults.length>0)){
            return;
        }

        for(int i = 0; i<grantResults.length;i++)
        {
            if(!(grantResults[i] == PackageManager.PERMISSION_GRANTED)){
                manipulateSwitch(permissions[i],false);
            }
        }

    }

    private void manipulateSwitch(String permission, boolean granted){
        switch(permission){
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:{
                if(granted){
                    swWriteExternal.setEnabled(false);
                    swWriteExternal.setChecked(true);
                    return;
                }
                swWriteExternal.setEnabled(true);
                swWriteExternal.setChecked(false);
                break;
            }

            case Manifest.permission.CAMERA:{
                if(granted){
                    swCamera.setEnabled(false);
                    swCamera.setChecked(true);
                    return;
                }
                swCamera.setEnabled(true);
                swCamera.setChecked(false);
                break;
            }

            case Manifest.permission.RECORD_AUDIO:{
                if(granted){
                    swMicrophone.setEnabled(false);
                    swMicrophone.setChecked(true);
                    return;
                }
                swMicrophone.setEnabled(true);
                swMicrophone.setChecked(false);
                break;
            }
        }
    }

}