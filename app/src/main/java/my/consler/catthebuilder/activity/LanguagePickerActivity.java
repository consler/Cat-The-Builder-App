package my.consler.catthebuilder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import my.consler.catthebuilder.R;
import my.consler.catthebuilder.button.LanguageChoiceButton;
import my.consler.catthebuilder.helper.LanguageHelper;
import my.consler.catthebuilder.helper.LanguageTextView;
import my.consler.catthebuilder.helper.LocaleHelper;
import my.consler.catthebuilder.helper.ThemeHelper;

import java.util.Locale;

public class LanguagePickerActivity  extends AppCompatActivity // filled w bad code, but what can you do?
{
    @Override
    protected void onCreate(Bundle saved_instance_state)
    {
        super.onCreate(saved_instance_state);
        EdgeToEdge.enable(this);
        setContentView(R.layout.language_picker);

        LanguageTextView english_text = findViewById(R.id.english);
        english_text.setOnClickListener(new LanguageChoiceButton());

        LanguageTextView ukrainian_text = findViewById(R.id.ukrainian);
        ukrainian_text.setOnClickListener(new LanguageChoiceButton());

        LanguageTextView russian_text = findViewById(R.id.russian);
        russian_text.setOnClickListener(new LanguageChoiceButton());

        LanguageTextView german_text = findViewById(R.id.german);
        german_text.setOnClickListener(new LanguageChoiceButton());

        if(LanguageHelper.getDisplayableLanguage(this).equals("English"))
        {
            english_text.setChosen(true);
        }
        else if(LanguageHelper.getDisplayableLanguage(this).equals("Українська"))
        {
            ukrainian_text.setChosen(true);
        }
        else if(LanguageHelper.getDisplayableLanguage(this).equals("Русский"))
        {
            russian_text.setChosen(true);
        }
        else if(LanguageHelper.getDisplayableLanguage(this).equals("Deutsch"))
        {
            german_text.setChosen(true);
        }

        english_text.setOnChosenChangeListener((view, isChosen) ->
        {
            if (isChosen)
            {
                ukrainian_text.setChosen(false);
                russian_text.setChosen(false);
                german_text.setChosen(false);

                english_text.setTextColor(ThemeHelper.getSecondaryColor(this));

                LanguageHelper.setLanguage(this, LocaleHelper.english);
            }
            else
            {
                english_text.setTextColor(ThemeHelper.getPrimaryColor(this));
            }
        });

        ukrainian_text.setOnChosenChangeListener((view, isChosen) ->
        {
            if (isChosen)
            {
                english_text.setChosen(false);
                russian_text.setChosen(false);
                german_text.setChosen(false);

                ukrainian_text.setTextColor(ThemeHelper.getSecondaryColor(this));

                LanguageHelper.setLanguage(this, LocaleHelper.ukrainian);
            }
            else
            {
                ukrainian_text.setTextColor(ThemeHelper.getPrimaryColor(this));
            }
        });

        russian_text.setOnChosenChangeListener((view, isChosen) ->
        {
            if (isChosen)
            {
                english_text.setChosen(false);
                ukrainian_text.setChosen(false);
                german_text.setChosen(false);

                russian_text.setTextColor(ThemeHelper.getSecondaryColor(this));

                LanguageHelper.setLanguage(this, LocaleHelper.russian);
            }
            else
            {
                russian_text.setTextColor(ThemeHelper.getPrimaryColor(this));
            }
        });

        german_text.setOnChosenChangeListener((view, isChosen) ->
        {
            if (isChosen)
            {
                english_text.setChosen(false);
                ukrainian_text.setChosen(false);
                russian_text.setChosen(false);

                german_text.setTextColor(ThemeHelper.getSecondaryColor(this));

                LanguageHelper.setLanguage(this, LocaleHelper.german);
            }
            else
            {
                german_text.setTextColor(ThemeHelper.getPrimaryColor(this));
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                setResult(RESULT_OK);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        Locale chosen = Locale.forLanguageTag(LanguageHelper.getLanguage(newBase));
        super.attachBaseContext(LocaleHelper.wrapLocale(newBase, chosen));
    }
}
