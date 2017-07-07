package com.example.coustomtoolbar.Observer;

import java.util.Observer;

/**
 * Created by yaojian on 2017/6/29.
 */

public interface Subject {

    void registerObserver(TaskObserver observer);

    void removeObserver(TaskObserver observer);

    void notifyObserver();
}
