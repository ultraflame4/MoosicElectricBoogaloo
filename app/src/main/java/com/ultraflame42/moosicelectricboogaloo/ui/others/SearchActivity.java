package com.ultraflame42.moosicelectricboogaloo.ui.others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        adapter = new SearchResultsAdapter(this);
        resultsList.setLayoutManager(new LinearLayoutManager(this));
        resultsList.setAdapter(adapter);

        searchQueryInput = findViewById(R.id.searchQueryInput);
        searchQueryInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.updateQuery(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


}