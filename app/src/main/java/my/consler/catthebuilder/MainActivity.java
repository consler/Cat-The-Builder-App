package my.consler.catthebuilder;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import my.consler.catthebuilder.build.Build;
import my.consler.catthebuilder.buttons.BuildButton;
import my.consler.catthebuilder.buttons.FilePicker;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        Button build_button = findViewById(R.id.build_button);
        build_button.setOnClickListener(new BuildButton(this));

        Button file_picker_button = findViewById(R.id.file_picker_button);
        file_picker_button.setOnClickListener(new FilePicker(this));

        Button icon_button = findViewById(R.id.icon_button);
        icon_button.setOnClickListener(new FilePicker(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 222) // 222 is exporting the apk
        {
            try
            {
                if (data == null || data.getData() == null)
                {
                    Log.d("MainActivity", "data = null");
                    return;
                }
                OutputStream stream = getContentResolver().openOutputStream(data.getData());
                File apk = new File(getCacheDir(), Build.get_apk_name());
                Files.copy(apk.toPath(), stream);
                if (stream != null) stream.close();

                // cleaning
                new File(getCacheDir(), "CATGAME.apk").delete();
                new File(getCacheDir(),  Build.get_apk_name()).delete();
                FilePicker.getIcon().delete();

                Build.is_running = false;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                // cleaning
                new File(getCacheDir(), "CATGAME.apk").delete();
                new File(getCacheDir(),  Build.get_apk_name()).delete();
                FilePicker.getIcon().delete();

                Build.is_running = false;
            }
        }
    }

}