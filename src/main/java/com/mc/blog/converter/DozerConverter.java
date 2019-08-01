package com.mc.blog.converter;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class DozerConverter {
	
	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D parseObject(O  originalObject, Class<D> destinationObject) {
    	return mapper.map(originalObject, destinationObject);
    }

    public static <O, D> List<D> parserListObjects(List<O> originalObjects, Class<D> destinationObject) {
        List<D> destinationObjects = new ArrayList<D>();
        for (Object originalObject : originalObjects) {
            destinationObjects.add(mapper.map(originalObject, destinationObject));
        }
        return destinationObjects;
    }




}
