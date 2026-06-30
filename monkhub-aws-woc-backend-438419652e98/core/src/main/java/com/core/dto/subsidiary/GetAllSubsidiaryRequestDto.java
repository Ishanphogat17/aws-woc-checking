package com.core.dto.subsidiary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllSubsidiaryRequestDto {
    private String searchQuery = "";
}

