package com.fedearribas.newfeedsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by fedea on 20/04/2017.
 */

public class ArticleLoaderAsync extends AsyncTaskLoader<ArrayList<Article>> {

    private String Url;

    public ArticleLoaderAsync(Context context, String url) {
        super(context);
        Url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Article> loadInBackground() {
        if (TextUtils.isEmpty(Url))
            return null;
        return QueryUtils.fetchArticlesData(Url);
    }
}
