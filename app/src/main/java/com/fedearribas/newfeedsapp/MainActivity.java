package com.fedearribas.newfeedsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>> {

    private final String JSON_URL = "https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.clarin.com%2Frss%2Flo-ultimo%2F";
    private final int ID_LOADER = 1;

    private ListView listView;
    private ArticleAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        adapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article currentArticle = adapter.getItem(position);
                Intent i = new Intent(ACTION_VIEW);
                i.setData(Uri.parse(currentArticle.getLink()));
                startActivity(i);
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(ID_LOADER, null, this);
    }

    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoaderAsync(this, JSON_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Article>> loader) {
        adapter.clear();
    }
}
