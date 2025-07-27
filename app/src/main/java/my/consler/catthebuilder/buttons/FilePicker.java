package my.consler.catthebuilder.buttons;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import my.consler.catthebuilder.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class FilePicker implements View.OnClickListener
{
    private final Context context;
    private final ActivityResultLauncher<String[]> pickLauncher;
    private static int id;

    public FilePicker(Context context)
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

                            Log.d("filepicker", uri.toString() );

                            if(id == R.id.file_picker_button)
                            {
                                copyUriToCache(uri, "catrobat");
                                Toast.makeText(context, "Catrobat file picked", Toast.LENGTH_SHORT).show();


                            }
                            else if(id == R.id.icon_button)
                            {

                                Toast.makeText(context, "Copied icon to cache: ", Toast.LENGTH_SHORT).show();
                                String fileName = copyUriToCache(uri, "icon");

                            }

                        }

                    }

                }

        );

    }

    @Override
    public void onClick(View view)
    {
        id = view.getId();

        pickLauncher.launch(new String[]{"*/*"});
    }

    private String copyUriToCache(Uri uri, String s)
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

            if (s.equals("catrobat"))
            {
                new File(context.getCacheDir(), file_name).renameTo(new File(context.getCacheDir(), "CATGAME.catrobat"));

            }
            else if (s.equals("icon"))
            {
                new File(context.getCacheDir(), "icon.png").delete();
                new File(context.getCacheDir(), "icon.jpg").delete();

                if (file_name.endsWith(".jpg") || file_name.endsWith(".jpeg"))
                {
                    new File(context.getCacheDir(), file_name).renameTo(new File(context.getCacheDir(), "icon.jpg"));


                }
                else if (file_name.endsWith(".png"))
                {
                    new File(context.getCacheDir(), file_name).renameTo(new File(context.getCacheDir(), "icon.png"));

                }
                else
                {
                    throw new RuntimeException("Icon not a png or a jpg");
                }

            }

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