package my.consler.catthebuilder;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import my.consler.catthebuilder.build.Build;
import my.consler.catthebuilder.buttons.AdvancedBuildOptionsButton;
import my.consler.catthebuilder.buttons.BuildButton;
import my.consler.catthebuilder.buttons.FilePicker;
import my.consler.catthebuilder.utils.VersionCheck;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        VersionCheck.check_version(this);

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

        Button advanced_build_button = findViewById(R.id.advanced_build_button);
        advanced_build_button.setOnClickListener(new AdvancedBuildOptionsButton(this));

        ((CheckBox) findViewById(R.id.auto_resizable_round_icon_option)).setChecked(true);
        ((CheckBox) findViewById(R.id.use_adaptive_icon_option)).setChecked(true);

        findViewById(R.id.more_build_options).setVisibility(GONE);
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
                File apk = new File(getCacheDir(), Build.getApkName());
                Files.copy(apk.toPath(), stream);
                if (stream != null) stream.close();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                // cleaning
                new File(getCacheDir(), "CATGAME.apk").delete();
                new File(getCacheDir(),  Build.getApkName()).delete();
                FilePicker.getIcon().delete();
                new File(getCacheDir(), "round_icon.png").delete();
                FilePicker.nullifyIcon();
                Build.is_running = false;
            }
        }
    }

}