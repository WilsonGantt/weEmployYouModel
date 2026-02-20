package we.employ.you.converter; // Adjust to your package

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false) // Set to true for global auto-application to all Boolean fields (use carefully)
public class YesNoConverter implements AttributeConverter<Boolean, Character> {

    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute ? 'Y' : 'N';
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData == 'Y' || dbData == 'y'; // Handles lowercase if your DB uses it
    }
}