package net.consler.catthebuilder.button;

import android.view.View;
import net.consler.catthebuilder.helper.LanguageTextView;

public class LanguageChoiceButton implements View.OnClickListener
{

    public LanguageChoiceButton()
    {}

    @Override
    public void onClick(View view)
    {
        LanguageTextView ltv = (LanguageTextView) view;
        ltv.setChosen(true);
    }

}
