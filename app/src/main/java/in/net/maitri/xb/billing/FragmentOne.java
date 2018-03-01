package in.net.maitri.xb.billing;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.Customer;
import in.net.maitri.xb.db.DbHandler;

public class FragmentOne extends Fragment implements View.OnClickListener {


    Button mCheckout, mclearBill;
    public static ArrayList<BillItems> billList;
    private ListView billListView;
    private static BillListAdapter billListAdapter;
    private static TextView bTotalProducts, bTotalPrice;
    static DecimalFormat df, df1;
    ImageButton imgCustomer;
    public static AutoCompleteTextView autoCustomer;
    CustomerAdapter customerAdapter;
    DbHandler dbHandler;
    LinearLayout custScreen;
    Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven,
            btn_eight, btn_nine, btn_zero, btn_point, btn_clear;
    TextInputEditText eQty;
    ArrayList<Customer> customerArrayList;
    public static Customer customerDetails;
    static double a = 0;
    Customer customer;
    int customerId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        billList = new ArrayList<BillItems>();
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        bTotalProducts = (TextView) view.findViewById(R.id.bTotalProducts);
        bTotalPrice = (TextView) view.findViewById(R.id.bTotalPrice);
        custScreen = (LinearLayout) view.findViewById(R.id.customerscrren);
        dbHandler = new DbHandler(getActivity());
        mCheckout = (Button) view.findViewById(R.id.mCheckout);
        mclearBill = (Button) view.findViewById(R.id.mClearBill);
        billListView = (ListView) view.findViewById(R.id.bill_lv);
        imgCustomer = (ImageButton) view.findViewById(R.id.selectCustomer);
        billListAdapter = new BillListAdapter(getActivity(), billList);
        billListView.setAdapter(billListAdapter);
        autoCustomer = (AutoCompleteTextView) view.findViewById(R.id.autoSearch);
        // populateList();
        customerArrayList = new ArrayList<>();
        customerArrayList = dbHandler.getAllCustomer();
        customer = new Customer();

        autoCustomer.setOnEditorActionListener(new DoneOnEditorActionListener());

        customerAdapter = new CustomerAdapter(getActivity(), customerArrayList, "NAME");
        autoCustomer.setAdapter(customerAdapter);


        autoCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customerDetails = (Customer) customerAdapter.getItem(position);
                autoCustomer.setText(customerDetails.getName());
                customerId = customerDetails.getId();
                System.out.println("CUSTID " + customerId);
                customer.setId(customerId);
                customer.setName(customerDetails.getName());
                customer.setMobileno(customerDetails.getMobileno());

            }
        });

        GradientDrawable bgShape = (GradientDrawable) mCheckout.getBackground();
        bgShape.setColor(getResources().getColor(R.color.dark_green));
        GradientDrawable bgShape1 = (GradientDrawable) mclearBill.getBackground();
        bgShape1.setColor(getResources().getColor(R.color.red));


        df = new DecimalFormat("0.00");
        df1 = new DecimalFormat("#,###");


        billListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                BillItems bi = billList.get(position);
                modifyItem(bi, billListAdapter);
            }
        });


        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCustomerDialog customerDialog = new AddCustomerDialog(getActivity(), customerAdapter, customerArrayList, autoCustomer.getText().toString());
                customerDialog.setCancelable(false);
                customerDialog.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                customerDialog.show();
            }
        });


        mCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billList.size() == 0) {
                    Toast.makeText(getActivity(), "Bill is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("products", bTotalProducts.getText().toString());
                    bundle.putString("price", String.valueOf(df.format(a)));
                    if (customerId == 0) {
                        customer.setId(0);
                        customer.setName(autoCustomer.getText().toString().trim());
                    }
                    Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                    intent.putExtras(bundle);
                    Log.i("CustomerName", customer.getName());
                    Log.i("CustomerId", String.valueOf(customer.getId()));
                    intent.putExtra("customer", customer);
                    startActivity(intent);
                }
            }
        });


        mclearBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (billList.size() == 0) {
                    Toast.makeText(getActivity(), "No Bills Found!", Toast.LENGTH_SHORT).show();
                    return;
                }


                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Are you sure you want to clear the bill?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        billList.clear();
                        bTotalProducts.setText("");
                        bTotalPrice.setText("");
                        billListAdapter.notifyDataSetChanged();

                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }


    public static void populateList(BillItems be) {

        if (billList.size() != 0) {


            for (int i = 0; i < billList.size(); i++) {
                BillItems bItm = billList.get(i);

                if (bItm.getDesc().equals(be.getDesc()) && bItm.getRate() == be.getRate()) {
                    System.out.println("Items Equalled");
                    int qty = bItm.getQty() + be.getQty();
                    double amt = bItm.getAmount() + be.getAmount();
                    billList.remove(bItm);
                    be.setQty(qty);
                    be.setAmount(amt);

                }

            }
            billList.add(be);
            billListAdapter.notifyDataSetChanged();
        } else {
            billList.add(be);
            billListAdapter.notifyDataSetChanged();
        }

        UpdateProdPriceList();
    }


    private static void UpdateProdPriceList() {
        double b = 0;
        String rs = "\u20B9";
        try {
            byte[] utf8 = rs.getBytes("UTF-8");
            rs = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        bTotalProducts.setText("");
        bTotalProducts.setText("Products   " + billList.size());
        for (int i = 0; i < billList.size(); i++) {
            BillItems bi = billList.get(i);

            b = b + bi.getAmount();
        }
        if (b == 0) {
            bTotalPrice.setText("Price(" + rs + ")   " + "");
        } else {
            a = b;
            bTotalPrice.setText("Price(" + rs + ")   " + commaSeperated(b));
        }
    }


    public static String commaSeperated(double s) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(s);
    }


    public void modifyItem(final BillItems bi, BillListAdapter adapter) {

    /*
     * Inflate the XML view. activity_main is in
     * res/layout/form_elements.xml
     */
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.bill_modify_dialog,
                null, false);

        // You have to list down your form elements
        final RadioGroup billModifyGroup = (RadioGroup) formElementsView.findViewById(R.id.billModifyGroup);
        final LinearLayout lGrid = (LinearLayout) formElementsView.findViewById(R.id.grid);
        eQty = (TextInputEditText) formElementsView.findViewById(R.id.eQty);
        eQty.setInputType(InputType.TYPE_NULL);


        eQty.setText(String.valueOf(bi.getQty()));
        // eQty.setSelection(eQty.getText().length());
        eQty.addTextChangedListener(watch);
        eQty.selectAll();

        btn_one = (Button) formElementsView.findViewById(R.id.btn_one);
        btn_two = (Button) formElementsView.findViewById(R.id.btn_two);
        btn_three = (Button) formElementsView.findViewById(R.id.btn_three);
        btn_four = (Button) formElementsView.findViewById(R.id.btn_four);
        btn_five = (Button) formElementsView.findViewById(R.id.btn_five);
        btn_six = (Button) formElementsView.findViewById(R.id.btn_six);
        btn_seven = (Button) formElementsView.findViewById(R.id.btn_seven);
        btn_eight = (Button) formElementsView.findViewById(R.id.btn_eight);
        btn_nine = (Button) formElementsView.findViewById(R.id.btn_nine);
        btn_zero = (Button) formElementsView.findViewById(R.id.btn_zero);
        btn_point = (Button) formElementsView.findViewById(R.id.btn_point);
        btn_clear = (Button) formElementsView.findViewById(R.id.btn_clear);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_six.setOnClickListener(this);
        btn_seven.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_nine.setOnClickListener(this);
        btn_zero.setOnClickListener(this);
        btn_point.setOnClickListener(this);
        btn_point.setEnabled(false);
        btn_clear.setOnClickListener(this);
        billModifyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.delete) {
                    eQty.setVisibility(View.GONE);
                    lGrid.setVisibility(View.GONE);
                } else if (checkedId == R.id.cQty) {
                    //some code
                    eQty.requestFocus();
                    eQty.setVisibility(View.VISIBLE);
                    lGrid.setVisibility(View.VISIBLE);
                    eQty.setText(String.valueOf(bi.getQty()));
                    eQty.setSelection(eQty.getText().length());
                }
            }
        });

        // the alert dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity()).setView(formElementsView);
        alertDialogBuilder.setTitle("Select Action");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @TargetApi(11)
            public void onClick(DialogInterface dialog, int id) {

                String qtyString = eQty.getText().toString();
                int selectedId = billModifyGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) formElementsView
                        .findViewById(selectedId);
                String selectedButton = selectedRadioButton.getText().toString();
                if (selectedButton.equals("Delete Item")) {
                    billList.remove(bi);
                    UpdateProdPriceList();
                    billListAdapter.notifyDataSetChanged();
                    dialog.cancel();
                } else {
                    if (qtyString.isEmpty()) {
                        Toast.makeText(getActivity(), "Enter Qty", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        bi.setQty(0);
                        modifyItem(bi, billListAdapter);
                    } else if (qtyString.equals("0")) {
                        Toast.makeText(getActivity(), "Qty can't be zero.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        bi.setQty(0);
                        modifyItem(bi, billListAdapter);
                    }else{
                        if (Integer.parseInt(qtyString) != bi.getQty()) {
                            double a = Integer.parseInt(qtyString) * bi.getRate();
                            bi.setAmount(a);
                            bi.setQty(Integer.parseInt(qtyString));
                        } else {
                            bi.setQty(Integer.parseInt(qtyString));
                        }
                        UpdateProdPriceList();
                        billListAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                }

            }

        });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @TargetApi(11)
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_one:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");
                eQty.setText(eQty.getText().toString() + btn_one.getText().toString());
                break;

            case R.id.btn_two:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");
                eQty.setText(eQty.getText().toString() + btn_two.getText().toString());
                break;

            case R.id.btn_three:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");
                eQty.setText(eQty.getText().toString() + btn_three.getText().toString());
                break;

            case R.id.btn_four:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");
                eQty.setText(eQty.getText().toString() + btn_four.getText().toString());
                break;

            case R.id.btn_five:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");
                eQty.setText(eQty.getText().toString() + btn_five.getText().toString());
                break;

            case R.id.btn_six:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");
                eQty.setText(eQty.getText().toString() + btn_six.getText().toString());
                break;

            case R.id.btn_seven:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");
                eQty.setText(eQty.getText().toString() + btn_seven.getText().toString());
                break;

            case R.id.btn_eight:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");
                eQty.setText(eQty.getText().toString() + btn_eight.getText().toString());
                break;

            case R.id.btn_nine:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");

                eQty.setText(eQty.getText().toString() + btn_nine.getText().toString());
                break;

            case R.id.btn_zero:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");

                eQty.setText(eQty.getText().toString() + btn_zero.getText().toString());
                break;

            case R.id.btn_point:
                if (!getSelectedText().isEmpty())
                    eQty.setText("");
                eQty.setText(eQty.getText().toString() + btn_point.getText().toString());
                break;

            case R.id.btn_clear:
                int length = eQty.getText().length();
                if (length > 0) {
                    eQty.getText().delete(length - 1, length);
                }
                //  et_result.setText("");
                break;
        }
    }

    TextWatcher watch = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            String searchString = s.toString();
            int textLength = searchString.length();
            eQty.setSelection(textLength);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
        }
    };

    public String getSelectedText() {
        int startSelection = eQty.getSelectionStart();
        int endSelection = eQty.getSelectionEnd();
        String selectedText = eQty.getText().toString().substring(startSelection, endSelection);
        return selectedText;
    }


    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        }
    }


}
