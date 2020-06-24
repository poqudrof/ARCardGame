package com.universitedebordeaux.jumathsji.download;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.universitedebordeaux.jumathsji.db.AppDatabase;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


// Task to manages the database update mechanism.
public class UpdateDBTask extends AsyncTask<String, Void, UpdateDBTask.Manifest> {
    private static final String MANIFEST = "manifest.yaml";

    // POJO to match Manifest file (manifest.yaml).
    public static class Manifest {
        public Manifest() {
            name = "unknown";
            version = "0.0.0";
            data = "null";
            figs = "null";
        }

        public String name;

        public String version;

        public String data;

        public String figs;
    }

    private Context mContext;
    private Yaml mParser;
    private PostUpdate mCallback;

    public UpdateDBTask(Context context, PostUpdate callback) {
        mContext = context;
        mParser = new Yaml(new Constructor(Manifest.class));
        mCallback = callback;
    }

    @Override
    protected Manifest doInBackground(String... uri) {
        Manifest major = new Manifest();
        for (int i = 0; i < uri.length; ++i) {

            // First, get the remote manifest.
            String url = "";
            try {
                url = new URI(uri[i]).resolve(MANIFEST).toString();
            } catch (URISyntaxException e) {
                Log.e("UpdateDBTask", "URL syntax incorrect");
            }

            try {
                major = mParser.load(download(url));
            } catch (Exception e) {
                Log.e("UpdateDBTask", "Manifest file malformed");
                e.printStackTrace();
            }

            if (major == null) return new Manifest();

            // Now we know the name of the database and can read the local manifest.
            File root = mContext.getExternalFilesDir(major.name);
            File file = new File(root, MANIFEST);

            // Maybe its the first start.
            if (file.exists()) {
                try {
                    Manifest minor = mParser.load(new FileInputStream(file));

                    // Check the version, maybe outdated.
                    if (test(major.version, minor.version)) upgrade(root, major);
                    else Log.i("UpdateDBTask", "Already up to date.");
                } catch (FileNotFoundException e) {
                    Log.e("UpdateDBTask", "Can not open manifest file");
                }
            } else upgrade(root, major);
        }

        return major;
    }

    // Download and return a stream.
    private InputStream download(String uri) {
        try {
            URL url = new URL(uri);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            return new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            Log.e("UpdateDBTask", "Download error : " + uri);
        }

        return new ByteArrayInputStream(new byte[0]);
    }

    // Check the version, use maven-artifact.
    private boolean test(String major, String minor) {
        return new DefaultArtifactVersion(major).compareTo(new DefaultArtifactVersion(minor)) > 0;
    }

    // Extracts files from the subfolders in the root folder.
    private void unzip(File root, ZipInputStream zip) {
        if (zip == null) return;
        if (!root.exists()) root.mkdirs();

        int count;
        byte[] buffer = new byte[1024];

        try {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;

                String filename = entry.getName();
                if (entry.getName().contains("/"))
                    filename = filename.substring(filename.lastIndexOf("/") + 1);
                FileOutputStream fout = new FileOutputStream(new File(root, filename));


                while ((count = zip.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                    fout.flush();
                }

                fout.close();
            }
        } catch (IOException e) {
            Log.e("UpdateDBTask", "Unzip failed");
            e.printStackTrace();
        }


    }

    // Write the Manifest in root folder.
    private void writeManifest(File root, Manifest mf) {
        try {
            FileWriter fw = new FileWriter(new File(root, MANIFEST));
            mParser.dump(mf, fw);
            fw.close();
        } catch (IOException e) {
            Log.e("UpdateDBTask", "Failed to upgrade manifest");
        }
    }

    // Run an update.
    private void upgrade(File root, Manifest mf) {
        Log.d("UpdateDBTask", "Upgrading " + mf.name + "...");

        ZipInputStream zip;

        // Download Cards
        File data = new File(root, "data");
        zip = new ZipInputStream(download(mf.data));
        unzip(data, zip);

        // Download Figures
        File figs = new File(root, "figs");
        zip = new ZipInputStream(download(mf.figs));
        unzip(figs, zip);

        // Update current manifest
        writeManifest(root, mf);

        // Reload the DB
        AppDatabase.reload(mContext, data);

        Log.d("UpdateDBTask", "Done !");

        AppDatabase db = AppDatabase.getInstance(mContext);
        Log.d("UpdateDBTask", "Entry Size : " + db.cardDao().getAll().size());
    }

    @Override
    protected void onPostExecute(Manifest mf) {
        if (mCallback != null) mCallback.updateDone(mf);
    }

    // Callback to do something after the update.
    public interface PostUpdate {
        void updateDone(Manifest mf);
    }
}
