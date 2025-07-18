package my.consler.catthebuilder.utils;
// Zip.java
import android.util.Log;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class Zip {

    private static final String TAG = "Zip";

    /**
     * ZIP only the contents of a folder (files + subfolders), placing them
     * at the root of the archive. Uses default compression.
     */
    public static void zipFolderContents(String sourceDirPath, String zipFilePath) throws IOException {
        Log.d(TAG, "zipFolderContents(): " + sourceDirPath + " -> " + zipFilePath);
        Path sourceDir = Paths.get(sourceDirPath);

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)))) {
            Files.walk(sourceDir)
                    .filter(p -> !Files.isDirectory(p))
                    .forEach(p -> {
                        String entryName = sourceDir.relativize(p).toString().replace("\\", "/");
                        Log.d(TAG, "Adding (COMPRESSED) " + entryName);
                        try {
                            zos.putNextEntry(new ZipEntry(entryName));
                            Files.copy(p, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (UncheckedIOException e) {
            Log.e(TAG, "zipFolderContents failed", e.getCause());
            throw e.getCause();
        }
        Log.d(TAG, "zipFolderContents completed");
    }

    /**
     * ZIP a file or entire folder (including the top-level folder) with default compression.
     */
    public static void zipFileOrFolder(String sourcePath, String zipFilePath) throws IOException {
        Log.d(TAG, "zipFileOrFolder(): " + sourcePath + " -> " + zipFilePath);
        Path source = Paths.get(sourcePath);

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)))) {
            Files.walk(source)
                    .forEach(p -> {
                        String entryName = source.getParent() == null
                                ? p.getFileName().toString()
                                : source.getParent().relativize(p).toString().replace("\\", "/");
                        try {
                            if (Files.isDirectory(p)) {
                                if (!entryName.endsWith("/")) entryName += "/";
                                zos.putNextEntry(new ZipEntry(entryName));
                                zos.closeEntry();
                                Log.d(TAG, "Adding dir (COMPRESSED) " + entryName);
                            } else {
                                zos.putNextEntry(new ZipEntry(entryName));
                                Files.copy(p, zos);
                                zos.closeEntry();
                                Log.d(TAG, "Adding file (COMPRESSED) " + entryName);
                            }
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (UncheckedIOException e) {
            Log.e(TAG, "zipFileOrFolder failed", e.getCause());
            throw e.getCause();
        }
        Log.d(TAG, "zipFileOrFolder completed");
    }

    /**
     * ZIP only the contents of a folder, **without** compression (STORED).
     */
    public static void zipFolderContentsStored(String sourceDirPath, String zipFilePath) throws IOException {
        Log.d(TAG, "zipFolderContentsStored(): " + sourceDirPath + " -> " + zipFilePath);
        Path sourceDir = Paths.get(sourceDirPath);

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)))) {
            Files.walk(sourceDir)
                    .filter(p -> !Files.isDirectory(p))
                    .forEach(p -> {
                        String entryName = sourceDir.relativize(p).toString().replace("\\", "/");
                        Log.d(TAG, "Adding (STORED) " + entryName);
                        try {
                            EntryMeta meta = computeMeta(p);
                            ZipEntry e = new ZipEntry(entryName);
                            e.setMethod(ZipEntry.STORED);
                            e.setSize(meta.size);
                            e.setCrc(meta.crc);
                            zos.putNextEntry(e);
                            zos.write(meta.data);
                            zos.closeEntry();
                        } catch (IOException ex) {
                            throw new UncheckedIOException(ex);
                        }
                    });
        } catch (UncheckedIOException e) {
            Log.e(TAG, "zipFolderContentsStored failed", e.getCause());
            throw e.getCause();
        }
        Log.d(TAG, "zipFolderContentsStored completed");
    }

    /**
     * ZIP a file or entire folder, **without** compression (STORED).
     */
    public static void zipFileOrFolderStored(String sourcePath, String zipFilePath) throws IOException {
        Log.d(TAG, "zipFileOrFolderStored(): " + sourcePath + " -> " + zipFilePath);
        Path source = Paths.get(sourcePath);

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)))) {
            Files.walk(source)
                    .forEach(p -> {
                        String entryName = source.getParent() == null
                                ? p.getFileName().toString()
                                : source.getParent().relativize(p).toString().replace("\\", "/");
                        try {
                            if (Files.isDirectory(p)) {
                                if (!entryName.endsWith("/")) entryName += "/";
                                ZipEntry dirEntry = new ZipEntry(entryName);
                                dirEntry.setMethod(ZipEntry.STORED);
                                dirEntry.setSize(0);
                                dirEntry.setCrc(0);
                                zos.putNextEntry(dirEntry);
                                zos.closeEntry();
                                Log.d(TAG, "Adding dir (STORED) " + entryName);
                            } else {
                                Log.d(TAG, "Adding file (STORED) " + entryName);
                                EntryMeta meta = computeMeta(p);
                                ZipEntry e = new ZipEntry(entryName);
                                e.setMethod(ZipEntry.STORED);
                                e.setSize(meta.size);
                                e.setCrc(meta.crc);
                                zos.putNextEntry(e);
                                zos.write(meta.data);
                                zos.closeEntry();
                            }
                        } catch (IOException ex) {
                            throw new UncheckedIOException(ex);
                        }
                    });
        } catch (UncheckedIOException e) {
            Log.e(TAG, "zipFileOrFolderStored failed", e.getCause());
            throw e.getCause();
        }
        Log.d(TAG, "zipFileOrFolderStored completed");
    }

    /**
     * Unzip an archive into a target directory, recreating subdirectories.
     */
    public static void unzip(String zipFilePath, String destDirPath) throws IOException {
        Log.d(TAG, "unzip(): " + zipFilePath + " -> " + destDirPath);
        File destDir = new File(destDirPath);
        if (!destDir.exists() && !destDir.mkdirs()) {
            throw new IOException("Cannot create dest dir: " + destDirPath);
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Log.d(TAG, "Extracting " + entry.getName());
                File outFile = newFile(destDir, entry);
                if (entry.isDirectory()) {
                    if (!outFile.isDirectory() && !outFile.mkdirs()) {
                        throw new IOException("Failed to create dir: " + outFile);
                    }
                } else {
                    File parent = outFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create dir: " + parent);
                    }
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
        Log.d(TAG, "unzip completed");
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    /** Prevent ZIP-slip by validating canonical paths. */
    private static File newFile(File destDir, ZipEntry entry) throws IOException {
        File outFile = new File(destDir, entry.getName());
        String destPath = destDir.getCanonicalPath() + File.separator;
        String outPath  = outFile.getCanonicalPath();
        if (!outPath.startsWith(destPath)) {
            throw new IOException("Entry outside target dir: " + entry.getName());
        }
        return outFile;
    }

    /** Holds data, size and CRC for a STORED entry. */
    private static class EntryMeta {
        final byte[] data;
        final long   size;
        final long   crc;
        EntryMeta(byte[] data, long size, long crc) {
            this.data = data; this.size = size; this.crc = crc;
        }
    }

    /**
     * Read an entire file to compute its size and CRC-32 (for STORED entries).
     * (Note: this loads whole file into memory.)
     */
    private static EntryMeta computeMeta(Path file) throws IOException {
        byte[] data = Files.readAllBytes(file);
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return new EntryMeta(data, data.length, crc32.getValue());
    }
}