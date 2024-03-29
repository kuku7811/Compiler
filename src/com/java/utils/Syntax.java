package com.java.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Syntax extends EditText
{
    private TextWatcher myWatcher = null;
    private ForegroundColorSpan red = new ForegroundColorSpan(Color.rgb(255, 0, 0));

    final Syntax that = this;
    private List<String> reservedWords;

    private void createWatcherForEditor()
	{
        myWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
			{

            }

            @Override
            public void afterTextChanged(Editable s)
			{
                String actualCode = that.getText().toString();

                highlight(s, actualCode);
            }
        };
    }

    public Syntax(Context context)
	{
        super(context);
        init();
    }

    public Syntax(Context context, AttributeSet attrs)
	{
        super(context, attrs);
        init();
    }

    public Syntax(Context context, AttributeSet attrs, int defStyle)
	{
        super(context, attrs, defStyle);
        init();
    }

    private void init()
	{
        this.createWatcherForEditor();
        this.addTextChangedListener(this.myWatcher);
		this.reservedWords = Arrays.asList("package", "import", "extends", "implements", "throws", "abstract", "interface", "enum", "class", "public", "protected", "private", "static", "final", "return", "synchronized", "volatile", "void", "int", "long", "float", "double", "byte", "char", "boolean", "new", "return", "true", "false", "null", "break", "while", "for", "if", "else", "switch", "case", "default", "throw", "try", "catch", "finally");
    }

    public void highlight(Editable s, String text)
	{
		String[] words = "package|import|extends|implements|throws|abstract|interface|enum|class|public|protected|private|static|final|return|synchronized|volatile|void|int|long|float|double|byte|char|boolean|new|return|true|false|null|break|while|for|if|else|switch|case|default|throw|try|catch|finally".split("|");
        String regex = "(";
        int counter = 0;
        for (String word : reservedWords)
		{
            counter += 1;
            regex += "\\b" + word;
            if (counter < reservedWords.size())
			{
                regex += "|";
            }
        }
        regex += "\\s)";

        Pattern p = Pattern.compile("\\b" + regex + "\\s");
        Matcher m = p.matcher(text);

        while (m.find())
		{
            s.setSpan(new ForegroundColorSpan(Color.rgb(0, 0, 255)), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}