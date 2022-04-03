package io.avreen.iso8583.common;

import io.avreen.common.context.*;
import io.avreen.common.netty.IMessageTypeSupplier;
import io.avreen.common.util.CodecUtil;
import io.avreen.iso8583.util.DumpUtil;
import io.avreen.iso8583.util.ISOUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The class Iso msg.
 */
public class ISOMsg implements ISOComponent<ISOMsg>, ISOComponentDumper, IRejectSupportObject, Serializable, IMsgContextAware<ISOMsg>, ICaptureTimeSupportObject,
        IMessageTypeSupplier {
    /**
     * The FieldsMap.
     */
    protected Map<Integer, ISOComponent> fieldMap = new HashMap<>();
    /**
     * The Iso header.
     */
    protected byte[] header = null;

    private Long captureTime;
    private Integer rejectCode;
    private byte[] rawBuffer;
    private transient MsgContext<ISOMsg> msgContext;

    /**
     * Instantiates a new Iso msg.
     */
    public ISOMsg() {
    }

    /**
     * Get raw buffer byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getRawBuffer() {
        return rawBuffer;
    }

    /**
     * Sets raw buffer.
     *
     * @param rawBuffer the raw buffer
     */
    public void setRawBuffer(byte[] rawBuffer) {
        this.rawBuffer = rawBuffer;
    }

    public Integer getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(int rejectCode) {
        this.rejectCode = rejectCode;
    }

    public boolean isReject() {
        return rejectCode != null;
    }


    /**
     * Get iso header byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getHeader() {
        return header;
    }

    /**
     * Sets iso header.
     *
     * @param header the iso header
     */
    public ISOMsg setHeader(byte[] header) {
        this.header = header;
        return this;
    }


    public int calcMaxField() {
        int maxField = 0;
        for (Object obj : fieldMap.keySet()) {
            if (obj instanceof Integer)
                maxField = Math.max(maxField, ((Integer) obj).intValue());
        }
        return maxField;
    }

    /**
     * Set.
     *
     * @param fldno the fldno
     * @param c     the c
     */
    public ISOMsg set(int fldno, ISOComponent c) {
        if (c != null) {
            fieldMap.put(fldno, c);
        }
        return this;
    }

    /**
     * Set.
     *
     * @param fldno the fldno
     * @param value the value
     */
    public ISOMsg set(int fldno, String value) {
        if (value == null) {
            remove(fldno);
            return this;
        }
        return set(fldno, new ISOStringField(value));

    }

    public ISOMsg set(int fldno, Integer value) {
        if (value == null) {
            remove(fldno);
            return this;
        }
        return set(fldno, new ISOStringField(value.toString()));
    }

    /**
     * Set.
     *
     * @param fldno the fldno
     * @param value the value
     */
    public ISOMsg set(int fldno, byte[] value) {
        if (value == null) {
            remove(fldno);
            return this;
        }
        return set(fldno, new ISOBinaryField(value));
    }

    /**
     * Unset.
     *
     * @param fldno the fldno
     */
    public ISOMsg remove(int fldno) {
        fieldMap.remove(fldno);
        return this;
    }

    /**
     * Unset.
     *
     * @param flds the flds
     */
    public ISOMsg remove(int[] flds) {
        for (int fld : flds)
            remove(fld);
        return this;
    }

    /**
     * Recalc bit map.
     */
    public ISOMsg reCalcBitMap() {
        return reCalcBitMap(1);

    }

    /**
     * Recalc bit map.
     *
     * @param bitmapIndex the bitmap index
     */
    public ISOMsg reCalcBitMap(int bitmapIndex) {
        if (bitmapIndex < 0)
            return this;
        int mf = Math.min(calcMaxField(), 192);
        BitSet bmap = new BitSet(mf + 62 >> 6 << 6);
        for (int i = bitmapIndex + 1; i <= mf; i++) {
            ISOComponent isoComponent = fieldMap.get(i);
            if (isoComponent != null)
                bmap.set(i);

        }
        set(bitmapIndex, new ISOBitMap(bmap));
        return this;
    }

    /**
     * Gets children.
     *
     * @return the children
     */
    public Set<Integer> fieldSet() {
        return fieldMap.keySet();
    }

    /**
     * Dump.
     *
     * @param p      the p
     * @param indent the indent
     */
    public ISOMsg dump(PrintStream p, String indent) {
        // ISOComponent c;
        p.println();
        p.println(indent + "{");
        String newIndent = indent + "   ";
        if (getHeader() != null) {
            String headerDump = DumpUtil.buildItem("header", CodecUtil.hexString(getHeader()));
            p.print(newIndent + headerDump + ",");
            p.println();
        }
        if (getCaptureTime() != null) {
            String dumpString = DumpUtil.buildItem("captureTime", getCaptureTime());
            p.print(newIndent + dumpString + ",");
            p.println();
        }


        if (!isReject()) {
            p.println(newIndent + "\"fields\":");
            p.println(newIndent + " [");
            int idx = 0;
            Integer[]  allFields = new Integer[fieldMap.keySet().size()];
            fieldMap.keySet().toArray(allFields);
            Arrays.sort(allFields);
            int count = allFields.length;
            for (int i : allFields) {
                ISOComponent c = fieldMap.get(i);
                ISOComponentDumper iValueDumper = DumpUtil.getIsoComponentDumper(c);
                iValueDumper.dump(i, c, p, newIndent + "   ");
                idx++;
                if (idx < count)
                    p.print(",");
                p.println();
            }
            p.println(newIndent + " ]");
        } else {
            String dumpString = DumpUtil.buildItem("rejectCode", getRejectCode());
            p.print(newIndent + dumpString);
            p.println();
        }
        p.println(indent + "}");
        return this;
    }

    /**
     * Gets component.
     *
     * @param idx the field no
     * @return the component
     */
    public ISOComponent getIsoComponent(int idx) {
        return get(idx, ISOComponent.class);
    }

    /**
     * Gets value.
     *
     * @param idx the fldno
     * @return the value
     */
    public <T> T get(int idx, Class<T> valueClass) {
        if (!fieldMap.containsKey(idx))
            return null;
        ISOComponent c = fieldMap.get(idx);
        if (ISOComponent.class.isAssignableFrom(valueClass))
            return (T) c;
        Object v = c.getValue();
        if (v == null)
            return null;
        if (valueClass == String.class) {
            if (v instanceof String)
                return (T) v;
            else if (v instanceof byte[])
                return (T) CodecUtil.hexString((byte[]) v);
            throw new RuntimeException("can not convert " + v.getClass() + " to String");
        } else if (valueClass == byte[].class) {
            if (v instanceof String)
                return (T) ((String) v).getBytes(StandardCharsets.ISO_8859_1);
            else if (v instanceof byte[])
                return (T) v;
            throw new RuntimeException("can not convert " + v.getClass() + " to byte[]");
        }
        throw new RuntimeException("not support value class= " + valueClass);
    }

    /**
     * Gets string.
     *
     * @param idx the field no
     * @return the string
     */
    public String getString(int idx) {
        return get(idx, String.class);
    }

    /**
     * Get bytes byte [ ].
     *
     * @param idx the fldno
     * @return the byte [ ]
     */
    public byte[] getBytes(int idx) {
        return get(idx, byte[].class);
    }

    /**
     * Has field boolean.
     *
     * @param idx the field no
     * @return the boolean
     */
    public boolean contains(int idx) {
        return fieldMap.get(idx) != null;
    }

    /**
     * Has fields boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return fieldMap.isEmpty();
    }

    @Override
    public Object clone() {
        try {
            ISOMsg m = (ISOMsg) super.clone();
            m.setRawBuffer(null);
            m.setHeader(getHeader());
            m.fieldMap = (HashMap) ((HashMap) fieldMap).clone();
            if (header != null)
                m.header = header;
            for (Integer k : fieldMap.keySet()) {
                ISOComponent c = (ISOComponent) m.fieldMap.get(k);
//                setOriginalFieldValue(k, null);
                if (c instanceof ISOMsg)
                    m.fieldMap.put(k, (ISOMsg) ((ISOMsg) c).clone());
            }
            return m;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override
    public ISOMsg getValue() {
        return this;
    }

    @Override
    public void setValue(ISOMsg obj) {
        throw new RuntimeException("setValue N/A in ISOMsg");
    }

    @Override
    public String getType() {
        return "isomsg";
    }

    @Override
    public ISOComponentDumper getDumper() {
        return this;
    }


    /**
     * Gets mti.
     *
     * @return the mti
     */
    public String getMTI() {
        if (!contains(0))
            throw new RuntimeException("MTI not available");
        return getString(0);
    }

    /**
     * Sets mti.
     *
     * @param mti the mti
     */
    public ISOMsg setMTI(String mti) {
        return set(0, new ISOStringField(mti));
    }

    /**
     * Is request boolean.
     *
     * @return the boolean
     */
    public boolean isRequest() {
        return Character.getNumericValue(getMTI().charAt(2)) % 2 == 0;
    }


    /**
     * Is response boolean.
     *
     * @return the boolean
     */
    public boolean isResponse() {
        return !isRequest();
    }

    /**
     * Is retransmission boolean.
     *
     * @return the boolean
     */
    public boolean isRetransmission() {
        return getMTI().charAt(3) == '1';
    }

    /**
     * Sets response mti.
     *
     * @return the response mti
     */
    public ISOMsg setResponseMTI() {
        if (!isRequest())
            return this;
        String mti = getMTI();
        char lastChar = mti.charAt(3);
        set(0, new ISOStringField(
                        mti.substring(0, 2)
                                + (Character.getNumericValue(getMTI().charAt(2)) + 1) + lastChar
                )
        );
        return this;
    }

    /**
     * Sets retransmission mti.
     */
    public ISOMsg setRetransmissionMTI() {
        if (!isRequest())
            throw new RuntimeException("iso message not a request");

        return set(0, new ISOStringField(getMTI().substring(0, 3) + "1"));
    }

    public Long getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Long captureTime) {
        this.captureTime = captureTime;
    }

    @Override
    public String toString() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        PrintStream printWriter = new PrintStream(result);
        this.dump(printWriter, "");
        return result.toString();
    }

    @Override
    public void dump(int fieldNumber, ISOComponent isoComponent, PrintStream p, String indent) {
        p.print(indent + "{ ");
        p.print(DumpUtil.buildItem(ISOComponentDumper.ID_ATTR, fieldNumber));
        p.print(" , ");
        p.print(DumpUtil.buildItem(ISOComponentDumper.TYPE_ATTR, "isomsg"));
        ((ISOMsg) isoComponent).dump(p, indent + " ");
        p.println();
        p.println(indent + "}");
    }

    @Override
    public String convertToString(ISOComponent isoComponent) {
        return toString();
    }

    @Override
    public void setMsgContext(MsgContext<ISOMsg> msgContext) {
        this.msgContext = msgContext;
    }

    @Override
    public MsgContext<ISOMsg> getMsgContext() {
        return msgContext;
    }

    @Override
    public MessageTypes getMessageTypes() {
        ISOMsg isoMsg = this;
        if (isoMsg.isReject())
            return MessageTypes.Reject;
        else if (isoMsg.isRequest())
            return MessageTypes.Request;
        return MessageTypes.Response;
    }

    @Override
    public void setResponse() {
        setResponseMTI();
    }

    /**
     * The enum Direction.
     */
    public enum Direction {
        /**
         * Incoming direction.
         */
        incoming,
        /**
         * Outgoing direction.
         */
        outgoing;

    }

}
