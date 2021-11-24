package io.avreen.common.util;

/**
 * The class Object utils.
 */
public class ObjectUtils {
    /**
     * Null safe hash code int.
     *
     * @param obj the obj
     * @return the int
     */
    public static int nullSafeHashCode(Object obj) {
        if (obj == null) {
            return 0;
        } else {
            if (obj.getClass().isArray()) {
                if (obj instanceof Object[]) {
                    return nullSafeHashCode((Object[]) ((Object[]) obj));
                }

                if (obj instanceof boolean[]) {
                    return nullSafeHashCode((boolean[]) ((boolean[]) obj));
                }

                if (obj instanceof byte[]) {
                    return nullSafeHashCode((byte[]) ((byte[]) obj));
                }

                if (obj instanceof char[]) {
                    return nullSafeHashCode((char[]) ((char[]) obj));
                }

                if (obj instanceof double[]) {
                    return nullSafeHashCode((double[]) ((double[]) obj));
                }

                if (obj instanceof float[]) {
                    return nullSafeHashCode((float[]) ((float[]) obj));
                }

                if (obj instanceof int[]) {
                    return nullSafeHashCode((int[]) ((int[]) obj));
                }

                if (obj instanceof long[]) {
                    return nullSafeHashCode((long[]) ((long[]) obj));
                }

                if (obj instanceof short[]) {
                    return nullSafeHashCode((short[]) ((short[]) obj));
                }
            }

            return obj.hashCode();
        }
    }

    /**
     * Null safe hash code int.
     *
     * @param array the array
     * @return the int
     */
    public static int nullSafeHashCode(Object[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            Object[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Object element = var2[var4];
                hash = 31 * hash + nullSafeHashCode(element);
            }

            return hash;
        }
    }

    /**
     * Null safe hash code int.
     *
     * @param array the array
     * @return the int
     */
    public static int nullSafeHashCode(boolean[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            boolean[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                boolean element = var2[var4];
                hash = 31 * hash + Boolean.hashCode(element);
            }

            return hash;
        }
    }

    /**
     * Null safe hash code int.
     *
     * @param array the array
     * @return the int
     */
    public static int nullSafeHashCode(byte[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            byte[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                byte element = var2[var4];
                hash = 31 * hash + element;
            }

            return hash;
        }
    }

    /**
     * Null safe hash code int.
     *
     * @param array the array
     * @return the int
     */
    public static int nullSafeHashCode(char[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            char[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                char element = var2[var4];
                hash = 31 * hash + element;
            }

            return hash;
        }
    }

    /**
     * Null safe hash code int.
     *
     * @param array the array
     * @return the int
     */
    public static int nullSafeHashCode(double[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            double[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                double element = var2[var4];
                hash = 31 * hash + Double.hashCode(element);
            }

            return hash;
        }
    }

    /**
     * Null safe hash code int.
     *
     * @param array the array
     * @return the int
     */
    public static int nullSafeHashCode(float[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            float[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                float element = var2[var4];
                hash = 31 * hash + Float.hashCode(element);
            }

            return hash;
        }
    }

    /**
     * Null safe hash code int.
     *
     * @param array the array
     * @return the int
     */
    public static int nullSafeHashCode(int[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            int[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                int element = var2[var4];
                hash = 31 * hash + element;
            }

            return hash;
        }
    }

    /**
     * Null safe hash code int.
     *
     * @param array the array
     * @return the int
     */
    public static int nullSafeHashCode(long[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            long[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                long element = var2[var4];
                hash = 31 * hash + Long.hashCode(element);
            }

            return hash;
        }
    }

    /**
     * Null safe hash code int.
     *
     * @param array the array
     * @return the int
     */
    public static int nullSafeHashCode(short[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            short[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                short element = var2[var4];
                hash = 31 * hash + element;
            }

            return hash;
        }
    }

}
