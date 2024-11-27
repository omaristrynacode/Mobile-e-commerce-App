package com.example.mobileproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileproject.Adapter.PopularAdapter;
import com.example.mobileproject.Domain.PopularDomain;
import com.example.mobileproject.Helper.DBHelper;
import com.example.mobileproject.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

private RecyclerView.Adapter adapter;
private RecyclerView recyclerview;
TextView usernameTxt;
public ArrayList<PopularDomain> items=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        usernameTxt=findViewById(R.id.userNameTxt);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        usernameTxt.setText(username);

        populateArrayList();
        initRecyclerView();
        bottomNavigation();

        SearchView searchView = findViewById(R.id.searchViewToo);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleQuery(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void handleQuery(String query) {
        Intent intent = new Intent(MainActivity.this, ViewAllActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("itemList",(Serializable)items);
        intent.putExtra("itemBundle",args);
        intent.putExtra("searchQuery",query);
        startActivity(intent);
    }


    private void bottomNavigation() {
        LinearLayout homeBtn=findViewById(R.id.homeBtn);
        LinearLayout cartBtn=findViewById(R.id.cartBtn);
        LinearLayout profileBtn=findViewById(R.id.profileBtn);
        LinearLayout viewAllBtn=findViewById(R.id.viewAllBtn);

        TextView viewAllTxtBtn=findViewById(R.id.viewAllTxtBtn);

        homeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this ,MainActivity.class)));

        cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

        profileBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        viewAllBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewAllActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("itemList",(Serializable)items);
            intent.putExtra("itemBundle",args);
            intent.putExtra("searchQuery","");
            startActivity(intent);
        });
        viewAllTxtBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewAllActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("itemList",(Serializable)items);
            intent.putExtra("itemBundle",args);
            intent.putExtra("searchQuery","");
            startActivity(intent);
        });

    }

    private void populateArrayList() {
        DBHelper dbHelper = new DBHelper(this);

        // Check if the database is empty before populating
        Cursor cursor = dbHelper.getAllProducts();
        if (cursor.getCount() == 0) {
            dbHelper.populateDBItems();
        }
        cursor.close(); // Close the cursor to free resources

        // Fetch products from the database
        cursor = dbHelper.getAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");
            int picUrlIndex = cursor.getColumnIndex("picUrl");
            int reviewIndex = cursor.getColumnIndex("review");
            int scoreIndex = cursor.getColumnIndex("score");
            int priceIndex = cursor.getColumnIndex("price");

            do {
                String title = cursor.getString(titleIndex);
                String description = cursor.getString(descriptionIndex);
                String picUrl = cursor.getString(picUrlIndex);
                int review = cursor.getInt(reviewIndex);
                double score = cursor.getDouble(scoreIndex);
                double price = cursor.getDouble(priceIndex);

                items.add(new PopularDomain(title, description, picUrl, review, score, price));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void initRecyclerView() {

        ArrayList<PopularDomain> filteredItems = new ArrayList<>();

        for (PopularDomain item : items) {
            if (item.getReview() > 10) {
                filteredItems.add(item);
            }
        }
        recyclerview=findViewById(R.id.view1);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        adapter=new PopularAdapter(filteredItems);
        recyclerview.setAdapter(adapter);
    }
}