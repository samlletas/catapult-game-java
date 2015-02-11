package com.engine.text;

public class IntegerSequence extends MutableSequence
{
    private static final int MAX_INTEGER_DIGITS = 10;
    private static final int MAX_SEQUENCE_CAPACITY = MAX_INTEGER_DIGITS + 1;

    protected static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    public IntegerSequence()
    {
        super(MAX_SEQUENCE_CAPACITY);
    }

    public void set(int value)
    {
        int index = 0;

        if (value == Integer.MIN_VALUE)
        {
            length = MAX_SEQUENCE_CAPACITY;

            setCharAt('-', index++);
            setCharAt('2', index++);
            setCharAt('1', index++);
            setCharAt('4', index++);
            setCharAt('7', index++);
            setCharAt('4', index++);
            setCharAt('8', index++);
            setCharAt('3', index++);
            setCharAt('6', index++);
            setCharAt('4', index++);
            setCharAt('8', index);
        }
        else
        {
            if (value < 0)
            {
                value = -value;
                length = getSequenceLenght(value) + 1;
                setCharAt('-', index++);
            }
            else
            {
                length = getSequenceLenght(value);
            }

            if (value >= 10000)
            {
                if (value >= 1000000000) setCharAt(DIGITS[(int) ((long) value % 10000000000L / 1000000000L)], index++);
                if (value >= 100000000) setCharAt(DIGITS[value % 1000000000 / 100000000], index++);
                if (value >= 10000000) setCharAt(DIGITS[value % 100000000 / 10000000], index++);
                if (value >= 1000000) setCharAt(DIGITS[value % 10000000 / 1000000], index++);
                if (value >= 100000) setCharAt(DIGITS[value % 1000000 / 100000], index++);
                setCharAt(DIGITS[value % 100000 / 10000], index++);
            }

            if (value >= 1000) setCharAt(DIGITS[value % 10000 / 1000], index++);
            if (value >= 100) setCharAt(DIGITS[value % 1000 / 100], index++);
            if (value >= 10) setCharAt(DIGITS[value % 100 / 10], index++);
            setCharAt(DIGITS[value % 10], index);
        }
    }

    private int getSequenceLenght(int value)
    {
        int result = (value < 0) ? 2 : 1;

        while ((value /= 10) != 0)
        {
            ++result;
        }

        return result;
    }
}