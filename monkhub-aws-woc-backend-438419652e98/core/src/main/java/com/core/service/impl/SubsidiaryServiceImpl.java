package com.core.service.impl;

import com.core.dto.subsidiary.GetAllSubsidiaryRequestDto;
import com.core.dto.subsidiary.SaveSubsidiaryRequestDto;
import com.core.dto.subsidiary.SubsidiaryResponseDto;
import com.core.entity.Subsidiary;
import com.core.mapper.SubsidiaryMapper;
import com.core.repository.SubsidiaryRepository;
import com.core.service.SubsidiaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubsidiaryServiceImpl implements SubsidiaryService {

    private final SubsidiaryRepository subsidiaryRepository;
    private final SubsidiaryMapper subsidiaryMapper;

    @Override
    public SubsidiaryResponseDto saveSubsidiary(SaveSubsidiaryRequestDto saveSubsidiaryRequestDto) {
        Subsidiary subsidiary;

        if (saveSubsidiaryRequestDto.getSubsidiaryId() != null) {
            subsidiary = subsidiaryRepository.findBySubsidiaryIdAndIsActiveTrue(saveSubsidiaryRequestDto.getSubsidiaryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Subsidiary not found with id: " + saveSubsidiaryRequestDto.getSubsidiaryId()
                    ));
            subsidiaryMapper.updateEntityFromRequest(saveSubsidiaryRequestDto, subsidiary);
        } else {
            subsidiary = subsidiaryMapper.toEntity(saveSubsidiaryRequestDto);
        }

        Subsidiary savedSubsidiary = subsidiaryRepository.save(subsidiary);
        return subsidiaryMapper.toDto(savedSubsidiary);
    }

    @Override
    public List<SubsidiaryResponseDto> getAllSubsidiaries(GetAllSubsidiaryRequestDto getAllSubsidiaryRequestDto) {
        String searchQuery = Optional.ofNullable(getAllSubsidiaryRequestDto.getSearchQuery())
                .orElse("")
                .trim();

        List<Subsidiary> subsidiaries = searchQuery.isEmpty()
                ? subsidiaryRepository.findByIsActiveTrue()
                : subsidiaryRepository.searchActiveSubsidiaries(searchQuery);

        return subsidiaryMapper.toDtoList(subsidiaries);
    }
}

