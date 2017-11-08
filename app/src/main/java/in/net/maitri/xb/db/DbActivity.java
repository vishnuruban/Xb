package in.net.maitri.xb.db;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import in.net.maitri.xb.R;

/**
 * Created by SYSRAJ4 on 06/11/2017.
 */

public class DbActivity extends Activity {

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHandler dbHandler= new DbHandler(this);
        Log.d("Insert: ", "Inserting ..");
        Category cat = new Category("DIARY PRODUCTS","IMAGE");
        dbHandler.addCategory(cat);
        Item ite = new Item("MILK","LTR",20,22,"SHJDUD11",12,1,"IMAGE");
        dbHandler.addItem(ite);
    }
}
