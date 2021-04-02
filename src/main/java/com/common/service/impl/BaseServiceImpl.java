package com.common.service.impl;

import com.common.entity.BaseEntity;
import com.common.repository.BaseRepository;
import com.common.service.BaseService;
import com.common.specification.GenericSpecification;
import com.common.specification.SearchCriteria;
import com.common.specification.SearchOperation;
import com.util.SecurityUtil;
import com.util.enums.EntityState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    @Autowired
    private BaseRepository<T> baseRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public List<T> saveAll(final List<T> list) {
        List<T> newList = list.stream()
                .peek(s -> s.setEntityState(EntityState.ACTIVE))
                .peek(s -> s.setCreatedBy(String.valueOf(securityUtil.getCurrentAuditor())))
                .peek(s -> s.setCreatedDate(new Date(System.currentTimeMillis())))
                .collect(Collectors.toList());
        return baseRepository.saveAll(newList);
    }

    @Override
    public List<T> findAllById(final List<String> strings) {
        return baseRepository.findAllById(strings);
    }

    @Override
    public void softDeleteAll(final List<T> list) {
        List<T> newList = list.stream()
                .peek(s -> s.setEntityState(EntityState.PASSIVE))
                .peek(s -> s.setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor())))
                .peek(s -> s.setLastUpdatedDate(new Date(System.currentTimeMillis())))
                .collect(Collectors.toList());
        baseRepository.saveAll(newList);
    }

    @Override
    public T getOne(final String id) {
        return baseRepository.getOne(id);
    }

    @Override
    public Optional<T> findById(final String id) {
        return baseRepository.findById(id);
    }

    @Override
    public void deleteById(final String id) {
        Optional<T> t = baseRepository.findById(id);
        t.ifPresent(entity -> {
            t.get().setEntityState(EntityState.PASSIVE);
            t.get().setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
            t.get().setLastUpdatedDate(new Date(System.currentTimeMillis()));
            baseRepository.save(t.get());
        });
    }

    @Override
    public boolean existsById(final String id) {
        return baseRepository.existsById(id);
    }

    @Override
    public void flush() {
        baseRepository.flush();
    }

    @Override
    public List<T> findAll(final Example<T> example, final Sort sort) {
        return baseRepository.findAll(example, sort);
    }

    @Override
    public T saveAndFlush(final T entity) {
        entity.setEntityState(EntityState.ACTIVE);
        entity.setCreatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        entity.setCreatedDate(new Date(System.currentTimeMillis()));
        return baseRepository.saveAndFlush(entity);
    }

    @Override
    public List<T> findAll(final Example<T> example) {
        return baseRepository.findAll(example);
    }

    @Override
    public void deleteAllInBatch() {
        baseRepository.deleteAllInBatch();
    }

    @Override
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public List<T> findAll(final Sort sort) {
        return baseRepository.findAll(sort);
    }

    @Override
    public Page<T> findAll(final Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

    @Override
    public T save(final T t) {
        t.setEntityState(EntityState.ACTIVE);
        t.setCreatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        t.setCreatedDate(new Date(System.currentTimeMillis()));
        return baseRepository.save(t);
    }

    @Override
    public void softDelete(final T t) {
        t.setEntityState(EntityState.PASSIVE);
        t.setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        t.setLastUpdatedDate(new Date(System.currentTimeMillis()));
        baseRepository.save(t);
    }

    @Override
    public long count() {
        return baseRepository.count();
    }

    @Override
    public T update(T t) {
        t.setEntityState(EntityState.ACTIVE);
        t.setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        t.setLastUpdatedDate(new Date(System.currentTimeMillis()));
        return baseRepository.save(t);
    }

    @Override
    public void deleteReal(T t) {
        baseRepository.delete(t);
    }

    @Override
    public T updateAndFlush(T t) {
        t.setEntityState(EntityState.ACTIVE);
        t.setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        t.setLastUpdatedDate(new Date(System.currentTimeMillis()));
        return baseRepository.saveAndFlush(t);
    }

    @Override
    public void realDeleteAll(List<T> list) {
        baseRepository.deleteAll(list);
    }

    @Override
    public List<T> findAllActive() {
        GenericSpecification<T> genericSpecification = new GenericSpecification<>();
        genericSpecification.add(new SearchCriteria("entityState", EntityState.ACTIVE, SearchOperation.EQUAL));
        return baseRepository.findAll(genericSpecification);
    }
}
