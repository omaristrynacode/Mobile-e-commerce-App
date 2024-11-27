package com.example.mobileproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mobileproject.Adapter.CartAdapter;
import com.example.mobileproject.Helper.ChangeNumberItemsListener;
import com.example.mobileproject.Helper.DBHelper;
import com.example.mobileproject.Helper.ManagementCart;
import com.example.mobileproject.R;
public class CartActivity extends AppCompatActivity {
private RecyclerView.Adapter adapter;
private RecyclerView recyclerView;
private ManagementCart managementCart;
private TextView totalFeeTxt,taxTxt,deliveryTxt,totalTxt,emptyTxt,locationTxt,paymentTxt;
private double tax;
private ScrollView scrollView;
private ImageView backBtn;
DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagementCart(this);
        initView();
        setVariable();
        calculateCart();
        initList();

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        dbHelper = new DBHelper(this);
        Cursor userCursor = dbHelper.getUsers(username);           //creating a dbhelper instance and loading the data of the username currently stored in shared pref as the logged in user

        if (userCursor.moveToFirst()) {
            int locationColumnIndex = userCursor.getColumnIndex("location");
            if (locationColumnIndex != -1) {
                String location = userCursor.getString(locationColumnIndex);
                locationTxt = findViewById(R.id.locationTxt);
                locationTxt.setText(location.toString());
            }
            int paymentColumnIndex = userCursor.getColumnIndex("payment_method");
            if (paymentColumnIndex != -1) {
                String paymentMethod = userCursor.getString(paymentColumnIndex);
                paymentTxt = findViewById(R.id.paymentTxt);
                paymentTxt.setText(paymentMethod.toString());
            }
        }
    }

    private void initList() {                       // initialize the cart, load its content and calculate the price
        if(managementCart.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
        }
        else {
            emptyTxt.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CartAdapter(managementCart.getListCart(), this, () -> calculateCart());
        recyclerView.setAdapter(adapter);
    }

    private void calculateCart() {      // calculate the total based on each items price and quantity
        double percentTax = 0.11;
        double delivery=10;
        tax = Math.round(managementCart.getTotalFee()*percentTax);

        double total = Math.round(managementCart.getTotalFee()+tax+delivery);
        double itemTotal = Math.round(managementCart.getTotalFee());

        totalFeeTxt.setText("$"+itemTotal);
        taxTxt.setText("$"+tax);
        deliveryTxt.setText("$"+delivery);
        totalTxt.setText("$"+total);
    }

    private void setVariable() {
        backBtn.setOnClickListener(v -> finish());
    }

    private void initView() {
        totalFeeTxt=findViewById(R.id.totalFeeTxt);
        taxTxt =findViewById(R.id.taxTxt);
        deliveryTxt=findViewById(R.id.deliveryTxt);
        totalTxt=findViewById(R.id.totalTxt);
        recyclerView=findViewById(R.id.view2);
        scrollView = findViewById(R.id.scrollView2);
        backBtn= findViewById(R.id.backBtn);
        emptyTxt=findViewById(R.id.emptyTxt);
    }
}