package com.example.mycontacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactFormActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone1, etPhone2;
    private Button btnCancel, btnSave;
    private ImageView img1;
    private String Name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone1 = findViewById(R.id.etPhone1);
        etPhone2 = findViewById(R.id.etPhone2);

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        img1 = findViewById(R.id.imageView);

        if (getIntent().hasExtra("Name")) {
            Name = getIntent().getStringExtra("Name");
            etName.setText(Name);
        }

        if (getIntent().hasExtra("Email")) {
            String name = getIntent().getStringExtra("Email");
            etEmail.setText(name);
        }

        if (getIntent().hasExtra("Phone1")) {
            String name = getIntent().getStringExtra("Phone1");
            etPhone1.setText(name);
        }

        if (getIntent().hasExtra("Phone2")) {
            String name = getIntent().getStringExtra("Phone2");
            etPhone2.setText(name);
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSave();
            }
        });

    }

    public void processSave() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String phone1 = etPhone1.getText().toString();
        String phone2 = etPhone2.getText().toString();
        String errMsg = "";

        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            errMsg += "Invalid Email Address\n";
        }

        if( (phone1.startsWith(("+880")) && phone1.length() == 14) ||
                (phone1.startsWith(("880")) && phone1.length() == 13) ||
                (phone1.startsWith(("01")) && phone1.length() == 11)){}
        else{
            errMsg += "Invalid Phone (Home) Number\n";
        }

        if( (phone2.startsWith(("+880")) && phone2.length() == 14) ||
                (phone2.startsWith(("880")) && phone2.length() == 13) ||
                (phone2.startsWith(("01")) && phone2.length() == 11)){}
        else{
            errMsg += "Invalid Phone (Office) Number\n";
        }

        if (errMsg.length()>0){
            showErrorDialog(errMsg);
            return;
        }

        // Convert the image to base64
        Bitmap bitmap = ((BitmapDrawable) img1.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        String photo = Base64.encodeToString(byteArray, Base64.DEFAULT);

        ContactsDB dbHelper = new ContactsDB(this);
        Cursor cursor = dbHelper.selectContact(Name);

        if (cursor != null && cursor.getCount() > 0) {
            int updateResult = dbHelper.updateContact(Name, name, email, phone1, phone2, photo);
            if (updateResult > 0) {
                Toast.makeText(this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating contact", Toast.LENGTH_SHORT).show();
            }
        } else {
            long insertResult = dbHelper.insertContact(name, email, phone1, phone2, photo);
            if (insertResult != -1) {
                Toast.makeText(this, "Contact saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error saving contact", Toast.LENGTH_SHORT).show();
            }
        }

        Intent i = new Intent(ContactFormActivity.this, ContactListActivity.class);
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