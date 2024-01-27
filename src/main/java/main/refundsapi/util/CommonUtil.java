package main.refundsapi.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class CommonUtil {

    public static int getYear(){
        LocalDate now = LocalDate.now();
        return now.getYear();
    }

    public static String amountFormat(BigDecimal amount){
        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        int amountVar = amount.intValue();

        return formatter.format(amountVar);
    }
}
