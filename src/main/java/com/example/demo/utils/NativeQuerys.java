package com.example.demo.utils;

public class NativeQuerys {
    public static final String GET_ALL_DEPARTMENTS = "SELECT * FROM departments";
    public static final String GET_ALL_CATEGORIES = "SELECT * FROM categories WHERE department_id = ?";
}
