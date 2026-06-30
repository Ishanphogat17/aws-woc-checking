package com.core.service;

import com.core.dto.subsidiary.GetAllSubsidiaryRequestDto;
import com.core.dto.subsidiary.SaveSubsidiaryRequestDto;
import com.core.dto.subsidiary.SubsidiaryResponseDto;

import java.util.List;

public interface SubsidiaryService {
    SubsidiaryResponseDto saveSubsidiary(SaveSubsidiaryRequestDto saveSubsidiaryRequestDto);

    List<SubsidiaryResponseDto> getAllSubsidiaries(GetAllSubsidiaryRequestDto getAllSubsidiaryRequestDto);
}

