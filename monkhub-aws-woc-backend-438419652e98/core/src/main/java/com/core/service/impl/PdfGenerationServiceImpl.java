package com.core.service.impl;

import com.core.dto.commitment.CommitmentResponseDto;
import com.core.service.PdfGenerationService;
import com.core.utility.MethodLoggerUtility;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class PdfGenerationServiceImpl implements PdfGenerationService {
    private final TemplateEngine templateEngine;


    public byte[] generateCommitmentPdf(List<CommitmentResponseDto> commitments) throws IOException {
        MethodLoggerUtility.start(this);
        Context context = new Context();
        context.setVariable("commitments", commitments);

        String html = templateEngine.process("commitment-template", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();

        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();

        MethodLoggerUtility.end(this);
        return outputStream.toByteArray();
    }
}
