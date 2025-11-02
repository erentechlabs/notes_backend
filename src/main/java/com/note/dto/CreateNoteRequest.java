package com.note.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNoteRequest {

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @Max(value = 730, message = "Duration must be at most 30 days")
    @Min(value = 1, message = "Duration must be at least 1 hour")
    @NotNull(message = "Duration in hours is required")
    private Integer durationInHours;

    @NotNull(message = "Is read only is required")
    private Boolean isReadOnly;

    @NotNull(message = "Is partial editing only is required")
    private Boolean isPartialEditingOnly;
}