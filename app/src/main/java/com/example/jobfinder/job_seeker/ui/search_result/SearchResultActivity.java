package com.example.jobfinder.job_seeker.ui.search_result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;


import com.example.jobfinder.R;
import com.example.jobfinder.CompanyList;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {


    public static Activity searchResult;
    public static boolean isSearchResultOpen = false;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<CompanyList> searchResultList;
    EditText searchBar;

    String category;
    String searchKeyWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        searchResult = this;
        isSearchResultOpen = true;


        searchBar = findViewById(R.id.searchBarSearchResult);
        recyclerView = findViewById(R.id.search_result_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        category = getIntent().getStringExtra("selected category");
        searchKeyWord = getIntent().getStringExtra("search key word");
        searchResultList = new ArrayList<>();

        if(searchKeyWord == null)
        {
            searchKeyWord = "0000";
        }

        CollectionReference reference = FirebaseFirestore.getInstance().collection("user");

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for(DocumentChange doc :value.getDocumentChanges())
                {


                    if(doc.getDocument().get("user type").equals("company"))
                    {
                        doc.getDocument().getReference().collection("post").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for(DocumentChange doc2 :value.getDocumentChanges())
                                {
                                    HashMap<String , Object> postInfo = (HashMap<String , Object>)doc2.getDocument().getData();

                                    if(postInfo.get("job category").toString().equals(category)
                                            ||postInfo.get("title").toString().toLowerCase().contains(searchKeyWord)
                                            ||postInfo.get("job description").toString().toLowerCase().contains(searchKeyWord)
                                            ||postInfo.get("job details").toString().toLowerCase().contains(searchKeyWord)
                                            ||postInfo.get("skills required").toString().toLowerCase().contains(searchKeyWord)
                                            ||doc.getDocument().get("name").toString().toLowerCase().contains(searchKeyWord)
                                            ||doc.getDocument().get("location").toString().toLowerCase().contains(searchKeyWord))
                                    {
                                        if(doc.getDocument().get("image url") != null)
                                        {
                                            searchResultList.add(new CompanyList(
                                                    doc.getDocument().get("image url").toString()
                                                    ,postInfo.get("title").toString()
                                                    ,postInfo.get("job description").toString()
                                                    ,doc.getDocument().getId().toString(),doc2.getDocument().getId().toString()));
                                        }
                                        else{
                                            searchResultList.add(new CompanyList(
                                                    "https://firebasestorage.googleapis.com/v0/b/fir-learn-1ba58.appspot.com/o/images%2Fproject_icon_concept_only_logo.png?alt=media&token=8a9f2b70-75b8-4829-a49e-313f51749bb8"
                                                    ,postInfo.get("title").toString()
                                                    ,postInfo.get("job description").toString()
                                                    ,doc.getDocument().getId().toString(),doc2.getDocument().getId().toString()));
                                        }
                                        adapter = new SearchResultAdapter(SearchResultActivity.this,searchResultList);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            }
                        });
                    }

                }
            }
            });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH)
                {

                    Close_keyboard();
                    PerformSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void Close_keyboard() {

        View view = getCurrentFocus();
        if(view != null )
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void PerformSearch()
    {
        String keyWord = searchBar.getText().toString().toLowerCase().trim();
        Intent intent = new Intent(SearchResultActivity.this , SearchResultActivity.class);
        intent.putExtra("search key word" , keyWord);
        startActivity(intent);
        finish();
    }
}