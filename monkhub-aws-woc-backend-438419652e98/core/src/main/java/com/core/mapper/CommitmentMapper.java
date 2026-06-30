package com.core.mapper;

import com.core.dto.commitment.AddCommitmentRequestDto;
import com.core.dto.commitment.CommitmentResponseDto;
import com.core.entity.Commitment;
import com.core.entity.Subsidiary;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommitmentMapper {
    Commitment toEntity(AddCommitmentRequestDto addCommitmentRequestDto);

    @Mapping(target = "formattedCreatedAt", source = "commitment.createdAt", dateFormat = "d MMM yyyy")
    @Mapping(target = "subsidiaryName", expression = "java(getSubsidiaryName(commitment, subsidiaryMap))")
    CommitmentResponseDto toDto(Commitment commitment, @Context Map<UUID, Subsidiary> subsidiaryMap);

    default String getSubsidiaryName(Commitment commitment, Map<UUID, Subsidiary> subsidiaryMap) {
        if(commitment.getSubsidiary() == null) {
            return null;
        }

        UUID subsidiaryId = commitment.getSubsidiary().getSubsidiaryId();

        Subsidiary subsidiary = subsidiaryMap.get(subsidiaryId);
        return subsidiary != null ? subsidiary.getDisplayName() : null;
    }

    List<CommitmentResponseDto> toDtoList(List<Commitment> commitments, @Context Map<UUID, Subsidiary> subsidiaryMap);

    @Named("toDtoFromEntity")
    @Mapping(target = "formattedCreatedAt", source = "commitment.createdAt", dateFormat = "d MMM yyyy")
    CommitmentResponseDto  toDtoFromEntity(Commitment commitment);

    @IterableMapping(qualifiedByName = "toDtoFromEntity")
    List<CommitmentResponseDto> toDtoListFromEntity(List<Commitment> commitments);
}
