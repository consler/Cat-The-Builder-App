package net.consler.catthebuilder.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public final class ErrorHandlerUtil
{
    private ErrorHandlerUtil()
    {
    }

    public static void install(Context context)
    {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> handle(context, throwable));
    }

    public static void handle(Context context, Throwable throwable)
    {
        if (context == null) return;

        String message = throwable == null ? "Unknown error" : (throwable.getMessage() != null ? throwable.getMessage() : throwable.toString());
        Log.e("ErrorHandlerUtil", message, throwable);

        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null)
        {
            ClipData clip = ClipData.newPlainText("Error! ", message);
            clipboardManager.setPrimaryClip(clip);
        }

        Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
    }
}
