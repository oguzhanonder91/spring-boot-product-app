package com.common.service;

import com.common.entity.BaseEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;


public interface BaseService<T extends BaseEntity> {

    List<T> findAll();

    List<T> findAll(final Sort sort);

    void flush();

    T saveAndFlush(final T entity);

    List<T> findAll(final Example<T> example, final Sort sort);

    List<T> findAll(final Example<T> example);

    T getOne(final String id);

    void deleteAllInBatch();

    Page<T> findAll(final Pageable pageable);

    Optional<T> findById(final String id);

    T save(final T t);

    T update(final T t);

    void deleteById(final String id);

    void delete(final T t);

    long count();

    boolean existsById(final String id);

    List<T> saveAll(final List<T> list);

    List<T> findAllById(final List<String> strings);

    void deleteAll(final List<T> list);

}
