package com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobfinder.R;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.home.categories.Categories;
import com.example.jobfinder.job_seeker.ui.home_jobs_notification_profile.home.categories.CategoriesAdapter;
import com.example.jobfinder.job_seeker.ui.search_result.SearchResultActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    GridLayoutManager gridLayoutManager;
    ArrayList<Categories> categoriesData;
    EditText searchBar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.categoryRecycleView);


        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setNestedScrollingEnabled(false);

        searchBar = view.findViewById(R.id.searchBar);

        recyclerView.setLayoutManager(gridLayoutManager);



        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {

                    Close_keyboard();
                    PerformSearch();
                    return true;
                }
                return false;
            }
        });

        categoriesData = new ArrayList<>();
        categoriesData.add( new Categories("Banking & Finance", R.drawable.banking));
        categoriesData.add( new Categories("Construction", R.drawable.construction));
        categoriesData.add( new Categories("Coding", R.drawable.coding));
        categoriesData.add( new Categories("Marketing", R.drawable.marketing));
        categoriesData.add( new Categories("Engineering", R.drawable.engineering));
        categoriesData.add( new Categories("Digital Designer & Editor", R.drawable.art_entertainment));
        categoriesData.add( new Categories("Business", R.drawable.buisness));
        categoriesData.add( new Categories("Science", R.drawable.science));
        categoriesData.add( new Categories("Repair & Maintenance", R.drawable.maintenance));
        categoriesData.add( new Categories("Government", R.drawable.governement));
        categoriesData.add( new Categories("Law", R.drawable.law));
        categoriesData.add( new Categories("Sales", R.drawable.sales));
        categoriesData.add( new Categories("Communications", R.drawable.communication));
        categoriesData.add( new Categories("Education", R.drawable.education));

        adapter = new CategoriesAdapter(getContext(),categoriesData);
        recyclerView.setAdapter(adapter);

        return view;

    }


    private void Close_keyboard() {

        View view = getActivity().getCurrentFocus();
        if(view != null )
        {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void PerformSearch()
    {
        String keyWord = searchBar.getText().toString().toLowerCase().trim();
        Intent intent = new Intent(getContext() , SearchResultActivity.class);
        intent.putExtra("search key word" , keyWord);
        startActivity(intent);
    }

}