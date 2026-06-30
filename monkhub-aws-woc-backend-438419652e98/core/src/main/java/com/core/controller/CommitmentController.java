package com.core.controller;

import com.core.constant.ApiConstant;
import com.core.dto.commitment.AddCommitmentRequestDto;
import com.core.dto.commitment.CommitmentResponseDto;
import com.core.dto.commitment.DeleteCommitmentRequestDto;
import com.core.dto.commitment.GetAllCommitmentRequestDto;
import com.core.dto.response.ResponseDto;
import com.core.enums.FilterType;
import com.core.service.CommitmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping(value = ApiConstant.COMMITMENT)
@AllArgsConstructor
public class CommitmentController {

    private final CommitmentService commitmentService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> saveCommitments(@RequestBody @Valid AddCommitmentRequestDto addCommitmentRequestDto) {
        CommitmentResponseDto savedCommitment =  commitmentService.saveCommitment(addCommitmentRequestDto);
        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Commitments saved successfully").data(savedCommitment).build());
    }

    @PostMapping("/get/all")
    public ResponseEntity<ResponseDto> getAllCommitments(@RequestBody GetAllCommitmentRequestDto getAllCommitmentRequestDto) {
        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Commitments retrieved successfully").data(commitmentService.getAllCommitments(getAllCommitmentRequestDto)).build());
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCommitment(@RequestBody DeleteCommitmentRequestDto deleteCommitmentRequestDto) {
        commitmentService.deleteCommitments(deleteCommitmentRequestDto); 
        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Commitment deleted successfully").build());
    }

    @PostMapping("/share")
    public ResponseEntity<ResponseDto> shareCommitment(@RequestParam UUID commitmentId) {
        commitmentService.shareCommitment(commitmentId);
        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Commitment shared successfully").build());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCommitments(@RequestParam FilterType filterType) throws IOException {
        byte[] pdfBytes = commitmentService.exportCommitments(filterType);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline()
                .filename("commitments.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportCommitmentsToExcel(@RequestBody GetAllCommitmentRequestDto getAllCommitmentRequestDto) throws IOException {
        byte[] excelBytes = commitmentService.exportCommitmentsToExcel(getAllCommitmentRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.inline()
                .filename("commitments.xlsx")
                .build());

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}
