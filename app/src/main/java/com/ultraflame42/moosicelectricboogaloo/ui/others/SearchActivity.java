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
import com.ultraflame42.moosicelectricboogaloo.search.ResultItemType;
import com.ultraflame42.moosicelectricboogaloo.search.SearchNameItem;
import com.ultraflame42.moosicelectricboogaloo.songs.SongPlayer;
import com.ultraflame42.moosicelectricboogaloo.songs.SongRegistry;
import com.ultraflame42.moosicelectricboogaloo.tools.UsefulStuff;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;

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
        adapter = new SearchResultsAdapter(this, data -> {
            Intent intent = new Intent();
            intent.putExtra("itemType", data.type.ordinal());
            intent.putExtra("itemId", data.targetRegId);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        resultsList.setLayoutManager(new LinearLayoutManager(this));
        resultsList.setAdapter(adapter);

        searchQueryInput = findViewById(R.id.searchQueryInput);
        // Detect when the user enter characters
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