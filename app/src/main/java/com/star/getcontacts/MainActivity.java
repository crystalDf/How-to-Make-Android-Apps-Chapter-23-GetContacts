package com.star.getcontacts;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TABLE_NAME = "cpcontacts";

    private static final String AUTHORITY = "com.star.contactprovider.provider";
    private static final String PATH = TABLE_NAME;
    private static final String SCHEME = "content://";
    static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/" + PATH);

    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";

    private Button mShowContactsButton;

    private EditText mIdToDeleteEditText;
    private EditText mIdToFindEditText;
    private EditText mNameToAddEditText;

    private Button mDeleteContactButton;
    private Button mFindContactButton;
    private Button mAddContactButton;

    private TextView mContactsTextView;

    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentResolver = getContentResolver();

        mShowContactsButton = (Button) findViewById(R.id.show_contacts_button);
        mShowContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContacts();
            }
        });

        mIdToDeleteEditText = (EditText) findViewById(R.id.id_to_delete_edit_text);
        mIdToFindEditText = (EditText) findViewById(R.id.id_to_find_edit_text);
        mNameToAddEditText = (EditText) findViewById(R.id.name_to_add_edit_text);

        mDeleteContactButton = (Button) findViewById(R.id.delete_contact_button);
        mDeleteContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        mFindContactButton = (Button) findViewById(R.id.find_contact_button);
        mFindContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findContact();
            }
        });

        mAddContactButton = (Button) findViewById(R.id.add_contact_button);
        mAddContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        mContactsTextView = (TextView) findViewById(R.id.contacts_text_view);
    }

    private void getContacts() {
        String[] projections = new String[] {COLUMN_ID, COLUMN_NAME};

        Cursor cursor = mContentResolver.query(CONTENT_URI, projections, null, null, null);

        String contactList = "";

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));

                contactList += id + " : " + name + "\n";
            }
        }

        mContactsTextView.setText(contactList);

        if (cursor != null) {
            cursor.close();
        }
    }

    private void deleteContact() {
        String idToDelete = mIdToDeleteEditText.getText().toString();

        long rowIdDeleted = mContentResolver.delete(CONTENT_URI, COLUMN_ID + " = ? ",
                new String[] {idToDelete});

        getContacts();
    }

    private void findContact() {
        String idToFind = mIdToFindEditText.getText().toString();

        String[] projections = new String[] {COLUMN_ID, COLUMN_NAME};

        Cursor cursor = mContentResolver.query(CONTENT_URI, projections, COLUMN_ID + " = ? ",
                new String[]{idToFind}, null);

        String contact = "";

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));

                contact += id + " : " + name + "\n";
            } else {
                Toast.makeText(this, "Contact Not Found", Toast.LENGTH_LONG).show();
            }
        }

        mContactsTextView.setText(contact);

        if (cursor != null) {
            cursor.close();
        }
    }

    private void addContact() {
        String nameToAdd = mNameToAddEditText.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, nameToAdd);

        Uri newUri = mContentResolver.insert(CONTENT_URI, contentValues);

        getContacts();
    }
}
