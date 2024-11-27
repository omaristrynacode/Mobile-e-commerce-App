package com.example.mobileproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobileproject.Domain.PopularDomain;
import com.example.mobileproject.Helper.ManagementCart;
import com.example.mobileproject.R;

public class DetailActivity extends AppCompatActivity {

    private Button addToCartBtn;
    private TextView titleTxt,feeTxt,descriptionTxt,reviewTxt,scoreTxt;
    private ImageView picItem,backBtn;
    private PopularDomain object;
    private int numberOrder=1;
    private ManagementCart managementCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        managementCart = new ManagementCart(this);

        initView();
        getBundle();
    }

    private void getBundle() {
        object=(PopularDomain) getIntent().getSerializableExtra("object");
        int drawableResourceId=this.getResources().getIdentifier(object.getPicUrl(),"drawable",this.getPackageName());

        Glide.with(this).load(drawableResourceId).into(picItem);
        titleTxt.setText(object.getTitle());
        feeTxt.setText("$"+ object.getPrice());
        descriptionTxt.setText(object.getDescription());
        reviewTxt.setText(object.getReview()+"");
        scoreTxt.setText(object.getScore()+"");

        addToCartBtn.setOnClickListener(v -> {
            object.setNumberInCart(numberOrder);
            managementCart.insertFood(object);
        });
        backBtn.setOnClickListener(v -> finish());
    }

    private void initView() {
        addToCartBtn=findViewById(R.id.addToCartBtn);
        titleTxt=findViewById(R.id.titleTxt);
        feeTxt=findViewById(R.id.priceTxt);
        descriptionTxt=findViewById(R.id.descriptionTxt);
        picItem=findViewById(R.id.itemPic);
        reviewTxt=findViewById(R.id.reviewTxt);
        scoreTxt=findViewById(R.id.scoreTxt);
        backBtn=findViewById(R.id.backBtn);
    }
}