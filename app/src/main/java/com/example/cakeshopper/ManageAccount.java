package com.example.cakeshopper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageAccount extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //my variables
    Button logoutButton, updateData;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    EditText name, address, phone, pin, email;
    FirebaseUser fUser;
    LinearLayout layout;

    public ManageAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageAccount newInstance(String param1, String param2) {
        ManageAccount fragment = new ManageAccount();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_manage_account, container, false);
        logoutButton = fragmentView.findViewById(R.id.LogoutButton);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        name = fragmentView.findViewById(R.id.Name);
        address = fragmentView.findViewById(R.id.Address);
        phone = fragmentView.findViewById(R.id.PhoneNo);
        pin = fragmentView.findViewById(R.id.pin);
        updateData = fragmentView.findViewById(R.id.Update);
        email = fragmentView.findViewById(R.id.Email);
        layout = fragmentView.findViewById(R.id.layout);

        setDataOnCreate();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutConfirmDialog();
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFieldEmpty()) {
                    updateFieldData();

                }
            }
        });

        return fragmentView;
    }

    private void updateFieldData() {
        String updatedName, updatedPhone, updatedAddress, updatedPin, updatedEmail;
        updatedName = name.getText().toString();
        updatedPhone = phone.getText().toString();
        updatedAddress = address.getText().toString();
        updatedPin = pin.getText().toString();
        updatedEmail = email.getText().toString();
        Map<String, Object> updatedDataMap = new HashMap<>();
        updatedDataMap.put("Name", updatedName);
        updatedDataMap.put("Email", updatedEmail);
        updatedDataMap.put("Pin", updatedPin);
        updatedDataMap.put("Phone", updatedPhone);
        updatedDataMap.put("Address", updatedAddress);

        db.collection("UsersData").document(fUser.getUid()).set(updatedDataMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar.make(layout, "Data Updated Successfully", Snackbar.LENGTH_SHORT).show();
//                        Snackbar snackbar= Snackbar.make(layout,"",Snackbar.LENGTH_INDEFINITE);
//                        View customSnackView=getLayoutInflater().inflate(R.layout.custom_snack_bar,null);
//                //        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
//                        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
//                        snackbarLayout.addView(customSnackView, 0);
//                        snackbar.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean isFieldEmpty() {
        if (name.getText().length() == 0) {
            name.setError("Field is Empty");
            return false;
        }

        if (phone.getText().length() == 0) {
            phone.setError("Field is Empty");
            return false;
        }
        if (address.getText().length() == 0) {
            address.setError("Field is Empty");
            return false;
        }
        if (pin.getText().length() == 0) {
            pin.setError("Field is Empty");
            return false;
        }
        if (email.getText().length() == 0) {
            email.setError("Field is Empty");
            return false;
        }
        if (pin.getText().length() < 4) {
            pin.setError("Pin can't be less than 4");
            return false;
        }

        return true;
    }

    private void setDataOnCreate() {
        DocumentReference ref = db.collection("UsersData").document(fUser.getUid());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText(documentSnapshot.getData().get("Name").toString());
                pin.setText(documentSnapshot.getData().get("Pin").toString());
                email.setText(documentSnapshot.getData().get("Email").toString());
                try {
                    if (documentSnapshot.getData().get("Phone").toString() != "") {
                        phone.setText(documentSnapshot.getData().get("Phone").toString());
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex+"", Toast.LENGTH_SHORT).show();
                }
                try {
                    if (documentSnapshot.getData().get("Address").toString() != "") {
                        address.setText(documentSnapshot.getData().get("Address").toString());
                    }
                } catch (NullPointerException ex) {

                }

            }
        });
    }

    private void logoutConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert")
                .setMessage("Are you sure to logout?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fAuth.signOut();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("LOGOUT", true);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).setNegativeButton("No", null);
        AlertDialog ad = builder.create();
        ad.show();

    }
}