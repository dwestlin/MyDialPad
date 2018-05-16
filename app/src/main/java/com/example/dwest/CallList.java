package com.example.dwest;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CallList extends AppCompatActivity
{

    private static final String TAG = "CallList";
    DatabaseHelper db;

    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);
        mListView = (ListView) findViewById(R.id.listView);
        db = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the listview");

        Cursor data = db.getData();

        ArrayList<String> listData = new ArrayList<>();

        while(data.moveToNext()){
            listData.add(data.getString(0));
            listData.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, listData);
        mListView.setAdapter(adapter);

    }

}
