package com.mygdx.game.screens.ui;

public interface ICustomWidget
{
    void setOriginalPosition(float x, float y);
    void setShowDelay(float delay);
    void setHideDelay(float delay);
    void show();
    void hide();
}
