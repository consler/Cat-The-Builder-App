package my.consler.catthebuilder.build;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class AdaptiveIcon
{
    public static void delete(Context context)
    {
        String tag = "adaptive icon";
        boolean b;
        b = new File(context.getCacheDir(), "CATGAME/res/P8.xml").delete();
        Log.d(tag, String.valueOf(b));
        b=new File(context.getCacheDir(), "CATGAME/res/WE.xml").delete();
        Log.d(tag, String.valueOf(b));
        b=new File(context.getCacheDir(), "CATGAME/res/oy.xml").delete();
        Log.d(tag, String.valueOf(b));
        b=new File(context.getCacheDir(), "CATGAME/res/uF.xml").delete();
        Log.d(tag, String.valueOf(b));
    }
}
