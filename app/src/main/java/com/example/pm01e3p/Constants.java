package com.example.pm01e3p;


public class Constants {
    // nombre base de datos
    public static final String DB_NAME = "My_RECORDS_DB";
    //version de base de datos
    public static final int DB_VERSION = 1;
    //nombre de la tabla
    public static final String TABLE_NAME = "MY_RECORDS_TABLE";
    //columnas/campos de la tabla
    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static  final String C_IMAGE = "IMAGE";
    public static final String C_BIO = "BIO";
    public static final String C_PHONE = "PHONE";
    public static final String C_EMAIL = "EMAIL";
    public static final String C_DOB = "DOB";
    public static final String C_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";
    public static final String C_UPDATED_TIMESTAMP = "UPDATED_TIME_STAMP";
    //Crea la tabla Query
    public static  final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + C_ID+ " INTEGER PRIMARY KEY,"
            + C_NAME+ " TEXT,"
            + C_IMAGE+ " TEXT,"
            + C_BIO+ " TEXT,"
            + C_PHONE+ " TEXT,"
            + C_EMAIL+ " TEXT,"
            + C_DOB+ " TEXT,"
            + C_ADDED_TIMESTAMP+ " TEXT,"
            + C_UPDATED_TIMESTAMP+ " TEXT"
            + ")";
}
