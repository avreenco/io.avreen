package io.avreen.iso8583.common;

import io.avreen.iso8583.util.DumpUtil;

import java.io.PrintStream;

/**
 * The class Default iso component dumper.
 */
public class DefaultISOComponentDumper
        implements ISOComponentDumper {

    /**
     * The constant INSTANCE.
     */
    public static final DefaultISOComponentDumper INSTANCE = new DefaultISOComponentDumper();

    private DefaultISOComponentDumper() {
    }

    @Override
    public void dump(int fieldNumber, ISOComponent isoComponent, PrintStream p, String indent) {
        p.print(indent);
        p.print("{ ");
        p.print(DumpUtil.buildItem(ISOComponentDumper.ID_ATTR, fieldNumber));
        p.print(" , ");
        p.print(DumpUtil.buildItem(ISOComponentDumper.TYPE_ATTR, isoComponent.getType()));
        p.print(" , ");
        String strValue = isoComponent.toString();
        p.print(DumpUtil.buildItem(ISOComponentDumper.VALUE_ATTR, strValue));
        p.print("}");
    }

    @Override
    public String convertToString(ISOComponent isoComponent) {
        return isoComponent.toString();
    }


}
