package my.consler.catthebuilder;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

// idk bro ai generated this
public class FilePickerButton implements View.OnClickListener
{
    private final Context context;
    private final ActivityResultLauncher<String[]> pickLauncher;

    public FilePickerButton(Context context)
    {
        this.context = context;
        if (!(context instanceof ComponentActivity))
        {
            throw new IllegalArgumentException("Context must be a ComponentActivity");

        }
        ComponentActivity activity = (ComponentActivity) context;
        pickLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                new ActivityResultCallback<Uri>()
                {
                    @Override
                    public void onActivityResult(Uri uri)
                    {
                        if (uri != null)
                        {
                            BuildButton.setPickedFileName( copyUriToCache(uri));

                        }

                    }

                }

        );

    }

    @Override
    public void onClick(View view)
    {
        // Launch the system picker for any file type
        pickLauncher.launch(new String[]{"*/*"});
    }

    private String copyUriToCache(Uri uri)
    {
        String fileName = queryFileName(uri);
        File cacheFile = new File(context.getCacheDir(), fileName);

        try (InputStream in = context.getContentResolver().openInputStream(uri);
             FileOutputStream out = new FileOutputStream(cacheFile))
        {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }

            String file_name = cacheFile.getName();

            Toast.makeText(context, "Copied to cache: " + file_name, Toast.LENGTH_SHORT).show();
            return file_name;
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Copy failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);

        }

    }

    private String queryFileName(Uri uri)
    {
        String name = "tempfile";
        ContentResolver resolver = context.getContentResolver();
        try (Cursor cursor = resolver.query(uri, null, null, null, null))
        {
            if (cursor != null)
            {
                int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (idx != -1 && cursor.moveToFirst())
                {
                    name = cursor.getString(idx);

                }

            }

        }
        return name;

    }

}