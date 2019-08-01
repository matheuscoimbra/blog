package com.mc.blog.domain.converters;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

public abstract class GenericConverter<T extends Enum<T>, ID extends Serializable> implements AttributeConverter<T, ID> {
	
	Class<T> enumType;
	
	private String nameFieldId;
	
	public GenericConverter(String nameFieldId) {
		this.nameFieldId = nameFieldId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ID convertToDatabaseColumn(T enumObj) {
		if (enumObj == null) {
			return null;
		}
		try {
			enumType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			Field idField = enumType.getDeclaredField(nameFieldId);
			idField.setAccessible(true);
			Object idValue = idField.get(enumObj);
			return (ID) idValue;
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e ) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T convertToEntityAttribute(ID id) {
		enumType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		if (id != null) {
			List<T> tipos = Arrays.asList(enumType.getEnumConstants());
			for(T tipo: tipos) {
				try {
					Field idField = enumType.getDeclaredField(nameFieldId);
					idField.setAccessible(true);
					Object idValue = idField.get(tipo);
					if (idValue.equals(id)) {
						return tipo;
					}
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e ) {
					return null;
				}
			}
		}
		return null;
	}


}
