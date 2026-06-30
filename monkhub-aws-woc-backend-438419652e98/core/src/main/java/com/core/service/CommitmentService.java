package com.core.service;

import com.core.dto.commitment.AddCommitmentRequestDto;
import com.core.dto.commitment.CommitmentResponseDto;
import com.core.dto.commitment.DeleteCommitmentRequestDto;
import com.core.dto.commitment.GetAllCommitmentRequestDto;
import com.core.dto.page.PageResponse;
import com.core.enums.FilterType;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface CommitmentService {
    CommitmentResponseDto saveCommitment(AddCommitmentRequestDto addCommitmentRequestDto);
    PageResponse<List<CommitmentResponseDto>> getAllCommitments(GetAllCommitmentRequestDto getAllCommitmentRequestDto);
    void deleteCommitments(DeleteCommitmentRequestDto deleteCommitmentRequestDto);
    void shareCommitment(UUID commitmentId);
    byte[] exportCommitments(FilterType filterType) throws IOException;
    byte[] exportCommitmentsToExcel(GetAllCommitmentRequestDto getAllCommitmentRequestDto) throws IOException;
}
