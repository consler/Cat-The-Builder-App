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
                uri ->
                {
                    if (uri != null)
                    {
                        Log.d("filepicker", uri.toString() );

                        if(id == R.id.file_picker_button)
                        {
                            String catrobat_name = copyUriToCache(uri, "catrobat");
                            if(catrobat_name.endsWith(".catrobat"))
                            {
                                Toast.makeText(context, context.getString(R.string.catrobat_imported), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                new File(context.getCacheDir(), "CATGAME.catrobat").delete();
                                Toast.makeText(context, context.getString(R.string.file_chosen_not_catrobat), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(id == R.id.icon_button)
                        {
                            String icon_name = copyUriToCache(uri, "icon");
                            if(icon != null)
                            {
                                Toast.makeText(context, context.getString(R.string.imported_icon), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                new File(context.getCacheDir(), icon_name).delete();
                                Toast.makeText(context, context.getString(R.string.invalid_icon), Toast.LENGTH_SHORT).show();
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

    private static File icon = null;

    public static File getIcon()
    {
        return icon;
    }
    public static void nullifyIcon()
    {
        icon = null;
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
                File catgame = new File(context.getCacheDir(), "CATGAME.catrobat");
                if(catgame.exists() && !file_name.equals("CATGAME.catrobat")) catgame.delete();
                new File(context.getCacheDir(), file_name).renameTo(new File(context.getCacheDir(), "CATGAME.catrobat"));
            }
            else if (s.equals("icon"))
            {
                if (file_name.endsWith(".jpg") || file_name.endsWith(".jpeg"))
                {
                    new File(context.getCacheDir(), file_name).renameTo(new File(context.getCacheDir(), "icon.jpg"));
                    icon = new File(context.getCacheDir(), "icon.jpg");
                }
                else if (file_name.endsWith(".png"))
                {
                    new File(context.getCacheDir(), file_name).renameTo(new File(context.getCacheDir(), "icon.png"));
                    icon = new File(context.getCacheDir(), "icon.png");
                }
                else if (file_name.endsWith(".webp"))
                {
                    new File(context.getCacheDir(), file_name).renameTo(new File(context.getCacheDir(), "icon.webp"));
                    icon = new File(context.getCacheDir(), "icon.webp");
                }
            }
            return file_name;
        }
        catch (Exception e)
        {
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