package com.example.zavrsniprojekt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    Button addNew;
    DBHelper mydb;
    ArrayList<Element> array_list;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mydb = new DBHelper(this);
        array_list = mydb.getAllCotacts();

        listView = findViewById(R.id.listView);

        MyAdapter myAdapter = new MyAdapter(this, array_list);
        listView.setAdapter(myAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHelper(this);
        array_list = mydb.getAllCotacts();

        listView = findViewById(R.id.listView);
        addNew = findViewById(R.id.addNewPassword);

        MyAdapter myAdapter = new MyAdapter(this, array_list);
        listView.setAdapter(myAdapter);

        Log.v("Baza", String.valueOf(array_list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ListDetail.class);
                intent.putExtra("id", array_list.get(position).id);
                startActivity(intent);
            }
        });




        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int witch_item = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Jeste sigurni ?")
                        .setMessage("Želite obrisati ovu lozinku")
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mydb.deleteContact(array_list.get(position).id);
                                array_list = mydb.getAllCotacts();
                                MyAdapter ad = new MyAdapter(MainActivity.this, array_list);
                                listView.setAdapter(null);
                                listView.setAdapter(ad);

                            }
                        })
                        .setNegativeButton("Ne", null)
                        .show();
                return true;
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText name = new EditText(context);
                name.setHint("Name of the site");
                layout.addView(name);

                final EditText password = new EditText(context);
                password.setHint("Password");
                layout.addView(password); // Another add method

                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("EntertheText:").setView(layout).setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                mydb.insertContact(name.getText().toString(), password.getText().toString());

                                array_list = mydb.getAllCotacts();
                                MyAdapter ad = new MyAdapter(MainActivity.this, array_list);
                                listView.setAdapter(null);
                                listView.setAdapter(ad);

                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                /*
                                 * User clicked cancel so do some stuff
                                 */
                            }
                        });
                alert.show();
            }
        });


    }

    class MyAdapter extends ArrayAdapter<String>{

        Context context;
        ArrayList<Element> he;

        MyAdapter(Context c, ArrayList h){
            super(c, R.layout.row, R.id.headline, h);
            this.context = c;
            this.he = h;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView headline = row.findViewById(R.id.headline);
            headline.setText(array_list.get(position).name);

            return row;
        }
    }

}