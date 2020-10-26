package com.leundo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * 账单的数据操作类
 */
public class BillDao {

    private BillDBHelper billDBHelper;

    public BillDao(Context context) {
        billDBHelper = new BillDBHelper(context);
    }


    /*
        插入账单数据。插入时bill.id对插入无影响，插入后bill.id被修改为数据库自动赋予的id。
        `bill`    插入的账单实体。
        `@return`    插入后账单实体的主键(id)。
     */
    public long insertBill(Bill bill) {

        long id = -1;
        // 创建数据库操作对象
        SQLiteDatabase db = null;
        // 封装数据
        ContentValues values = new ContentValues();
        values.put(BillContract.MAIN_COLUMN_NAME_ACCOUNT, bill.getAccount());
        values.put(BillContract.MAIN_COLUMN_NAME_CATEGORY, bill.getCategory());
        values.put(BillContract.MAIN_COLUMN_NAME_TYPE_1, bill.getType1());
        values.put(BillContract.MAIN_COLUMN_NAME_TYPE_2, bill.getType2());
        values.put(BillContract.MAIN_COLUMN_NAME_AMOUNT, bill.getAmount());
        values.put(BillContract.MAIN_COLUMN_NAME_TIME, bill.getTime());
        values.put(BillContract.MAIN_COLUMN_NAME_NUMBER, bill.getNumber());
        values.put(BillContract.MAIN_COLUMN_NAME_FIRM, bill.getFirm());
        values.put(BillContract.MAIN_COLUMN_NAME_ITEM, bill.getItem());
        values.put(BillContract.MAIN_COLUMN_NAME_REMARK, bill.getRemark());
        values.put(BillContract.MAIN_COLUMN_NAME_CURRENCY, bill.getCurrency());
        try {
            db = billDBHelper.getWritableDatabase();
            id = db.insert(BillContract.MAIN_TABLE_NAME, null, values);
            // 修改原bill的id
            bill.setId(id);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return id;
    }

    /*
        根据账单id查询
        `bill`    插入的账单实体。
        `@return`    插入后账单实体的主键(id)。
     */
    public Bill queryBillById(long aId) {
        ArrayList<Bill> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Bill result = null;
        try {
            db = billDBHelper.getReadableDatabase();


            Cursor cursor = db.query(BillContract.MAIN_TABLE_NAME,
                    null,
                    BillContract.MAIN_COLUMN_NAME_ID + " = ?",
                    new String[]{aId + ""},
                    null, null,
                    null);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ID));
                String account = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ACCOUNT));
                String category = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CATEGORY));
                String type1 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_1));
                String type2 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_2));
                double amount = cursor.getDouble(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_AMOUNT));
                long time = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TIME));
                String number = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_NUMBER));
                String firm = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_FIRM));
                String item = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ITEM));
                String remark = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_REMARK));
                String currency = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CURRENCY));

                result = new Bill(id, account, category, type1, type2, amount, time, number, firm, item, remark, currency);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return result;
    }

    /*
        根据账单账户类型(如现金、支付宝)查询，结果按时间降序排序。
        `accountName`    作为查询条件的账单账户类型。
        `@return`    存储查询结果的Bill列表。
     */
    public ArrayList<Bill> queryBillByAccount(String accountName) {
        ArrayList<Bill> list = new ArrayList<>();

        try (SQLiteDatabase db = billDBHelper.getReadableDatabase()) {
            Cursor cursor = db.query(BillContract.MAIN_TABLE_NAME,
                    null,
                    BillContract.MAIN_COLUMN_NAME_ACCOUNT + " = ? ",
                    new String[]{accountName},
                    null, null,
                    BillContract.MAIN_COLUMN_NAME_TIME + " DESC");

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ID));
                String account = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ACCOUNT));
                String category = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CATEGORY));
                String type1 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_1));
                String type2 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_2));
                double amount = cursor.getDouble(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_AMOUNT));
                long time = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TIME));
                String number = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_NUMBER));
                String firm = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_FIRM));
                String item = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ITEM));
                String remark = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_REMARK));
                String currency = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CURRENCY));

                Bill bill = new Bill(id, account, category, type1, type2, amount, time, number, firm, item, remark, currency);
                list.add(bill);
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /*
        查询所有账单，结果按时间降序排序。
        `@return`    存储查询结果的Bill列表。
     */
    public ArrayList<Bill> queryBill() {
        ArrayList<Bill> list = new ArrayList<>();

        try (SQLiteDatabase db = billDBHelper.getReadableDatabase()) {
            Cursor cursor = db.query(BillContract.MAIN_TABLE_NAME,
                    null,
                    null,
                    null,
                    null, null,
                    BillContract.MAIN_COLUMN_NAME_TIME + " DESC");

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ID));
                String account = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ACCOUNT));
                String category = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CATEGORY));
                String type1 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_1));
                String type2 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_2));
                double amount = cursor.getDouble(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_AMOUNT));
                long time = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TIME));
                String number = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_NUMBER));
                String firm = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_FIRM));
                String item = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ITEM));
                String remark = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_REMARK));
                String currency = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CURRENCY));

                Bill bill = new Bill(id, account, category, type1, type2, amount, time, number, firm, item, remark, currency);
                list.add(bill);
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /*
        根据账单时间和账户类型(如现金、支付宝)查询，结果按时间降序排序。查询结果的时间在区间[start,end)。
        `start`    开始时间，包括该点。
        `end`    结束时间，不包括该点。
        `accountName`    作为查询条件的账单账户类型。
        `@return`    存储查询结果的Bill列表。
    */
    public ArrayList<Bill> queryBillByTime(Date start, Date end, String accountName) {
        ArrayList<Bill> list = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            long startTime = start.getTime();
            long endTime = end.getTime();
            db = billDBHelper.getReadableDatabase();


            Cursor cursor = db.query(BillContract.MAIN_TABLE_NAME,
                    null,
                    BillContract.MAIN_COLUMN_NAME_ACCOUNT + " = ? AND " + BillContract.MAIN_COLUMN_NAME_TIME + " >= ? AND " + BillContract.MAIN_COLUMN_NAME_TIME + " < ? ",
                    new String[]{accountName, startTime + "", endTime + ""},
                    null, null,
                    BillContract.MAIN_COLUMN_NAME_TIME + " DESC");

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ID));
                String account = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ACCOUNT));
                String category = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CATEGORY));
                String type1 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_1));
                String type2 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_2));
                double amount = cursor.getDouble(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_AMOUNT));
                long time = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TIME));
                String number = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_NUMBER));
                String firm = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_FIRM));
                String item = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ITEM));
                String remark = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_REMARK));
                String currency = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CURRENCY));

                Bill bill = new Bill(id, account, category, type1, type2, amount, time, number, firm, item, remark, currency);
                list.add(bill);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return list;
    }


    /*
        根据账单时间查询，结果按时间降序排序。查询结果的时间在区间[start,end)，结果包括所有账户类型。
        `start`    开始时间，包括该点。
        `end`    结束时间，不包括该点。
        `@return`    存储查询结果的Bill列表。
     */
    public ArrayList<Bill> queryBillByTime(Date start, Date end) {
        ArrayList<Bill> list = new ArrayList<Bill>();
        SQLiteDatabase db = null;
        try {
            long startTime = start.getTime();
            long endTime = end.getTime();

            db = billDBHelper.getReadableDatabase();

            Cursor cursor = db.query(BillContract.MAIN_TABLE_NAME,
                    null,
                    BillContract.MAIN_COLUMN_NAME_TIME + " >= ? AND " + BillContract.MAIN_COLUMN_NAME_TIME + " < ? ",
                    new String[]{startTime + "", endTime + ""},
                    null, null,
                    BillContract.MAIN_COLUMN_NAME_TIME + " DESC");

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ID));
                String account = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ACCOUNT));
                String category = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CATEGORY));
                String type1 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_1));
                String type2 = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TYPE_2));
                double amount = cursor.getDouble(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_AMOUNT));
                long time = cursor.getLong(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_TIME));
                String number = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_NUMBER));
                String firm = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_FIRM));
                String item = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_ITEM));
                String remark = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_REMARK));
                String currency = cursor.getString(cursor.getColumnIndex(BillContract.MAIN_COLUMN_NAME_CURRENCY));

                Bill bill = new Bill(id, account, category, type1, type2, amount, time, number, firm, item, remark,currency);
                list.add(bill);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return list;
    }


    // 根据一级类别修改Bill，重命名，只修改type1
    private int updateBillWithType(String oldType1Name, String newType1Name) {
        SQLiteDatabase db = null;
        int num = -1;

        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.MAIN_COLUMN_NAME_TYPE_1, newType1Name);
            num = db.update(BillContract.MAIN_TABLE_NAME,
                    values,
                    BillContract.MAIN_COLUMN_NAME_TYPE_1 + " = ? ",
                    new String[]{oldType1Name});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 根据二级类别修改Bill，重命名，只修改type2
    private int updateBillWithType(String type1Name, String oldType2Name, String newType2Name) {
        SQLiteDatabase db = null;
        int num = -1;

        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.MAIN_COLUMN_NAME_TYPE_2, newType2Name);
            num = db.update(BillContract.MAIN_TABLE_NAME,
                    values,
                    BillContract.MAIN_COLUMN_NAME_TYPE_1 + " = ? AND " + BillContract.MAIN_COLUMN_NAME_TYPE_2 + " = ? ",
                    new String[]{type1Name, oldType2Name});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 根据二级类别修改Bill，重命名，只修改Account
    private int updateBillWithAccount(String oldAccountName, String newAccountName) {
        SQLiteDatabase db = null;
        int num = -1;

        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.MAIN_COLUMN_NAME_ACCOUNT, newAccountName);
            num = db.update(BillContract.MAIN_TABLE_NAME,
                    values,
                    BillContract.MAIN_COLUMN_NAME_ACCOUNT + " = ? ",
                    new String[]{oldAccountName});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }


    // 根据bill的id修改数据
    /*
        更新数据，数据库中与bill.id相同的数据行将被重置为bill的数据。
        `bill`    作为修改标准的账单实体。
        `@return`    1为成功修改，0为数据库中不存在与bill.id相同的数据行。
     */
    public int updateBill(Bill bill) {
        SQLiteDatabase db = null;
        int num = -1;
        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.MAIN_COLUMN_NAME_ACCOUNT, bill.getAccount());
            values.put(BillContract.MAIN_COLUMN_NAME_CATEGORY, bill.getCategory());
            values.put(BillContract.MAIN_COLUMN_NAME_TYPE_1, bill.getType1());
            values.put(BillContract.MAIN_COLUMN_NAME_TYPE_2, bill.getType2());
            values.put(BillContract.MAIN_COLUMN_NAME_AMOUNT, bill.getAmount());
            values.put(BillContract.MAIN_COLUMN_NAME_TIME, bill.getTime());
            values.put(BillContract.MAIN_COLUMN_NAME_NUMBER, bill.getNumber());
            values.put(BillContract.MAIN_COLUMN_NAME_FIRM, bill.getFirm());
            values.put(BillContract.MAIN_COLUMN_NAME_ITEM, bill.getItem());
            values.put(BillContract.MAIN_COLUMN_NAME_REMARK, bill.getRemark());
            values.put(BillContract.MAIN_COLUMN_NAME_CURRENCY, bill.getCurrency());

            // 执行修改操作
            num = db.update(BillContract.MAIN_TABLE_NAME, values, BillContract.MAIN_COLUMN_NAME_ID + " = ? ", new String[]{bill.getId() + ""});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;

    }

    // 根据id删除数据
    public int deleteBill(long id) {
        SQLiteDatabase db = null;
        int num = -1;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.MAIN_TABLE_NAME, BillContract.MAIN_COLUMN_NAME_ID + " = ? ", new String[]{id + ""});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 重载Bill，删除数据
    public int deleteBill(Bill bill) {
        return deleteBill(bill.getId());
    }

    // 插入账单类别，如"餐饮"，"医疗"
    // 一级
    public long insertType(String type1Name) {
        long id = -1;
        SQLiteDatabase db = null;

        if (isTypeExisted(type1Name)) {
            return id;
        }
        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.TYPE_COLUMN_NAME_LEVEL, 1);
            values.put(BillContract.TYPE_COLUMN_NAME_TITLE, type1Name);

            id = db.insert(BillContract.TYPE_TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return id;
    }

    // 插入账单类别，如"餐饮"，"医疗"
    // 二级
    public long insertType(String type1Name, String type2Name) {
        long id = -1;
        SQLiteDatabase db = null;

        if (isTypeExisted(type1Name, type2Name)) {
            return id;
        }
        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.TYPE_COLUMN_NAME_LEVEL, 2);
            values.put(BillContract.TYPE_COLUMN_NAME_TITLE, type2Name);
            values.put(BillContract.TYPE_COLUMN_NAME_FATHER, type1Name);

            id = db.insert(BillContract.TYPE_TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return id;
    }

    // 根据名称和级别查询账单类别，用于判断某类别是否存在
    // 一级
    private boolean isTypeExisted(String type1Name) {
        long id = -1;
        boolean result = false;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.TYPE_TABLE_NAME,
                    null,
                    BillContract.TYPE_COLUMN_NAME_TITLE + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? ",
                    new String[]{type1Name, "1"}, null, null, null);

            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(BillContract.TYPE_COLUMN_NAME_ID));
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    // 根据名称和级别查询账单类别，用于判断某类别是否存在
    // 二级
    private boolean isTypeExisted(String type1Name, String type2Name) {
        long id = -1;
        boolean result = false;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.TYPE_TABLE_NAME,
                    null,
                    BillContract.TYPE_COLUMN_NAME_TITLE + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? AND " + BillContract.TYPE_COLUMN_NAME_FATHER + " = ? ",
                    new String[]{type2Name, "2", type1Name}, null, null, null);

            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(BillContract.TYPE_COLUMN_NAME_ID));
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    // 查询一级别的账单类别
    public List<String> queryType() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = null;

        try {

            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.TYPE_TABLE_NAME,
                    null,
                    BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? ",
                    new String[]{"1"},
                    null, null, BillContract.MAIN_COLUMN_NAME_ID + " ASC");

            while (cursor.moveToNext()) {

                String title = cursor.getString(cursor.getColumnIndex(BillContract.TYPE_COLUMN_NAME_TITLE));

                list.add(title);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return list;
    }

    // 查询二级别的账单类别
    public List<String> queryType(String type1Name) {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = null;

        try {

            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.TYPE_TABLE_NAME,
                    null,
                    BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? AND " + BillContract.TYPE_COLUMN_NAME_FATHER + " = ? ",
                    new String[]{"2", type1Name},
                    null, null, BillContract.MAIN_COLUMN_NAME_ID + " ASC");

            while (cursor.moveToNext()) {

                String title = cursor.getString(cursor.getColumnIndex(BillContract.TYPE_COLUMN_NAME_TITLE));

                list.add(title);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return list;
    }

    /*
     * 修改账单类型的名称
     * 返回值类型：int
     *      -1：重名
     *      0：旧名不存在
     *      1：成功修改
     */
    // 一级
    public int updateType(String oldType1Name, String newType1Name) {
        int num = -1;
        SQLiteDatabase db = null;

        // 判断修改后会不会重名
        if (isTypeExisted(newType1Name)) {
            return num;
        }

        try {

            db = billDBHelper.getWritableDatabase();
            // 封装数据，修改一级Type
            ContentValues values = new ContentValues();
            values.put(BillContract.TYPE_COLUMN_NAME_TITLE, newType1Name);

            // 执行修改操作
            num = db.update(BillContract.TYPE_TABLE_NAME,
                    values,
                    BillContract.TYPE_COLUMN_NAME_TITLE + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? ",
                    new String[]{oldType1Name, "1"});

            // 封装数据，把二级type中father为oldType1Name的更改
            ContentValues values2 = new ContentValues();
            values2.put(BillContract.TYPE_COLUMN_NAME_FATHER, newType1Name);

            // 执行修改操作
            num = num + db.update(BillContract.TYPE_TABLE_NAME,
                    values2,
                    BillContract.TYPE_COLUMN_NAME_FATHER + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? ",
                    new String[]{oldType1Name, "2"});



            // 修改Bill的数据

            updateBillWithType(oldType1Name, newType1Name);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 二级
    public int updateType(String type1Name, String oldType2Name, String newType2Name) {
        int num = -1;
        SQLiteDatabase db = null;



        // 判断修改后会不会重名
        if (isTypeExisted(type1Name, newType2Name)) {
            return num;
        }

        try {


            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.TYPE_COLUMN_NAME_TITLE, newType2Name);

            // 执行修改操作
            num = db.update(BillContract.TYPE_TABLE_NAME,
                    values,
                    BillContract.TYPE_COLUMN_NAME_TITLE + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? AND " + BillContract.TYPE_COLUMN_NAME_FATHER + " = ?",
                    new String[]{oldType2Name, "2", type1Name});



            // 修改Bill的数据
            updateBillWithType(type1Name, oldType2Name, newType2Name);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 删除账单类别
    // 一级
    // 但是删除后Bill记录对应字段还在
    // 所有请使用合并
    public int deleteType(String type1Name) {
        int num = -1;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.TYPE_TABLE_NAME,
                    BillContract.TYPE_COLUMN_NAME_TITLE + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? ",
                    new String[]{type1Name, "1"});

            num = num + db.delete(BillContract.TYPE_TABLE_NAME,
                    BillContract.TYPE_COLUMN_NAME_FATHER + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? ",
                    new String[]{type1Name, "2"});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 删除类别相应的记账记录
    public int deleteBillByType(String type1Name) {
        SQLiteDatabase db = null;
        int num = -1;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.MAIN_TABLE_NAME,
                    BillContract.MAIN_COLUMN_NAME_TYPE_1 + " = ? ",
                    new String[]{type1Name});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    public int deleteBillByType(String type1Name, String type2Name) {
        SQLiteDatabase db = null;
        int num = -1;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.MAIN_TABLE_NAME,
                    BillContract.MAIN_COLUMN_NAME_TYPE_1 + " = ? AND " + BillContract.MAIN_COLUMN_NAME_TYPE_2 + " = ? ",
                    new String[]{type1Name, type2Name});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 删除账单类别
    // 二级
    public int deleteType(String type1Name, String type2Name) {
        int num = -1;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.TYPE_TABLE_NAME,
                    BillContract.TYPE_COLUMN_NAME_TITLE + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? AND " + BillContract.TYPE_COLUMN_NAME_FATHER + " = ? ",
                    new String[]{type1Name, "2", type2Name});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 一级类别合并
    // 合并后二级字段也归入对应的一级类别
    // deletedType1Name合并后被删除
    // extendedType1Name字段规模将会变大
    // 与updateType唯一不同就是不判断字段是否重复
    public int mergeType(String deletedType1Name, String extendedType1Name) {
        int num = -1;
        SQLiteDatabase db = null;

        try {

            db = billDBHelper.getWritableDatabase();
            // 封装数据，修改一级Type
            ContentValues values = new ContentValues();
            values.put(BillContract.TYPE_COLUMN_NAME_TITLE, extendedType1Name);

            // 执行修改操作
            num = db.update(BillContract.TYPE_TABLE_NAME,
                    values,
                    BillContract.TYPE_COLUMN_NAME_TITLE + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? ",
                    new String[]{deletedType1Name, "1"});

            // 封装数据，把二级type中father为oldType1Name的更改
            ContentValues values2 = new ContentValues();
            values2.put(BillContract.TYPE_COLUMN_NAME_FATHER, extendedType1Name);

            // 执行修改操作
            num = num + db.update(BillContract.TYPE_TABLE_NAME,
                    values2,
                    BillContract.TYPE_COLUMN_NAME_FATHER + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? ",
                    new String[]{deletedType1Name, "2"});



            // 修改Bill的数据

            updateBillWithType(deletedType1Name, extendedType1Name);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 二级
    public int mergeType(String type1Name, String deletedType2Name, String extendedType2Name) {
        int num = -1;
        SQLiteDatabase db = null;

        try {


            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.TYPE_COLUMN_NAME_TITLE, extendedType2Name);

            // 执行修改操作
            num = db.update(BillContract.TYPE_TABLE_NAME,
                    values,
                    BillContract.TYPE_COLUMN_NAME_TITLE + " = ? AND " + BillContract.TYPE_COLUMN_NAME_LEVEL + " = ? AND " + BillContract.TYPE_COLUMN_NAME_FATHER + " = ?",
                    new String[]{deletedType2Name, "2", type1Name});



            // 修改Bill的数据
            updateBillWithType(type1Name, deletedType2Name, extendedType2Name);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }



    /// ACCOUNT

    /*
        查询给定account是否存在。
        `accountName`    插入的账单实体。
        `@return`    存在为true，不存在为false。
     */
    private boolean isAccountExisted(String accountName) {
        long id = -1;
        boolean result = false;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.ACCOUNT_TABLE_NAME,
                    null,
                    BillContract.ACCOUNT_COLUMN_NAME_TITLE + " = ? ",
                    new String[]{accountName}, null, null, null);

            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(BillContract.ACCOUNT_COLUMN_NAME_ID));
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }


    /*
        新建账户。
        `accountName`    新建的账户名称。
        `@return`    插入后账户在数据库的id，-1为重名。
     */
    public long insertAccount(String accountName) {
        long id = -1;
        SQLiteDatabase db = null;

        if (isAccountExisted(accountName)) {
            return id;
        }
        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.ACCOUNT_COLUMN_NAME_TITLE, accountName);

            id = db.insert(BillContract.ACCOUNT_TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return id;
    }

    /*
        查询账户。
        `@return`    列表，包括所有账单名称
     */
    public List<String> queryAccount() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = null;
        try {

            db = billDBHelper.getReadableDatabase();

            Cursor cursor = db.query(BillContract.ACCOUNT_TABLE_NAME,
                    null,
                    null,
                    null,
                    null, null,
                    null);

            while (cursor.moveToNext()) {
                String accountName = cursor.getString(cursor.getColumnIndex(BillContract.ACCOUNT_COLUMN_NAME_TITLE));
                list.add(accountName);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return list;
    }

    // 删除账单相应的记账记录
    public int deleteBillByAccount(String accountName) {
        SQLiteDatabase db = null;
        int num = -1;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.MAIN_TABLE_NAME,
                    BillContract.MAIN_COLUMN_NAME_ACCOUNT + " = ? ",
                    new String[]{accountName});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 更新名称
    // 不判断重名
    public int updateAccount(String oldAccountName, String newAccountName) {
        int num = -1;
        SQLiteDatabase db = null;

        try {

            db = billDBHelper.getWritableDatabase();
            // 封装数据，修改账单
            ContentValues values = new ContentValues();
            values.put(BillContract.ACCOUNT_COLUMN_NAME_TITLE, newAccountName);

            // 执行修改操作
            num = db.update(BillContract.ACCOUNT_TABLE_NAME,
                    values,
                    BillContract.ACCOUNT_COLUMN_NAME_TITLE + " = ?",
                    new String[]{oldAccountName});

            // 修改Bill的数据
            updateBillWithAccount(oldAccountName, newAccountName);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }


    // 删除账单
    // 不影响账单记录
    public int deleteAccount(String accountName) {
        int num = -1;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.ACCOUNT_TABLE_NAME,
                    BillContract.ACCOUNT_COLUMN_NAME_TITLE + " = ?",
                    new String[]{accountName});



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    /// Number

    /*
        查询给定number是否存在。
        `numberName`    查询的成员名称。
        `@return`    存在为true，不存在为false。
     */
    private boolean isNumberExisted(String numberName) {
        long id = -1;
        boolean result = false;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.NUMBER_TABLE_NAME,
                    null,
                    BillContract.NUMBER_COLUMN_NAME_TITLE + " = ? ",
                    new String[]{numberName}, null, null, null);

            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(BillContract.NUMBER_COLUMN_NAME_ID));
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    /*
        新建成员。
        `numberName`    新建的账户名称。
        `@return`    插入后成员在数据库的id，-1为重名。
     */
    public long insertNumber(String numberName) {
        long id = -1;
        SQLiteDatabase db = null;

        if (isNumberExisted(numberName)) {
            return id;
        }
        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.NUMBER_COLUMN_NAME_TITLE, numberName);

            id = db.insert(BillContract.NUMBER_TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return id;
    }

    /*
        查询成员。
        `@return`    列表，包括所有账单名称
     */
    public List<String> queryNumber() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = null;
        try {

            db = billDBHelper.getReadableDatabase();

            Cursor cursor = db.query(BillContract.NUMBER_TABLE_NAME,
                    null,
                    null,
                    null,
                    null, null,
                    null);

            while (cursor.moveToNext()) {
                String accountName = cursor.getString(cursor.getColumnIndex(BillContract.NUMBER_COLUMN_NAME_TITLE));
                list.add(accountName);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return list;
    }

    // 删除成员
    // 不影响账单记录
    public int deleteNumber(String numberName) {
        int num = -1;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.NUMBER_TABLE_NAME,
                    BillContract.NUMBER_COLUMN_NAME_TITLE + " = ?",
                    new String[]{numberName});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 删除成员相应的记账记录
    public int deleteBillByNumber(String numberName) {
        SQLiteDatabase db = null;
        int num = -1;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.MAIN_TABLE_NAME,
                    BillContract.MAIN_COLUMN_NAME_NUMBER + " = ? ",
                    new String[]{numberName});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    // 删除商家相应的记账记录
    public int deleteBillByFirm(String firmName) {
        SQLiteDatabase db = null;
        int num = -1;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.MAIN_TABLE_NAME,
                    BillContract.MAIN_COLUMN_NAME_FIRM + " = ? ",
                    new String[]{firmName});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }

    /// Firm

    /*
        查询给定firm是否存在。
        `firmName`    查询的成员名称。
        `@return`    存在为true，不存在为false。
     */
    private boolean isFirmExisted(String firmName) {
        long id = -1;
        boolean result = false;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.FIRM_TABLE_NAME,
                    null,
                    BillContract.FIRM_COLUMN_NAME_TITLE + " = ? ",
                    new String[]{firmName}, null, null, null);

            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(BillContract.FIRM_COLUMN_NAME_ID));
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    /*
        新建成员。
        `firmName`    新建的商家名称。
        `@return`    插入后成员在数据库的id，-1为重名。
     */
    public long insertFirm(String firmName) {
        long id = -1;
        SQLiteDatabase db = null;

        if (isNumberExisted(firmName)) {
            return id;
        }
        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.FIRM_COLUMN_NAME_TITLE, firmName);

            id = db.insert(BillContract.FIRM_TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return id;
    }

    /*
        查询成员。
        `@return`    列表，包括所有商家名称
     */
    public List<String> queryFirm() {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = null;
        try {

            db = billDBHelper.getReadableDatabase();

            Cursor cursor = db.query(BillContract.FIRM_TABLE_NAME,
                    null,
                    null,
                    null,
                    null, null,
                    null);

            while (cursor.moveToNext()) {
                String accountName = cursor.getString(cursor.getColumnIndex(BillContract.FIRM_COLUMN_NAME_TITLE));
                list.add(accountName);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return list;
    }

    // 删除成员
    // 不影响账单记录
    public int deleteFirm(String firmName) {
        int num = -1;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getWritableDatabase();
            num = db.delete(BillContract.FIRM_TABLE_NAME,
                    BillContract.FIRM_COLUMN_NAME_TITLE + " = ?",
                    new String[]{firmName});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return num;
    }



    // 判断一级类别对应的Icon资源号是否存在
    private boolean isTypeIconExisted(String type1Name) {
        long id = -1;
        boolean result = false;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.TYPE_ICON_TABLE_NAME,
                    null,
                    BillContract.TYPE_ICON_COLUMN_NAME_TITLE + " = ?",
                    new String[]{type1Name}, null, null, null);

            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(BillContract.TYPE_ICON_COLUMN_NAME_ID));
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    // 插入一级类别对应的Icon资源号
    public long insertTypeIcon(String type1Name, int resourceId) {
        long id = -1;
        SQLiteDatabase db = null;

        if (isTypeIconExisted(type1Name)) {
            return id;
        }
        try {
            db = billDBHelper.getWritableDatabase();
            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.TYPE_ICON_COLUMN_NAME_TITLE, type1Name);
            values.put(BillContract.TYPE_ICON_COLUMN_NAME_ICON, resourceId);

            id = db.insert(BillContract.TYPE_ICON_TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return id;
    }

    // 查询一级类型对应的图标
    public Map<String, Integer> queryTypeIcon() {
        Map<String, Integer> typeIcon = new HashMap<String, Integer>();
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getReadableDatabase();

            Cursor cursor = db.query(BillContract.TYPE_ICON_TABLE_NAME,
                    null,
                    null,
                    null,
                    null, null,
                    null);

            while (cursor.moveToNext()) {

                String type1Name = cursor.getString(cursor.getColumnIndex(BillContract.TYPE_ICON_COLUMN_NAME_TITLE));
                int iconId = cursor.getInt(cursor.getColumnIndex(BillContract.TYPE_ICON_COLUMN_NAME_ICON));

                typeIcon.put(type1Name, iconId);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return typeIcon;
    }


    // 判断 字符-数字 键值对是否存在
    public boolean isNumExisted(String titleName) {
        long id = -1;
        boolean result = false;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.NUM_TABLE_NAME,
                    null,
                    BillContract.NUM_COLUMN_NAME_TITLE + " = ?",
                    new String[]{titleName}, null, null, null);

            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(BillContract.NUM_COLUMN_NAME_ID));
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    // 插入或更新 字符-数字 键值对
    public long insertOrUpdateNum(String titleName, double num) {
        long id = -1;
        SQLiteDatabase db = null;

        // 插入
        try {
            db = billDBHelper.getWritableDatabase();

            // 删除原数据
            db.delete(BillContract.NUM_TABLE_NAME,
                    BillContract.NUM_COLUMN_NAME_TITLE + " = ? ",
                    new String[]{titleName});

            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.NUM_COLUMN_NAME_TITLE, titleName);
            values.put(BillContract.NUM_COLUMN_NAME_VALUE, num);

            id = db.insert(BillContract.NUM_TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return id;
    }

    // 查询 字符-数字 键值对
    public double queryNum(String titleName) {
        double value = -1;
        SQLiteDatabase db = null;

        try {

            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.NUM_TABLE_NAME,
                    null,
                    BillContract.NUM_COLUMN_NAME_TITLE + " = ? ",
                    new String[]{titleName},
                    null, null, null);

            while (cursor.moveToNext()) {
                value = cursor.getDouble(cursor.getColumnIndex(BillContract.NUM_COLUMN_NAME_VALUE));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return value;
    }



    // 判断用户数据是否存在
    public boolean isUserDataExisted(String titleName) {
        long id = -1;
        boolean result = false;
        SQLiteDatabase db = null;

        try {
            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.USER_TABLE_NAME,
                    null,
                    BillContract.USER_COLUMN_NAME_TITLE + " = ?",
                    new String[]{titleName}, null, null, null);

            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(BillContract.USER_COLUMN_NAME_ID));
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }



    // 插入或更新用户数据
    public long insertOrUpdateUserData(String titleName, String data) {
        long id = -1;
        SQLiteDatabase db = null;

        // 插入
        try {
            db = billDBHelper.getWritableDatabase();

            // 删除原数据
            db.delete(BillContract.USER_TABLE_NAME,
                    BillContract.USER_COLUMN_NAME_TITLE + " = ? ",
                    new String[]{titleName});

            // 封装数据
            ContentValues values = new ContentValues();
            values.put(BillContract.USER_COLUMN_NAME_TITLE, titleName);
            values.put(BillContract.USER_COLUMN_NAME_VALUE, data);

            id = db.insert(BillContract.USER_TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return id;
    }

    // 查询用户数据
    public String queryUserData(String titleName) {
        String value = null;
        SQLiteDatabase db = null;

        try {

            db = billDBHelper.getReadableDatabase();
            Cursor cursor = db.query(BillContract.USER_TABLE_NAME,
                    null,
                    BillContract.USER_COLUMN_NAME_TITLE + " = ? ",
                    new String[]{titleName},
                    null, null, null);

            while (cursor.moveToNext()) {
                value = cursor.getString(cursor.getColumnIndex(BillContract.USER_COLUMN_NAME_VALUE));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return value;
    }

}
