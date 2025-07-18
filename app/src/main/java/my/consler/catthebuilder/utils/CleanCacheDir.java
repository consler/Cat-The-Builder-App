package my.consler.catthebuilder.utils;

import android.content.Context;

import java.io.File;

public class CleanCacheDir
{
    public static void clean(Context context)
    {
        File cacheDir = context.getCacheDir();

        deleteRecursively(cacheDir);

    }

    static void deleteRecursively(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        // at this point it’s either an empty directory or a file
        fileOrDirectory.delete();
    }

}
