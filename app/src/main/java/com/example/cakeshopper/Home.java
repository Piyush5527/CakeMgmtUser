package com.example.cakeshopper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


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
    private FirebaseFirestore db;

    /////////Banner Slider
    private ViewPager bannerSliderViewPager;
    private List<SliderModel> sliderModelList;
    private int currentPage = 2;
    private Timer timer;
    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME = 3000;
    ////////Banner Slider
    ////////products view
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView = view.findViewById(R.id.Categories);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);

        db=FirebaseFirestore.getInstance();

        categoryModelsList = new ArrayList<CategoryModel>();
        categoryModelsList.add(new CategoryModel(R.mipmap.house, "Home"));
        categoryModelsList.add(new CategoryModel(R.mipmap.cake, "Cake"));
        categoryModelsList.add(new CategoryModel(R.mipmap.cupcake, "Cup Cake"));
        categoryModelsList.add(new CategoryModel(R.mipmap.cookies, "Cookie"));
        categoryModelsList.add(new CategoryModel(R.mipmap.carrotcake, "Pastry"));
        categoryModelsList.add(new CategoryModel(R.mipmap.chocolatebar, "Chocolate"));
        categoryModelsList.add(new CategoryModel(R.mipmap.other, "Customs"));

        categoryAdapter = new CategoryAdapter(categoryModelsList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        ////////banner slider
        bannerSliderViewPager = view.findViewById(R.id.banner_slider_view_pager);

        sliderModelList = new ArrayList<SliderModel>();
        sliderModelList.add(new SliderModel(R.mipmap.banner3));
        sliderModelList.add(new SliderModel(R.mipmap.banner4));

        sliderModelList.add(new SliderModel(R.mipmap.banner1));
        sliderModelList.add(new SliderModel(R.mipmap.banner2));
        sliderModelList.add(new SliderModel(R.mipmap.banner3));
        sliderModelList.add(new SliderModel(R.mipmap.banner4));

        sliderModelList.add(new SliderModel(R.mipmap.banner1));
        sliderModelList.add(new SliderModel(R.mipmap.banner2));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
        bannerSliderViewPager.setAdapter(sliderAdapter);
        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    pageLooper();
                }
            }
        };
        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
        startBannerSlideShow();

        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pageLooper();
                stopBannerSlideShow();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startBannerSlideShow();
                }
                return false;
            }
        });

        ///Grid Product Layout
        TextView gridLayoutTitle = view.findViewById(R.id.grid_product_layout_title);
        //Button gridLayoutViewAllBtn = view.findViewById(R.id.grid_product_layout_view_all_btn);
        GridView gridView = view.findViewById(R.id.grid_product_layout_grid_view);

        horizontalProductScrollModelList = new ArrayList<HorizontalProductScrollModel>();

        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.cake, "Cakes", "Different Flavours Available", "From Rs.350/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.cupcake, "Cup Cakes", "2-4-6 pc Cup Cakes", "From Rs.300/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.cookies, "Cookies", "6pc Different Flavours", "TBA"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.carrotcake, "Pastries", "2 pc Your choice Flavours", "From Rs.150/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.chocolatebar, "Chocolates", "4pc Chocolates Set", "From Rs.100/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.other, "Others", "Custom Made Designs", "Based on Products"));

        gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductScrollModelList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent personal = new Intent(getContext(), ProductCategoryWise.class);
                personal.putExtra("CategoryName", horizontalProductScrollModelList.get(i).getProductTitle());
                startActivity(personal);
            }
        });

        ///Grid Product Layout
        return view;
    }

    private void pageLooper() {
        if (currentPage == sliderModelList.size() - 2) {
            currentPage = 2;
            bannerSliderViewPager.setCurrentItem(currentPage, false);
        }
        if (currentPage == 1) {
            currentPage = sliderModelList.size() - 3;
            bannerSliderViewPager.setCurrentItem(currentPage, false);
        }
    }

    private void startBannerSlideShow() {
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage >= sliderModelList.size()) {
                    currentPage = 1;
                }
                bannerSliderViewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_TIME, PERIOD_TIME);
    }

    private void stopBannerSlideShow() {
        timer.cancel();
    }
    //Banner Slider

}