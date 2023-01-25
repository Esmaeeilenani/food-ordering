package com.food.ordering.system.domain.valueobject;


import java.util.List;

public interface EntityMapper<E, D> {

    E toEntity(D dto);

    D toDto(E entity);


    List<E> toEntity(List<D> dto);

    List<D> toDto(List<E> entity);

}


