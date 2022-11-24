package com.example.cakeshopper;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderProduct extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 22;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //my variables
    FirebaseFirestore db;
    ImageView itemImage;
    TextView weight, itemName, price;
    ImageButton addWeight, subWeight;
    Button submitOrder,resetButton;
    EditText orderDate, orderTime;
    static String price500, price1000;
    float orderItemWeight = 1.0f;
    float totalPrice;
    private Uri filePath;
    private boolean imgChange = false;
    FirebaseStorage storage;
    StorageReference reference;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String address, phone, name;
    String itemId;

    public OrderProduct() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderProduct.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderProduct newInstance(String param1, String param2) {
        OrderProduct fragment = new OrderProduct();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_product, container, false);
        db = FirebaseFirestore.getInstance();
        itemImage = view.findViewById(R.id.itemImage);

        itemName = view.findViewById(R.id.cakeName);
        weight = view.findViewById(R.id.weight);
        price = view.findViewById(R.id.itemPrice);
        submitOrder = view.findViewById(R.id.submitOrder);
        addWeight = view.findViewById(R.id.addWeight);

        orderDate = view.findViewById(R.id.order_date1);
        orderTime = view.findViewById(R.id.OrderTime);
        subWeight = view.findViewById(R.id.subWeight);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        resetButton=view.findViewById(R.id.resetDetails);


        itemId = getArguments().getString("ItemId");
        db.collection("Cake_Details").document(itemId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
//                        Toast.makeText(getContext(), documentSnapshot.getData().get("CakeName").toString(), Toast.LENGTH_SHORT).show();
                    String url = String.valueOf(documentSnapshot.getData().get("ImageUrl"));
                    Glide.with(view).load(url).into(itemImage);

                    itemName.setText(String.valueOf(documentSnapshot.getData().get("CakeName")));
                    price.setText(String.valueOf(documentSnapshot.getData().get("Price1kg")));
                    price500 = String.valueOf(documentSnapshot.getData().get("Price500"));
                    price1000 = String.valueOf(documentSnapshot.getData().get("Price1kg"));
                } catch (Exception ex) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        db.collection("UsersData").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    name = documentSnapshot.getData().get("Name").toString();
                    address = documentSnapshot.getData().get("Address").toString();
                    phone = documentSnapshot.getData().get("Phone").toString();
                } catch (NullPointerException ex) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
                    adb.setTitle("Attention");
                    adb.setMessage("Address & Phone number not provided please Enter that before continuation");
                    adb.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            ManageAccount manageAccount = new ManageAccount();
                            activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, manageAccount).commit();
                        }
                    });
                    adb.setCancelable(false);
                    AlertDialog ad = adb.create();
                    ad.show();
                }
            }
        });

        addWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Float.parseFloat(weight.getText().toString()) == 3 || Float.parseFloat(weight.getText().toString()) > 3) {
                    addWeight.setVisibility(View.INVISIBLE);
                } else {
                    if (addWeight.getVisibility() == View.INVISIBLE) {
                        addWeight.setVisibility(View.VISIBLE);
                    }
                    if (subWeight.getVisibility() == View.INVISIBLE) {
                        subWeight.setVisibility(View.VISIBLE);
                    }
                    orderItemWeight = Float.parseFloat(weight.getText().toString()) + 0.5f;
                    weight.setText(String.valueOf(orderItemWeight));
                    if (orderItemWeight % 1.0f == 0) {
                        totalPrice = Float.parseFloat(weight.getText().toString()) * Float.parseFloat(price1000);
                        price.setText(String.valueOf(totalPrice));
                    } else if (orderItemWeight == 0.5f) {
                        price.setText(price500);
                    } else {
                        totalPrice = Float.parseFloat(weight.getText().toString()) * Float.parseFloat(price1000);
                        price.setText(String.valueOf(totalPrice));
                    }
                }
            }
        });
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        orderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(OrderProduct.this, 0);

                datePicker.show(getFragmentManager(), "date picker");
            }
        });
        tpd = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this, hour, min, false);

        orderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpd.show();
            }
        });


        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Attention");
                adb.setMessage("Want to Upload new design for Cake. Then additional charges may apply!");
                adb.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectImage();
                    }
                });
                adb.setNegativeButton("Cancel", null);
                adb.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setData(view);
                        weight.setText("1");
                        imgChange = false;
                    }
                });
                AlertDialog ad = adb.create();
                ad.show();
            }
        });
        subWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Float.parseFloat(weight.getText().toString()) == 0.5 || Float.parseFloat(weight.getText().toString()) < 0.5) {
                    subWeight.setVisibility(View.INVISIBLE);
                } else {
                    if (subWeight.getVisibility() == View.INVISIBLE) {
                        subWeight.setVisibility(View.VISIBLE);
                    }
                    if (addWeight.getVisibility() == View.INVISIBLE) {
                        addWeight.setVisibility(View.VISIBLE);
                    }

                    orderItemWeight = Float.parseFloat(weight.getText().toString()) - 0.5f;
                    weight.setText(String.valueOf(orderItemWeight));
                    if (orderItemWeight % 1.0f == 0) {
                        totalPrice = Float.parseFloat(weight.getText().toString()) * Float.parseFloat(price1000);
                        price.setText(String.valueOf(totalPrice));
                    } else if (orderItemWeight == 0.5f) {
                        price.setText(price500);
                    } else {
                        totalPrice = Float.parseFloat(weight.getText().toString()) * Float.parseFloat(price1000);
                        price.setText(String.valueOf(totalPrice));
                    }
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(view);
                orderDate.setText("");
                orderTime.setText("");
                weight.setText("1");
                imgChange = false;
            }
        });
        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
//
                    if (isAllFieldFilled()) {
                        Map<String, Object> orderDetails = new HashMap<>();
                        if (imgChange) {
                            ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setTitle("Uploading...");
                            progressDialog.show();
                            String fileNameWithUUID = UUID.randomUUID().toString();
                            StorageReference ref = reference.child("cake images/" + fileNameWithUUID);
                            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String generatedFilePath = uri.toString();
                                            String itemWeight = weight.getText().toString();
                                            orderDetails.put("Weight", itemWeight);
                                            String itemPrice = price.getText().toString();
                                            orderDetails.put("TotalPrice", itemPrice);
                                            orderDetails.put("CustName", name);
                                            orderDetails.put("CustAddress", address);
                                            orderDetails.put("CustPhone", phone);
                                            orderDetails.put("OrderDate", orderDate.getText().toString());
                                            orderDetails.put("OrderTime", orderTime.getText().toString());
                                            orderDetails.put("ExtraPrice", "0");
                                            orderDetails.put("CakeId", itemId);
                                            orderDetails.put("ImageUrl",generatedFilePath);
                                            db.collection("Orders").document().set(orderDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getContext(), "New Order Send", Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
                            ;

                        } else {
                            String itemWeight = weight.getText().toString();
                            orderDetails.put("Weight", itemWeight);
                            String itemPrice = price.getText().toString();
                            orderDetails.put("TotalPrice", itemPrice);
                            orderDetails.put("CustName", name);
                            orderDetails.put("CustAddress", address);
                            orderDetails.put("CustPhone", phone);
                            orderDetails.put("OrderDate", orderDate.getText().toString());
                            orderDetails.put("OrderTime", orderTime.getText().toString());
                            orderDetails.put("ExtraPrice", "0");
                            orderDetails.put("CakeId", itemId);
                            db.collection("Orders").document().set(orderDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "New Order Send", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                                }
                            });
//                            Toast.makeText(getContext(),orderTime , Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Intent intent = new Intent(getActivity(), LoginPage.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private boolean isAllFieldFilled() {
        if (orderDate.getText().length() == 0) {
            orderDate.setError("Select Date");
            return false;
        }
        if (orderTime.getText().length() == 0) {
            orderTime.setError("Select Date");
            return false;
        }
        return true;
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                itemImage.setImageBitmap(bitmap);
                imgChange = true;
            } catch (Exception ex) {
                Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(View view) {
        String itemId = getArguments().getString("ItemId");
        db.collection("Cake_Details").document(itemId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
//                        Toast.makeText(getContext(), documentSnapshot.getData().get("CakeName").toString(), Toast.LENGTH_SHORT).show();
                    String url = String.valueOf(documentSnapshot.getData().get("ImageUrl"));
                    Glide.with(view).load(url).into(itemImage);

                    itemName.setText(String.valueOf(documentSnapshot.getData().get("CakeName")));
                    price.setText(String.valueOf(documentSnapshot.getData().get("Price1kg")));
                    price500 = String.valueOf(documentSnapshot.getData().get("Price500"));
                    price1000 = String.valueOf(documentSnapshot.getData().get("Price1kg"));
                } catch (Exception ex) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        month += 1;
        String date = year + "-" + month + "-" + dayOfMonth;
        orderDate.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        orderTime.setText(time);
    }
}