package my.consler.catthebuilder;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import my.consler.catthebuilder.buttons.Build;
import my.consler.catthebuilder.buttons.FilePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


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
        build_button.setOnClickListener(new Build(this));

        Button file_picker_button = findViewById(R.id.file_picker_button);
        file_picker_button.setOnClickListener(new FilePicker(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 222) {
            try {
                if (data == null || data.getData() == null) {
                    Log.d("MainActivity", "data = null");
                    return;
                }
                OutputStream stream = getContentResolver().openOutputStream(data.getData());
                File apk = new File(getCacheDir(), "CATGAME_signed.apk");
                Files.copy(apk.toPath(), stream);
                Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                if (stream != null) stream.close();
            } catch (Exception e) {
                Log.d("MainActivity", "error");
                e.printStackTrace();
            }
        }
    }


}