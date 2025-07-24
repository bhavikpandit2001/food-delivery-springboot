package com.assesment.food_delivery.dto;

import com.assesment.food_delivery.enums.OnboardingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusRequestDto {
    private OnboardingStatus status;
}
