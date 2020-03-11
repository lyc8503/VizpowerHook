package me.lyc8503.vizpowerhook.activity;

import androidx.appcompat.app.AppCompatActivity;
import me.lyc8503.vizpowerhook.R;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("config", MODE_PRIVATE);

        Log.d(TAG, "onCreate: " + preferences.getAll());

        TextView changeNameTextView = findViewById(R.id.changeName);
        changeNameTextView.setText(preferences.getString("name", ""));
        changeNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                onContentChange();
            }
        });


        CheckBox forceVertical = findViewById(R.id.forceVertical);
        forceVertical.setChecked(preferences.getBoolean("forceVertical", false));
        forceVertical.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onContentChange();
            }
        });

        CheckBox autoRollcall = findViewById(R.id.autoRollcall);
        autoRollcall.setChecked(preferences.getBoolean("autoRollcall", false));
        autoRollcall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onContentChange();
            }
        });


    }

    protected void onContentChange() {
        SharedPreferences.Editor editor = preferences.edit();

        TextView changeNameTextView = (TextView) findViewById(R.id.changeName);
        String name = changeNameTextView.getText().toString();
        editor.putString("name", name);

        CheckBox forceVerticalBox = (CheckBox) findViewById(R.id.forceVertical);
        boolean forceVertical = forceVerticalBox.isChecked();
        editor.putBoolean("forceVertical", forceVertical);

        CheckBox autoRollcallBox = (CheckBox) findViewById(R.id.autoRollcall);
        boolean autoRollcall = autoRollcallBox.isChecked();
        editor.putBoolean("autoRollcall", autoRollcall);

        editor.apply();
    }

}
