package com.example.cakeshopper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cakes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cakes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //my variables
    FirebaseFirestore db;
    Spinner category;
    ArrayList<String> categoryList = new ArrayList<>();
    ArrayAdapter<String> aad;
    LinearLayout layout;

    //recycler
    RecyclerView cakePicsRecycler;
    static List<PicModel> picModelList;
    StorageReference storageReference;

    public Cakes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cakes.
     */
    // TODO: Rename and change types and number of parameters
    public static Cakes newInstance(String param1, String param2) {
        Cakes fragment = new Cakes();
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
        View view = inflater.inflate(R.layout.fragment_cakes, container, false);

        cakePicsRecycler = view.findViewById(R.id.cakePicsRecycler);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        cakePicsRecycler.setLayoutManager(linearLayoutManager);
        cakePicsRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        category = view.findViewById(R.id.category);
        db = FirebaseFirestore.getInstance();
        categoryList.add("Cakes");
        categoryList.add("Cookies");
        categoryList.add("Pastries");
        categoryList.add("Chocolates");
        categoryList.add("Cup Cakes");

//        layout=view.findViewById(R.id.layout);
        //custom snackbar

        layout=view.findViewById(R.id.linearlayout5);


        //custom snackbar

        aad = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categoryList);
        category.setAdapter(aad);

        picModelList = new ArrayList<>();
        db.collection("ImageMapper").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot snapshot: task.getResult())
                    {
                        picModelList.add(new PicModel(snapshot.getData().get("ImageUrl").toString()));
                    }
                    picAdapter adapter=new picAdapter(getContext(), picModelList);
                    cakePicsRecycler.setAdapter(adapter);
                }
            }
        });


        return view;
    }
}