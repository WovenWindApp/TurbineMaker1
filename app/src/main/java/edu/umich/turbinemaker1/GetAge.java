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

public class GetAge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_age);

        // Force landscape mode, remove action bar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // Save user's age to SharedPreferences and send intent to next activity
    public void sendAge(View view) {
        final SharedPreferences userData = this.getSharedPreferences(
                getResources().getString(R.string.userData_pref_key), Context.MODE_PRIVATE);
        String user_age_key = getResources().getString(R.string.user_age_key);

        EditText age_editText = (EditText) findViewById(R.id.age_editText);
        String age_string = age_editText.getText().toString();

        Integer age;
        if (age_string.length() == 0) {
            Toast.makeText(this, R.string.enter_age, Toast.LENGTH_SHORT).show();
        }
        else {
            age = Integer.parseInt(age_string);
            userData.edit().putInt(user_age_key, age).apply();

            Intent intent = new Intent(this, PartListActivity.class);
            startActivity(intent);
        }
    }

}
