package com.example.dwest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;


public class CallList extends AppCompatActivity
{
    private SharedPreferences sharedPrefs;

    private TextView call_list;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);

        init();
    }


    private void init()
    {
        sharedPrefs = this.getSharedPreferences("PrefsFile", 0);
        call_list = (TextView) findViewById(R.id.call_list);

        call_list.setMovementMethod(new ScrollingMovementMethod());

        try
        {
            printNumbers();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void printNumbers() throws JSONException
    {
        JSONArray numbers;

        if(sharedPrefs.contains("numbers"))
        {
            numbers = new JSONArray(sharedPrefs.getString("numbers", ""));
        }
        else
        {
            numbers = new JSONArray();
        }

        int numberSize = numbers.length();

        if(numberSize > 0){

            for(int i = 0; i < numberSize; i++)
            {
                if(!call_list.getText().toString().equals(""))
                {
                    call_list.append("\n");
                    call_list.append(numbers.get(i).toString());
                }
                else
                {
                    call_list.append(numbers.get(i).toString());
                }
            }
        }
    }
}
