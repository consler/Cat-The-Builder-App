package my.consler.catthebuilder.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadDummyManifest
{
    public static String read(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            InputStream is = context.getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error as needed
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {}
            }
        }

        return sb.toString();
    }
}
