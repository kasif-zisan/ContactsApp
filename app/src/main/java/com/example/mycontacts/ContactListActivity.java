package com.example.mycontacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    private ArrayList<Contact> contacts;
    private ListView lvContactList;
    private CustomListAdapter clAdapter;
    private Button btnExit, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        lvContactList = findViewById(R.id.ListView);
        btnAdd = findViewById(R.id.btnAdd);
        btnExit = findViewById(R.id.btnExit);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, ContactFormActivity.class);
                startActivity(intent);
            }
        });

        contacts = new ArrayList<>();
        clAdapter = new CustomListAdapter(this, contacts);
        lvContactList.setAdapter(clAdapter);
        loadContacts();

    }

    private void loadContacts() {
        String q = "SELECT * FROM contacts";
        ContactsDB db = new ContactsDB(this);
        Cursor cursor = db.selectContact(q);
        contacts.clear();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String Name = cursor.getString(0);
                String Email = cursor.getString(1);
                String Phone1 = cursor.getString(2);
                String Phone2 = cursor.getString(3);
                String Photo = cursor.getString(4);

                Contact c = new Contact(Name, Email, Phone1, Phone2, Photo);
                contacts.add(c);
            }
            clAdapter.notifyDataSetInvalidated();
            clAdapter.notifyDataSetChanged();
        }
    }

}