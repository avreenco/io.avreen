package org.avreen.security.module.impl.hsm.safenet.engine;

import org.avreen.security.module.api.SafenetItemAnnotaion;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Hsm field comaprer.
 */
public class HSMFieldComaprer implements Comparator<Field> {
    @Override
    public int compare(Field o1, Field o2) {
        SafenetItemAnnotaion hsmField1 = o1.getAnnotation(SafenetItemAnnotaion.class);
        SafenetItemAnnotaion hsmField2 = o2.getAnnotation(SafenetItemAnnotaion.class);
        if (hsmField1 == null || hsmField2 == null)
            return 0;
        return new Integer(hsmField1.order()).compareTo(new Integer(hsmField2.order()));
    }
}
