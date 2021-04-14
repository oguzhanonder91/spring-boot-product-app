package com.common.service.impl;

import com.common.dto.BaseDto;
import com.common.entity.BaseEntity;
import com.common.repository.BaseRepository;
import com.common.service.BaseService;
import com.common.specification.GenericSpecification;
import com.common.specification.SearchCriteria;
import com.common.specification.SearchOperation;
import com.google.common.reflect.TypeToken;
import com.util.ConvertTypeToken;
import com.util.SecurityUtil;
import com.util.enums.EntityState;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public abstract class BaseServiceImpl<T extends BaseEntity, D extends BaseDto> extends BaseCriteriaServiceImpl<T> implements BaseService<T, D> {

    @Autowired
    private BaseRepository<T> baseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SecurityUtil securityUtil;

    private final ConvertTypeToken<T, D> convertTypeToken = new ConvertTypeToken<>();

    @Override
    public D saveForDto(D dto) {
        Type type = new TypeToken<T>(getClass()) {
        }.getType();
        Type dtoType = new TypeToken<D>(getClass()) {
        }.getType();
        T t = modelMapper.map(dto, type);
        t.setEntityState(EntityState.ACTIVE);
        t.setCreatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        t.setCreatedDate(new Date(System.currentTimeMillis()));
        t = this.saveForEntity(t);
        return modelMapper.map(t, dtoType);
    }

    @Override
    public List<T> saveForEntity(List<T> entity) {
        List<T> newList = entity.stream()
                .peek(s -> s.setEntityState(EntityState.ACTIVE))
                .peek(s -> s.setCreatedBy(String.valueOf(securityUtil.getCurrentAuditor())))
                .peek(s -> s.setCreatedDate(new Date(System.currentTimeMillis())))
                .collect(Collectors.toList());
        return baseRepository.saveAll(newList);
    }

    @Override
    public List<D> saveForDto(List<D> dtos) {
        Type type = new TypeToken<List<T>>(getClass()) {
        }.getType();
        Type dtoType = new TypeToken<List<D>>(getClass()) {
        }.getType();
        List<T> tList = modelMapper.map(dtos, type);
        return modelMapper.map(this.saveForEntity(tList), dtoType);
    }

    @Override
    public D saveForDto(T entity) {
        Type dtoType = new TypeToken<D>(getClass()) {
        }.getType();
        entity.setEntityState(EntityState.ACTIVE);
        entity.setCreatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        entity.setCreatedDate(new Date(System.currentTimeMillis()));
        entity = this.saveForEntity(entity);
        return modelMapper.map(entity, dtoType);
    }

    @Override
    public T saveForEntity(T entity) {
        entity.setEntityState(EntityState.ACTIVE);
        entity.setCreatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        entity.setCreatedDate(new Date(System.currentTimeMillis()));
        return baseRepository.save(entity);
    }

    @Override
    public T updateForEntity(T entity) {
        entity.setEntityState(EntityState.ACTIVE);
        entity.setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        entity.setLastUpdatedDate(new Date(System.currentTimeMillis()));
        return baseRepository.save(entity);
    }

    @Override
    public D updateForDto(D dto) {
        Type type = new TypeToken<T>(getClass()) {
        }.getType();
        Type dtoType = new TypeToken<D>(getClass()) {
        }.getType();
        T t = modelMapper.map(dto, type);
        return modelMapper.map(this.updateForEntity(t), dtoType);
    }

    @Override
    public D updateForDto(T entity) {
        Type type = new TypeToken<D>(getClass()) {
        }.getType();
        return modelMapper.map(this.updateForEntity(entity), type);
    }

    @Override
    public void realDeleteAllForEntity(List<T> entities) {
        baseRepository.deleteAll(entities);
    }

    @Override
    public void realDeleteAllForDto(List<D> dtos) {
        Type type = new TypeToken<List<T>>(getClass()) {
        }.getType();
        List<T> tList = modelMapper.map(dtos, type);
        this.realDeleteAllForEntity(tList);
    }

    @Override
    public void realDeleteForEntity(T entity) {
        baseRepository.delete(entity);
    }

    @Override
    public void realDeleteForDto(D d) {
        Type type = new TypeToken<T>(getClass()) {
        }.getType();
        T t = modelMapper.map(d, type);
        this.realDeleteForEntity(t);
    }

    @Override
    public void realDeleteById(String id) {
        baseRepository.deleteById(id);
    }

    @Override
    public void softDeleteForEntity(T entity) {
        entity.setEntityState(EntityState.PASSIVE);
        entity.setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
        entity.setLastUpdatedDate(new Date(System.currentTimeMillis()));
        baseRepository.save(entity);
    }

    @Override
    public void softDeleteForDto(D d) {
        Type type = new TypeToken<T>(getClass()) {
        }.getType();
        T t = modelMapper.map(d, type);
        this.softDeleteForEntity(t);
    }

    @Override
    public void softDeleteById(String id) {
        Optional<T> entity = baseRepository.findById(id);
        entity.ifPresentOrElse(item -> {
            item.setEntityState(EntityState.PASSIVE);
            item.setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
            item.setLastUpdatedDate(new Date(System.currentTimeMillis()));
            baseRepository.save(item);
        }, () -> Assert.isNull(null, id + " Not Found !"));

    }

    @Override
    public void softDeleteAllForEntity(List<T> entities) {
        entities.forEach(item -> {
            item.setEntityState(EntityState.PASSIVE);
            item.setLastUpdatedBy(String.valueOf(securityUtil.getCurrentAuditor()));
            item.setLastUpdatedDate(new Date(System.currentTimeMillis()));
        });
        baseRepository.saveAll(entities);
    }

    @Override
    public void softDeleteAllForDto(List<D> dtos) {
        Type type = new TypeToken<List<T>>(getClass()) {
        }.getType();
        List<T> tList = modelMapper.map(dtos, type);
        this.softDeleteAllForEntity(tList);
    }

    @Override
    public Optional<T> findByIdActiveForEntity(String id) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.ACTIVE)
                .addFilter(SearchOperation.EQUAL, "id", id)
                .build());

        return baseRepository.findOne(specification);
    }

    @Override
    public Optional<T> findByIdPassiveForEntity(String id) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.PASSIVE)
                .addFilter(SearchOperation.EQUAL, "id", id)
                .build());

        return baseRepository.findOne(specification);
    }

    @Override
    public Optional<T> findByIdForEntity(String id) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "id", id)
                .build());

        return baseRepository.findOne(specification);
    }

    @Override
    public Optional<D> findByIdActiveForDto(String id) {
        Type type = new TypeToken<D>(getClass()) {
        }.getType();
        Optional<T> tOptional = this.findByIdActiveForEntity(id);
        D d = modelMapper.map(tOptional.orElse(null), type);
        return Optional.ofNullable(d);
    }

    @Override
    public Optional<D> findByIdPassiveForDto(String id) {
        Type type = new TypeToken<D>(getClass()) {
        }.getType();
        Optional<T> tOptional = this.findByIdPassiveForEntity(id);
        D d = modelMapper.map(tOptional.orElse(null), type);
        return Optional.ofNullable(d);
    }

    @Override
    public Optional<D> findByIdForDto(String id) {
        Type type = new TypeToken<D>(getClass()) {
        }.getType();
        Optional<T> tOptional = this.findByIdForEntity(id);
        D d = modelMapper.map(tOptional.orElse(null), type);
        return Optional.ofNullable(d);
    }

    @Override
    public List<T> findAllActiveForEntity() {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.ACTIVE)
                .build());

        return baseRepository.findAll(specification);
    }

    @Override
    public List<T> findAllPassiveForEntity() {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.PASSIVE)
                .build());

        return baseRepository.findAll(specification);
    }

    @Override
    public List<T> findAllForEntity() {
        return baseRepository.findAll();
    }

    @Override
    public List<D> findAllActiveForDto() {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllActiveForEntity(), type);
    }

    @Override
    public List<D> findAllPassiveForDto() {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        List<T> tList = this.findAllPassiveForEntity();
        return modelMapper.map(tList, type);
    }

    @Override
    public List<D> findAllForDto() {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllForEntity(), type);
    }

    @Override
    public List<T> findAllActiveForEntity(Sort sort) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.ACTIVE)
                .build());

        return baseRepository.findAll(specification, sort);
    }

    @Override
    public List<T> findAllPassiveForEntity(Sort sort) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.PASSIVE)
                .build());

        return baseRepository.findAll(specification, sort);
    }

    @Override
    public List<T> findAllForEntity(Sort sort) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .build());

        return baseRepository.findAll(specification, sort);
    }

    @Override
    public List<D> findAllActiveForDto(Sort sort) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllActiveForEntity(sort), type);
    }

    @Override
    public List<D> findAllPassiveForDto(Sort sort) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllPassiveForEntity(sort), type);
    }

    @Override
    public List<D> findAllForDto(Sort sort) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllForEntity(sort), type);
    }

    @Override
    public Page<T> findAllActiveForEntity(Pageable pageable) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.ACTIVE)
                .build());

        return baseRepository.findAll(specification, pageable);
    }

    @Override
    public Page<T> findAllPassiveForEntity(Pageable pageable) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.PASSIVE)
                .build());

        return baseRepository.findAll(specification, pageable);
    }

    @Override
    public Page<T> findAllForEntity(Pageable pageable) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .build());

        return baseRepository.findAll(specification, pageable);
    }

    @Override
    public Page<D> findAllActiveForDto(Pageable pageable) {
        Type type = new TypeToken<Page<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllActiveForEntity(pageable), type);
    }

    @Override
    public Page<D> findAllPassiveForDto(Pageable pageable) {
        Type type = new TypeToken<Page<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllPassiveForEntity(pageable), type);
    }

    @Override
    public Page<D> findAllForDto(Pageable pageable) {
        Type type = new TypeToken<Page<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllForEntity(pageable), type);
    }

    @Override
    public List<T> findAllActiveForEntity(List<String> ids) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.IN, "id", ids)
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.ACTIVE)
                .build());

        return baseRepository.findAll(specification);
    }

    @Override
    public List<T> findAllPassiveForEntity(List<String> ids) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.IN, "id", ids)
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.PASSIVE)
                .build());

        return baseRepository.findAll(specification);
    }

    @Override
    public List<T> findAllForEntity(List<String> ids) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.IN, "id", ids)
                .build());

        return baseRepository.findAll(specification);
    }

    @Override
    public List<D> findAllActiveForDto(List<String> ids) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllActiveForEntity(ids), type);
    }

    @Override
    public List<D> findAllPassiveForDto(List<String> ids) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllPassiveForEntity(ids), type);
    }

    @Override
    public List<D> findAllForDto(List<String> ids) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllForEntity(ids), type);
    }

    @Override
    public List<T> findAllForEntity(Example<T> example) {
        return baseRepository.findAll(example);
    }

    @Override
    public List<T> findAllForEntity(Example<T> example, Sort sort) {
        return baseRepository.findAll(example, sort);
    }

    @Override
    public List<T> findAllForEntity(Example<T> example, Pageable pageable) {
        return baseRepository.findAll(example, pageable).getContent();
    }

    @Override
    public List<T> findAllForEntity(Specification<T> spec) {
        return baseRepository.findAll(spec);
    }

    @Override
    public List<T> findAllActiveForEntity(Sort.Direction type, String field) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.ACTIVE)
                .sort(type, field)
                .build();
        GenericSpecification<T> specification = new GenericSpecification<>(searchCriteria);

        return baseRepository.findAll(specification, searchCriteria.getSort());
    }

    @Override
    public List<T> findAllPassiveForEntity(Sort.Direction type, String field) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.PASSIVE)
                .sort(type, field)
                .build();
        GenericSpecification<T> specification = new GenericSpecification<>(searchCriteria);

        return baseRepository.findAll(specification, searchCriteria.getSort());
    }

    @Override
    public List<T> findAllForEntity(Sort.Direction type, String field) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .sort(type, field)
                .build();
        GenericSpecification<T> specification = new GenericSpecification<>(searchCriteria);

        return baseRepository.findAll(specification, searchCriteria.getSort());
    }

    @Override
    public List<D> findAllActiveForDto(Sort.Direction direction, String field) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllActiveForEntity(direction, field), type);
    }

    @Override
    public List<D> findAllPassiveForDto(Sort.Direction direction, String field) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllPassiveForEntity(direction, field), type);
    }

    @Override
    public List<D> findAllForDto(Sort.Direction direction, String field) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllForEntity(direction, field), type);
    }

    @Override
    public List<T> findAllActiveForEntity(Integer page, Integer pageSize, Sort sort) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.ACTIVE)
                .build());

        return baseRepository.findAll(specification, PageRequest.of(page, pageSize, sort)).getContent();
    }

    @Override
    public List<T> findAllPassiveForEntity(Integer page, Integer pageSize, Sort sort) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.PASSIVE)
                .build());

        return baseRepository.findAll(specification, PageRequest.of(page, pageSize, sort)).getContent();
    }

    @Override
    public List<T> findAllForEntity(Integer page, Integer pageSize, Sort sort) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .build());

        return baseRepository.findAll(specification, PageRequest.of(page, pageSize, sort)).getContent();
    }

    @Override
    public List<D> findAllActiveForDto(Integer page, Integer pageSize, Sort sort) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllActiveForEntity(page, pageSize, sort), type);
    }

    @Override
    public List<D> findAllPassiveForDto(Integer page, Integer pageSize, Sort sort) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllPassiveForEntity(page, pageSize, sort), type);
    }

    @Override
    public List<D> findAllForDto(Integer page, Integer pageSize, Sort sort) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.findAllForEntity(page, pageSize, sort), type);
    }

    @Override
    public long countActive() {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.ACTIVE)
                .build());
        return baseRepository.count(specification);
    }

    @Override
    public long countPassive() {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.PASSIVE)
                .build());
        return baseRepository.count(specification);
    }

    @Override
    public long count() {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .build());
        return baseRepository.count(specification);
    }

    @Override
    public boolean existsActive(String id) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.ACTIVE)
                .addFilter(SearchOperation.EQUAL, "id", id)
                .build());
        return baseRepository.findOne(specification).isPresent();
    }

    @Override
    public boolean existsPassive(String id) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "entityState", EntityState.PASSIVE)
                .addFilter(SearchOperation.EQUAL, "id", id)
                .build());
        return baseRepository.findOne(specification).isPresent();
    }

    @Override
    public boolean exists(String id) {
        GenericSpecification<T> specification = new GenericSpecification<>(new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "id", id)
                .build());
        return baseRepository.findOne(specification).isPresent();
    }

    @Override
    public List<T> caboryaFindByParamsForEntity(SearchCriteria searchCriteria) {
        GenericSpecification<T> specification = new GenericSpecification<>(searchCriteria);
        if (searchCriteria.getPageable() != null) {
            return baseRepository.findAll(specification, searchCriteria.getPageable()).getContent();
        }
        return baseRepository.findAll(specification, searchCriteria.getSort());
    }

    @Override
    public List<D> caboryaFindByParamsForDto(SearchCriteria searchCriteria) {
        Type type = new TypeToken<List<D>>(getClass()) {
        }.getType();
        return modelMapper.map(this.caboryaFindByParamsForEntity(searchCriteria), type);
    }

    @Override
    public Long caboryaCountByParams(SearchCriteria adaletCoreSearchCriteria) {
        GenericSpecification<T> specification = new GenericSpecification<>(adaletCoreSearchCriteria);
        return baseRepository.count(specification);
    }

    @Override
    public <R> List<R> caboryaFindByParams(SearchCriteria searchCriteria) {
        if (searchCriteria.getFieldsMap().size() <= 0) {
            return (List<R>) this.caboryaFindByParamsForDto(searchCriteria);
        }
        if (searchCriteria.getResultClass() == null) {
            Type type = new TypeToken<D>(getClass()) {
            }.getType();
            searchCriteria.setResultClass(convertTypeToken.convertClassForDto(type));
        }
        return this.findAllToSelectionFields(searchCriteria);
    }
}
