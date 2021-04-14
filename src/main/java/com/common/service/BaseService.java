package com.common.service;

import com.common.dto.BaseDto;
import com.common.entity.BaseEntity;
import com.common.specification.SearchCriteria;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;


public interface BaseService<T extends BaseEntity,D extends BaseDto> extends BaseCriteriaService{

    T updateForEntity(T entity);

    D updateForDto(T entity);

    D updateForDto(D dto);

    //

    T saveForEntity(T entity);

    List<T> saveForEntity(List<T> entity);

    List<D> saveForDto(List<D> dtos);

    D saveForDto(T entity);

    D saveForDto(D dto);

    //
    void realDeleteForEntity(T entity);

    void realDeleteForDto(D d);

    void realDeleteById(String id);

    void softDeleteForEntity(T entity);

    void softDeleteForDto(D d);

    void softDeleteById(String id);

    void softDeleteAllForEntity(List<T> entities);

    void softDeleteAllForDto(List<D> dtos);

    void realDeleteAllForEntity(List<T> entities);

    void realDeleteAllForDto(List<D> dtos);

    //

    Optional<T> findByIdActiveForEntity(String id);

    Optional<T> findByIdPassiveForEntity(String id);

    Optional<T> findByIdForEntity(String id);

    Optional<D> findByIdActiveForDto(String id);

    Optional<D> findByIdPassiveForDto(String id);

    Optional<D> findByIdForDto(String id);

    //

    List<T> findAllActiveForEntity();

    List<T> findAllForEntity();

    List<T> findAllPassiveForEntity();

    List<D> findAllActiveForDto();

    List<D> findAllPassiveForDto();

    List<D> findAllForDto();

    //

    List<T> findAllActiveForEntity(Sort sort);

    List<T> findAllPassiveForEntity(Sort sort);

    List<T> findAllForEntity(Sort sort);

    List<D> findAllActiveForDto(Sort sort);

    List<D> findAllPassiveForDto(Sort sort);

    List<D> findAllForDto(Sort sort);

    //

    Page<T> findAllActiveForEntity(Pageable pageable);

    Page<T> findAllPassiveForEntity(Pageable pageable);

    Page<T> findAllForEntity(Pageable pageable);

    Page<D> findAllActiveForDto(Pageable pageable);

    Page<D> findAllPassiveForDto(Pageable pageable);

    Page<D> findAllForDto(Pageable pageable);

    //

    List<T> findAllActiveForEntity(List<String> ids);

    List<T> findAllPassiveForEntity(List<String> ids);

    List<T> findAllForEntity(List<String> ids);

    List<D> findAllActiveForDto(List<String> ids);

    List<D> findAllPassiveForDto(List<String> ids);

    List<D> findAllForDto(List<String> ids);

    //

    List<T> findAllForEntity(Example<T> example);

    List<T> findAllForEntity(Example<T> example, Sort sort);

    List<T> findAllForEntity(Example<T> example, Pageable pageable);

    //

    List<T> findAllForEntity(Specification<T> spec);

    //

    List<T> findAllActiveForEntity(Sort.Direction type, String field);

    List<T> findAllPassiveForEntity(Sort.Direction type, String field);

    List<T> findAllForEntity(Sort.Direction type, String field);

    List<D> findAllActiveForDto(Sort.Direction type, String field);

    List<D> findAllPassiveForDto(Sort.Direction type, String field);

    List<D> findAllForDto(Sort.Direction type, String field);

    //

    List<T> findAllActiveForEntity(Integer page, Integer pageSize, Sort sort);

    List<T> findAllPassiveForEntity(Integer page, Integer pageSize, Sort sort);

    List<T> findAllForEntity(Integer page, Integer pageSize, Sort sort);

    List<D> findAllActiveForDto(Integer page, Integer pageSize, Sort sort);

    List<D> findAllPassiveForDto(Integer page, Integer pageSize, Sort sort);

    List<D> findAllForDto(Integer page, Integer pageSize, Sort sort);

    // counts

    long countActive();

    long countPassive();

    long count();

    // exists

    boolean existsActive(String id);

    boolean existsPassive(String id);

    boolean exists(String id);

    //
    //

    List<T> caboryaFindByParamsForEntity(SearchCriteria searchCriteria);

    List<D> caboryaFindByParamsForDto(SearchCriteria searchCriteria);

    <R> List<R> caboryaFindByParams(SearchCriteria searchCriteria);

    Long caboryaCountByParams(SearchCriteria searchCriteria);
}
