package com.core.dto.commitment;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommitmentRequestDto {
    private UUID commitmentId;
    @NotEmpty(message = "Name must not be empty")
    private String name;
    @NotEmpty(message = "Message must not be empty")
    private String message;
    private String profileImageUrl;
    private String site;
}
