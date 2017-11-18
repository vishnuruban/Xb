package in.net.maitri.xb.billing;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import in.net.maitri.xb.R;

/**
 * Created by SYSRAJ4 on 10/11/2017.
 */

public class FragmentOne extends Fragment {


    Button mCheckout,mclearBill;
    public static ArrayList<BillItems> billList;
    private ListView billListView;
    private static BillListAdapter billListAdapter;
    private static TextView bTotalProducts,bTotalPrice;
    static DecimalFormat df;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        billList = new ArrayList<BillItems>();
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        bTotalProducts = (TextView) view.findViewById(R.id.bTotalProducts);
        bTotalPrice    = (TextView) view.findViewById(R.id.bTotalPrice);

        mCheckout = (Button) view.findViewById(R.id.mCheckout);
        mclearBill = (Button) view.findViewById(R.id.mClearBill);
        billListView = (ListView) view.findViewById(R.id.bill_lv);

        billListAdapter = new BillListAdapter(getActivity(),billList);
        billListView.setAdapter(billListAdapter);
       // populateList();

        GradientDrawable bgShape = (GradientDrawable)mCheckout.getBackground();
        bgShape.setColor(getResources().getColor(R.color.green));
        GradientDrawable bgShape1 = (GradientDrawable)mclearBill.getBackground();
        bgShape1.setColor(getResources().getColor(R.color.dark_orange));


         df = new DecimalFormat("0.00");

        billListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                BillItems bi = billList.get(position);
                modifyItem(bi,billListAdapter);

            }
        });


        mCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(billList.size()==0)
                {
                    Toast.makeText(getActivity(),"Bill is empty",Toast.LENGTH_SHORT).show();
                }
                else {

                    Bundle bundle = new Bundle();
                    bundle.putString("products", bTotalProducts.getText().toString());
                    bundle.putString("price", bTotalPrice.getText().toString());
                    Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

        mclearBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog =new AlertDialog.Builder(getActivity());
                dialog.setTitle("Are you sure you want to clear the bill?");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        billList.clear();
                        billListAdapter.notifyDataSetChanged();

                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dialog.show();
            }
        });



        return  view;
    }





    public static void populateList(BillItems be)
    {

      /*  BillItems item1,item2,item3,item4;

        item1 = new BillItems("Annapurna salt 1 kg",2,18,36);
        billList.add(item1);
        item2 = new BillItems("Aaashirvad Atta 1 kg",2,45,90);
        billList.add(item2);
        item3 = new BillItems("Fair and lovely 100gm  new arrival",1,80.90,80.90);
        billList.add(item3);*/




      if(billList.size()!=0)
      {


          for(int i=0;i<billList.size();i++)
          {
              BillItems bItm = billList.get(i);

              if(bItm.getDesc().equals(be.getDesc()))
              {
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
      }
      else
      {
          billList.add(be);
          billListAdapter.notifyDataSetChanged();
    }

        UpdateProdPriceList();
    }



    private static void UpdateProdPriceList(){

        String rs = "\u20B9";
        try{
        byte[] utf8 = rs.getBytes("UTF-8");

        rs = new String(utf8, "UTF-8");}
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }


        double a = 0;
        bTotalProducts.setText("Products   "+billList.size());

        for(int i =0;i<billList.size();i++)
        {
            BillItems bi= billList.get(i);

             a  = a + bi.getAmount();
        }

        bTotalPrice.setText("Price("+rs+")   "+String.valueOf(df.format(a)));

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


        final RadioGroup billModifyGroup = (RadioGroup) formElementsView
                .findViewById(R.id.billModifyGroup);

        final TextInputEditText eQty = (TextInputEditText) formElementsView
                .findViewById(R.id.eQty);
        eQty.setVisibility(View.INVISIBLE);



        billModifyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {


                        if (checkedId == R.id.delete) {
                            eQty.setVisibility(View.INVISIBLE);
                        } else if (checkedId == R.id.cQty) {
                            //some code
                            eQty.setVisibility(View.VISIBLE);
                            eQty.setText(String.valueOf(bi.getQty()));
                        }
                    }
                });

        // the alert dialog
        AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(getActivity()).setView(formElementsView);
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
                }
                else {
                    if (qtyString.isEmpty()) {
                        Toast.makeText(getActivity(), "Enter Qty", Toast.LENGTH_SHORT).show();
                    }
                   else if(Integer.parseInt(qtyString)!=bi.getQty())
                    {
                        double a = Integer.parseInt(qtyString) * bi.getRate();
                        bi.setAmount(a);
                        bi.setQty(Integer.parseInt(qtyString));

                    }
                    else {
                        bi.setQty(Integer.parseInt(qtyString));

                    }
                }
                UpdateProdPriceList();
                billListAdapter.notifyDataSetChanged();
                dialog.cancel();
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
















}
