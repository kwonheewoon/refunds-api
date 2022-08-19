package main.refundsapi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Slf4j
public class convertMoneyTest {






    public String NumberToKor(String amt){

        String amt_msg = "";
        String[] arrayNum = {"", "일","이","삼","사","오","육","칠","팔","구"};
        String[] arrayUnit = {"","십","백","천","만","십만","백만","천만","억","십억","백업","천억","조","십조","백조","천조","경","십경","백경","천경","해","십해","백해","천해"};

        if(amt.length() > 0){
            int len = amt.length();

            String[] arrayStr = new String[len];
            String hanStr = "";
            String tmpUnit = "";
            for(int i = 0; i < len; i++){
                arrayStr[i] = amt.substring(i, i+1);
            }
            int code = len;
            for(int i = 0; i < len; i++){
                code--;
                tmpUnit = "";
                if(arrayNum[Integer.parseInt(arrayStr[i])] != ""){
                    tmpUnit = arrayUnit[code];
                    if(code > 4){
                        if((Math.floor(code/4) == Math.floor((code-1)/4)
                                && arrayNum[Integer.parseInt(arrayStr[i+1])] != "") ||
                                (Math.floor(code/4) == Math.floor((code-2)/4)
                                        && arrayNum[Integer.parseInt(arrayStr[i+2])] != "")) {
                            tmpUnit = arrayUnit[code].substring(0,1);
                        }
                    }
                }
                hanStr += arrayStr[i]+tmpUnit;
            }
            amt_msg = hanStr;
        }else{
            amt_msg = amt;
        }

        return amt_msg;
    }


    public static String amountFormat(BigDecimal amount){
        DecimalFormat formatter = new DecimalFormat("###억 ###만 ###원");
        int amountVar = amount.intValue();

        return formatter.format(amountVar).toString();
    }

    @Test
    void 원화_포맷(){
        log.info("데이터 : {}", NumberToKor("578000"));
        log.info("데이터 : {}", amountFormat(new BigDecimal("578000")));

    }
}
