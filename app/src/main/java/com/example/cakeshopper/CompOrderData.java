package com.example.cakeshopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CompOrderData extends AppCompatActivity implements PaymentResultListener {
    FirebaseFirestore db;
    ImageView cakeImage;
    TextView cakeName;
    TableLayout tb;
    Button payMoney;
    String amount;
    FirebaseAuth auth;
    FirebaseUser user;
    String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_order_data);

        cakeImage=findViewById(R.id.cakeImage);
        cakeName=findViewById(R.id.cakeName);
        tb=findViewById(R.id.cakeData);
        payMoney=findViewById(R.id.payMoney);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        Intent intent=getIntent();
        orderId=intent.getStringExtra("OrderId");

        db=FirebaseFirestore.getInstance();

        getDataOnLoad();
        payMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("UsersData").document(user.getUid()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                int mainAmount=Math.round(Float.parseFloat(amount)*100);
                                Checkout checkout=new Checkout();
                                checkout.setKeyID("rzp_test_2CJgvKASylip9B");
                                checkout.setImage(R.mipmap.cakeicon);
                                JSONObject object = new JSONObject();

                                try {
                                    object.put("name","Cakes Hopper");
                                    object.put("description", "Order Payment");
                                    object.put("theme.color", "");
                                    object.put("currency", "INR");
                                    object.put("amount", mainAmount);
                                    object.put("prefill.contact", documentSnapshot.getData().get("Phone").toString());
                                    object.put("prefill.email",documentSnapshot.getData().get("Email").toString());
                                    checkout.open(CompOrderData.this, object);
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        });

    }

    private void getDataOnLoad() {
        db.collection("CompletedOrders").document(orderId).get()
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

                        TextView custNameHeader=new TextView(CompOrderData.this);
                        custNameHeader.setText("Customer:-");
//                        custNameHeader.setGravity(Gravity.LEFT);
                        custNameHeader.setTextColor(Color.BLACK);
                        custNameHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row.addView(custNameHeader);


                        TextView custName=new TextView(CompOrderData.this);
                        custName.setText(documentSnapshot.getData().get("CustName").toString());
//                        custName.setGravity(Gravity.LEFT);
                        custName.setTextColor(Color.BLACK);
                        custName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row.addView(custName);

                        tb.addView(row);
                        TableRow row1=new TableRow(getApplicationContext());

                        TextView addressHeader=new TextView(CompOrderData.this);
                        addressHeader.setText("Address:-");
//                        addressHeader.setGravity(Gravity.LEFT);
                        addressHeader.setTextColor(Color.BLACK);
                        addressHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row1.addView(addressHeader);

                        TextView address=new TextView(CompOrderData.this);
                        address.setText(documentSnapshot.getData().get("CustAddress").toString());
//                        address.setGravity(Gravity.LEFT);
                        address.setTextColor(Color.BLACK);
                        address.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row1.addView(address);

                        tb.addView(row1);
                        TableRow row2=new TableRow(getApplicationContext());

                        TextView priceHeader=new TextView(CompOrderData.this);
                        priceHeader.setText("Price:-");
//                        priceHeader.setGravity(Gravity.LEFT);
                        priceHeader.setTextColor(Color.BLACK);
                        priceHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row2.addView(priceHeader);

                        TextView price=new TextView(CompOrderData.this);
                        price.setText(documentSnapshot.getData().get("TotalPrice").toString());
//                        price.setGravity(Gravity.LEFT);
                        price.setTextColor(Color.BLACK);
                        price.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row2.addView(price);

                        tb.addView(row2);
                        TableRow row3=new TableRow(getApplicationContext());

                        TextView orderDateHeader=new TextView(CompOrderData.this);
                        orderDateHeader.setText("Order Date:-");
//                        orderDateHeader.setGravity(Gravity.LEFT);
                        orderDateHeader.setTextColor(Color.BLACK);
                        orderDateHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row3.addView(orderDateHeader);

                        TextView orderDate=new TextView(CompOrderData.this);
                        orderDate.setText(documentSnapshot.getData().get("OrderDate").toString());
//                        orderDate.setGravity(Gravity.LEFT);
                        orderDate.setTextColor(Color.BLACK);
                        orderDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row3.addView(orderDate);

                        tb.addView(row3);
                        TableRow row4=new TableRow(getApplicationContext());

                        TextView orderTimeHeader=new TextView(CompOrderData.this);
                        orderTimeHeader.setText("Order Time:-");
//                        orderTimeHeader.setGravity(Gravity.LEFT);
                        orderTimeHeader.setTextColor(Color.BLACK);
                        orderTimeHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row4.addView(orderTimeHeader);

                        TextView orderTime=new TextView(CompOrderData.this);
                        orderTime.setText(documentSnapshot.getData().get("OrderTime").toString());
//                        orderTime.setGravity(Gravity.LEFT);
                        orderTime.setTextColor(Color.BLACK);
                        orderTime.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row4.addView(orderTime);

                        tb.addView(row4);

                        TableRow row5=new TableRow(getApplicationContext());

                        TextView paymentHeader=new TextView(CompOrderData.this);
                        paymentHeader.setText("Payment Status:-");
//                        orderTimeHeader.setGravity(Gravity.LEFT);
                        paymentHeader.setTextColor(Color.BLACK);
                        paymentHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row5.addView(paymentHeader);

                        TextView payment=new TextView(CompOrderData.this);
//                        payment.setText(documentSnapshot.getData().get("PaymentStatus").toString());
                        if(!(boolean)documentSnapshot.getData().get("PaymentStatus"))
                        {
                            payMoney.setEnabled(true);
                            payment.setTextColor(getResources().getColor(R.color.errorRed2));
                            payment.setText("Not Paid");

                        }
                        else
                        {
                            payMoney.setEnabled(false);
                            payment.setTextColor(getResources().getColor(R.color.successGreen2));
                            payment.setText("Paid");

                        }
//                        orderTime.setGravity(Gravity.LEFT);
                        payment.setTextColor(Color.BLACK);
                        payment.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row5.addView(payment);

                        tb.addView(row5);

                        amount=documentSnapshot.getData().get("TotalPrice").toString();

                    }
                });

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();
        boolean flag=true;
        DocumentReference updateRef=db.collection("CompletedOrders").document(orderId);
        updateRef.update("PaymentStatus",flag).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CompOrderData.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CompOrderData.this, e+"", Toast.LENGTH_SHORT).show();
            }
        });
        tb.removeAllViews();
        getDataOnLoad();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }
}