package my.consler.catthebuilder.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AssetsCopier {
    private static final String TAG = "AssetsCopier";

    /**
     * Copy entire folder recursively from assets into cacheDir.
     *
     * @param context       your Activity or Application context
     * @param assetFolder   the path under /assets (e.g. "", or "data/images")
     * @param threadCount   number of parallel copy threads
     * @throws IOException          if we fail to read the asset tree
     * @throws InterruptedException if we’re interrupted waiting for copy tasks
     */
    public static void copyFolderFromAssets(
            Context context,
            String assetFolder,
            int threadCount
    ) throws IOException, InterruptedException {
        AssetManager am = context.getAssets();
        File targetRoot = context.getCacheDir(); // or getFilesDir(), etc.

        // Create thread pool
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // Kick off recursive copy
        copyAssetFolderRecursive(am, assetFolder, new File(targetRoot, assetFolder), executor, context);

        // No more tasks
        executor.shutdown();
        // Wait (tunable timeout) for tasks to finish
        if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
            Log.w(TAG, "Timed out waiting for asset copy to complete");
        }
    }

    /**
     * Helper: recursively walk assetFolder, scheduling file‐copy tasks.
     */
    private static void copyAssetFolderRecursive(
            AssetManager am,
            String assetPath,
            File outDir,
            ExecutorService executor,
            Context ctx
    ) throws IOException {
        // List entries at this level. 
        // Note: if this folder is empty, list() returns zero-length array.
        String[] children = am.list(assetPath);
        if (children == null || children.length == 0) {
            // assetPath is a file, not a folder
            scheduleFileCopy(am, assetPath, outDir, executor, ctx);
        } else {
            // assetPath is a folder
            if (!outDir.exists() && !outDir.mkdirs()) {
                throw new IOException("Could not create dir: " + outDir.getAbsolutePath());
            }
            // Recurse into each child
            for (String child : children) {
                String childAssetPath = assetPath.isEmpty()
                        ? child
                        : assetPath + "/" + child;
                File childOut = new File(outDir, child);
                copyAssetFolderRecursive(am, childAssetPath, childOut, executor, ctx    );
            }
        }
    }

    /**
     * Schedule a single file‐copy from assets to disk.
     */
    private static void scheduleFileCopy(
            AssetManager am,
            String assetFilePath,
            File outFile,
            ExecutorService executor,
            Context ctx
    ) {
        executor.submit(() -> {
            try (InputStream in = am.open(assetFilePath);
                 FileOutputStream out = new FileOutputStream(outFile)) {
                byte[] buf = new byte[16 * 1024];
                int read;
                while ((read = in.read(buf)) != -1) {
                    out.write(buf, 0, read);
                }
                Log.d(TAG, "Copied asset: " + assetFilePath
                        + " -> " + outFile.getAbsolutePath());
            } catch (IOException e) {
                Log.e(TAG, "Failed copying asset " + assetFilePath, e);
            }
        });
    }

    public static File copyAssetToCache(Context context, String assetName) {
        AssetManager assetManager = context.getAssets();
        File outFile = new File(context.getCacheDir(), assetName);

        // Ensure parent directories exist (in case assetName has subdirs)
        File parent = outFile.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                // failed to create directory
                return null;
            }
        }

        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = assetManager.open(assetName);
            out = new FileOutputStream(outFile);

            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            return outFile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        } finally {
            // Close streams, ignoring secondary exceptions
            if (in != null) {
                try { in.close(); } catch (IOException ignored) {}
            }
            if (out != null) {
                try { out.close(); } catch (IOException ignored) {}
            }
        }
    }
}
