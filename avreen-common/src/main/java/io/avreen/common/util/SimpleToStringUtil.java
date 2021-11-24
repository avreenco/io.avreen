package io.avreen.common.util;

import io.avreen.common.log.LoggerDomain;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class Simple to string util.
 */
public class SimpleToStringUtil {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.util.SimpleToStringUtil");

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        return fields;
    }

    private static String convertToString(Object o) {
        return o.toString();
    }

    public static String toString(Object o) {
        if (o == null)
            return "";
        try {
            return dumpStr(o,
                    "");
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("error in convert object to string",
                        e);
            return "exception to string";
        }
    }

    private static String dumpStr(Object obj,
                                  String indent) throws
            Exception {
        StringBuilder res = new StringBuilder();
        List<Field> allFields = new ArrayList<>();
        getAllFields(allFields,
                obj.getClass());
        if(indent==null)
            indent="";
        res.append(indent);
        res.append("[ class=(" + obj.getClass().getSimpleName()+")");
        res.append(System.lineSeparator());

        for (Field f : allFields) {
            boolean isTransient = Modifier.isTransient(f.getModifiers());
            boolean isStatic = Modifier.isStatic(f.getModifiers());
            if (isTransient || isStatic)
                continue;
            f.setAccessible(true);
            Object val = f.get(obj);
            if (val != null) {
                res.append(indent+"  ");
                String strVal = convertToString(val);
                res.append(f.getName());
                res.append(":");
                res.append(strVal);
                res.append(System.lineSeparator());
            }
        }
        res.append("]");
        return res.toString();
    }
}
