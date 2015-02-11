package com.engine.text;

//import com.badlogic.gdx.Gdx;
//import sun.misc.FloatingDecimal;

public class FloatSequence extends MutableSequence
{
    private static final int DEFAULT_DECIMAL_DIGITS = 6;
    private static final int INTEGER_DIGITS = 39;

    private final int decimalDigits;
    private final char[] tempDigits;

    public FloatSequence()
    {
        this(DEFAULT_DECIMAL_DIGITS);
    }

    public FloatSequence(int decimalDigits)
    {
        super(INTEGER_DIGITS + decimalDigits + 2);

        this.decimalDigits = decimalDigits;
        this.tempDigits = new char[maxCapacity];
    }

//    public void set(float value)
//    {
//        if (value == Float.NaN)
//        {
//            fillNaN();
//        }
//        else if (value == Float.POSITIVE_INFINITY)
//        {
//            fillPositiveInfinity();
//        }
//        else if (value == Float.NEGATIVE_INFINITY)
//        {
//            fillNegativeInfinity();
//        }
//        else
//        {
//            try
//            {
//                FloatingDecimal.BinaryToASCIIConverter converter = FloatingDecimal.getBinaryToASCIIConverter(value);
//
//                int digits = converter.getDigits(tempDigits);
//                int exponent = converter.getDecimalExponent();
//                boolean isNegative = converter.isNegative();
//
//                if (exponent > 0)
//                {
//                    digits = insertPostfix(tempDigits, digits, '0', exponent - 17);
//                } else
//                {
//                    digits = insertPrefix(tempDigits, digits, '0', Math.min(-exponent, decimalDigits + 1) + 1);
//                    exponent = 1;
//                }
//
//                digits = insertPostfix(tempDigits, digits, '0', decimalDigits - (digits - exponent));
//                round(tempDigits, digits, exponent);
//
//                int index = 0;
//                int totalDigits = exponent + decimalDigits;
//                length = totalDigits + 1;
//
//                if (isNegative)
//                {
//                    setCharAt('-', index++);
//                    length++;
//                }
//
//                for (int i = 0; i < totalDigits; i++)
//                {
//                    if (exponent == i)
//                    {
//                        setCharAt('.', index++);
//                    }
//
//                    setCharAt(tempDigits[i], index++);
//                }
//            }
//            catch (Exception e)
//            {
//                Gdx.app.log("Exception", e.getMessage());
//            }
//        }
//    }

//    private void fillNaN()
//    {
//        int index = 0;
//
//        setCharAt('N', index++);
//        setCharAt('a', index++);
//        setCharAt('N', index++);
//
//        length = index;
//    }
//
//    private void fillPositiveInfinity()
//    {
//        int index = 0;
//
//        setCharAt('I', index++);
//        setCharAt('N', index++);
//        setCharAt('F', index++);
//        setCharAt('I', index++);
//        setCharAt('N', index++);
//        setCharAt('I', index++);
//        setCharAt('T', index++);
//        setCharAt('Y', index++);
//
//        length = index;
//    }
//
//    private void fillNegativeInfinity()
//    {
//        int index = 0;
//
//        setCharAt('-', index++);
//        setCharAt('I', index++);
//        setCharAt('N', index++);
//        setCharAt('F', index++);
//        setCharAt('I', index++);
//        setCharAt('N', index++);
//        setCharAt('I', index++);
//        setCharAt('T', index++);
//        setCharAt('Y', index++);
//
//        length = index;
//    }
//
//    private int insertPrefix(char[] digits, int numDigits, char prefix, int count)
//    {
//        if (count > 0)
//        {
//            System.arraycopy(digits, 0, digits, count, numDigits);
//
//            for (int i = 0; i < count; i++)
//            {
//                digits[i] = prefix;
//            }
//
//            return numDigits + count;
//        }
//
//        return numDigits;
//    }
//
//    private int insertPostfix(char[] digits, int numDigits, char postfix, int count)
//    {
//        if (count > 0)
//        {
//            for (int i = 0; i < count; i++)
//            {
//                digits[numDigits + i] = postfix;
//            }
//
//            return numDigits + count;
//        }
//
//        return numDigits;
//    }
//
//    private void round(char[] digits, int numDigits, int exponent)
//    {
//        int index = exponent + (decimalDigits - 1);
//
//        if (index < numDigits - 1)
//        {
//            int currentDigit = getDigitFromChar(digits[index]);
//            int nextDigit = getDigitFromChar(digits[index + 1]);
//            boolean roundedUp = false;
//
//            if (nextDigit >= 5)
//            {
//                currentDigit++;
//
//                if (currentDigit == 10)
//                {
//                    currentDigit = 0;
//                    roundedUp = true;
//                }
//
//                digits[index] = getCharFromDigit(currentDigit);
//
//                if (roundedUp)
//                {
//                    while (index > 0)
//                    {
//                        index--;
//
//                        currentDigit = getDigitFromChar(digits[index]);
//                        currentDigit++;
//
//                        if (currentDigit == 10)
//                        {
//                            currentDigit = 0;
//                            digits[index] = getCharFromDigit(currentDigit);
//                        }
//                        else
//                        {
//                            digits[index] = getCharFromDigit(currentDigit);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private int getDigitFromChar(char character)
//    {
//        return character - 48;
//    }
//
//    private char getCharFromDigit(int digit)
//    {
//        return (char)(digit + 48);
//    }
}