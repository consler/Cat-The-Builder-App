package my.consler.catthebuilder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import my.consler.catthebuilder.R;
import my.consler.catthebuilder.button.CreditsButton;
import my.consler.catthebuilder.button.LanguagePickerButton;
import my.consler.catthebuilder.helper.LanguageHelper;
import my.consler.catthebuilder.helper.LocaleHelper;

import java.util.Locale;

public class OptionsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle saved_instance_state)
    {
        super.onCreate(saved_instance_state);
        EdgeToEdge.enable(this);
        setContentView(R.layout.options);

        TextView language_text = findViewById(R.id.language);
        language_text.setOnClickListener(new LanguagePickerButton(this));

        TextView credits = findViewById(R.id.credits);
        credits.setOnClickListener(new CreditsButton(this));

        language_text.setText(this.getString(R.string.language_is) + LanguageHelper.getDisplayableLanguage(this));

        OnBackPressedCallback callback = new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK)
        {
            setResult(RESULT_OK);
            recreate();
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale chosen = Locale.forLanguageTag(LanguageHelper.getLanguage(newBase));
        super.attachBaseContext(LocaleHelper.wrapLocale(newBase, chosen));
    }

}
