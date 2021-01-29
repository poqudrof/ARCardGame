package com.universitedebordeaux.jumathsji.download;

import android.content.Context;

import com.universitedebordeaux.jumathsji.db.AppDatabase;

import java.lang.ref.WeakReference;

// Task to manages the database update mechanism.
public class UpdateDBTask {
    // private static final String MANIFEST = "manifest.yaml";

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

    private final WeakReference<Context> mContext;
    // private final Yaml mParser;

    public UpdateDBTask(Context context) {
        mContext = new WeakReference<>(context);
        // mParser = new Yaml(new Constructor(Manifest.class));
    }

    // This function temporarily replaces the original one that you can see below until
    // we have a new manifest online
    public Manifest doInBackground(String uri) {
        AppDatabase.reload(mContext.get());
        // Return an empty manifest.
        return new Manifest();
    }

    // Due to the deletion of the original url, we are using a local database without the manifest.
    // So, this function is ignored.
    /*
     @Override
     public Manifest doInBackground(String uri) {
        Manifest major = new Manifest();

        // First, get the remote manifest.
        String url = "";
        try {
            url = new URI(s).resolve(MANIFEST).toString();
        } catch (URISyntaxException e) {
            Log.e("UpdateDBTask", "URL syntax incorrect");
        }

        try {
            major = mParser.load(download(url));
        } catch (Exception e) {
            Log.e("UpdateDBTask", "Manifest file malformed");
            e.printStackTrace();
        }

        if (major == null) {
            return new Manifest();
        }

        // Now we know the name of the database and can read the local manifest.
        File root = mContext.get().getExternalFilesDir(major.name);
        File file = new File(root, MANIFEST);

        // Maybe its the first start.
        if (file.exists()) {
            try {
                Manifest minor = mParser.load(new FileInputStream(file));

                // Check the version, maybe outdated.
                if (test(major.version, minor.version)) {
                    upgrade(root, major);
                }
                else Log.i("UpdateDBTask", "Already up to date.");
            } catch (FileNotFoundException e) {
                Log.e("UpdateDBTask", "Can not open manifest file");
            }
        } else {
            upgrade(root, major);
        }
        return major;
    }
    */

    // Download and return a stream.
    // Due to the deletion of the original url, we are using a local database without the manifest.
    // So, this function is ignored.
    /*
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
    */

    // Check the version, use maven-artifact.
    /*
    private boolean test(String major, String minor) {
        return new DefaultArtifactVersion(major).compareTo(new DefaultArtifactVersion(minor)) > 0;
    }
    */

    /*
    // Extracts files from the subfolders in the root folder.
    // Due to the deletion of the original url, we are using a local database without the manifest.
    // So, this function is ignored.
    private void unzip(File root, ZipInputStream zip) {
        if (zip == null) {
            return;
        }
        if (!root.exists()) {
            try {
                if (!root.mkdirs()) {
                    Log.w("UpdateDBTask", "The directory has not been created");
                }
            } catch (SecurityException e) {
                Log.e("UpdateDBTask", "Cannot create directory");
                e.printStackTrace();
                return;
            }
        }

        int count;
        byte[] buffer = new byte[1024];

        try {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                String filename = entry.getName();

                if (entry.getName().contains("/")) {
                    filename = filename.substring(filename.lastIndexOf("/") + 1);
                }
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
    */

    /*
    // Write the Manifest in root folder.
    // Due to the deletion of the original url, we are using a local database without the manifest.
    // So, this function is ignored.
    private void writeManifest(File root, Manifest mf) {
        try {
            FileWriter fw = new FileWriter(new File(root, MANIFEST));
            mParser.dump(mf, fw);
            fw.close();
        } catch (IOException e) {
            Log.e("UpdateDBTask", "Failed to upgrade manifest");
        }
    }
    */

    // Run an update.
    // Due to the deletion of the original url, we are using a local database without the manifest.
    // So, this function is ignored.
    /*
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
        AppDatabase.reload(mContext.get(), data);

        Log.d("UpdateDBTask", "Done !");

        AppDatabase db = AppDatabase.getInstance(mContext.get());
        Log.d("UpdateDBTask", "Entry Size : " + db.cardDao().getAll().size());
    }
    */
}