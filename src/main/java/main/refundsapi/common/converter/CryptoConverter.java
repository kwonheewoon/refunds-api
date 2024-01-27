package main.refundsapi.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import main.refundsapi.util.AESUtil;


@Converter
public class CryptoConverter implements AttributeConverter<String, String> {


    @Override
    public String convertToDatabaseColumn(String data) {
        if (data == null) return null;
        return AESUtil.encrypt(data);
    }

    @Override
    public String convertToEntityAttribute(String encData) {
        if (encData == null) return null;
        return AESUtil.decrypt(encData);
    }
}
