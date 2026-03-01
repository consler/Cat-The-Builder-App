package my.consler.catthebuilder.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.*;

public class VersionHelper
{
    public static void check_version(Context context)
    {
        int version_code = getVersionCode(context);

        File version_file = new File(context.getFilesDir(), "version.txt");
        if(!version_file.exists())
        {
            clearApplicationData(context);

            try
            {
                version_file.createNewFile();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            try
            {
                FileWriter fw = new FileWriter(version_file);
                fw.write(String.valueOf(version_code));
                fw.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            try
            {
                FileReader fr = new FileReader(version_file);

                StringBuilder output = new StringBuilder();
                int character;
                while((character = fr.read()) != -1)
                {
                    output.append(character);
                }
                if(!output.toString().equals(String.valueOf(version_code)))
                {
                    Log.d("VersionCheck", "Old version detected: " + output);

                    clearApplicationData(context);
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private static int getVersionCode(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return (int) packageInfo.getLongVersionCode();
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.e("VersionCheck", "Could not get version code", e);
            return -1;
        }
    }

    private static void clearApplicationData(Context context)
    {
        try
        {
            File cacheDirectory = context.getCacheDir();
            if (cacheDirectory != null)
            {
                File[] files = cacheDirectory.listFiles();
                if (files != null)
                {
                    for (File file : files)
                    {
                        file.delete();
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e("VersionCheck", "Error clearing cache", e);
        }
    }
}
