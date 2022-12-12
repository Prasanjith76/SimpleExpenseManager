package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private SQLiteDatabase DB;
    private final List<Transaction> transactions;
    public PersistentTransactionDAO(SQLiteDatabase DB){
        this.transactions = new LinkedList<>();
        this.DB = DB;
        getTransactionDetails(); // loading transaction details from the database into the the list



    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        transactions.add(transaction);
        DateFormat format = new SimpleDateFormat("M-d-yyyy", Locale.ENGLISH);
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",format.format(transaction.getDate()));
        contentValues.put("accountNo",accountNo);
        contentValues.put("expenseType",String.valueOf(expenseType));
        contentValues.put("amount",amount);
        DB.insert("Logs", null, contentValues);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }

    public void getTransactionDetails(){
        DateFormat format = new SimpleDateFormat("M-d-yyyy", Locale.ENGLISH);
        Cursor cursor = DB.rawQuery("Select * from Logs", null);


        while(cursor.moveToNext()){
            Date date = new Date();
            ExpenseType expenseType;
            try {
                date = format.parse(cursor.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String accountNo = cursor.getString(2);
            expenseType = ExpenseType.valueOf(cursor.getString(3));
            double amount = cursor.getDouble(4);
            transactions.add(new Transaction(date,accountNo,expenseType,amount));
        }

    }
}
