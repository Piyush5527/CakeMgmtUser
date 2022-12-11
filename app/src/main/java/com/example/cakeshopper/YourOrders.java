package com.example.cakeshopper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourOrders extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //my variables
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    static List<OrderModel> orderModelList,compOrderList;
    OrdersAdapter adapter,adapter1;
    RecyclerView orderDetails,compOrderDisplay;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YourOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static YourOrders newInstance(String param1, String param2) {
        YourOrders fragment = new YourOrders();
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
        View view = inflater.inflate(R.layout.fragment_your_orders, container, false);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        orderDetails = view.findViewById(R.id.orderDetails);
        compOrderDisplay=view.findViewById(R.id.compOrderDisplay);

        orderModelList = new ArrayList<>();
        compOrderList=new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderDetails.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        compOrderDisplay.setLayoutManager((linearLayoutManager1));

        db.collection("Orders").whereEqualTo("UserId", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                final String orderDate = snapshot.getData().get("OrderDate").toString();
                                final String cakeName = snapshot.getData().get("CakeName").toString();
                                final String orderTime = snapshot.getData().get("OrderTime").toString();

                                orderModelList.add(new OrderModel(cakeName,orderDate,orderTime,snapshot.getId(),false));
//                                Toast.makeText(getContext(), orderModelList.size() + "", Toast.LENGTH_SHORT).show();
                            }
                            adapter=new OrdersAdapter(orderModelList,getContext());
                            orderDetails.setAdapter(adapter);
                        }
                    }
                });
        db.collection("CompletedOrders").whereEqualTo("UserId",user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot snapshot: task.getResult())
                            {
                                final String orderDate = snapshot.getData().get("OrderDate").toString();
                                final String cakeName = snapshot.getData().get("CakeName").toString();
                                final String orderTime = snapshot.getData().get("OrderTime").toString();

                                compOrderList.add(new OrderModel(cakeName,orderDate,orderTime,snapshot.getId(),true));
                            }
                            adapter1=new OrdersAdapter(compOrderList,getContext());
                            compOrderDisplay.setAdapter(adapter1);
                        }

                    }
                });

        return view;
    }


}