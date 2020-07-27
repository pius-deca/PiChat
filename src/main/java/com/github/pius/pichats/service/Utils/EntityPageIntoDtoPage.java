package com.github.pius.pichats.service.Utils;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * This class maps an entity page into a DTO page
 */
@Service
public class EntityPageIntoDtoPage {

    private final ModelMapper modelMapper;

    public EntityPageIntoDtoPage(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <D, T> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
    }
}
