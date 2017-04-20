package com.fedearribas.newfeedsapp;

/**
 * Created by fedea on 20/04/2017.
 */

public class Article {

    private String Title;
    private String Author;
    private String Date;
    private String Link;
    private String ImageUrl;

    public Article(String title, String author, String date, String link, String imageUrl) {
        Title = title;
        Author = author;
        Date = date;
        Link = link;
        ImageUrl = imageUrl;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getDate() {
        return Date;
    }

    public String getLink() {
        return Link;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}
