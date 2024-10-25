package com.example.movies.bean;

import java.io.Serializable;
import java.util.List;

public class Results implements Serializable {
    private String title;

    private int id;

    private List<Genres> genres;

    private int count;

    private String year;

    private String rating;

    private String votes;

    private String poster_url;

    private String director;

    private String writer;

    private String cast;

    private String country;

    private String language;

    private String release_date;

    private String runtime;

    private String summary;

    private List<Celebrities> celebrities;

    private List<Photos> photos;

    private List<Comments> comments;

    //小说
    private String author;
    private String publisher;
    private String producer;
    private String pub_year;
    private int pages;
    private String price;
    private String author_intro;
    private List<Tag> tags;


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }

    public List<Genres> getGenres() {
        return this.genres;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return this.year;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return this.rating;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getVotes() {
        return this.votes;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getPoster_url() {
        return this.poster_url;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDirector() {
        return this.director;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriter() {
        return this.writer;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCast() {
        return this.cast;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRelease_date() {
        return this.release_date;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getRuntime() {
        return this.runtime;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setCelebrities(List<Celebrities> celebrities) {
        this.celebrities = celebrities;
    }

    public List<Celebrities> getCelebrities() {
        return this.celebrities;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }

    public List<Photos> getPhotos() {
        return this.photos;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<Comments> getComments() {
        return this.comments;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getPub_year() {
        return pub_year;
    }

    public void setPub_year(String pub_year) {
        this.pub_year = pub_year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
