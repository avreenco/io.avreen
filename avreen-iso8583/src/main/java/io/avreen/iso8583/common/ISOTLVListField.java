package io.avreen.iso8583.common;

import io.avreen.tlv.TLVList;

/**
 * The class Isotlv list field.
 */
public class ISOTLVListField
        extends ISOField<TLVList> {

    /**
     * The Value.
     */
    protected TLVList value;

    /**
     * Instantiates a new Isotlv list field.
     */
    public ISOTLVListField() {
    }

//    @Override
//    public ISOComponentDumper getDumper() {
//        return this;
//    }

    /**
     * Instantiates a new Isotlv list field.
     *
     * @param v the v
     */
    public ISOTLVListField(TLVList v) {
        value = v;
    }

    @Override
    public TLVList getValue() {
        return value;
    }

    @Override
    public void setValue(TLVList obj) {
        value = obj;
    }

    @Override
    public String getType() {
        return "tlvlist";
    }

    @Override
    public String toString() {
        return value.toString();
    }

//    @Override
//    public void dump(int fieldNumber, ISOComponent isoComponent, PrintStream p, String indent)
//    {
//            p.print(indent + "{ ");
//            p.print(DumpUtil.buildItem(ISOComponentDumper.ID_ATTR, fieldNumber));
//            p.print(" , ");
//            p.print(DumpUtil.buildItem(ISOComponentDumper.TYPE_ATTR, "tlv"));
//            p.print(" , ");
//            p.print("\""+ISOComponentDumper.VALUE_ATTR+"\""+":");
//            value.dump(p, indent + " ");
//            p.println();
//            p.print(indent + "}");
//    }
}
