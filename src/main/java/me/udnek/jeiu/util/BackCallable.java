package me.udnek.jeiu.util;

public interface BackCallable {

    BackCallable EMPTY = () -> {};

    void backCall();
}
