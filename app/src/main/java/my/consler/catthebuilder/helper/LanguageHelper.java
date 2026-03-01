package my.consler.catthebuilder.helper;

import android.content.Context;
import android.os.LocaleList;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class LanguageHelper
{

    public static void setLanguage(Context context, Locale language)
    {
        File language_savefile = new File(context.getFilesDir(), "language.txt");

        try
        {
            if(! language_savefile.exists())
            {
                language_savefile.createNewFile();
            }

            FileWriter fw = new FileWriter(language_savefile);
            fw.write(language.toLanguageTag());
            Log.d("aaa", getSystemLanguage());
            fw.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static String getLanguage(Context context)
    {
        File language_savefile = new File(context.getFilesDir(), "language.txt");

        if (!language_savefile.exists())
        {
            setLanguage(context, Locale.forLanguageTag(getSystemLanguage()));
        }

        try (FileReader fr = new FileReader(language_savefile))
        {
            StringBuilder language = new StringBuilder();
            int c;
            while ((c = fr.read()) != -1)
            {
                language.append((char) c);
            }

            return language.toString().trim();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String getDisplayableLanguage(Context context)
    {
        Locale l = new Locale(getLanguage(context));
        String language = l.getDisplayLanguage().trim();
        Log.d("sss", language);
        if(language.equals("uk-ua")) return "Українська";
        else if(language.equals("ru-ru")) return "Русский";
        else if(language.equals("de-de")) return "Deutsch";
        else return "English";

    }

    public static String getSystemLanguage()
    {
        return LocaleList.getDefault().get(0).getLanguage();
    }

}
