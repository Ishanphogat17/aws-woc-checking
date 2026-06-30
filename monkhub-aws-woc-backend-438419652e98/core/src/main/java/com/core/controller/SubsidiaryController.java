package com.core.controller;

import com.core.constant.ApiConstant;
import com.core.dto.response.ResponseDto;
import com.core.dto.subsidiary.GetAllSubsidiaryRequestDto;
import com.core.dto.subsidiary.SaveSubsidiaryRequestDto;
import com.core.dto.subsidiary.SubsidiaryResponseDto;
import com.core.service.SubsidiaryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstant.SUBSIDIARY)
@AllArgsConstructor
public class SubsidiaryController {

    private final SubsidiaryService subsidiaryService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> saveSubsidiary(@RequestBody @Valid SaveSubsidiaryRequestDto saveSubsidiaryRequestDto) {
        SubsidiaryResponseDto savedSubsidiary = subsidiaryService.saveSubsidiary(saveSubsidiaryRequestDto);
        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Subsidiary saved successfully").data(savedSubsidiary).build());
    }

    @PostMapping("/get/all")
    public ResponseEntity<ResponseDto> getAllSubsidiaries(@RequestBody GetAllSubsidiaryRequestDto getAllSubsidiaryRequestDto) {
        List<SubsidiaryResponseDto> subsidiaries = subsidiaryService.getAllSubsidiaries(getAllSubsidiaryRequestDto);
        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Subsidiaries retrieved successfully").data(subsidiaries).build());
    }
}

