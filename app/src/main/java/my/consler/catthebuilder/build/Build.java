package my.consler.catthebuilder.build;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.apksig.apk.ApkFormatException;
import com.android.apksig.internal.apk.AndroidBinXmlParser;
import my.consler.catthebuilder.R;
import my.consler.catthebuilder.utils.*;

import java.io.*;


public class Build
{

    private static final String tag = "Build.java";

    public static boolean is_running = false;

    public static String apk_name;

    public static void set_apk_name(String name) {apk_name = name + ".apk";} // setter
    public static String get_apk_name() {return apk_name;} // getter

    public static void start(Context context, String app_name, String package_name, String app_version, String version_code, TextView action)
    {
        if (!is_running) // to not run more than 1 concurrently
        {
            new Thread(() ->
            {
                is_running = true;

                Log.d(tag, "Starting build");

                set_apk_name(app_name);

                try
                {
                    if (! new File(context.getCacheDir(), "CATGAME").exists()) // only copy assets if not already done
                    {
                        action.setText( context.getString(R.string.copying_assets_to_cache));
                        Assets.copyFolderFromAssets(context, "CATGAME", Thread.activeCount());
                        Log.d(tag, "Assets copy done!");

                    }

                }
                catch (IOException | InterruptedException e)
                {
                    Log.e(tag, "Assets copy failed", e);
                }

                new File(context.getCacheDir(), "CATGAME/assets/CATGAME").delete(); //deleting old user catrobat stuff
                new File(context.getCacheDir(), "CATGAME/assets/CATGAME").mkdirs();


                File f = new File(context.getCacheDir(), "CATGAME.catrobat"); // the file to be unzipped
                try
                {
                    Zip.unzip(String.valueOf( f.toPath()), String.valueOf( new File( context.getCacheDir(), "CATGAME/assets/CATGAME").toPath())); // unzipping the file so CATGAME could load it, this allows for a slightly faster loading speed than cbuilder

                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);

                }

                try
                {
                    Icon.change(context);
                }
                catch (IOException e)
                {
                     throw new RuntimeException(e);
                }


                // for now we just delete these, but later there is gonna be an option to not
//                new File(context.getCacheDir(), "CATGAME/assets/CATGAME/lib/x86").delete();
//                new File(context.getCacheDir(), "CATGAME/assets/CATGAME/lib/x86_64").delete();

                // no point in having the catrobat file in the cache after unzipping
                f.delete();

                // build starts
                action.setText(context.getString(R.string.building_apk));
                try
                {
                    Zip.zipFolderContentsStored(String.valueOf( new File(context.getCacheDir(), "CATGAME").toPath()), String.valueOf( new File(context.getCacheDir(), "CATGAME.apk").toPath())); // zipping the apk bc apktool takes too long

                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);

                }

                // copying the keystore to cache
                Assets.copyAssetToCache(context, "ks.p12");
                File keystore = new File(context.getCacheDir(), "ks.p12");

                File catgame = new File(context.getCacheDir(), "CATGAME.apk"); //apk to be signed

                action.setText(context.getString(R.string.updating_android_manifest));
                try
                {
                    Manifest.change(catgame, package_name, app_name, app_version, version_code); // updating manifest to match the user's preference
                }
                catch (IOException | ApkFormatException e)
                {
                    throw new RuntimeException(e);
                }

                File out_game = new File(context.getCacheDir(), apk_name); // the output apk
                try
                {
                    Sign.sign(catgame, out_game, keystore, "password", "cert2", "password"); // signing
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                Log.d("Build.java", "Signed APK size: " + out_game.length());

                ((Activity) context).runOnUiThread(() -> Export.shareFile( context, out_game)); // exporting the file

                action.setText(context.getString(R.string.apk_installed));

                Log.d(tag, "Done!");
            }).start();

        }

    }

}
