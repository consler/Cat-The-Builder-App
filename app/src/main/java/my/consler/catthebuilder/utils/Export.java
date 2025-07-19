package my.consler.catthebuilder.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.*;


public class Export
{
    public static void shareFile(Context context, File file) {
        if (!file.exists() || file.length() == 0) {
            // Handle the error: file does not exist or is empty
            Log.d( "ApkInstaller", "File does not exist or is empty");
        }

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String authority = context.getPackageName() + ".provider";
        Uri fileUri = FileProvider.getUriForFile(context, authority, file);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, file.getName());
        intent.setClipData(ClipData.newUri(context.getContentResolver(), "File to Save", fileUri));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ((Activity) context).startActivityForResult(intent, 222);
    }

}