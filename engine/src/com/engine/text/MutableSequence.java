package com.engine.text;

public abstract class MutableSequence implements CharSequence
{
    protected final int maxCapacity;
    protected final char[] chars;
    protected int length;

    public MutableSequence(int maxCapacity)
    {
        this.maxCapacity = maxCapacity;
        this.chars = new char[maxCapacity];
        this.length = 0;
    }

    @Override
    public int length()
    {
        return length;
    }

    @Override
    public char charAt(int index)
    {
        if (index < 0 || index >= length)
        {
            throw new StringIndexOutOfBoundsException(index);
        }

        return chars[index];
    }

    @Override
    public CharSequence subSequence(int start, int end)
    {
        return subString(start, end);
    }

    public String subString(int start, int end)
    {
        if (0 <= start && start <= end && end <= length)
        {
            if (start == end)
            {
                return "";
            }

            return new String(chars, start, end - start);
        }

        throw new StringIndexOutOfBoundsException();
    }

    protected void setCharAt(char character, int index)
    {
        if (index >= 0 && index < length)
        {
            chars[index] = character;
        }
        else
        {
            throw new StringIndexOutOfBoundsException();
        }
    }

    protected void setValue(char[] characters)
    {
        int count = Math.min(characters.length, maxCapacity);

        System.arraycopy(characters, 0, chars, 0, count);
        length = count;
    }

    protected void replace(char[] characters, int index)
    {
        if (index >= 0 && index < maxCapacity)
        {
            int charCount = Math.min(maxCapacity - index, characters.length);
            int position = index;

            for (int i = 0; i < charCount; i++)
            {
                chars[position++] = characters[i];
            }

            length = position + 1;
        }
        else
        {
            throw new StringIndexOutOfBoundsException();
        }
    }

    public void clear()
    {
        length = 0;
    }

    @Override
    public String toString()
    {
        if (length == 0)
        {
            return "";
        }

        return new String(chars, 0, length);
    }
}