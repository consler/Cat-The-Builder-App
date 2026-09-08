package net.consler.catthebuilder.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AssetsUtil //ngl this is all ai generated
{
    private static final String TAG = "AssetsCopier";
    public static void copyFolderFromAssets(Context context, String assetFolder, int threadCount)
    {
        AssetManager am = context.getAssets();
        File targetRoot = context.getCacheDir();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try
        {
            copyAssetFolderRecursive(am, assetFolder, new File(targetRoot, assetFolder), executor);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        executor.shutdown();

        try
        {
            if (!executor.awaitTermination(5, TimeUnit.MINUTES))
            {
                Log.w(TAG, "Timed out waiting for asset copy to complete");
            }
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void copyAssetFolderRecursive(AssetManager am, String assetPath, File outDir, ExecutorService executor) throws IOException
    {
        String[] children = am.list(assetPath);
        if (children == null || children.length == 0)
        {
            scheduleFileCopy(am, assetPath, outDir, executor);
        }
        else
        {
            if (!outDir.exists() && !outDir.mkdirs())
            {
                throw new IOException("Could not create dir: " + outDir.getAbsolutePath());
            }
            for (String child : children)
            {
                String childAssetPath = assetPath.isEmpty() ? child : assetPath + "/" + child;
                File childOut = new File(outDir, child);
                copyAssetFolderRecursive(am, childAssetPath, childOut, executor);
            }
        }
    }
    private static void scheduleFileCopy(AssetManager am, String assetFilePath, File outFile, ExecutorService executor)
    {
        executor.submit(() ->
        {
            try (InputStream in = am.open(assetFilePath);
                 FileOutputStream out = new FileOutputStream(outFile))
            {
                byte[] buf = new byte[16 * 1024];
                int read;
                while ((read = in.read(buf)) != -1)
                {
                    out.write(buf, 0, read);
                }
            }
            catch (IOException e)
            {
                Log.e(TAG, "Failed copying asset " + assetFilePath, e);
            }
        });
    }

    public static void copyAssetToCache(Context context, String assetName)
    {
        AssetManager assetManager = context.getAssets();
        File outFile = new File(context.getCacheDir(), assetName);

        File parent = outFile.getParentFile();
        if (parent != null && !parent.exists())
        {
            if (!parent.mkdirs())
            {
                return;
            }
        }

        try (InputStream in = assetManager.open(assetName); FileOutputStream out = new FileOutputStream(outFile))
        {
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
