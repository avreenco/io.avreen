package io.avreen.iso8583.util;

import io.avreen.iso8583.common.*;

import java.util.Map;
import java.util.Set;

/**
 * The class Dump util.
 */
public class DumpUtil {
    /**
     * Gets iso component dumper.
     *
     * @param isoComponent the iso component
     * @return the iso component dumper
     */
    public static ISOComponentDumper getIsoComponentDumper(ISOComponent isoComponent) {
        if (isoComponent.getDumper() != null)
            return isoComponent.getDumper();
        return DefaultISOComponentDumper.INSTANCE;
    }

    /**
     * Sets iso component dumper.
     *
     * @param isoComponent the iso component
     * @param dumper       the dumper
     */
    public static void setIsoComponentDumper(ISOComponent isoComponent, ISOComponentDumper dumper) {
        if (isoComponent instanceof ISOComponentDumperAware) {
            ((ISOComponentDumperAware) isoComponent).setISOComponentDumper(dumper);
        }
    }

    /**
     * Sets iso component dumper.
     *
     * @param isoMsg       the iso msg
     * @param dumper       the dumper
     * @param fieldNumbers the field numbers
     */
    public static void setIsoComponentDumper(ISOMsg isoMsg, ISOComponentDumper dumper, int... fieldNumbers) {
        for (int fieldNumber : fieldNumbers) {
            ISOComponent isoComponent = isoMsg.getComponent(fieldNumber);
            if (isoComponent != null) {
                setIsoComponentDumper(isoComponent, dumper);
            }
        }
    }

    /**
     * Sets iso component dumper.
     *
     * @param isoMsg    the iso msg
     * @param dumperMap the dumper map
     */
    public static void setIsoComponentDumper(ISOMsg isoMsg, Map<Integer, ISOComponentDumper> dumperMap) {
        Set<Integer> fieldNumbers = dumperMap.keySet();
        for (int fieldNumber : fieldNumbers) {
            ISOComponent isoComponent = isoMsg.getComponent(fieldNumber);
            if (isoComponent != null) {
                setIsoComponentDumper(isoComponent, dumperMap.get(fieldNumber));
            }
        }
    }


    /**
     * Build item string.
     *
     * @param key   the key
     * @param value the value
     * @return the string
     */
    public static String buildItem(String key, Object value) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"");
        stringBuilder.append(key);
        stringBuilder.append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(value);
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }

}
