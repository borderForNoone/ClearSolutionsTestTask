package org.example.mappers.impl;

import org.example.domain.UserEntity;
import org.example.dto.UserDto;
import org.example.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {
    private ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(UserEntity animalEntity) {
        return modelMapper.map(animalEntity, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto animalDto) {
        return modelMapper.map(animalDto, UserEntity.class);
    }
}
