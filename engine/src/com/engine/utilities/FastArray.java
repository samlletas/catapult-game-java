package com.engine.utilities;

import com.badlogic.gdx.utils.Array;

public class FastArray<T> extends Array<T>
{
    public FastArray()
    {
        super();
        initializeIterator();
    }

    public FastArray(int capacity)
    {
        super(capacity);
        initializeIterator();
    }

    public FastArray(boolean ordered, int capacity)
    {
        super(ordered, capacity);
        initializeIterator();
    }

    public FastArray(boolean ordered, int capacity, Class arrayType)
    {
        super(ordered, capacity, arrayType);
        initializeIterator();
    }

    public FastArray(Class arrayType)
    {
        super(arrayType);
        initializeIterator();
    }

    public FastArray(Array array)
    {
        super(array);
        initializeIterator();
    }

    public FastArray(T[] array)
    {
        super(array);
        initializeIterator();
    }

    public FastArray(boolean ordered, T[] array, int start, int count)
    {
        super(ordered, array, start, count);
        initializeIterator();
    }

    private void initializeIterator()
    {
        for(T item : this)
        {
        }
    }
}
