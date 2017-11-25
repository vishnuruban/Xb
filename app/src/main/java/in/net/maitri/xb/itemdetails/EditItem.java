package in.net.maitri.xb.itemdetails;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.db.Unit;
import in.net.maitri.xb.settings.GetSettings;

public class EditItem extends DialogFragment {

    private ImageView mItemImage;
    private String mImagePath;
    private DbHandler dbHandler;
    private Bitmap mSelectedImage;
    private AddItemCategory mAddItemCategory;
    private List<Unit> unitList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAddItemCategory = (AddItemCategory) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.add_item, container, false);

        Bundle bundle = getArguments();
        final Item item = (Item) bundle.getSerializable("itemObj");
        final String registrationType = new GetSettings(mAddItemCategory).getCompanyRegistrationType();

        TextView dialogHeader = (TextView) view.findViewById(R.id.dialog_header);
        dialogHeader.setText("Edit Item");
        final LinearLayout newUomLayout = (LinearLayout) view.findViewById(R.id.new_uom_layout);
        LinearLayout itemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
        itemLayout.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAddItemCategory);
        final String selectedCategoryName = sharedPreferences.getString("catName", "");
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
        final Switch decimalAllowed = (Switch) view.findViewById(R.id.decimal_allowed);
        final EditText itemNameField = (EditText) view.findViewById(R.id.item_name);
        if (item != null) {
            itemNameField.setText(item.getItemName());
        }
        final EditText costPriceField = (EditText) view.findViewById(R.id.cp);
        if (item != null) {
            costPriceField.setText(String.valueOf(item.getItemCP()));
        }
        final EditText sellingPriceField = (EditText) view.findViewById(R.id.sp);
        if (item != null) {
            sellingPriceField.setText(String.valueOf(item.getItemSP()));
        }
        final Spinner uomField = (Spinner) view.findViewById(R.id.uom);
        unitList = dbHandler.getAllUnit();
        final ArrayList<String> uomAdapter = new ArrayList<>();
        uomAdapter.add("--Select Uom--");
        for (int i = 0; i < unitList.size(); i++) {
            uomAdapter.add(unitList.get(i).getDesc());
        }
        uomAdapter.add("Enter new UOM");
        uomField.setAdapter(createAdapter(uomAdapter));
        final EditText newUomField = (EditText) view.findViewById(R.id.new_uom);
        if (item != null ) {
           uomField.setSelection(uomAdapter.indexOf(getUnitName(Integer.parseInt(item.getItemUOM()))));
        }
        final EditText hsnCodeField = (EditText) view.findViewById(R.id.hsn_code);
        if (item != null) {
            hsnCodeField.setText(item.getItemHSNcode());
        }
        final EditText gstField = (EditText) view.findViewById(R.id.gst);
        if (item != null) {
            gstField.setText(String.valueOf(item.getItemGST()));
        }
        if (item != null) {
            mImagePath = item.getItemImage();
            File imgFile = new File(mImagePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mItemImage.setImageBitmap(myBitmap);
            }
        }

        if (registrationType.equals("3")) {
            hsnCodeField.setVisibility(View.GONE);
            gstField.setVisibility(View.GONE);
        }

        Button addDetails = (Button) view.findViewById(R.id.add_details);
        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String catName = selectedCategoryName;
                String iteName =itemNameField.getText().toString();
                String cp= costPriceField.getText().toString();
                String sp = sellingPriceField.getText().toString();
                String uoM;
                if (newUomField.isEnabled()) {
                    uoM = newUomField.getText().toString();
                } else {
                    uoM = uomField.getSelectedItem().toString();
                }
                String gsT = gstField.getText().toString();
                String hsn = hsnCodeField.getText().toString();


                if (!registrationType.equals("3") && (catName.isEmpty() || iteName.isEmpty()
                        || cp.isEmpty()
                        || sp.isEmpty()
                        || uoM.isEmpty()
                        || gsT.isEmpty()
                        || hsn.isEmpty())) {
                    Toast.makeText(getActivity(), "Enter all the fields.", Toast.LENGTH_SHORT).show();
                } else if (registrationType.equals("3") && (iteName.isEmpty()
                        || cp.isEmpty()
                        || sp.isEmpty())){
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
                    if (newUomField.isEnabled()) {
                        Unit unit = new Unit();
                        unit.setDesc(uoM);
                        if (decimalAllowed.isChecked()) {
                            unit.setDecimalAllowed(1);
                        } else {
                            unit.setDecimalAllowed(0);
                        }
                        addUom(unit);
                    }
                    if (gsT.isEmpty()){
                        gsT = "0";
                    }
                    String uomValue = "0";
                    if (!uoM.equals("--Select Uom--")){
                        uomValue = String.valueOf(dbHandler.getUomId(uoM));
                    }
                    item1.setItemImage(mImagePath);
                    item1.setItemName(iteName);
                    item1.setItemCP(Float.parseFloat(cp));
                    item1.setItemSP(Float.parseFloat(sp));
                    item1.setItemUOM(uomValue);
                    Log.d("UOM", uoM);
                    item1.setItemHSNcode(hsn);
                    item1.setItemGST(Float.parseFloat(gsT));
                    editItem(item1);
                    mAddItemCategory.updateItem(item.getCategoryId());
                }
            }
        });

        uomField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (uomAdapter.get(position).equals("Enter new UOM")){
                    newUomField.requestFocus();
                    newUomLayout.setVisibility(View.VISIBLE);
                } else {
                    newUomLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    private void addUom(Unit unit) {
        dbHandler.addUnit(unit);
    }


    private ArrayAdapter<String> createAdapter(ArrayList<String> list) {
        return new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_dropdown_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
    }

    private String getUnitName(int unitId){
        for (int i = 0; i<unitList.size(); i++){
            if (unitList.get(i).getId() == unitId){
                return unitList.get(i).getDesc();
            }
        }
        return "--Select Uom--";
    }
}
