package in.net.maitri.xb.itemdetails;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.DbHandler;
import in.net.maitri.xb.db.HsnMst;
import in.net.maitri.xb.db.Item;
import in.net.maitri.xb.db.TaxMst;
import in.net.maitri.xb.db.Unit;
import in.net.maitri.xb.settings.GetSettings;

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
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_item, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAddItemCategory);
        final String registrationType = new GetSettings(mAddItemCategory).getCompanyRegistrationType();
        final String selectedCategoryName = sharedPreferences.getString("catName", "");
        final int selectedCategoryId = sharedPreferences.getInt("catId", 0);
        TextView dialogHeader = view.findViewById(R.id.dialog_header);
        String headerText = "Add Item to " + selectedCategoryName;
        dialogHeader.setText(headerText);

        final LinearLayout newUomLayout = view.findViewById(R.id.new_uom_layout);

        LinearLayout itemLayout = view.findViewById(R.id.item_layout);
        itemLayout.setVisibility(View.VISIBLE);
        dbHandler = new DbHandler(getActivity());
        mItemImage = view.findViewById(R.id.item_image);
        ImageView close = view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button browseImage = view.findViewById(R.id.browse_image);
        browseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage();
            }
        });

        // final EditText categoryNameField = (EditText) view.findViewById(R.id.category_name);
        //categoryNameField.setText(selectedCategoryName);
        // categoryNameField.setEnabled(false);
        final EditText itemNameField = view.findViewById(R.id.item_name);
        final EditText costPriceField = view.findViewById(R.id.cp);
        final EditText sellingPriceField = view.findViewById(R.id.sp);
        final Spinner hsnCodeField = view.findViewById(R.id.hsn_code);
        final Spinner gstField = view.findViewById(R.id.gst);
        final EditText newUomField = view.findViewById(R.id.new_uom);
        final Spinner uomField = view.findViewById(R.id.uom);
        final Switch decimalAllowed = view.findViewById(R.id.decimal_allowed);
        final EditText barcode = view.findViewById(R.id.barcode);
        final EditText newHsnField = view.findViewById(R.id.new_hsn);

        if (registrationType.equals("3")) {
            hsnCodeField.setVisibility(View.GONE);
            gstField.setVisibility(View.GONE);
            uomField.setVisibility(View.GONE);
            newUomField.setVisibility(View.GONE);
            decimalAllowed.setVisibility(View.GONE);
            barcode.setVisibility(View.GONE);
        }

        List<Unit> unitList = dbHandler.getAllUnit();
        final ArrayList<String> uomAdapter = new ArrayList<>();
        uomAdapter.add("--Select Uom--");
        for (int i = 0; i < unitList.size(); i++) {
            uomAdapter.add(unitList.get(i).getDesc());
        }
        uomAdapter.add("Enter new UOM");
        uomField.setAdapter(createAdapter(uomAdapter));

        List<TaxMst> taxList = dbHandler.getAllTaxRates();
        final ArrayList<String> taxAdapter = new ArrayList<>();
        final HashMap<String, Integer> gstMap = new HashMap<>();
        taxAdapter.add("--Select Tax--");
        for (int i = 0; i < taxList.size(); i++) {
            taxAdapter.add(String.valueOf(taxList.get(i).getTaxRate()));
            gstMap.put(String.valueOf(taxList.get(i).getTaxRate()), taxList.get(i).getTaxCode());
        }
        gstField.setAdapter(createAdapter(taxAdapter));

        List<HsnMst> hsnList = dbHandler.getAllHsn();
        final ArrayList<String> hsnAdapter = new ArrayList<>();
        hsnAdapter.add("--Select Hsn--");
        for (int i = 0; i < hsnList.size(); i++) {
            hsnAdapter.add(String.valueOf(hsnList.get(i).getHsnCode()));
        }
        hsnAdapter.add("Enter new HSN");
        hsnCodeField.setAdapter(createAdapter(hsnAdapter));


        Button addDetails = view.findViewById(R.id.add_details);
        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String catName = categoryNameField.getText().toString();
                String catName = selectedCategoryName;
                String iteName = itemNameField.getText().toString();
                String cp = costPriceField.getText().toString();
                String sp = sellingPriceField.getText().toString();
                String uoM;
                if (newUomField.isEnabled()) {
                    uoM = newUomField.getText().toString();
                } else {
                    uoM = uomField.getSelectedItem().toString();
                }
                String gsT = gstField.getSelectedItem().toString();
                String hsn;
                if (newHsnField.isEnabled()) {
                    hsn = newHsnField.getText().toString();
                } else {
                    hsn = hsnCodeField.getSelectedItem().toString();
                }

                String getBarCode = barcode.getText().toString();
                if (getBarCode.isEmpty()) {
                    getBarCode = "";
                }

                if (iteName.isEmpty()) {
                    Toast.makeText(getActivity(), "Item name can't be empty.", Toast.LENGTH_SHORT).show();
                } else if (cp.isEmpty()) {
                    Toast.makeText(getActivity(), "Cost price can't be empty.", Toast.LENGTH_SHORT).show();
                } else if (sp.isEmpty()) {
                    Toast.makeText(getActivity(), "Selling price can't be empty.", Toast.LENGTH_SHORT).show();
                } else if (Float.valueOf(cp) > Float.valueOf(sp)) {
                    Toast.makeText(getActivity(), "Cost Price can't be greater than selling price.", Toast.LENGTH_SHORT).show();
                } else if (!registrationType.equals("3")) {
                    if (uoM.isEmpty()) {
                        Toast.makeText(getActivity(), "UOM can't be empty.", Toast.LENGTH_SHORT).show();
                    } else if (hsn.isEmpty()) {
                        Toast.makeText(getActivity(), "HSN can't be empty.", Toast.LENGTH_SHORT).show();
                    } else if (gsT.isEmpty()) {
                        Toast.makeText(getActivity(), "GST can't be empty.", Toast.LENGTH_SHORT).show();
                    }else {
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
                        if (gsT.isEmpty()) {
                            gsT = "0.0";
                        }
                        String uomValue = "0";
                        if (!uoM.equals("--Select Uom--")) {
                            uomValue = String.valueOf(dbHandler.getUomId(uoM));
                        }
                        copyImage();
                        addItem(iteName, uomValue, Float.valueOf(cp), Float.valueOf(sp),
                                hsn, Float.valueOf(gsT), selectedCategoryId, getBarCode, gstMap.get(gsT));
                        mAddItemCategory.updateItem(selectedCategoryId);
                    }
                } else {
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
                    if (gsT.isEmpty()) {
                        gsT = "0.0";
                    }
                    String uomValue = "0";
                    if (!uoM.equals("--Select Uom--")) {
                        uomValue = String.valueOf(dbHandler.getUomId(uoM));
                    }
                    copyImage();
                    addItem(iteName, uomValue, Float.valueOf(cp), Float.valueOf(sp),
                            hsn, Float.valueOf(gsT), selectedCategoryId, getBarCode, gstMap.get(gsT));
                    mAddItemCategory.updateItem(selectedCategoryId);
                }
            }
        });

        uomField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (uomAdapter.get(position).equals("Enter new UOM")) {
                    newUomField.setEnabled(true);
                    newUomField.requestFocus();
                    newUomLayout.setVisibility(View.VISIBLE);
                } else {
                    newUomLayout.setVisibility(View.GONE);
                    newUomField.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final LinearLayout newHsnLayout = view.findViewById(R.id.new_hsn_layout);

        hsnCodeField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (hsnAdapter.get(position).equals("Enter new HSN")) {
                    newHsnLayout.setVisibility(View.VISIBLE);
                    newHsnField.setEnabled(true);
                    newHsnField.requestFocus();
                } else {
                    newHsnLayout.setVisibility(View.GONE);
                    newHsnField.setEnabled(false);
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
        Log.d("Image Result", "1");
        mSelectedImage = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);
        Log.d("Image Result", "2");
        mItemImage.setImageBitmap(mSelectedImage);
        Log.d("Image Result", "3");
    }

    public void onPickImage() {
        ImagePicker.pickImage(AddItem.this, "Select your image:");
    }

    private void copyImage() {
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
        String name = "XPand_img" + timeStamp + ".jpg";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/XPand/Images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        myDir = new File(myDir, name);
        return myDir;
    }


    private void addItem(String itemName, String itemUOM, float itemCP, float itemSP,
                         String itemHSNcode, float itemGST, int categoryId, String barcode, int gstId) {
        Item item = new Item(itemName, itemUOM, itemCP, itemSP, itemHSNcode, itemGST, categoryId,
                mImagePath, barcode, gstId);
        dbHandler.addItem(item);
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
}
