package io.avreen.iso8583.packager;

import io.avreen.iso8583.packager.api.ISOComponentPackager;
import io.avreen.iso8583.packager.impl.*;

import java.util.HashMap;

/**
 * The class Iso component registry.
 */
class ISOComponentRegistry {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        of("IFA_NUMERIC", 2, "TERMINAL TYPE CODE");
    }


    private static HashMap<String, Class<? extends ISOComponentPackager>> map = new HashMap<>();

    static {
        put("IF_CHAR", IF_CHAR.class);
        put("IF_NOP", IF_NOP.class);
        put("IFA_BINARY", IFA_BINARY.class);
        put("IFA_BITMAP", IFA_BITMAP.class);
        put("IFA_LBINARY", IFA_LBINARY.class);
        put("IFA_LCHAR", IFA_LCHAR.class);
        put("IFA_LLBINARY", IFA_LLBINARY.class);
        put("IFA_LLCHAR", IFA_LLCHAR.class);
        put("IFA_LLLBINARY", IFA_LLLBINARY.class);
        put("IFA_LLLCHAR", IFA_LLLCHAR.class);
        put("IFA_LLLLBINARY", IFA_LLLLBINARY.class);
        put("IFA_LLLLCHAR", IFA_LLLLCHAR.class);
        put("IFA_LLNUM", IFA_LLNUM.class);
        put("IFA_NUMERIC", IFA_NUMERIC.class);


        put("IFB_BINARY", IFB_BINARY.class);
        put("IFB_BITMAP", IFB_BITMAP.class);
        put("IFB_LLBINARY", IFB_LLBINARY.class);
        put("IFB_LLCHAR", IFB_LLCHAR.class);
        put("IFB_LLLBINARY", IFB_LLLBINARY.class);
        put("IFB_LLLCHAR", IFB_LLLCHAR.class);
        put("IFB_LLLHCHAR", IFB_LLLHCHAR.class);
        put("IFB_LLNUM", IFB_LLNUM.class);
        put("IFB_NUMERIC", IFB_NUMERIC.class);
        put("IFHex_BITMAP", IFHex_BITMAP.class);
    }

    /**
     * Put.
     *
     * @param name   the name
     * @param aClass the a class
     */
    public static void put(String name, Class<? extends ISOComponentPackager> aClass) {
        map.put(name, aClass);
    }


    /**
     * Of iso component packager.
     *
     * @param name        the name
     * @param len         the len
     * @param description the description
     * @return the iso component packager
     */
    public static ISOComponentPackager of(String name, int len, String description) {
        if (!map.containsKey(name))
            throw new RuntimeException("can not found iso component packager with name=" + name);
        Class<? extends ISOComponentPackager> componentPackagerBase = map.get(name);
        try {
            ISOComponentPackager isoComponentPackager = componentPackagerBase.getConstructor(int.class, String.class).newInstance(len, description);
            return isoComponentPackager;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ofb iso component packager.
     *
     * @param name         the name
     * @param len          the len
     * @param description  the description
     * @param isLeftPadded the is left padded
     * @return the iso component packager
     */
    public static ISOComponentPackager ofb(String name, int len, String description, boolean isLeftPadded) {
        if (!map.containsKey(name))
            throw new RuntimeException("can not found iso component packager with name=" + name);
        Class<? extends ISOComponentPackager> componentPackagerBase = map.get(name);
        try {
            ISOComponentPackager isoComponentPackager = componentPackagerBase.getConstructor(int.class, String.class, boolean.class).newInstance(len, description, isLeftPadded);
            return isoComponentPackager;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

