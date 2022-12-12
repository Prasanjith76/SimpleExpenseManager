package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        super(context,"200357D.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        // create tables
        DB.execSQL("create Table Accountdetails(accountNo TEXT primary key, bankName TEXT, accountHolder TEXT, balance REAL)");
        DB.execSQL("create Table Logs(ID INTEGER primary key autoincrement,date Date, accountNo TEXT, expenseType TEXT, amount REAL,foreign key(accountNo) references user_account(accountNo))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        // delete tables
        DB.execSQL("drop Table if exists Accountdetails");
        DB.execSQL("drop Table if exists Logs");
    }

    public SQLiteDatabase getDataBase(){
        return this.getWritableDatabase();
    }

}
