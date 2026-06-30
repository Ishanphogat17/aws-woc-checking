package com.core.dto.commitment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommitmentResponseDto implements Serializable {
    private UUID commitmentId;
    private String name;
    private String message;
    private String profileImageUrl;
    private ZonedDateTime createdAt;
    private String formattedCreatedAt;
    private String compressedProfileImageUrl;
    private String subsidiaryName;
    private String site;
}
