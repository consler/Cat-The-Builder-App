package net.consler.catthebuilder.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import net.consler.catthebuilder.R;
import net.consler.catthebuilder.helper.VersionHelper;

public final class FirebaseWarningHandler
{
    private static final String TAG = "FirebaseWarningHandler";

    private FirebaseWarningHandler()
    {
    }

    public static void checkWarnings(Activity activity)
    {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        DatabaseReference warningsRef = FirebaseDatabase.getInstance("https://catthebuilder-consler-default-rtdb.europe-west1.firebasedatabase.app").getReference("warnings");
        DatabaseReference versionRef = warningsRef.child("version" + VersionHelper.getVersionCode(activity.getApplicationContext()));

        versionRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (!snapshot.exists()) return;

                WarningData warning = WarningData.fromSnapshot(snapshot);
                if (warning != null) showWarningDialog(activity, warning);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.e(TAG, "Failed to load Firebase warnings", error.toException());
            }
        });
    }

    private static void showWarningDialog(Activity activity, WarningData warning)
    {
        if (activity.isFinishing() || activity.isDestroyed()) return;

        String title;
        String positiveButton;

        if ("security-warning".equals(warning.type))
        {
            title = activity.getString(R.string.security_warning);
            positiveButton = activity.getString(R.string.quit_right_now);
        }
        else if ("new-update".equals(warning.type))
        {
            title = activity.getString(R.string.new_update_available);
            positiveButton = activity.getString(R.string.update);
        }
        else
        {
            title = activity.getString(R.string.warning);
            positiveButton = activity.getString(R.string.okay);
        }

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(warning.message)
                .setPositiveButton(positiveButton, (dialog, which) ->
                {
                    dialog.dismiss();
                    performAction(activity, warning);
                })
                .setNegativeButton(activity.getString(R.string.i_dont_care), (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }


    private static void performAction(Activity activity, WarningData warning)
    {
        switch (warning.type)
        {
            case "security-warning" -> activity.finishAffinity();
            case "new-update" -> activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/consler/Cat-The-Builder-App/releases")));
        }
    }

    private record WarningData(String type, String message)
    {

        public static WarningData fromSnapshot(DataSnapshot snapshot)
        {
                String type = readString(snapshot, "type");
                String message = readString(snapshot, "message");

                if (message == null || message.isEmpty()) return null;
                if (type == null || type.isEmpty()) return null;

                return new WarningData(type, message);
            }

            private static String readString(DataSnapshot snapshot, String key)
            {
                Object value = snapshot.child(key).getValue();
                return value == null ? null : String.valueOf(value);
            }

        }
}
