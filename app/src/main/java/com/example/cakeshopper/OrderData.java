package com.example.cakeshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Table;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderData extends AppCompatActivity {

    FirebaseFirestore db;
    ImageView cakeImage;
    TextView cakeName;
    TableLayout tb;
    Button cancelOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_data);

        cakeImage=findViewById(R.id.cakeImage);
        cakeName=findViewById(R.id.cakeName);
        tb=findViewById(R.id.cakeData);
        cancelOrder=findViewById(R.id.CancelOrder);

        Intent intent=getIntent();
        String orderId=intent.getStringExtra("OrderId");

        db=FirebaseFirestore.getInstance();

        db.collection("Orders").document(orderId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        try{
                            Glide.with(getApplication()).load(documentSnapshot.getData().get("ImageUrl").toString()).into(cakeImage);
//                            flag=true;
                        }
                        catch (Exception ex)
                        {
                            db.collection("Cake_Details").document(documentSnapshot.getData().get("CakeId").toString()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Glide.with(getApplication()).load(documentSnapshot.getData().get("ImageUrl").toString()).into(cakeImage);
                                        }
                                    });
                        }

                        cakeName.setText(documentSnapshot.getData().get("CakeName").toString());
                        TableRow row=new TableRow(getApplicationContext());

                        TextView custNameHeader=new TextView(OrderData.this);
                        custNameHeader.setText("Customer:-");
//                        custNameHeader.setGravity(Gravity.LEFT);
                        custNameHeader.setTextColor(Color.BLACK);
                        custNameHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row.addView(custNameHeader);


                        TextView custName=new TextView(OrderData.this);
                        custName.setText(documentSnapshot.getData().get("CustName").toString());
//                        custName.setGravity(Gravity.LEFT);
                        custName.setTextColor(Color.BLACK);
                        custName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row.addView(custName);

                        tb.addView(row);
                        TableRow row1=new TableRow(getApplicationContext());

                        TextView addressHeader=new TextView(OrderData.this);
                        addressHeader.setText("Address:-");
//                        addressHeader.setGravity(Gravity.LEFT);
                        addressHeader.setTextColor(Color.BLACK);
                        addressHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row1.addView(addressHeader);

                        TextView address=new TextView(OrderData.this);
                        address.setText(documentSnapshot.getData().get("CustAddress").toString());
//                        address.setGravity(Gravity.LEFT);
                        address.setTextColor(Color.BLACK);
                        address.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row1.addView(address);

                        tb.addView(row1);
                        TableRow row2=new TableRow(getApplicationContext());

                        TextView priceHeader=new TextView(OrderData.this);
                        priceHeader.setText("Price:-");
//                        priceHeader.setGravity(Gravity.LEFT);
                        priceHeader.setTextColor(Color.BLACK);
                        priceHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row2.addView(priceHeader);

                        TextView price=new TextView(OrderData.this);
                        price.setText(documentSnapshot.getData().get("TotalPrice").toString());
//                        price.setGravity(Gravity.LEFT);
                        price.setTextColor(Color.BLACK);
                        price.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row2.addView(price);

                        tb.addView(row2);
                        TableRow row3=new TableRow(getApplicationContext());

                        TextView orderDateHeader=new TextView(OrderData.this);
                        orderDateHeader.setText("Order Date:-");
//                        orderDateHeader.setGravity(Gravity.LEFT);
                        orderDateHeader.setTextColor(Color.BLACK);
                        orderDateHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row3.addView(orderDateHeader);

                        TextView orderDate=new TextView(OrderData.this);
                        orderDate.setText(documentSnapshot.getData().get("OrderDate").toString());
//                        orderDate.setGravity(Gravity.LEFT);
                        orderDate.setTextColor(Color.BLACK);
                        orderDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row3.addView(orderDate);

                        tb.addView(row3);
                        TableRow row4=new TableRow(getApplicationContext());

                        TextView orderTimeHeader=new TextView(OrderData.this);
                        orderTimeHeader.setText("Order Time:-");
//                        orderTimeHeader.setGravity(Gravity.LEFT);
                        orderTimeHeader.setTextColor(Color.BLACK);
                        orderTimeHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row4.addView(orderTimeHeader);

                        TextView orderTime=new TextView(OrderData.this);
                        orderTime.setText(documentSnapshot.getData().get("OrderTime").toString());
//                        orderTime.setGravity(Gravity.LEFT);
                        orderTime.setTextColor(Color.BLACK);
                        orderTime.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row4.addView(orderTime);

                        tb.addView(row4);

                    }
                });

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Orders").document(orderId).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(OrderData.this, "Order Cancelled Successfully", Toast.LENGTH_SHORT).show();     
                                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OrderData.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}