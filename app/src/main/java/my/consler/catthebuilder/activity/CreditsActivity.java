package my.consler.catthebuilder.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import my.consler.catthebuilder.R;

public class CreditsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle saved_instance_state)
    {
        super.onCreate(saved_instance_state);
        EdgeToEdge.enable(this);
        setContentView(R.layout.credits);

        OnBackPressedCallback callback = new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                Intent intent = new Intent(CreditsActivity.this, OptionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
