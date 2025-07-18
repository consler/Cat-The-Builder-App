package my.consler.catthebuilder.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;

public class ApkInstaller {
    /**
     * Prompt the system installer to install the given APK file.
     * @param context any Context (e.g. Activity or Application)
     * @param apkFileName the APK file name in cacheDir (e.g. "CATGAME.apk")
     */
    public static void promptInstall(Context context, String apkFileName) {
        // 1) locate the APK
        File apkFile = new File(context.getCacheDir(), apkFileName);
        if (!apkFile.exists()) {
            throw new IllegalStateException("APK not found: " + apkFile.getAbsolutePath());
        }

        // 2) build a content:// URI via FileProvider
        String authority = context.getPackageName() + ".provider";
        Uri apkUri = FileProvider.getUriForFile(context, authority, apkFile);

        // 3) create install intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 4) launch
        context.startActivity(intent);
    }
}