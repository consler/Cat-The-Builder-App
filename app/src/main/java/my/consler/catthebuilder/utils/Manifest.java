package my.consler.catthebuilder.utils;

import android.util.Log;
import com.reandroid.apk.ApkModule;
import com.reandroid.app.AndroidManifest;
import com.reandroid.arsc.chunk.xml.AndroidManifestBlock;

import java.io.*;

public class Manifest
{
    public static void change(File apk_file, String package_name, String app_name, String version, String version_code)
    {
        if (!apk_file.exists() || !apk_file.isFile())
        {
            Log.wtf("Manifest", "Invalid apk file given?");
        }
        ApkModule apk;
        try
        {
            apk = ApkModule.loadApkFile(apk_file);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        AndroidManifestBlock manifest = apk.getAndroidManifest();
        manifest.setPackageName(package_name);
        manifest.setApplicationLabel(app_name);
        manifest.setVersionName(version);
        manifest.setVersionCode( Integer.parseInt( version_code));
        manifest.setIconResourceId(0x7f08028e);
        manifest.setRoundIconResourceId(0x7f08028e);
        manifest = change_providers(manifest, package_name);

        apk.setManifest( manifest);

        try
        {
            apk.writeApk( apk_file);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    private static AndroidManifestBlock change_providers(AndroidManifestBlock manifest, String package_name)
    {
        manifest.recursiveAttributes().forEachRemaining(attribute ->
        {
            if (attribute.getName().equals(AndroidManifest.NAME_authorities) && attribute.getValueAsString().equals("my.catgame.fileprovider"))
            {
                attribute.setValueAsString(package_name + ".fileprovider");

            }
            else if (attribute.getName().equals(AndroidManifest.NAME_authorities) && attribute.getValueAsString().equals("my.catgame.fileProvider"))
            {
                attribute.setValueAsString(package_name + ".fileProvider");

            }
            else if (attribute.getName().equals(AndroidManifest.NAME_authorities) && attribute.getValueAsString().equals("my.catgame.androidx-startup"))
            {
                attribute.setValueAsString(package_name + ".androidx-startup");

            }
            else if (attribute.getName().equals(AndroidManifest.NAME_authorities) && attribute.getValueAsString().equals("my.catgame.com.squareup.picasso"))
            {
                attribute.setValueAsString(package_name + ".com.squareup.picasso");

            }
            else if (attribute.getName().equals(AndroidManifest.NAME_authorities) && attribute.getValueAsString().equals("my.catgame.MLInitializerProvider"))
            {
                attribute.setValueAsString(package_name + ".MLInitializerProvider");

            }
            else if (attribute.getName().equals(AndroidManifest.NAME_authorities) && attribute.getValueAsString().equals("my.catgame.mlkitinitprovider"))
            {
                attribute.setValueAsString(package_name + ".mlkitinitprovider");

            }
            else if (attribute.getName().equals(AndroidManifest.NAME_authorities) && attribute.getValueAsString().equals("my.catgame.lifecycle-process"))
            {
                attribute.setValueAsString(package_name + ".lifecycle-process");

            }
            else if (attribute.getName().equals(AndroidManifest.NAME_authorities) && attribute.getValueAsString().equals("my.catgame.AGCInitializeProvider"))
            {
                attribute.setValueAsString(package_name + ".AGCInitializeProvider");

            }

        });

        return manifest;

    }

}
