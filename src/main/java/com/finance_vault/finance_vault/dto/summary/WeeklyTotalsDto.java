package com.finance_vault.finance_vault.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyTotalsDto {

    List<Float> totals;

    List<LocalDateTime> dates;

}
