package com.note.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
}