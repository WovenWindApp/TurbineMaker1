package edu.umich.turbinemaker1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class GetName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);

        // Force landscape mode, remove action bar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // Save user's name to SharedPreferences and send intent to next activity
    public void sendName(View view) {
        final SharedPreferences userData = this.getSharedPreferences(
                getResources().getString(R.string.userData_pref_key), Context.MODE_PRIVATE);
        String user_name_key = getResources().getString(R.string.user_name_key);

        EditText name_editText = (EditText) findViewById(R.id.name_editText);
        String name = name_editText.getText().toString();

        if (name.length() == 0) {
            Toast.makeText(this, R.string.enter_name, Toast.LENGTH_SHORT).show();
        }
        else {
            userData.edit().putString(user_name_key, name).apply();

            Intent intent = new Intent(this, GetAge.class);
            startActivity(intent);
        }
    }
}
