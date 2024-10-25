package com.example.movies.bean;

import java.util.ArrayList;
import java.util.List;

class GenreRatio {
    private String genre;
    private double ratio;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}

public class Statistics {
    private String username;

    private String nickname;

    private int books_read;

    private int movies_watched;

    private List<GenreRatio> favorite_book_genres;

    private List<GenreRatio> favorite_movie_genres;

    private String most_watched_movie;

    private int most_watched_movie_count;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setBooks_read(int books_read) {
        this.books_read = books_read;
    }

    public int getBooks_read() {
        return this.books_read;
    }

    public void setMovies_watched(int movies_watched) {
        this.movies_watched = movies_watched;
    }

    public int getMovies_watched() {
        return this.movies_watched;
    }

    public void setFavorite_book_genres(List<GenreRatio> favorite_book_genres) {
        this.favorite_book_genres = favorite_book_genres;
    }

    public List<GenreRatio> getFavorite_book_genres() {
        return favorite_book_genres;
    }

    public List<String> getFavoriteBookGenreNames() {
        if (favorite_book_genres == null) {
            return null;
        }
        List<String> genreNames = new ArrayList<>();
        for (GenreRatio gr : favorite_book_genres) {
            genreNames.add(gr.getGenre());
        }
        return genreNames;
    }

    public void setFavorite_movie_genres(List<GenreRatio> favorite_movie_genres) {
        this.favorite_movie_genres = favorite_movie_genres;
    }

    public List<GenreRatio> getFavorite_movie_genres() {
        return favorite_movie_genres;
    }

    public List<String> getFavoriteMovieGenreNames() {
        if (favorite_movie_genres == null) {
            return null;
        }
        List<String> genreNames = new ArrayList<>();
        for (GenreRatio gr : favorite_movie_genres) {
            genreNames.add(gr.getGenre());
        }
        return genreNames;
    }


    public void setMost_watched_movie(String most_watched_movie) {
        this.most_watched_movie = most_watched_movie;
    }

    public String getMost_watched_movie() {
        return this.most_watched_movie;
    }

    public void setMost_watched_movie_count(int most_watched_movie_count) {
        this.most_watched_movie_count = most_watched_movie_count;
    }

    public int getMost_watched_movie_count() {
        return this.most_watched_movie_count;
    }
}
