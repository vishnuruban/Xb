package in.net.maitri.xb.itemdetails;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;

public class AddItem extends DialogFragment {

    private ImageView mItemImage;
    private String mImagePath;
    private DbHandler dbHandler;
    private Bitmap mSelectedImage;
    private AddItemCategory mAddItemCategory;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAddItemCategory = (AddItemCategory) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.add_category, container, false);

        TextView dialogHeader = (TextView) view.findViewById(R.id.dialog_header);
        dialogHeader.setText("Add Item");

        LinearLayout itemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
        itemLayout.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAddItemCategory);
        String selectedCategoryName = sharedPreferences.getString("catName", "");
        final int selectedCategoryId = sharedPreferences.getInt("catId", 0);
        dbHandler = new DbHandler(getActivity());
        mItemImage = (ImageView) view.findViewById(R.id.item_image);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button browseImage = (Button) view.findViewById(R.id.browse_image);
        browseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage();
            }
        });

        final EditText categoryName = (EditText) view.findViewById(R.id.category_name);
        categoryName.setText(selectedCategoryName);
        categoryName.setEnabled(false);
        final EditText itemName = (EditText) view.findViewById(R.id.item_name);
        final EditText costPrice = (EditText) view.findViewById(R.id.cp);
        final EditText sellingPrice = (EditText) view.findViewById(R.id.sp);
        final EditText uom = (EditText) view.findViewById(R.id.uom);
        final EditText hsnCode = (EditText) view.findViewById(R.id.hsn_code);
        final EditText gst = (EditText) view.findViewById(R.id.gst);

        Button addDetails = (Button) view.findViewById(R.id.add_details);
        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String catName = categoryName.getText().toString();
                String iteName =itemName.getText().toString();
                String cp= costPrice.getText().toString();
                String sp = sellingPrice.getText().toString();
                String uoM =  uom.getText().toString();
                String gsT = gst.getText().toString();
                String hsn = hsnCode.getText().toString();


                if (catName.isEmpty() || iteName.isEmpty()
                        || cp.isEmpty()
                        || sp.isEmpty()
                        || uoM.isEmpty()
                        || gsT.isEmpty()
                        || hsn.isEmpty()){
                    Toast.makeText(getActivity(),"Enter all the fields.", Toast.LENGTH_SHORT).show();
                }else{
                    copyImage();
                    addItem(iteName,uoM,Float.valueOf(cp),Float.valueOf(sp),hsn,Float.valueOf(gsT),selectedCategoryId);
                    mAddItemCategory.updateItem(selectedCategoryId);
                }
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSelectedImage = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);
        mItemImage.setImageBitmap(mSelectedImage);
    }

    public void onPickImage() {
        ImagePicker.pickImage(this, "Select your image:");
    }

    private void copyImage(){
        File file = createFile();
        if (file != null) {
            FileOutputStream fout;
            try {
                fout = new FileOutputStream(file);
                mSelectedImage.compress(Bitmap.CompressFormat.PNG, 70, fout);
                fout.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mImagePath = String.valueOf(file);
            Log.d("path", mImagePath);
        }
    }

    private File createFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String name = "Xb_Img"+timeStamp + ".jpg";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Xb");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        myDir = new File(myDir, name);
        return  myDir;
    }


    private void addItem(String itemName, String itemUOM, float itemCP, float itemSP, String itemHSNcode, float itemGST, int categoryId){
        Item item = new Item(itemName,itemUOM,itemCP,itemSP,itemHSNcode,itemGST,categoryId,mImagePath);
        dbHandler.addItem(item);
        dismiss();
    }

}
