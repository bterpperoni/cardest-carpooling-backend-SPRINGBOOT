package com.cardest.backend.domain.service;

import java.util.Optional;

import com.cardest.backend.adapter.out.mapper.GenericMapper;
import com.cardest.backend.exception.RuleException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

public abstract class GenericService<P, E, M extends GenericMapper<P, E>, R extends JpaRepository<E, Long>> {

    protected abstract R getRepo();

    protected abstract M getMapper();

    public P getById(Long id){
        Optional<E> objectEntity = getRepo().findById(id);
        if (objectEntity.isPresent()) {
            return getMapper().toDomainEntity(objectEntity.get());
        } else {
            throw new RuleException("object not found", HttpStatus.BAD_REQUEST);
        }
    }

    public void delete(Long id) {
        getRepo().deleteById(id);
    }

    public P create(P object) {
        E objectEntity = getMapper().toJpaEntity(object);
        getRepo().save(objectEntity);
        P objectToReturn = getMapper().toDomainEntity(objectEntity);
        return objectToReturn;
    }

    public void update(P object, Long id) {
        Optional<E> objectToUpdate = getRepo().findById(id);
        if (objectToUpdate.isPresent()) {
            E objectEntity = getMapper().toJpaEntity(object);
            E createdObject = getRepo().save(objectEntity);
            getMapper().toDomainEntity(createdObject);
        } else {
            throw new RuleException("Object not found", HttpStatus.BAD_REQUEST);
        }
    }

}
