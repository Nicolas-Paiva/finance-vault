package com.nicolaspaiva.finance_vault.utils;

import java.time.LocalDateTime;

public class DateUtils {

    public static LocalDateTime getFirstDayOfTheMonth(){
        return LocalDateTime.now().withDayOfMonth(1);
    }

}
