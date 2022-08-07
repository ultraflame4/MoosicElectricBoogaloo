package com.ultraflame42.moosicelectricboogaloo.ui.others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.ultraflame42.moosicelectricboogaloo.R;
import com.ultraflame42.moosicelectricboogaloo.adapters.SearchResults.SearchResultsAdapter;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView resultsList;
    private EditText searchQueryInput;
    private SearchResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UsefulStuff.setupActivity(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        findViewById(R.id.closeSearchBtn).setOnClickListener(view -> finish());

        resultsList = findViewById(R.id.resultsRecyclerView);
        // Create adapter for the Recycler View
        adapter = new SearchResultsAdapter(this, data -> {
            // on item clicked, return the result
            Intent intent = new Intent();
            // Set item clicked type
            intent.putExtra("itemType", data.type.ordinal());
            // Set item clicked id
            intent.putExtra("itemId", data.targetRegId);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        // set the layout manager for the recylcer view
        resultsList.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        resultsList.setAdapter(adapter);

        // Get the EditText view for the search query
        searchQueryInput = findViewById(R.id.searchQueryInput);
        // Detect when the user enter characters
        searchQueryInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //stud methoooood
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // On te xt changed, update query
                adapter.updateQuery(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //stud methoooood
            }
        });
    }

}