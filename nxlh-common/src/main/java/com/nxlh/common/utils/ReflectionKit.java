package com.nxlh.common.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ReflectionKit {

    public ReflectionKit() {
    }

    public static String getMethodCapitalize(Field field, String str) {
        Class<?> fieldType = field.getType();
        return StringUtils.concatCapitalize(Boolean.TYPE.equals(fieldType) ? "is" : "get", str);
    }

    public static String setMethodCapitalize(Field field, String str) {
        Class<?> fieldType = field.getType();
        return StringUtils.concatCapitalize("set", str);
    }

    public static Object getMethodValue(Class<?> cls, Object entity, String str) {
        Map fieldMaps = getFieldMap(cls);

        try {
            if (CollectionUtils.isEmpty(fieldMaps)) {
                throw ExceptionUtils.mpe(String.format("Error: NoSuchField in %s for %s.  Cause:", cls.getSimpleName(), str));
            } else {
                Method method = cls.getMethod(getMethodCapitalize((Field)fieldMaps.get(str), str));
                return method.invoke(entity);
            }
        } catch (NoSuchMethodException var5) {
            throw ExceptionUtils.mpe(String.format("Error: NoSuchMethod in %s.  Cause:", cls.getSimpleName()) + var5);
        } catch (IllegalAccessException var6) {
            throw ExceptionUtils.mpe(String.format("Error: Cannot execute a private method. in %s.  Cause:", cls.getSimpleName()) + var6);
        } catch (InvocationTargetException var7) {
            throw ExceptionUtils.mpe("Error: InvocationTargetException on getMethodValue.  Cause:" + var7);
        }
    }

    public static Object getMethodValue(Object entity, String str) {
        return null == entity ? null : getMethodValue(entity.getClass(), entity, str);
    }

    public static Class getSuperClassGenericType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            log.warn(String.format("Warn: %s's superclass not ParameterizedType", clazz.getSimpleName()));
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    log.warn(String.format("Warn: %s not set the actual class on superclass generic parameter", clazz.getSimpleName()));
                    return Object.class;
                } else {
                    return (Class)params[index];
                }
            } else {
                log.warn(String.format("Warn: Index: %s, Size of %s's Parameterized Type: %s .", index, clazz.getSimpleName(), params.length));
                return Object.class;
            }
        }
    }

    public static Map<String, Field> getFieldMap(Class<?> clazz) {
        List<Field> fieldList = getFieldList(clazz);
        if (!CollectionUtils.isEmpty(fieldList)) {
            Map<String, Field> fieldMap = new LinkedHashMap();
            fieldList.forEach((field) -> {
                Field var10000 = (Field)fieldMap.put(field.getName(), field);
            });
            return fieldMap;
        } else {
            return Collections.emptyMap();
        }
    }

    public static List<Field> getFieldList(Class<?> clazz) {
        if (null == clazz) {
            return null;
        } else {
            List<Field> fieldList = (List) Stream.of(clazz.getDeclaredFields()).filter((field) -> {
                return !Modifier.isStatic(field.getModifiers());
            }).filter((field) -> {
                return !Modifier.isTransient(field.getModifiers());
            }).collect(Collectors.toCollection(LinkedList::new));
            Class<?> superClass = clazz.getSuperclass();
            return superClass.equals(Object.class) ? fieldList : excludeOverrideSuperField(fieldList, getFieldList(superClass));
        }
    }

    public static List<Field> excludeOverrideSuperField(List<Field> fieldList, List<Field> superFieldList) {
        Map<String, Field> fieldMap = (Map)fieldList.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        superFieldList.stream().filter((field) -> {
            return fieldMap.get(field.getName()) == null;
        }).forEach(fieldList::add);
        return fieldList;
    }
}
