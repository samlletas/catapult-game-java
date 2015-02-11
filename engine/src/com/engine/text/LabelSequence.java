package com.engine.text;

public class LabelSequence<T extends MutableSequence> implements CharSequence
{
    private String label;
    private final T value;

    public LabelSequence(String label, T value)
    {
        this.label = label;
        this.value =  value;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public T getValue()
    {
        return value;
    }

    @Override
    public int length()
    {
        return label.length() + value.length();
    }

    @Override
    public char charAt(int index)
    {
        if (index < 0 || index >= length())
        {
            throw new StringIndexOutOfBoundsException();
        }

        if (index < label.length())
        {
            return label.charAt(index);
        }
        else
        {
            return value.charAt(index - label.length());
        }
    }

    @Override
    public CharSequence subSequence(int start, int end)
    {
        return null;
    }
}
