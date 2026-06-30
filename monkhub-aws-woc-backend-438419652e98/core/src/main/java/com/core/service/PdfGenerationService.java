package com.core.service;

import com.core.dto.commitment.CommitmentResponseDto;

import java.io.IOException;
import java.util.List;

public interface PdfGenerationService {
    byte[] generateCommitmentPdf(List<CommitmentResponseDto> commitments) throws IOException;
}
