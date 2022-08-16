package main.refundsapi.converter;

import main.refundsapi.util.SeedUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CryptoConverter<T> implements AttributeConverter<T, String> {


    @Override
    public String convertToDatabaseColumn(T data) {
        if (data == null) return null;
        return SeedUtil.encrypt((String) data);
    }

    @Override
    public T convertToEntityAttribute(String encData) {
        if (encData == null) return null;
        return (T) SeedUtil.decrypt(encData);
    }
}
