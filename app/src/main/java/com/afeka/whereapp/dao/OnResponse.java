package com.afeka.whereapp.dao;

public interface OnResponse<T> {
    void onData(T data);
    void onError(String msg);
}
