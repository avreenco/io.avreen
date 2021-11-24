package io.avreen.iso8583.packager;

import io.avreen.iso8583.packager.api.ISOComponentPackager;

/**
 * The class Iso field packager.
 */
public class ISOFieldPackager {

    /**
     * Put.
     *
     * @param name   the name
     * @param aClass the a class
     */
    public static void put(String name, Class<? extends ISOComponentPackager> aClass) {
        ISOComponentRegistry.put(name, aClass);
    }

    /**
     * Register.
     *
     * @param name   the name
     * @param aClass the a class
     */
    public static synchronized void register(String name, Class<? extends ISOComponentPackager> aClass) {
        ISOComponentRegistry.put(name, aClass);

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
        return ISOComponentRegistry.of(name, len, description);
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
        return ISOComponentRegistry.ofb(name, len, description, isLeftPadded);
    }

}

