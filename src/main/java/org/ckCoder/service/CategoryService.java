package org.ckCoder.service;

import org.ckCoder.models.Category;
import org.ckCoder.service.contract.IService;

import java.util.Set;

public class CategoryService implements IService<Category, Long> {

    @Override
    public Category create(Category category) {
        return null;
    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    @Override
    public Set<Category> findAll(Category category) {
        return null;
    }

    @Override
    public Category findById(Long aLong) {
        return null;
    }
}
