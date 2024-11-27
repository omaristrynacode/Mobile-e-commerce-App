package com.example.mobileproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.StringSearch;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mobileproject.Adapter.PopularAdapter;
import com.example.mobileproject.Domain.PopularDomain;
import com.example.mobileproject.R;

import java.util.ArrayList;

public class ViewAllActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    public ArrayList<PopularDomain> items=new ArrayList<>();
    private ArrayList<PopularDomain> filteredItems = new ArrayList<>();
    private SearchView searchView;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("itemBundle");
        items = (ArrayList<PopularDomain>) args.getSerializable("itemList");

        filteredItems.addAll(items); // Initialize filteredItems with the entire list

        searchView = findViewById(R.id.searchView);
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        loadRecyclerView(filteredItems);

        String Query = "";
        Query = intent.getStringExtra("searchQuery");

        searchQuery();
        if(Query.length()>0){
            searchView.setQuery(Query,false);
        }

    }

    private void searchQuery() {
        searchView.setOnClickListener(v -> { searchView.onActionViewExpanded();});

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String query) {
        filteredItems.clear();
        for (PopularDomain item : items) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredItems.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }


    private void loadRecyclerView(ArrayList<PopularDomain> items) {
        recyclerview=findViewById(R.id.viewAll);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        adapter=new PopularAdapter(items);
        recyclerview.setAdapter(adapter);
    }


}