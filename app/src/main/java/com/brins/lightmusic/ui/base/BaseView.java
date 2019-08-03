package com.brins.lightmusic.ui.base;

import androidx.appcompat.app.AppCompatActivity;

public interface BaseView<T> {

    void setPresenter(T presenter);

    AppCompatActivity getLifeActivity();
}
