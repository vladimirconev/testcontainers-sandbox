package com.example.testcontainers.sandbox;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {

  PersonMapper MAPPER = Mappers.getMapper(PersonMapper.class);

  Person toPerson(PersonRequest personRequest);

  @InheritInverseConfiguration
  PersonResponse fromPerson(Person person);
}
