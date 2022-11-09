package com.example.cakeshopper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProductCategoryWise extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category_wise);

        tv=findViewById(R.id.textView);
        tv.setText(getIntent().getStringExtra("CategoryName"));
    }
}