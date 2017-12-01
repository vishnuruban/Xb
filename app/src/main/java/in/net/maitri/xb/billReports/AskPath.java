package in.net.maitri.xb.billReports;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import in.net.maitri.xb.BuildConfig;
import in.net.maitri.xb.R;

public class AskPath extends Dialog {

    private RadioGroup mFolder;
    private RadioButton mDefault, mNew;
    private TextInputEditText mFolderName,mFileName;
    private Context mContext;
    private ArrayList mImageLink;
    private int mSuccess, mFailed;
    private byte[] excel;
    String type;
    File myDir;




    public AskPath(@NonNull Context context, byte[] excel) {
        super(context);
        mContext = context;
        this.excel = excel;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ask_path);

        String name = "Report"+System.currentTimeMillis();
        ImageView mClose = (ImageView) findViewById(R.id.close);
        Button mSave = (Button) findViewById(R.id.save);
        mFolder = (RadioGroup) findViewById(R.id.radiogroup);
        mFolderName = (TextInputEditText) findViewById(R.id.input_name);
        mFileName =(TextInputEditText) findViewById(R.id.file_name);
        mFileName.setText(name);

        mFolder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.defaultFolder:
                        mFolderName.setText("");
                        mFolderName.setVisibility(View.GONE);
                        break;

                    case R.id.newFolder:
                        mFolderName.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mFolder.getCheckedRadioButtonId()) {
                    case R.id.defaultFolder:
                        //saveImage(mImageLink,"");

                            if(saveExcel(excel,"",mFileName.getText().toString()))
                            {
                                cancel();
                                readExcel();
                            }

                        break;

                    case R.id.newFolder:
                        String foldername = mFolderName.getText().toString();
                        if (foldername.isEmpty()) {
                            Toast.makeText(mContext, "Folder name can't be blank.", Toast.LENGTH_SHORT).show();
                        } else {
                            File dir = new File(Environment.getExternalStorageDirectory() + "/Xb/"+foldername);
                            if(dir.isDirectory()) {
                                Toast.makeText(mContext, "Folder name exist.", Toast.LENGTH_SHORT).show();
                            }else {
                                //saveImage(mImageLink, foldername);

                                    if(saveExcel(excel,foldername,mFileName.getText().toString()))
                                    {
                                        cancel();
                                       readExcel();
                                    }

                            }
                        }
                        break;
                }
            }
        });
    }









    private boolean saveExcel(final byte[] excel,final String folder,String n)
    {

        String name = n+".xls";

        try {
            String root = Environment.getExternalStorageDirectory().toString();

            if (folder.isEmpty()) {
                myDir = new File(root + "/Xb");
            }else{
                myDir = new File(root + "/Xb/" + folder);
            }
            //   myDir1 = myDir;
            myDir.setReadable(true);

            if (!myDir.exists()) {
                myDir.mkdirs();
            }


            myDir = new File(myDir, name);
            myDir.setReadable(true);

            File futureStudioIconFile = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/" + name );
            FileUtils.writeByteArrayToFile(myDir, excel);


              return  true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }

    }



    private void readExcel()
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage("Excel downloaded successfully!. Do you want to open it?");
        dialog.setPositiveButton("YES", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            //   Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",myDir);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(myDir), "application/vnd.ms-excel");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    //// Snackbar.make(rootCoordinatorLayout, "No PDF reader found to open this file.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        dialog.setNegativeButton("NO", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

        });

        dialog.show();



    }

}

