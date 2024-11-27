package com.example.mobileproject.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mobileproject.Helper.DBHelper;
import com.example.mobileproject.Helper.ManagementCart;
import com.example.mobileproject.R;

public class ProfileActivity extends AppCompatActivity {

    Button logout;
    private ImageView backBtn;
    private ManagementCart managementCart;

    EditText newUsernameTxt, newPassTxt, confirmNewPassTxt, newLocationTxt;

    RadioGroup newPMRB;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        managementCart = new ManagementCart(this);

        Intent intent = getIntent();

        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        dbHelper = new DBHelper(this);

        logout=findViewById(R.id.logoutBtn);
        logout.setOnClickListener(v -> showConfirmationDialog());
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to Log Out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isLoggedIn", false);
                        editor.putString("username", "");
                        editor.apply();

                        managementCart.clearCart();

                        Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


    public void saveBtn(View view) {
        newUsernameTxt = findViewById(R.id.newUsernameTxt);
        newPassTxt = findViewById(R.id.newPassTxt);
        confirmNewPassTxt = findViewById(R.id.confirmNewPassTxt);
        newLocationTxt = findViewById(R.id.newLocationTxt);
        newPMRB = findViewById(R.id.newPMRB);


        int RBId = newPMRB.getCheckedRadioButtonId();
        RadioButton selected = findViewById(RBId);
        String paymentMethod="";
        if (selected!=null){paymentMethod = selected.getText().toString();} else {
            paymentMethod = "";
        }

        String username = newUsernameTxt.getText().toString();
        String password = newPassTxt.getText().toString();
        String confPassword = confirmNewPassTxt.getText().toString();
        String location = newLocationTxt.getText().toString();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String finalPaymentMethod = paymentMethod;
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to make these Changes?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                    String oldUsername = preferences.getString("username", "");

                    if(location.length()>3){ dbHelper.updateLocation(oldUsername,location); }
                    else {if(location.length()>0){Toast.makeText(this, "Invalid Location",Toast.LENGTH_LONG).show();}}

                    if(password.equals(confPassword) && password.length()>7){dbHelper.updatePassword(oldUsername,password);}
                    else {if (password.length()>0){Toast.makeText(this, "Invalid Password",Toast.LENGTH_LONG).show();}}

                    if(finalPaymentMethod.length()>1){dbHelper.updatePaymentMethod(username, finalPaymentMethod);}
                    else {if (finalPaymentMethod.length()>0){Toast.makeText(this, "Invalid Payment Method",Toast.LENGTH_LONG).show();}}

                    if(username.length()>3 && !(username.equals(oldUsername))){dbHelper.updateUsername(oldUsername,username);}
                    else {if (username.length()>0){Toast.makeText(this, "New username cant be the same as the old",Toast.LENGTH_LONG).show();}}


                    Toast.makeText(this,"Changes Saved Successfully", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("No", (dialog, which) -> {}).show();
    }

}