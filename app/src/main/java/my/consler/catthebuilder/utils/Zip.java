package my.consler.catthebuilder.utils;

import android.util.Log;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class Zip // this is ai generated
{

    private static final String TAG = "Zip";
    public static void zipFolder(String sourceDirPath, String zipFilePath)
    {
        Log.d(TAG, "zipFolderContentsStored(): " + sourceDirPath + " -> " + zipFilePath);
        Path sourceDir = Paths.get(sourceDirPath);

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath))))
        {
            Files.walk(sourceDir).filter(p -> !Files.isDirectory(p)).forEach(p ->
            {
                String entryName = sourceDir.relativize(p).toString().replace("\\", "/");
                try
                {
                    EntryMeta meta = computeMeta(p);
                    ZipEntry e = new ZipEntry(entryName);
                    e.setMethod(ZipEntry.STORED);
                    e.setSize(meta.size);
                    e.setCrc(meta.crc);
                    zos.putNextEntry(e);
                    zos.write(meta.data);
                    zos.closeEntry();
                }
                catch (IOException ex)
                {
                    throw new UncheckedIOException(ex);
                }
            });
        } catch (UncheckedIOException | IOException e)
        {
            Log.e(TAG, "zipFolderContentsStored failed", e.getCause());
            throw new RuntimeException(e);
        }
        Log.d(TAG, "zipFolderContentsStored completed");
    }

    public static void unzip(String zipFilePath, String destDirPath)
    {
        Log.d(TAG, "unzip(): " + zipFilePath + " -> " + destDirPath);
        File destDir = new File(destDirPath);
        if (!destDir.exists() && !destDir.mkdirs())
        {
            throw new RuntimeException("Cannot create dest dir: " + destDirPath);
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath)))
        {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null)
            {
                Log.d(TAG, "Extracting " + entry.getName());
                File outFile = newFile(destDir, entry);
                if (entry.isDirectory())
                {
                    if (!outFile.isDirectory() && !outFile.mkdirs()) throw new RuntimeException("Failed to create dir: " + outFile);
                }
                else
                {
                    File parent = outFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) throw new RuntimeException("Failed to create dir: " + parent);

                    try (FileOutputStream fos = new FileOutputStream(outFile))
                    {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zis.read(buffer)) > 0)
                        {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "unzip completed");
    }

    private static File newFile(File destDir, ZipEntry entry) throws IOException
    {
        File outFile = new File(destDir, entry.getName());
        String destPath = destDir.getCanonicalPath() + File.separator;
        String outPath  = outFile.getCanonicalPath();
        if (!outPath.startsWith(destPath)) throw new IOException("Entry outside target dir: " + entry.getName());

        return outFile;
    }

    private static class EntryMeta {
        final byte[] data;
        final long   size;
        final long   crc;
        EntryMeta(byte[] data, long size, long crc)
        {
            this.data = data; this.size = size; this.crc = crc;
        }
    }

    private static EntryMeta computeMeta(Path file) throws IOException
    {
        byte[] data = Files.readAllBytes(file);
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return new EntryMeta(data, data.length, crc32.getValue());
    }
}