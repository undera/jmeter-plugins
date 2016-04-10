package org.jmeterplugins.repository;

public interface GenericCallback<T> {
    public void notify(T t);
}