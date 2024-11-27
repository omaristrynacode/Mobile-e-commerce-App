package com.example.mobileproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileproject.Helper.DBHelper;
import com.example.mobileproject.R;

public class LoginActivity extends AppCompatActivity {
    TextView switchMode,textViewPm;
    EditText editEmail, editPass, locationEditText;
    RadioGroup paymentMethodUsed;
    Button validate;
    private DBHelper dbHelper;
    private boolean signInFlag = true; // if true log in mode if false register (save to DB then sign in)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intentRecieved = getIntent();

        dbHelper= new DBHelper(this);

        switchMode = findViewById(R.id.registrationLink);
        editEmail = findViewById(R.id.emailEditText);
        editPass = findViewById(R.id.passwordEditText);
        validate = findViewById(R.id.loginButton);
        textViewPm=findViewById(R.id.textViewPM);
        locationEditText=findViewById(R.id.locationEditText);
        paymentMethodUsed=findViewById(R.id.paymentMethodUsed);

        textViewPm.setVisibility(View.GONE);
        locationEditText.setVisibility(View.GONE);
        paymentMethodUsed.setVisibility(View.GONE);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if(isLoggedIn){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void logIn(View view) {
        if (signInFlag == true) {
            Cursor userCursor = dbHelper.getUsers(editEmail.getText().toString());
            if (userCursor.moveToFirst()) {
                int passwordColumnIndex = userCursor.getColumnIndex("password");
                if (passwordColumnIndex != -1) {
                    String password = userCursor.getString(passwordColumnIndex);
                    if (password.equals(editPass.getText().toString())) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);


                        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", editEmail.getText().toString());
                        editor.apply();

                    } else {
                        Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, "Entered username is not registered", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Cursor userCursor = dbHelper.getUsers(editEmail.getText().toString());
            if (!userCursor.moveToFirst()) {
                if(editPass.getText().toString().length()>7){
                    int RBId = paymentMethodUsed.getCheckedRadioButtonId();
                    RadioButton selected = findViewById(RBId);
                    String paymentMethod="";
                    if (selected!=null){paymentMethod = selected.getText().toString();}

                    if (locationEditText.getText().toString().length()>1 && paymentMethod.length()>1){
                        dbHelper.createUser(editEmail.getText().toString(), editPass.getText().toString(), locationEditText.getText().toString(), paymentMethod);
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);

                        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", editEmail.getText().toString());
                        editor.apply();
                    }
                    else {Toast.makeText(this,"Enter a valid address and Select a payment method", Toast.LENGTH_LONG).show();}
                }
                else {
                    Toast.makeText(this,"password must be longer than 7 characters", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "Entered username is already linked to an account please log in", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void swtchMode(View view) {
        if (validate.getText().toString().equals("Log In")){
            validate.setText("Register");
            switchMode.setText("Already Have an Account? Log In Here");
            signInFlag=false;
            textViewPm.setVisibility(View.VISIBLE);
            locationEditText.setVisibility(View.VISIBLE);
            paymentMethodUsed.setVisibility(View.VISIBLE);
        }
        else {
            validate.setText("Log In");
            switchMode.setText("Don't have an account? Register Here");
            signInFlag=true;
            textViewPm.setVisibility(View.GONE);
            locationEditText.setVisibility(View.GONE);
            paymentMethodUsed.setVisibility(View.GONE);
        }

    }
}