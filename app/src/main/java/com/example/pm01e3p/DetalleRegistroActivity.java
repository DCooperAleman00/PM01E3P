package com.example.pm01e3p;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetalleRegistroActivity extends AppCompatActivity {

    private ImageView profileIv3;
    private TextView bioTv, nameTv, phoneTv, emailTv, dobTv, addedTimeTv, updatedTimeTv;

    private ActionBar actionBar;


    private String recordID;

    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_registro);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Detalle del Registro");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");

        dbHelper = new MyDbHelper(this);

        profileIv3 = findViewById(R.id.profileIv3);
        bioTv = findViewById(R.id.bioTv);
        nameTv = findViewById(R.id.nameTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        dobTv = findViewById(R.id.dobTv);
        addedTimeTv = findViewById(R.id.addedTimeTv);
        updatedTimeTv = findViewById(R.id.updateTimeTv);

        showRecordDetails();
    }

    private void showRecordDetails() {

        String selectQuery = " SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ID +" =\""+ recordID+"\"";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {

                //Obtener datos
                String id = ""+ cursor.getInt(cursor.getColumnIndex(Constants.C_ID));
                String name = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_NAME));
                String image = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE));
                String bio = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_BIO));
                String phone = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_PHONE));
                String email = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_EMAIL));
                String dob = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_DOB));
                String timestampAdded = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP));
                String timestampUpdated = ""+ cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP));

                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(timestampAdded));
                String timeAdded = ""+ DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar1);

                Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
                calendar2.setTimeInMillis(Long.parseLong(timestampUpdated));
                String timeupdated = ""+ DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar2);

                nameTv.setText(name);
                bioTv.setText(bio);
                phoneTv.setText(phone);
                emailTv.setText(email);
                dobTv.setText(dob);
                addedTimeTv.setText(timeAdded);
                updatedTimeTv.setText(timeupdated);


                if (image.equals("null")){
                    profileIv3.setImageResource(R.drawable.ic_person_black);
                }
                else {
                    // tener imagen en el registro
                    profileIv3.setImageURI(Uri.parse(image));
                }


            }while(cursor.moveToNext());
        }
        db.close();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
