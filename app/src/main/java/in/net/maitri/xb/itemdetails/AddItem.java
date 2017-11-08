package in.net.maitri.xb.itemdetails;

import android.app.Dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.io.IOException;

import in.net.maitri.xb.R;

public class AddItem extends DialogFragment {

    public static final int PICK_IMAGE = 1;
    private ImageView mItemImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.add_category, container, false);
        LinearLayout itemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
        itemLayout.setVisibility(View.VISIBLE);

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
                selectImage();
            }
        });

        final EditText categoryName = (EditText) view.findViewById(R.id.category_name);
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
                if (categoryName.getText().toString().isEmpty() || itemName.getText().toString().isEmpty()
                        || costPrice.getText().toString().isEmpty()
                        || sellingPrice.getText().toString().isEmpty()
                        || uom.getText().toString().isEmpty()
                        || gst.getText().toString().isEmpty()
                        || hsnCode.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Category name can't be empty.", Toast.LENGTH_SHORT).show();
                }else{
                    addCategory();
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

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {

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
        }

    }

    private void addCategory(){
        dismiss();
    }

}
