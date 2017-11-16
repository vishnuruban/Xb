package in.net.maitri.xb.itemdetails;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


public class EditItem extends DialogFragment {

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
        dialogHeader.setText("Edit Item");

        Bundle bundle = getArguments();
        final Item item = (Item) bundle.getSerializable("itemObj");

        LinearLayout itemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
        itemLayout.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAddItemCategory);
        String selectedCategoryName = sharedPreferences.getString("catName", "");
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
        if (item != null) {
            itemName.setText(item.getItemName());
        }
        final EditText costPrice = (EditText) view.findViewById(R.id.cp);
        if (item != null) {
            costPrice.setText(String.valueOf(item.getItemCP()));
        }
        final EditText sellingPrice = (EditText) view.findViewById(R.id.sp);
        if (item != null) {
            sellingPrice.setText(String.valueOf(item.getItemSP()));
        }
        final EditText uom = (EditText) view.findViewById(R.id.uom);
        if (item != null) {
            uom.setText(item.getItemUOM());
        }
        final EditText hsnCode = (EditText) view.findViewById(R.id.hsn_code);
        if (item != null) {
            hsnCode.setText(item.getItemHSNcode());
        }
        final EditText gst = (EditText) view.findViewById(R.id.gst);
        if (item != null) {
            gst.setText(String.valueOf(item.getItemGST()));
        }
        if (item != null) {
            mImagePath = item.getItemImage();
            File imgFile = new File(mImagePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mItemImage.setImageBitmap(myBitmap);
            }
        }

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
                    if (mSelectedImage != null) {
                        copyImage();
                    }
                    Item item1 = new Item();
                    if (item != null) {
                        item1.setId(item.getId());
                    }
                    if (item != null) {
                        item1.setCategoryId(item.getCategoryId());
                    }
                    item1.setItemImage(mImagePath);
                    item1.setItemName(iteName);
                    item1.setItemCP(Float.parseFloat(cp));
                    item1.setItemSP(Float.parseFloat(sp));
                    item1.setItemUOM(uoM);
                    item1.setItemHSNcode(hsn);
                    item1.setItemGST(Float.parseFloat(gsT));
                    editItem(item1);
                    mAddItemCategory.updateItem(item.getCategoryId());
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


    private void editItem(Item item){
        dbHandler.updateItem(item);
        dismiss();
    }
}
