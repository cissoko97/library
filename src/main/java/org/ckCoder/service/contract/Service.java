package org.ckCoder.service.contract;

import java.util.Set;

public interface Service<T, I> {
    public T create(T t);

    public T update(T t);

    public boolean delete(I i);

    public Set<T> findAll(T t);

    public T findById(I i);
}
