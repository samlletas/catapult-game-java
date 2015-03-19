package com.engine.text;

import com.badlogic.gdx.utils.GdxRuntimeException;

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

    /**
     * Recorre los caracteres de la secuencia e inserta al principio el
     * caracter especificado
     *
     * @param count veces que se insertará el caracter
     * @param character caracter
     */
    public void offset(int count, char character)
    {
        offset(0, count, character);
    }

    /**
     * Recorre los caracteres de la secuencia e inserta el caracter especificado
     *
     * @param start posición inicial a partir de donde se comenzará a insertar
     *              el caracter
     * @param count veces que se insertará el caracter
     * @param character caracter
     */
    public void offset(int start, int count, char character)
    {
        if (count > 0)
        {
            int end = start + count - 1;

            if (0 <= start && start < length && end < maxCapacity)
            {
                // Se recorren primero todos los caracteres n espacios donde
                // n es igual a count
                for (int i = length - 1; i >= start; i--)
                {
                    if (i + count < maxCapacity)
                    {
                        chars[i + count] = chars[i];
                    }
                }

                // Se inserta el caracter especificado n veces donde n es
                // igual a count
                for (int i = start; i <= end; i++)
                {
                    chars[i] = character;
                }

                length = Math.min(length + count, maxCapacity);
            }
            else
            {
                throw new StringIndexOutOfBoundsException();
            }
        }
        else
        {
            throw new GdxRuntimeException("count must be a positive non zero value");
        }
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