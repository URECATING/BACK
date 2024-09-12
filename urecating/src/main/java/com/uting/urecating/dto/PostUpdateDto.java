package com.uting.urecating.dto;

import com.uting.urecating.domain.Category;

import java.time.LocalDateTime;

public record PostUpdateDto(String title, String content, Category category, LocalDateTime meetingDate, String place, int maxCapacity) {
}
