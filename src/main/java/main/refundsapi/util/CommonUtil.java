package main.refundsapi.util;

import java.time.LocalDate;

public class CommonUtil {

    public static int getYear(){
        LocalDate now = LocalDate.now();
        return now.getYear();
    }
}
