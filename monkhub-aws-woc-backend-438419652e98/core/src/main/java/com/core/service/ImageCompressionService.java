package com.core.service;

import com.core.dto.storage.GetCompressImageUrlRequestDto;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ImageCompressionService {
    String getCompressImageUrl(GetCompressImageUrlRequestDto getCompressImageUrlRequestDto) throws IOException, URISyntaxException;
}
