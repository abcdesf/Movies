package com.example.movies.utils;

public class PageCalculator {
    public static int calculateTotalPages(int totalCount, int itemsPerPage) {
        return totalCount % itemsPerPage == 0? totalCount / itemsPerPage : totalCount / itemsPerPage + 1;
    }

    public static void main(String[] args) {
        int totalCount = 3706;
        int itemsPerPage = 20;
        int totalPages = calculateTotalPages(totalCount, itemsPerPage);
        System.out.println("总页数为：" + totalPages);
    }
}