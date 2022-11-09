package com.example.cakeshopper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //my variables
    private View view;
    private RecyclerView categoryRecyclerView;

    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelsList;
    private FirebaseFirestore firebaseFirestore;

    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        view= inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView = view.findViewById(R.id.Categories);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);

        categoryModelsList = new ArrayList<CategoryModel>();
        categoryModelsList.add(new CategoryModel(R.mipmap.house,"Home"));
        categoryModelsList.add(new CategoryModel(R.mipmap.cake,"Cake"));
        categoryModelsList.add(new CategoryModel(R.mipmap.cupcake,"Cup Cake"));
        categoryModelsList.add(new CategoryModel(R.mipmap.cookies,"Cookie"));
        categoryModelsList.add(new CategoryModel(R.mipmap.carrotcake,"Pastry"));
        categoryModelsList.add(new CategoryModel(R.mipmap.chocolatebar,"Chocolate"));

        categoryAdapter = new CategoryAdapter(categoryModelsList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        return view;
    }
}