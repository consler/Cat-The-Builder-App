package my.consler.catthebuilder;

import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import my.consler.catthebuilder.utils.CleanCacheDir;

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

        // CleanCacheDir.clean(this);

        Button build_button = findViewById(R.id.build_button);
        build_button.setOnClickListener(new BuildButton(this));

        Button file_picker_button = findViewById(R.id.file_picker_button);
        file_picker_button.setOnClickListener(new FilePickerButton(this));

    }



}