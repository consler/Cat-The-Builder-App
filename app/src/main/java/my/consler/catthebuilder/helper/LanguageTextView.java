package my.consler.catthebuilder.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import my.consler.catthebuilder.R;

public class LanguageTextView extends AppCompatTextView
{
    private boolean isChosen;

    public LanguageTextView(Context context)
    {
        this(context, null);
    }

    public LanguageTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public LanguageTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        if (attrs != null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LanguageTextView, defStyleAttr, 0);
            try
            {
                isChosen = a.getBoolean(R.styleable.LanguageTextView_isChosen, false);
            }
            finally
            {
                a.recycle();
            }
        }
        applyState();
    }

    public boolean isChosen()
    {
        return isChosen;
    }

    public void setChosen(boolean chosen)
    {
        if (this.isChosen == chosen) return;
        this.isChosen = chosen;
        applyState();
        if (onChosenChangeListener != null)
        {
            onChosenChangeListener.onChosenChanged(this, chosen);
        }
    }

    public void applyState()
    {
        int colorPrimary = ThemeHelper.getPrimaryColor(getContext());
        int colorSecondary = ThemeHelper.getSecondaryColor(getContext());
        setTextColor(isChosen ? colorSecondary : colorPrimary);
    }

        public interface OnChosenChangeListener
    {
        void onChosenChanged(LanguageTextView view, boolean isChosen);
    }
    private OnChosenChangeListener onChosenChangeListener;

    public void setOnChosenChangeListener(OnChosenChangeListener l)
    {
        this.onChosenChangeListener = l;
    }
}

