package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;

public abstract class Controller<T> {

    public abstract Collection<T> findAll();

    abstract public T create(T t);

    abstract public T update(T t);

    abstract public void delete(Long id);

}
