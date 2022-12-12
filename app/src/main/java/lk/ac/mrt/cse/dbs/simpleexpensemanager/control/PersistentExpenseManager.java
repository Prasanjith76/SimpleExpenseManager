package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;


/**
 *
 */
public class PersistentExpenseManager extends ExpenseManager {
    private Context context;
    public PersistentExpenseManager (Context context) {
        this.context = context;
        setup();
    }
    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/


        DBHelper DB = new DBHelper(context);
        SQLiteDatabase database = DB.getDataBase(); // get a writable database

        // passing the writable database into persistentTransactionDAO, persistentAccountDAO constructors
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(database);
        setTransactionsDAO(persistentTransactionDAO);
        AccountDAO persistentAccountDAO = new PersistentAccountDAO(database);
        setAccountsDAO(persistentAccountDAO);


        /*** End ***/
    }
}