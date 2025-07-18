package my.consler.catthebuilder.build;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import at.favre.tools.apksigner.SignTool;

import my.consler.catthebuilder.MainActivity;
import my.consler.catthebuilder.utils.*;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Build
{

    private static final String tag = "Build.java";

    public static void start(Context context, String app_name, String package_name, String app_version, String file_name, TextView action) throws IOException
    {;
        Log.d(tag, "Starting build");
        action.setText("Building...");

        File f = new File(context.getCacheDir(), file_name);


        try
        {
            if (! new File(context.getCacheDir(), "CATGAME").exists())
            {
                AssetsCopier.copyFolderFromAssets(context, "CATGAME", Thread.activeCount());
                Log.d(tag, "Assets copy done!");

            }

        }
        catch (IOException | InterruptedException e)
        {
            Log.e(tag, "Assets copy failed", e);
        }

        new File(context.getCacheDir(), "CATGAME/assets/CATGAME").delete();
        new File(context.getCacheDir(), "CATGAME/assets/CATGAME").mkdirs();

        boolean b = f.renameTo(new File(context.getCacheDir(), "CATGAME/assets/CATGAME.zip"));
        Log.d(tag, "Renamed .catrobat to assets/CATGAME.zip");
        Log.d(tag, String.valueOf( b));

        action.setText("Moved .catrobat to cache dir");
        Log.d(tag, "Moved .catrobat to cache dir");


        Zip.unzip(String.valueOf( new File(context.getCacheDir(),"/CATGAME/assets/CATGAME.zip").toPath()), String.valueOf( new File( context.getCacheDir(), "CATGAME/assets/").toPath()));

        action.setText("Unzipped .catrobat");

        f.delete();
        action.setText("Deleted .catrobat");


        action.setText("Updating AndroidManifest");
        Log.d(tag, "Updating Manifest");
        String DummyManifest = ReadDummyManifest.read(context, "DummyManifest.xml");

        DummyManifest = DummyManifest.replace("CATGAME", app_name);
        DummyManifest = DummyManifest.replace("my.catgame", package_name);


        FileWriter fw;
        fw = new FileWriter(context.getCacheDir() + "/CATGAME/assets/CATGAME/AndroidManifest.xml");
        fw.write(DummyManifest);
        fw.close();

        Log.d(tag, "Deleting old APK");
        new File(context.getCacheDir(), "CATGAME.apk").delete();

        action.setText("Building APK...");

        Zip.zipFolderContentsStored(String.valueOf( new File(context.getCacheDir(), "CATGAME").toPath()), String.valueOf( new File(context.getCacheDir(), "CATGAME.apk").toPath()));
        Log.d(tag, "Zipped APK");
        action.setText("APK built");

        AssetsCopier.copyAssetToCache(context, "ks.jks");
        Log.d(tag, "Copied ks.jks to cache dir");

        File catgame = new File(context.getCacheDir(), "CATGAME.apk");
        File ks_direcotry = new File(context.getCacheDir(), "ks.p12");
        File out_game = new File(context.getCacheDir(), "CATGAME_signed.apk");
        //SignTool.main(new String[]{"-a", catgame.getPath(), "-o", context.getCacheDir().getPath(), "--ks", ks_direcotry.getPath(), "--ksKeyPass", "password", "--ksPass", "--password", "--ksAlias", "cert2",  "--skipZipAlign"});
        try
        {
            SignApk.sign(catgame, out_game, ks_direcotry, "password", "cert2", "password");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        action.setText("APK signed and zipaligned");
        Log.d(tag, "Apk signed and zipaligned");

        ApkInstaller.promptInstall(context, "CATGAME.apk");
        action.setText("APK installed");
        Log.d(tag, "Done!");

    }

}
