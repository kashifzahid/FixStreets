package com.example.fixstreet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class register_incident_2 extends AppCompatActivity {

    private static final String TAG = "RegisterIncident2";
    private Switch swt_stay_anonymous;
    private EditText name, email, phone, dweller;
    private TextView txt_detail_anonymous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_incident_2);

        swt_stay_anonymous = findViewById(R.id.switch_stay_anonymous);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        dweller = findViewById(R.id.dweller);
        txt_detail_anonymous = findViewById(R.id.txt_detail_anonymous);

        swt_stay_anonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Disable EditText
                if(isChecked) {
                    Log.d(TAG, "onCheckedChanged: " + isChecked);
                    name.setHint("");
                    email.setHint("");
                    phone.setHint("");
                    dweller.setHint("");
                    // Disable EditText
                    name.setFocusable(false);
                    email.setFocusable(false);
                    phone.setFocusable(false);
                    dweller.setFocusable(false);

                    name.setBackground(getDrawable(R.drawable.square_edit_text_disable));
                    email.setBackground(getDrawable(R.drawable.square_edit_text_disable));
                    phone.setBackground(getDrawable(R.drawable.square_edit_text_disable));
                    dweller.setBackground(getDrawable(R.drawable.square_edit_text_disable));
                    txt_detail_anonymous.setVisibility(View.VISIBLE);
                }else {
                    // Enable EditText
                    name.setHint("Name *");
                    email.setHint("Email *");
                    phone.setHint("Phone *");
                    dweller.setHint("Dweller *");
                    // Enable EditText
                    name.setFocusable(true);
                    email.setFocusable(true);
                    phone.setFocusable(true);
                    dweller.setFocusable(true);

                    name.setBackground(getDrawable(R.drawable.square_edit_text));
                    email.setBackground(getDrawable(R.drawable.square_edit_text));
                    phone.setBackground(getDrawable(R.drawable.square_edit_text));
                    dweller.setBackground(getDrawable(R.drawable.square_edit_text));

                    txt_detail_anonymous.setVisibility(View.GONE);
                }
            }
        });
    }

    public void back_pressed(View view) {
        finish();
    }
}
