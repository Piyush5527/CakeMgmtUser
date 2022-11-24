package com.example.cakeshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //my variables
    FirebaseFirestore db;
    FirebaseStorage storage;
    List<ItemModel> itemModelList;
    ItemsAdapter adapter;
    RecyclerView recyclerView;
    LinearLayout layout;

    public ItemsPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsPage.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsPage newInstance(String param1, String param2) {
        ItemsPage fragment = new ItemsPage();
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
        View view= inflater.inflate(R.layout.fragment_items_page, container, false);
        db=FirebaseFirestore.getInstance();
        itemModelList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.itemsRecycler);
        layout=view.findViewById(R.id.layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        String category=getArguments().getString("Category");
//        Toast.makeText(getContext(),category , Toast.LENGTH_SHORT).show();

        setItemsOnLoad(category);
        return view;
    }

    private void setItemsOnLoad(String category) {
        db.collection("Cake_Details").whereEqualTo("Category",category).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            int cnt=0;
                            for(QueryDocumentSnapshot snapshot:task.getResult())
                            {
                                cnt++;
//                                String imageName="hello";
                                String itemName=String.valueOf(snapshot.getData().get("CakeName"));
                                String itemPrice=String.valueOf(snapshot.getData().get("Price500")+"/-");
                                String itemFlavour=String.valueOf(snapshot.getData().get("CakeFlavour"));
                                String iconUrl=String.valueOf(snapshot.getData().get("ImageUrl"));
                                String id=snapshot.getId();
                                itemModelList.add(new ItemModel(itemName,itemPrice,itemFlavour,iconUrl,id));
                            }
                            if(cnt==0)
                            {
                                Snackbar.make(layout,"No Items in this category",Snackbar.LENGTH_LONG).show();
                            }
                            adapter=new ItemsAdapter(getContext(),itemModelList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });


    }
}