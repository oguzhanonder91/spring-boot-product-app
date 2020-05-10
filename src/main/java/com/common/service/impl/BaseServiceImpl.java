package com.common.service.impl;

import com.common.entity.BaseEntity;
import com.common.repository.BaseRepository;
import com.common.service.BaseService;
import com.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    @Autowired
    private BaseRepository<T> repository;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public List<T> saveAll(final List<T> list) {
        list.stream()
                .peek(s->s.setEntityState(BaseEntity.EntityState.ACTIVE))
                .collect(Collectors.toList());
        return repository.saveAll(list);
    }

    @Override
    public List<T> findAllById(final List<String> strings) {
        return repository.findAllById(strings);
    }

    @Override
    public void deleteAll(final List<T> list) {
        list.stream()
                .peek(s->s.setEntityState(BaseEntity.EntityState.PASSIVE))
                .collect(Collectors.toList());
        repository.saveAll(list);
    }

    @Override
    public T getOne(final String id) {
        return (T) repository.getOne(id);
    }

    @Override
    public Optional<T> findById(final String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(final String id) {
        Optional<T> t = repository.findById(id);
        t.get().setEntityState(BaseEntity.EntityState.PASSIVE);
        repository.save(t.get());
    }

    @Override
    public boolean existsById(final String id) {
        return repository.existsById(id);
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    public  List<T> findAll(final Example<T> example, final Sort sort) {
        return repository.findAll(example, sort);
    }

    @Override
    public T saveAndFlush(final T entity) {
        return (T) repository.saveAndFlush(entity);
    }

    @Override
    public List<T> findAll(final Example<T> example) {
        return repository.findAll(example);
    }

    @Override
    public void deleteAllInBatch() {
        repository.deleteAllInBatch();
    }

    @Override
    public  List<T>  findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findAll(final Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<T> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public T save(final T t) {
        t.setEntityState(BaseEntity.EntityState.ACTIVE);
        t.setCreatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        t.setCreatedDate(new Date(System.currentTimeMillis()));
        return (T) repository.save(t);
    }

    @Override
    public void softDelete(final T t) {
        t.setEntityState(BaseEntity.EntityState.PASSIVE);
        repository.save(t);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public T update(T t) {
        t.setEntityState(BaseEntity.EntityState.ACTIVE);
        t.setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        t.setLastUpdatedDate(new Date(System.currentTimeMillis()));
        return (T) repository.save(t);
    }

    @Override
    public void deleteReal(T t) {
     repository.delete(t);
    }
}
