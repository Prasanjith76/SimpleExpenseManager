package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class PersistentAccountDAO implements AccountDAO {

    private SQLiteDatabase DB;
    private final Map<String, Account> accounts;
    public PersistentAccountDAO(SQLiteDatabase DB){
        this.accounts = new HashMap<>();
        this.DB = DB;
        LoadAccountDetails(); // loading stored account details from the database into the hashmap


    }

    @Override
    public List<String> getAccountNumbersList() {
        return new ArrayList<>(accounts.keySet());
    }

    @Override
    public List<Account> getAccountsList() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        if (accounts.containsKey(accountNo)) {
            return accounts.get(accountNo);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        // adding data to database
        accounts.put(account.getAccountNo(), account);
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo",account.getAccountNo());
        contentValues.put("bankName",account.getBankName());
        contentValues.put("accountHolder",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        DB.insert("Accountdetails", null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        accounts.remove(accountNo);
        // removing data from database
        DB.delete("Accountdetails", "accountNo=?", new String[]{accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = accounts.get(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        // update data of the database
        accounts.put(accountNo, account);
        ContentValues contentValues = new ContentValues();
        contentValues.put("bankName",account.getBankName());
        contentValues.put("accountHolder",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        DB.update("Accountdetails", contentValues, "accountNo=?", new String[]{accountNo});

    }
    public void LoadAccountDetails(){
        Cursor cursor = DB.rawQuery("Select * from Accountdetails", null);
        while(cursor.moveToNext()){
            String accountNo = cursor.getString(0);
            String bankName = cursor.getString(1);
            String accountHolderName = cursor.getString(2);
            double balance = cursor.getDouble(3);
            accounts.put(accountNo,new Account(accountNo,bankName,accountHolderName,balance));
        }

    }

}

