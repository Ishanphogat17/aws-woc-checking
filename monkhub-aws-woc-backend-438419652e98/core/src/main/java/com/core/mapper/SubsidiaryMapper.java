package com.core.mapper;

import com.core.dto.subsidiary.SaveSubsidiaryRequestDto;
import com.core.dto.subsidiary.SubsidiaryResponseDto;
import com.core.entity.Subsidiary;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubsidiaryMapper {

    @Mapping(target = "subsidiaryId", ignore = true)
    Subsidiary toEntity(SaveSubsidiaryRequestDto saveSubsidiaryRequestDto);

    SubsidiaryResponseDto toDto(Subsidiary subsidiary);

    List<SubsidiaryResponseDto> toDtoList(List<Subsidiary> subsidiaries);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "subsidiaryId", ignore = true)
    void updateEntityFromRequest(SaveSubsidiaryRequestDto saveSubsidiaryRequestDto, @MappingTarget Subsidiary subsidiary);
}

