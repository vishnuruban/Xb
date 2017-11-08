package in.net.maitri.xb.itemdetails;

import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Category;
import in.net.maitri.xb.db.DbHandler;

public class AddCategory extends DialogFragment {

    public static final int PICK_IMAGE = 1;
    private ImageView mItemImage;
    String path="";
    DbHandler dbHandler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.add_category, container, false);
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
                onPickImage(view);
            }
        });
        final EditText categoryName = (EditText) view.findViewById(R.id.category_name);
        Button addDetails = (Button) view.findViewById(R.id.add_details);
        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mCatName =categoryName.getText().toString();
                if (mCatName.isEmpty()){
                    Toast.makeText(getActivity(),"Category name can't be empty.", Toast.LENGTH_SHORT).show();
                }else{
                    addCategory(mCatName,path);
                }
            }
        });
        dbHandler = new DbHandler(getActivity());
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK ) {
            Uri selectedImageUri = data.getData();
             Log.d("uri", String.valueOf(selectedImageUri));
            String path = selectedImageUri.getPath();
             Log.d("path", path);
            Bitmap bitmap = null;
            try {
                   bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImageUri);
            } catch (IOException e) {
                   e.printStackTrace();
            }
            mItemImage.setImageBitmap(bitmap);
              Uri selectedImageUri = data.getData();
        Log.d("uri", String.valueOf(selectedImageUri));
        path = selectedImageUri.getPath();
        Log.d("path", path);
        }*/

        Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);
        mItemImage.setImageBitmap(bitmap);
        File file = createFile();
        if (file != null) {
            FileOutputStream fout;
            try {
                fout = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, fout);
                fout.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Uri uri=Uri.fromFile(file);
            path = uri.getPath();
            Log.d("path", path);
        }
    }

    private void addCategory(String catName,String imgPath){
        Category category = new Category(catName,imgPath);
        dbHandler.addCategory(category);
        dismiss();
    }



    public void onPickImage(View view) {
        // Click on image button
        ImagePicker.pickImage(this, "Select your image:");
    }






    private File createFile() {
     //   String root = Environment.getExternalStorageDirectory().toString();

    //    File myDir = new File(Environment.getExternalStorageDirectory() + "/Xb/" + "Images");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String name = "Xb_"+timeStamp + ".jpg";
        String root = Environment.getExternalStorageDirectory().toString();
     File
             myDir = new File(root + "/Xb");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            myDir = new File(myDir, name);

       return  myDir;

}}




