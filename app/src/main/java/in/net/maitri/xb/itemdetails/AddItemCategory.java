package in.net.maitri.xb.itemdetails;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.DbHandler;

public class AddItemCategory extends AppCompatActivity {

    private CategoryAdapter mCategoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_category);

        List<Category> getAllCategories = new DbHandler(AddItemCategory.this).getAllcategorys();
        final RecyclerView categoryView = (RecyclerView) findViewById(R.id.category_view);
        if (getAllCategories.size() == 0) {
             findViewById(R.id.no_category).setVisibility(View.VISIBLE);
        } else {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddItemCategory.this);
            categoryView.setLayoutManager(linearLayoutManager);
            mCategoryAdapter = new CategoryAdapter(AddItemCategory.this, getAllCategories);
            categoryView.setAdapter(mCategoryAdapter);

        }



        final FloatingActionButton addCategoryBtn = (FloatingActionButton) findViewById(R.id.add_category);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        FloatingActionButton addItemBtn = (FloatingActionButton) findViewById(R.id.add_item_button);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    private void addCategory(){
        DialogFragment newFragment = new AddCategory();
        newFragment.setCancelable(false);
        newFragment.show(getSupportFragmentManager(), "");
    }

    private void addItem(){
        DialogFragment newFragment = new AddItem();
        newFragment.setCancelable(false);
        newFragment.show(getSupportFragmentManager(), "");
    }

}
