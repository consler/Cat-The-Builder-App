package my.consler.catthebuilder.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Read
{
    public static String readFileToString(File file) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        finally
        {
            // Always close in reverse-order of opening:
            if (reader != null) reader.close();
            else if (isr != null) isr.close();
            else if (fis != null) fis.close();
        }
        return sb.toString();
    }
}
