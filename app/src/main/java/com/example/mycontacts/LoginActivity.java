package com.example.mycontacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
public class LoginActivity extends AppCompatActivity {

    private EditText etUserName, etPW;
    private CheckBox cbRemUserName, cbRemPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = findViewById(R.id.etUserName);
        etPW = findViewById(R.id.etPW);

        cbRemUserName =  findViewById(R.id.cbRemUserName);
        cbRemPass = findViewById(R.id.cbRemPass);

        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        boolean isRemUserNameChecked = sp.getBoolean("REM_USER", false);
        boolean isRemPassChecked = sp.getBoolean("REM_PASS", false);

        cbRemUserName.setChecked(isRemUserNameChecked);
        cbRemPass.setChecked(isRemPassChecked);

        if (isRemUserNameChecked) {
            String spUserName = sp.getString("USER_NAME", "CREATED");
            etUserName.setText(spUserName);
        }
        if (isRemPassChecked) {
            String spPW = sp.getString("PASSWORD", "CREATED");
            etPW.setText(spPW);
        }


        findViewById(R.id.btnSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin();
            }
        });
    }

    private void processLogin(){
        String userName = etUserName.getText().toString().trim();
        String userPW = etPW.getText().toString().trim();
        String errMsg = "";

        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        String spUserName = sp.getString("USER_NAME", "CREATED");
        String spPW = sp.getString("PASSWORD", "CREATED");

        if (!userName.equals(spUserName)){
            errMsg+="Username not found\n";
        }
        if(!userPW.equals(spPW)){
            errMsg+="Password not found\n";
        }
        if (errMsg.length()>0){
            showErrorDialog(errMsg);
            return;
        }

        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("REM_USER", cbRemUserName.isChecked());
        e.putBoolean("REM_PASS", cbRemPass.isChecked());
        e.commit();

        Intent i = new Intent(LoginActivity.this, ContactListActivity.class);
        startActivity(i);
        finish();
    }

    private void showErrorDialog(String errMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errMsg);
        builder.setTitle("Error");
        builder.setCancelable(true);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }



}