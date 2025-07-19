package my.consler.catthebuilder.utils;

import android.util.Log;
import com.reandroid.apk.ApkModule;
import com.reandroid.arsc.chunk.xml.AndroidManifestBlock;

import java.io.File;
import java.io.IOException;

public class Manifest
{
    public static void change(File apk_file, String package_name, String app_name, String version) throws IOException
    {
        if (!apk_file.exists() || !apk_file.isFile())
        {
            throw new IOException("The specified file does not exist or is not a valid file.");
        }

        ApkModule module = ApkModule.loadApkFile(apk_file);
        Log.d("reader", module.toString());

        AndroidManifestBlock manifest = module.getAndroidManifest();

        manifest.setPackageName(package_name);
        manifest.setApplicationLabel(app_name);
        manifest.setVersionName(version);

        module.setManifest(manifest);
        module.writeApk(apk_file);
    }

}
