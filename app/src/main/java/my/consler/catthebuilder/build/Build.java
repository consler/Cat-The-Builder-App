package my.consler.catthebuilder.build;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import my.consler.catthebuilder.R;
import my.consler.catthebuilder.utils.*;

import java.io.*;


public class Build
{

    private static final String tag = "Build.java";

    public static boolean is_running = false;

    public static String apk_name;
    public static void setApkName(String name) {apk_name = name + ".apk";} //setter
    public static String getApkName() {return apk_name;} // getter

    public static void start(Context context, String app_name, String package_name, String app_version, String version_code, TextView action, boolean is_debuggable, boolean auto_resize_round_icon_option, boolean use_adaptive_icon)
    {
        if(is_running)
        {
            return;
        }
        action.setVisibility(TextView.VISIBLE);
        new Thread(() ->
        {
            is_running = true;
            Looper.prepare();

            Log.d(tag, "Starting build");


            setApkName(app_name);

            // copy assets to cache only if not already done
            if (!new File(context.getCacheDir(), "CATGAME").exists())
            {
                action.setText( context.getString(R.string.copying_assets_to_cache));
                Assets.copyFolderFromAssets(context, "CATGAME", Thread.activeCount());
                Log.d(tag, "Assets copy done!");
            }

            // build starts
            ((Activity) context).runOnUiThread(() -> action.setText(context.getString(R.string.building_apk)));

            new File(context.getCacheDir(), "CATGAME/assets/CATGAME").delete(); //deleting old user catrobat stuff
            new File(context.getCacheDir(), "CATGAME/assets/CATGAME").mkdirs();

            File catrobat_file = new File(context.getCacheDir(), "CATGAME.catrobat"); // the file to be unzipped

            Zip.unzip(String.valueOf( catrobat_file.toPath()), String.valueOf( new File( context.getCacheDir(), "CATGAME/assets/CATGAME").toPath())); // unzipping the file so CATGAME could load it, this allows for a slightly faster loading speed than cbuilder

            catrobat_file.delete(); // no point in having the catrobat file in the cache after unzipping

            // icon stuff
            Icon.change(context);
            RoundIcon.change(context, auto_resize_round_icon_option);
            if(!use_adaptive_icon) AdaptiveIcon.delete(context);

            Zip.zipFolder(String.valueOf( new File(context.getCacheDir(), "CATGAME").toPath()), String.valueOf( new File(context.getCacheDir(), "CATGAME.apk").toPath())); // using zip to make the apk because apktool takes too long

            // copying the keystore to cache
            File keystore = new File(context.getCacheDir(), "ks.p12");
            if(!keystore.exists()) Assets.copyAssetToCache(context, "ks.p12");

            File catgame = new File(context.getCacheDir(), "CATGAME.apk"); //apk to be edited and signed

            ((Activity) context).runOnUiThread(() -> action.setText(context.getString(R.string.updating_android_manifest)));
            Manifest.change(catgame, package_name, app_name, app_version, version_code, is_debuggable, context); // updating manifest to match the user's preference

            File out_game = new File(context.getCacheDir(), apk_name); // the output apk

            Signer.sign(catgame, out_game, keystore, "password", "cert2", "password"); // signing

            Log.d("Build.java", "Signed APK size: " + out_game.length());

            ((Activity) context).runOnUiThread(() -> Exporter.export( context, out_game)); // exporting the file

            ((Activity) context).runOnUiThread(() -> action.setText(context.getString(R.string.apk_installed)));

            Log.d(tag, "Done!");
        }).start();
    }
}
