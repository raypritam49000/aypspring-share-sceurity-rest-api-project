package com.share.security.rest.api.mappers.base;

import java.util.List;

public interface BaseMapper<T, E> {
    T toDto(E entity);

    E toEntity(T dto);

    List<T> toDtoList(List<E> entities);

    List<E> toEntityList(List<T> entities);
}