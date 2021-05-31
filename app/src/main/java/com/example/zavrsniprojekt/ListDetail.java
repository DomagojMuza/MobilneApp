package com.example.zavrsniprojekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ListDetail extends AppCompatActivity {

    TextView headline;
    EditText password;
    ToggleButton showPassword;
    Button copyPassword;
    Button save;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail2);

        mydb = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        Cursor rs = mydb.getData(extras.getInt("id"));
        rs.moveToFirst();


        headline = findViewById(R.id.detail_headline);
        password = findViewById(R.id.password);
        showPassword = findViewById(R.id.showPassword);
        copyPassword = findViewById(R.id.copyPassword);
        save = findViewById(R.id.save);

        headline.setText(rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME)));
        password.setText(rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PASS)));

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());



        copyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Password", password.getText());
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getApplicationContext(), "Password copied", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydb.updateContact(extras.getInt("id"), headline.getText().toString(), password.getText().toString());
                Toast toast = Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
}