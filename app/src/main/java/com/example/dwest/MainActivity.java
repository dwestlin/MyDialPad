package com.example.dwest;

import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    private ExternalStorageHandler storageHandler;
    private String voiceUrl, storageLocation;
    private EditText editText;
    private String URL;
    private String state = Environment.getExternalStorageState();


    public static final String STORAGE = "com.example.dwest";


    private static final int PERMISSIONS_CALL_PHONE = 2;
    private static final int PERMISSIONS_READ_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageHandler = new ExternalStorageHandler(this);

        if(!hasReadPermission()){
            requestReadPermission();
        }
        else{
            initContent();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)|| Environment.MEDIA_MOUNTED.equals(state)){
                        Toast.makeText(this, getString(R.string.storage_granted), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(this, getString(R.string.storage_external_unavailable), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.storage_denied), Toast.LENGTH_LONG).show();
                }
                initContent();
                break;
            case PERMISSIONS_CALL_PHONE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, R.string.call_granted, Toast.LENGTH_SHORT).show();
                    try {
                        callFunction();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(this, R.string.call_not_granted, Toast.LENGTH_SHORT).show();
                }

                break;
            }

        }
    }

    private void initContent() {
        setContentView(R.layout.activity_main);
        findViewById(R.id.callBtn).setOnClickListener(onClickListener);
        URL = "com.example.dwest.URL";

    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.callBtn:
                    try {
                        checkCallPermission();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void checkCallPermission() throws JSONException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
        {
            callFunction();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_CALL_PHONE);
        }
    }

    private void callFunction() throws SecurityException, JSONException {
        editText = (EditText) findViewById(R.id.editTextNumber);
        String number = editText.getText().toString();

        if (storageHandler.getSaveNumber()) {
            storageHandler.saveNumbers(number);
        }

        number = number.replace("#", Uri.encode("#"));
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private boolean hasReadPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSIONS_READ_EXTERNAL_STORAGE);
    }


    private void checkStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            setDownloadPath();
        }
        else
        {
            Toast.makeText(this, getString(R.string.download_not_allowed), Toast.LENGTH_SHORT).show();
        }
    }


    private void setDownloadPath(){
        voiceUrl = getResources().getString(R.string.voice_url);

        storageLocation = storageHandler.getSoundsPath();

        Intent intent = new Intent(this,DownloadSound.class);

        intent.putExtra(URL, voiceUrl);
        intent.putExtra(STORAGE, storageLocation);

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;

        switch (item.getItemId()) {
            case R.id.call_settings:
                i = new Intent(this, Settings.class);
                startActivity(i);
                return true;
            case R.id.call_list:
                i = new Intent(this, CallList.class);
                startActivity(i);
                return true;
            case R.id.download_sounds:
                checkStoragePermission();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

