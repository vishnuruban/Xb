package in.net.maitri.xb.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import in.net.maitri.xb.R;

public class FilePicker extends AppCompatActivity {

    public final static int PICKFILE_RESULT_CODE=1;
    public final static String EXTRA_FILE_PATH = "file_path";
    protected File Directory;
    protected ArrayList<File> Files;
    protected FilePickerListAdapter Adapter;
    protected boolean ShowHiddenFiles = false;
    protected String[] acceptedFileExtensions;
    private TextView mNoBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_picker);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        LinearLayout browseMore = (LinearLayout) findViewById(R.id.browse_db);
        ListView backupListView = (ListView) findViewById(R.id.backup_lv);
        mNoBackup = (TextView) findViewById(R.id.no_backup);
        String root = Environment.getExternalStorageDirectory().toString();
        Directory = new File(root + "/XPand/Backup");
        Files = new ArrayList<>();
        Adapter = new FilePickerListAdapter(this, Files);
        backupListView.setAdapter(Adapter);
        acceptedFileExtensions = new String[]{};
        backupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                File newFile = (File) adapterView.getItemAtPosition(i);
                if (newFile.isFile()) {
                    Intent extra = new Intent();
                    extra.putExtra(EXTRA_FILE_PATH, newFile.getAbsolutePath());
                    setResult(RESULT_OK, extra);
                    finish();
                } else {
                    Directory = newFile;
                    refreshFilesList();
                }
            }
        });

        browseMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICKFILE_RESULT_CODE:
                    String file = data.getData().getPath();
                    Log.d("File", file);
                    if ( file.contains(".db")) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_FILE_PATH, data.getData().getPath());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(FilePicker.this,"This is not a backup file", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        refreshFilesList();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    protected void refreshFilesList() {

        Files.clear();
        ExtensionFilenameFilter filter =
                new ExtensionFilenameFilter(acceptedFileExtensions);
        File[] files = Directory.listFiles(filter);
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isHidden() && !ShowHiddenFiles) {
                    continue;
                }
                Files.add(f);
            }
            Collections.sort(Files, new FileComparator());
        }
        if (Files.isEmpty()) {
            mNoBackup.setVisibility(View.VISIBLE);
        } else {
            mNoBackup.setVisibility(View.GONE);
        }
        Adapter.notifyDataSetChanged();
    }

    private class FilePickerListAdapter extends ArrayAdapter<File> {
        private List<File> mObjects;

        public FilePickerListAdapter(Context context, List<File> objects) {
            super(context, R.layout.file_adapter, android.R.id.text1, objects);
            mObjects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.file_adapter, parent, false);
            } else
                row = convertView;
            File object = mObjects.get(position);
            ImageView imageView = (ImageView) row.findViewById(R.id.file_picker_image);
            TextView textView = (TextView) row.findViewById(R.id.file_picker_text);
            textView.setSingleLine(true);
            textView.setText(object.getName());
            imageView.setImageResource(R.mipmap.ic_database_grey600_36dp);
            return row;
        }
    }

    private class FileComparator implements Comparator<File> {

        public int compare(File f1, File f2) {
            if (f1 == f2)
                return 0;
            if (f1.isDirectory() && f2.isFile())
                // Show directories above files
                return -1;
            if (f1.isFile() && f2.isDirectory())
                // Show files below directories
                return 1;
            // Sort the directories alphabetically
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    }

    private class ExtensionFilenameFilter implements FilenameFilter {

        private String[] Extensions;

        public ExtensionFilenameFilter(String[] extensions) {
            super();
            Extensions = extensions;
        }

        public boolean accept(File dir, String filename) {
            if (new File(dir, filename).isDirectory()) {
                // Accept all directory names
                return true;
            }

            if (Extensions != null && Extensions.length > 0) {
                for (int i = 0; i < Extensions.length; i++) {
                    if (filename.endsWith(Extensions[i])) {
                        // The filename ends with the extension
                        return true;
                    }
                }
                // The filename did not match any of the extensions
                return false;
            }
            // No extensions has been set. Accept all file extensions.
            return true;
        }
    }
}
