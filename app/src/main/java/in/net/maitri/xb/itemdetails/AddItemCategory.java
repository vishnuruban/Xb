package in.net.maitri.xb.itemdetails;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import in.net.maitri.xb.R;

public class AddItemCategory extends AppCompatActivity {

    private CategoryAdapter mCategoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_category);

        if (true) {
             findViewById(R.id.no_category).setVisibility(View.VISIBLE);
        } else {
            RecyclerView categoryView = (RecyclerView) findViewById(R.id.category_view);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddItemCategory.this);
            categoryView.setLayoutManager(linearLayoutManager);
            mCategoryAdapter = new CategoryAdapter(AddItemCategory.this);
            categoryView.setAdapter(mCategoryAdapter);

        }
        final FloatingActionButton addCategoryBtn = (FloatingActionButton) findViewById(R.id.add_category);
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });
    }

    private void addCategory(){
        DialogFragment newFragment = new AddCategory();
        newFragment.setCancelable(false);
        newFragment.show(getSupportFragmentManager(), "");
    }

}
