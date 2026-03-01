package my.consler.catthebuilder.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.os.LocaleList;

import java.util.Locale;

public class LocaleHelper
{
    public static Context wrapLocale(Context base, Locale locale)
    {
        Locale.setDefault(locale);
        Configuration config = new Configuration(base.getResources().getConfiguration());
        config.setLocale(locale);
        config.setLocales(new LocaleList(locale));
        return base.createConfigurationContext(config);
    }

    public static final Locale english   = Locale.forLanguageTag("en-US");
    public static final Locale russian   = Locale.forLanguageTag("ru-RU");
    public static final Locale ukrainian = Locale.forLanguageTag("uk-UA");
    public static final Locale german    = Locale.forLanguageTag("de-DE");
}

