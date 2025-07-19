package my.consler.catthebuilder.build;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import my.consler.catthebuilder.utils.*;

import java.io.*;


public class Build
{

    private static final String tag = "Build.java";

    @SuppressLint("SetTextI18n")
    public static void start(Context context, String app_name, String package_name, String app_version, String file_name, TextView action)
    {
        new Thread(() -> {
            Log.d(tag, "Starting build");
            action.setText("Copying assets to cache...");
            File f = new File(context.getCacheDir(), file_name);
            try
            {
                if (! new File(context.getCacheDir(), "CATGAME").exists())
                {
                    Assets.copyFolderFromAssets(context, "CATGAME", Thread.activeCount());
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


            try
            {
                Zip.unzip(String.valueOf( new File(context.getCacheDir(),"/CATGAME/assets/CATGAME.zip").toPath()), String.valueOf( new File( context.getCacheDir(), "CATGAME/assets/CATGAME").toPath()));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            action.setText("Unzipped .catrobat");

            f.delete();
            action.setText("Deleted .catrobat");




            Log.d(tag, "Deleting old APK");
            new File(context.getCacheDir(), "CATGAME.apk").delete();

            action.setText("Building APK...");

            try
            {
                Zip.zipFolderContentsStored(String.valueOf( new File(context.getCacheDir(), "CATGAME").toPath()), String.valueOf( new File(context.getCacheDir(), "CATGAME.apk").toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Log.d(tag, "Zipped APK");
            action.setText("APK built");

            Assets.copyAssetToCache(context, "ks.p12");
            Log.d(tag, "Copied ks.p12 to cache dir");

            File catgame = new File(context.getCacheDir(), "CATGAME.apk");

            action.setText("Updating AndroidManifest");
            Log.d(tag, "Updating Manifest");
            try
            {
                Manifest.change(catgame, package_name, app_name, app_version);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            File ks_direcotry = new File(context.getCacheDir(), "ks.p12");
            Log.d(tag, String.valueOf(ks_direcotry.exists()));
            File out_game = new File(context.getCacheDir(), "CATGAME_signed.apk");
            out_game.delete();
            try
            {
                Sign.sign(catgame, out_game, ks_direcotry, "password", "cert2", "password");
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            action.setText("APK signed and zipaligned");
            Log.d(tag, "Apk signed and zipaligned");

            Log.d("Build.java", "Signed APK size: " + out_game.length());
            ((Activity) context).runOnUiThread(() -> Export.shareFile((Activity) context, out_game));
            action.setText("APK installed");
            Log.d(tag, "Done!");
        }).start();

    }

}
