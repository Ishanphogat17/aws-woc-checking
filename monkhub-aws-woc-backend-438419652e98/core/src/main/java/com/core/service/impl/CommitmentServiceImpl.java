package com.core.service.impl;

import com.core.constant.AppConstant;
import com.core.constant.CommitmentConstant;
import com.core.constant.DateConstant;
import com.core.dto.commitment.AddCommitmentRequestDto;
import com.core.dto.commitment.CommitmentResponseDto;
import com.core.dto.commitment.DeleteCommitmentRequestDto;
import com.core.dto.commitment.GetAllCommitmentRequestDto;
import com.core.dto.commitment.WebSocketMessageBuffer;
import com.core.dto.page.PageResponse;
import com.core.dto.storage.GetCompressImageUrlRequestDto;
import com.core.entity.Commitment;
import com.core.entity.Subsidiary;
import com.core.enums.FilterType;
import com.core.enums.SortBy;
import com.core.mapper.CommitmentMapper;
import com.core.repository.CommitmentRepository;
import com.core.repository.SubsidiaryRepository;
import com.core.service.CommitmentService;
import com.core.service.ImageCompressionService;
import com.core.service.PdfGenerationService;
import com.core.utility.AppUtility;
import com.core.utility.DateUtility;
import com.core.utility.MethodLoggerUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommitmentServiceImpl implements CommitmentService {

    private final CommitmentRepository commitmentRepository;
    private final CommitmentMapper commitmentMapper;
    private final PdfGenerationService pdfGenerationService;
    private final ImageCompressionService imageCompressionService;
    private final SubsidiaryRepository subsidiaryRepository;

    @Override
    public CommitmentResponseDto saveCommitment(AddCommitmentRequestDto addCommitmentRequestDto) {
        MethodLoggerUtility.start(this);

        Commitment commitment = commitmentMapper.toEntity(addCommitmentRequestDto);

        String compressedProfileImageUrl = null;
        if(addCommitmentRequestDto.getProfileImageUrl() != null) {
            try {
                compressedProfileImageUrl = imageCompressionService.getCompressImageUrl(GetCompressImageUrlRequestDto.builder().imageUrl(addCommitmentRequestDto.getProfileImageUrl()).height(AppConstant.COMPRESS_IMAGE_HEIGHT).width(AppConstant.COMPRESS_IMAGE_WIDTH).build());
            } catch (Exception e) {
                log.error("Error while compressing image: {}", e.getMessage());
                compressedProfileImageUrl = addCommitmentRequestDto.getProfileImageUrl();
            }
        }

        commitment.setCompressedProfileImageUrl(compressedProfileImageUrl);

        Commitment savedCommitment = commitmentRepository.save(commitment);

        CommitmentResponseDto responseDto = commitmentMapper.toDtoFromEntity(savedCommitment);

        if (addCommitmentRequestDto.getCommitmentId() == null) {
            WebSocketMessageBuffer.setMessageQueues(responseDto);
        }

        MethodLoggerUtility.end(this);

        return responseDto;
    }

    @Override
    public PageResponse<List<CommitmentResponseDto>> getAllCommitments(GetAllCommitmentRequestDto getAllCommitmentRequestDto) {

        MethodLoggerUtility.start(this);

        WebSocketMessageBuffer.clearMessageQueues();

        Page<Commitment> page;
        Pageable pageable = PageRequest.of(getAllCommitmentRequestDto.getPageNumber(), getAllCommitmentRequestDto.getPageSize(), AppUtility.getSortBy(getAllCommitmentRequestDto.getSortBy(), CommitmentConstant.NAME));

        if((getAllCommitmentRequestDto.getStartDateTime()!=null && getAllCommitmentRequestDto.getEndDateTime()!=null) || (getAllCommitmentRequestDto.getFilterType() != null && !FilterType.TILL_DATE.equals(getAllCommitmentRequestDto.getFilterType()))) {
            Pair<ZonedDateTime, ZonedDateTime> dateRange;
            if(getAllCommitmentRequestDto.getStartDateTime() != null && getAllCommitmentRequestDto.getEndDateTime() != null)
                dateRange = Pair.of(getAllCommitmentRequestDto.getStartDateTime(), getAllCommitmentRequestDto.getEndDateTime());
            else
                dateRange = AppUtility.getDateRange(getAllCommitmentRequestDto.getFilterType());
            page = commitmentRepository.findCommitments(getAllCommitmentRequestDto.getSearchQuery().trim(), true, dateRange.getFirst(), dateRange.getSecond(), pageable);
        }
        else {
            page = commitmentRepository.findCommitments(getAllCommitmentRequestDto.getSearchQuery().trim(),true, pageable);
        }

        PageResponse<List<CommitmentResponseDto>> result = PageResponse.<List<CommitmentResponseDto>>builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .data(commitmentMapper.toDtoListFromEntity(page.getContent()))
                .build();

        MethodLoggerUtility.end(this);

        return result;

    }

    @Override
    public void deleteCommitments(DeleteCommitmentRequestDto deleteCommitmentRequestDto) {
        MethodLoggerUtility.start(this);

        List<Commitment> commitments = commitmentRepository.findByCommitmentIdInAndIsActive(deleteCommitmentRequestDto.getCommitmentIds(),true);

        for (Commitment commitment : commitments) {
            commitment.deactivate();
        }
        commitmentRepository.saveAll(commitments);

        MethodLoggerUtility.end(this);
    }

    @Override
    public void shareCommitment(UUID commitmentId) {
        // Implementation for sharing commitment goes here
    }

    @Override
    public byte[] exportCommitments(FilterType filterType) throws IOException {

        MethodLoggerUtility.start(this);

        List<CommitmentResponseDto> commitmentResponseDtos = getAllCommitments(
                GetAllCommitmentRequestDto.builder()
                        .filterType(filterType)
                        .pageNumber(0)
                        .pageSize(Integer.MAX_VALUE)
                        .sortBy(SortBy.LATEST)
                        .build()
        ).getData();

        byte[] pdfBytes = pdfGenerationService.generateCommitmentPdf(commitmentResponseDtos);

        MethodLoggerUtility.end(this);

        return pdfBytes;
    }

    @Override
    public byte[] exportCommitmentsToExcel(GetAllCommitmentRequestDto getAllCommitmentRequestDto) throws IOException {
        MethodLoggerUtility.start(this);

        List<CommitmentResponseDto> commitments = getAllCommitments(getAllCommitmentRequestDto).getData();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Commitments");

            // Create Header Row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Date", "Time", "Name", "Commitment", "Site"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Fill Data Rows
            int rowIdx = 1;
            for (CommitmentResponseDto dto : commitments) {
                Row row = sheet.createRow(rowIdx++);

                String dateStr = "";
                String timeStr = "";

                if (dto.getCreatedAt() != null) {
                    dateStr = DateUtility.formatZonedDateTime(dto.getCreatedAt(), "yyyy-MM-dd");
                    timeStr = DateUtility.formatZonedDateTime(dto.getCreatedAt(), "hh:mm a");
                }

                row.createCell(0).setCellValue(dateStr);
                row.createCell(1).setCellValue(timeStr);
                row.createCell(2).setCellValue(dto.getName() != null ? dto.getName() : "");
                row.createCell(3).setCellValue(dto.getMessage() != null ? dto.getMessage() : "");
                row.createCell(4).setCellValue(dto.getSite() != null ? dto.getSite() : "");
            }

            workbook.write(out);
            byte[] excelBytes = out.toByteArray();

            MethodLoggerUtility.end(this);
            return excelBytes;
        }
    }
}
