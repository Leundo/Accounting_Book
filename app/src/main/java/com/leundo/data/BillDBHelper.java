package com.leundo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/*
 * 用于创建和更新数据库
 */

public class BillDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "BillDBHelper";

    private static final String DBNAME = "mainDB.db";
    private static final int CURRENT_VERSION = 8;

    public BillDBHelper(@Nullable Context context) {
        super(context, DBNAME, null, CURRENT_VERSION);
    }
    // 数据库第一次被创建时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: 数据表准备创建");
        // 存储bill的表
        String sql1 = "create table " + BillContract.MAIN_TABLE_NAME +
                "(" + BillContract.MAIN_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.MAIN_COLUMN_NAME_ACCOUNT + " varchar(50) not null," +
                BillContract.MAIN_COLUMN_NAME_CATEGORY + " varchar(10) not null," +
                BillContract.MAIN_COLUMN_NAME_TYPE_1 + " varchar(20) not null," +
                BillContract.MAIN_COLUMN_NAME_TYPE_2 + " varchar(20) not null," +
                BillContract.MAIN_COLUMN_NAME_AMOUNT + " double not null," +
                BillContract.MAIN_COLUMN_NAME_TIME + " integer not null," +
                BillContract.MAIN_COLUMN_NAME_NUMBER + " varchar(20)," +
                BillContract.MAIN_COLUMN_NAME_FIRM + " varchar(20)," +
                BillContract.MAIN_COLUMN_NAME_ITEM + " varchar(20)," +
                BillContract.MAIN_COLUMN_NAME_REMARK + " varchar(50)," +
                BillContract.MAIN_COLUMN_NAME_CURRENCY + " varchar(10)" +
                ")";
        db.execSQL(sql1);

        // 存储type的表
        String sql2 = "create table " + BillContract.TYPE_TABLE_NAME +
                "(" + BillContract.TYPE_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.TYPE_COLUMN_NAME_LEVEL + " integer not null," +
                BillContract.TYPE_COLUMN_NAME_TITLE + " varchar(10) not null," +
                BillContract.TYPE_COLUMN_NAME_FATHER + " varchar(10)" +
                ")";
        db.execSQL(sql2);

        // 存储account的表
        String sql3 = "create table " + BillContract.ACCOUNT_TABLE_NAME +
                "(" + BillContract.ACCOUNT_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.ACCOUNT_COLUMN_NAME_TITLE + " varchar(20) not null" +
                ")";
        db.execSQL(sql3);

        // 存储number的表
        String sql4 = "create table " + BillContract.NUMBER_TABLE_NAME +
                "(" + BillContract.NUMBER_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.NUMBER_COLUMN_NAME_TITLE + " varchar(10) not null" +
                ")";
        db.execSQL(sql4);

        // 存储firm的表
        String sql5 = "create table " + BillContract.FIRM_TABLE_NAME +
                "(" + BillContract.FIRM_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.FIRM_COLUMN_NAME_TITLE + " varchar(10) not null" +
                ")";
        db.execSQL(sql5);

        // 存储type icon的表
        String sql6 = "create table " + BillContract.TYPE_ICON_TABLE_NAME +
                "(" + BillContract.TYPE_ICON_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.TYPE_ICON_COLUMN_NAME_TITLE + " varchar(10) not null," +
                BillContract.TYPE_ICON_COLUMN_NAME_ICON + " integer not null" +
                ")";
        db.execSQL(sql6);

        // 存储数字的表
        String sql7 = "create table " + BillContract.NUM_TABLE_NAME +
                "(" + BillContract.NUM_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.NUM_COLUMN_NAME_TITLE + " varchar(10) not null," +
                BillContract.NUM_COLUMN_NAME_VALUE + " double not null" +
                ")";
        db.execSQL(sql7);

        // 存储user信息的表
        String sql8 = "create table " + BillContract.USER_TABLE_NAME +
                "(" + BillContract.USER_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.USER_COLUMN_NAME_TITLE + " varchar(10) not null," +
                BillContract.USER_COLUMN_NAME_VALUE + " varchar(65) not null" +
                ")";
        db.execSQL(sql8);

        Log.d(TAG, "onCreate: 数据表创建");
    }

    // 数据库更新时调用
    // 会把原来的数据删掉
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + BillContract.MAIN_TABLE_NAME);
        db.execSQL("DROP TABLE " + BillContract.TYPE_TABLE_NAME);
        db.execSQL("DROP TABLE " + BillContract.ACCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE " + BillContract.NUMBER_TABLE_NAME);
        db.execSQL("DROP TABLE " + BillContract.FIRM_TABLE_NAME);
        db.execSQL("DROP TABLE " + BillContract.TYPE_ICON_TABLE_NAME);
        db.execSQL("DROP TABLE " + BillContract.NUM_TABLE_NAME);
        db.execSQL("DROP TABLE " + BillContract.USER_TABLE_NAME);

        // 存储Bill的表
        String sql1 = "create table " + BillContract.MAIN_TABLE_NAME +
                "(" + BillContract.MAIN_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.MAIN_COLUMN_NAME_ACCOUNT + " varchar(50) not null," +
                BillContract.MAIN_COLUMN_NAME_CATEGORY + " varchar(10) not null," +
                BillContract.MAIN_COLUMN_NAME_TYPE_1 + " varchar(20) not null," +
                BillContract.MAIN_COLUMN_NAME_TYPE_2 + " varchar(20) not null," +
                BillContract.MAIN_COLUMN_NAME_AMOUNT + " double not null," +
                BillContract.MAIN_COLUMN_NAME_TIME + " integer not null," +
                BillContract.MAIN_COLUMN_NAME_NUMBER + " varchar(20)," +
                BillContract.MAIN_COLUMN_NAME_FIRM + " varchar(20)," +
                BillContract.MAIN_COLUMN_NAME_ITEM + " varchar(20)," +
                BillContract.MAIN_COLUMN_NAME_REMARK + " varchar(50)," +
                BillContract.MAIN_COLUMN_NAME_CURRENCY + " varchar(10)" +
                ")";
        db.execSQL(sql1);

        // 存储type的表
        String sql2 = "create table " + BillContract.TYPE_TABLE_NAME +
                "(" + BillContract.TYPE_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.TYPE_COLUMN_NAME_LEVEL + " integer not null," +
                BillContract.TYPE_COLUMN_NAME_TITLE + " varchar(10) not null," +
                BillContract.TYPE_COLUMN_NAME_FATHER + " varchar(10)" +
                ")";
        db.execSQL(sql2);

        // 存储account的表
        String sql3 = "create table " + BillContract.ACCOUNT_TABLE_NAME +
                "(" + BillContract.ACCOUNT_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.ACCOUNT_COLUMN_NAME_TITLE + " varchar(20) not null" +
                ")";
        db.execSQL(sql3);

        // 存储number的表
        String sql4 = "create table " + BillContract.NUMBER_TABLE_NAME +
                "(" + BillContract.NUMBER_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.NUMBER_COLUMN_NAME_TITLE + " varchar(10) not null" +
                ")";
        db.execSQL(sql4);

        // 存储firm的表
        String sql5 = "create table " + BillContract.FIRM_TABLE_NAME +
                "(" + BillContract.FIRM_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.FIRM_COLUMN_NAME_TITLE + " varchar(10) not null" +
                ")";
        db.execSQL(sql5);

        // 存储type icon的表
        String sql6 = "create table " + BillContract.TYPE_ICON_TABLE_NAME +
                "(" + BillContract.TYPE_ICON_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.TYPE_ICON_COLUMN_NAME_TITLE + " varchar(10) not null," +
                BillContract.TYPE_ICON_COLUMN_NAME_ICON + " integer not null" +
                ")";
        db.execSQL(sql6);

        // 存储数字的表
        String sql7 = "create table " + BillContract.NUM_TABLE_NAME +
                "(" + BillContract.NUM_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.NUM_COLUMN_NAME_TITLE + " varchar(10) not null," +
                BillContract.NUM_COLUMN_NAME_VALUE + " integer not null" +
                ")";
        db.execSQL(sql7);

        // 存储user信息的表
        String sql8 = "create table " + BillContract.USER_TABLE_NAME +
                "(" + BillContract.USER_COLUMN_NAME_ID + " integer primary key autoincrement," +
                BillContract.USER_COLUMN_NAME_TITLE + " varchar(10) not null," +
                BillContract.USER_COLUMN_NAME_VALUE + " varchar(65) not null" +
                ")";
        db.execSQL(sql8);


        Log.d(TAG, "onUpgrade: 数据表更新");
    }
}
