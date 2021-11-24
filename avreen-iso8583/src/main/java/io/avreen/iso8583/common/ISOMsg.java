package io.avreen.iso8583.common;

import io.avreen.common.context.*;
import io.avreen.common.netty.IMessageTypeSupplier;
import io.avreen.iso8583.packager.api.ISOMsgPackager;
import io.avreen.iso8583.util.DumpUtil;
import io.avreen.iso8583.util.ISOUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * The class Iso msg.
 */
public class ISOMsg implements ISOComponent<ISOMsg>, ISOComponentDumper, IRejectSupportObject, Serializable, IMsgContextAware<ISOMsg>, ICaptureTimeSupportObject,
        IMessageTypeSupplier {
    /**
     * The Fields.
     */
    protected Map<Integer, ISOComponent> fields;
    /**
     * The Max field.
     */
    protected transient int maxField;
    /**
     * The Dirty.
     */
    protected transient boolean dirty,
    /**
     * The Max field dirty.
     */
    maxFieldDirty;
    /**
     * The Iso header.
     */
    protected byte[] isoHeader = null;

    private Long captureTime;
    /**
     * The Trailer.
     */
    protected byte[] trailer;
    private Integer rejectCode;
    private transient byte[] rejectBuffer;
    private byte[] rawBuffer;
    private transient ISOMsgPackager packager;

    private transient MsgContext<ISOMsg> msgContext;

    /**
     * Instantiates a new Iso msg.
     */
    public ISOMsg() {
        fields = new TreeMap();
        maxField = -1;
        dirty = true;
        maxFieldDirty = true;
        isoHeader = null;
        trailer = null;
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
     * Get reject buffer byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getRejectBuffer() {
        return rejectBuffer;
    }

    /**
     * Sets reject buffer.
     *
     * @param rejectBuffer the reject buffer
     */
    public void setRejectBuffer(byte[] rejectBuffer) {
        this.rejectBuffer = rejectBuffer;
    }


//    public byte[] getRejectBuffer() {
//        return rejectBuffer;
//    }
//
//    public void setRejectBuffer(byte[] rejectBuffer) {
//        this.rejectBuffer = rejectBuffer;
//    }
//
//    public int getRejectCode() {
//        return rejectCode;
//    }
//
//    public void setRejectCode(int rejectCode) {
//        this.rejectCode = rejectCode;
//    }


    /**
     * Get iso header byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getISOHeader() {
        return isoHeader;
    }

    /**
     * Sets iso header.
     *
     * @param isoHeader the iso header
     */
    public void setISOHeader(byte[] isoHeader) {
        this.isoHeader = isoHeader;
    }


    /**
     * Get trailer byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getTrailer() {
        return this.trailer;
    }

    /**
     * Sets trailer.
     *
     * @param trailer the trailer
     */
    public void setTrailer(byte[] trailer) {
        this.trailer = trailer;
    }


    /**
     * Gets max field.
     *
     * @return the max field
     */
    public int getMaxField() {
        if (maxFieldDirty)
            recalcMaxField();
        return maxField;
    }

    private void recalcMaxField() {
        maxField = 0;
        for (Object obj : fields.keySet()) {
            if (obj instanceof Integer)
                maxField = Math.max(maxField, ((Integer) obj).intValue());
        }
        maxFieldDirty = false;
    }

    /**
     * Set.
     *
     * @param fldno the fldno
     * @param c     the c
     */
    public void set(int fldno, ISOComponent c) {
        if (c != null) {
            fields.put(fldno, c);
            if (fldno > maxField)
                maxField = fldno;
            dirty = true;
        }
    }

    /**
     * Set.
     *
     * @param fldno the fldno
     * @param value the value
     */
    public void set(int fldno, String value) {
        if (value == null) {
            unset(fldno);
            return;
        }
        set(fldno, new ISOStringField(value));
    }

    public void set(int fldno, Integer value) {
        if (value == null) {
            unset(fldno);
            return;
        }
        set(fldno, new ISOStringField(value.toString()));
    }


    /**
     * Set.
     *
     * @param fpath the fpath
     * @param value the value
     */
    public void set(String fpath, String value) {
        StringTokenizer st = new StringTokenizer(fpath, ".");
        ISOMsg m = this;
        for (; ; ) {
            int fldno = Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) {
                Object obj = m.getValue(fldno);
                if (obj instanceof ISOMsg)
                    m = (ISOMsg) obj;
                else
                /**
                 * we need to go deeper, however, if the value == null then
                 * there is nothing to do (unset) at the lower levels, so break now and save some processing.
                 */
                    if (value == null) {
                        break;
                    } else {

                        // We have a value to set, so adding a level to hold it is sensible.
                        m.set(fldno, m = new ISOMsg());

                    }
            } else {
                m.set(fldno, value);
                break;
            }
        }
    }

    /**
     * Set.
     *
     * @param fpath the fpath
     * @param c     the c
     */
    public void set(String fpath, ISOComponent c) {
        StringTokenizer st = new StringTokenizer(fpath, ".");
        ISOMsg m = this;
        for (; ; ) {
            int fldno = Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) {
                Object obj = m.getValue(fldno);
                if (obj instanceof ISOMsg)
                    m = (ISOMsg) obj;
                else
                    /*
                     * we need to go deeper, however, if the value == null then
                     * there is nothing to do (unset) at the lower levels, so break now and save some processing.
                     */
                    if (c == null) {
                        break;
                    } else {
                        // We have a value to set, so adding a level to hold it is sensible.
                        m.set(fldno, m = new ISOMsg());
                    }
            } else {
                m.set(fldno, c);
                break;
            }
        }
    }

    /**
     * Set.
     *
     * @param fpath the fpath
     * @param value the value
     */
    public void set(String fpath, byte[] value) {
        StringTokenizer st = new StringTokenizer(fpath, ".");
        ISOMsg m = this;
        for (; ; ) {
            int fldno = Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) {
                Object obj = m.getValue(fldno);
                if (obj instanceof ISOMsg)
                    m = (ISOMsg) obj;
                else

                    m.set(fldno, m = new ISOMsg());

            } else {
                m.set(fldno, value);
                break;
            }
        }
    }

    /**
     * Set.
     *
     * @param fldno the fldno
     * @param value the value
     */
    public void set(int fldno, byte[] value) {
        if (value == null) {
            unset(fldno);
            return;
        }


        set(fldno, new ISOBinaryField(value));

    }

    /**
     * Unset.
     *
     * @param fldno the fldno
     */
    public void unset(int fldno) {
        if (fields.remove(fldno) != null)
            dirty = maxFieldDirty = true;
    }

    /**
     * Unset.
     *
     * @param flds the flds
     */
    public void unset(int[] flds) {
        for (int fld : flds)
            unset(fld);
    }

    /**
     * Unset.
     *
     * @param fpath the fpath
     */
    public void unset(String fpath) {
        StringTokenizer st = new StringTokenizer(fpath, ".");
        ISOMsg m = this;
        ISOMsg lastm = m;
        int fldno = -1;
        int lastfldno;
        for (; ; ) {
            lastfldno = fldno;
            fldno = Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) {
                Object obj = m.getValue(fldno);
                if (obj instanceof ISOMsg) {
                    lastm = m;
                    m = (ISOMsg) obj;
                } else {
                    // No real way of unset further subfield, exit.
                    break;
                }
            } else {
                m.unset(fldno);
                if (!m.hasFields() && lastfldno != -1) {
                    lastm.unset(lastfldno);
                }
                break;
            }
        }
    }

    /**
     * Recalc bit map.
     */
    public void recalcBitMap() {
        recalcBitMap(1);
    }

    /**
     * Recalc bit map.
     *
     * @param bitmapIndex the bitmap index
     */
    public void recalcBitMap(int bitmapIndex) {
        if (!dirty)
            return;
        if (bitmapIndex < 0)
            return;
        int mf = Math.min(getMaxField(), 192);
        BitSet bmap = new BitSet(mf + 62 >> 6 << 6);
        for (int i = bitmapIndex + 1; i <= mf; i++) {
            ISOComponent isoComponent = fields.get(i);
            if (isoComponent != null)
                bmap.set(i);

        }
        set(bitmapIndex, new ISOBitMap(bmap));
        dirty = false;
    }

    /**
     * Gets children.
     *
     * @return the children
     */
    public Map<Integer, ISOComponent> getChildren() {
        return (Map) ((TreeMap) fields).clone();
    }

    /**
     * Dump.
     *
     * @param p      the p
     * @param indent the indent
     */
    public void dump(PrintStream p, String indent) {
        // ISOComponent c;
        p.println();
        p.println(indent + "[");
        String newIndent = indent + "   ";
        if (getISOHeader() != null) {
            String headerDump = DumpUtil.buildItem("header", ISOUtil.hexString(getISOHeader()));
            p.print(newIndent + "{" + headerDump + "},");
            p.println();
        }
        if (getCaptureTime() != null) {
            String dumpString = DumpUtil.buildItem("captureTime", getCaptureTime());
            p.print(newIndent + "{" + dumpString + "},");
            p.println();
        }


        if (!isReject()) {
            int idx = 0;
            int count = fields.keySet().size();
            for (Integer i : fields.keySet()) {
                ISOComponent c = fields.get(i);
                ISOComponentDumper iValueDumper = DumpUtil.getIsoComponentDumper(c);
                iValueDumper.dump(i, c, p, newIndent);
                idx++;
                if (idx < count)
                    p.print(",");
                p.println();
            }


        } else {
            p.println(indent + "  {rejectCode:\"" + getRejectCode() + "\"}");
            if (getRejectBuffer() != null) {
                p.println(indent + "reject buffer dump:");
                p.println(ISOUtil.hexdump(getRejectBuffer()));
            }
        }
        p.println(indent + "]");
    }

    /**
     * Gets component.
     *
     * @param fldno the fldno
     * @return the component
     */
    public ISOComponent getComponent(int fldno) {
        return fields.get(fldno);
    }

    /**
     * Gets value.
     *
     * @param fldno the fldno
     * @return the value
     */
    public Object getValue(int fldno) {
        ISOComponent c = getComponent(fldno);

        return c != null ? c.getValue() : null;

    }

    /**
     * Gets value.
     *
     * @param fpath the fpath
     * @return the value
     */
    public Object getValue(String fpath) {
        StringTokenizer st = new StringTokenizer(fpath, ".");
        ISOMsg m = this;
        Object obj;
        for (; ; ) {
            int fldno = Integer.parseInt(st.nextToken());
            obj = m.getValue(fldno);
            if (obj == null) {
                // The user will always get a null value for an incorrect path or path not present in the message
                // no point having the  thrown for fields that were not received.
                break;
            }
            if (st.hasMoreTokens()) {
                if (obj instanceof ISOMsg) {
                    m = (ISOMsg) obj;
                } else
                    throw new RuntimeException("Invalid path '" + fpath + "'");
            } else
                break;
        }
        return obj;
    }

    /**
     * Gets component.
     *
     * @param fpath the fpath
     * @return the component
     */
    public ISOComponent getComponent(String fpath) {
        StringTokenizer st = new StringTokenizer(fpath, ".");
        ISOMsg m = this;
        ISOComponent obj;
        for (; ; ) {
            int fldno = Integer.parseInt(st.nextToken());
            obj = m.getComponent(fldno);
            if (st.hasMoreTokens()) {
                if (obj instanceof ISOMsg) {
                    m = (ISOMsg) obj;
                } else
                    break; // 'Quick' exit if hierachy is not present.
            } else
                break;
        }
        return obj;
    }

    /**
     * Gets string.
     *
     * @param fldno the fldno
     * @return the string
     */
    public String getString(int fldno) {
        String s = null;
        if (hasField(fldno)) {
            Object obj = getValue(fldno);
            if (obj instanceof String)
                s = (String) obj;
            else if (obj instanceof byte[])
                s = ISOUtil.hexString((byte[]) obj);
        }
        return s;
    }


    /**
     * Gets string.
     *
     * @param fpath the fpath
     * @return the string
     */
    public String getString(String fpath) {
        String s = null;

        Object obj = getValue(fpath);
        if (obj instanceof String)
            s = (String) obj;
        else if (obj instanceof byte[])
            s = ISOUtil.hexString((byte[]) obj);

        return s;
    }

    /**
     * Get bytes byte [ ].
     *
     * @param fldno the fldno
     * @return the byte [ ]
     */
    public byte[] getBytes(int fldno) {
        byte[] b = null;
        if (hasField(fldno)) {
            Object obj = getValue(fldno);
            if (obj instanceof String)
                b = ((String) obj).getBytes(ISOUtil.CHARSET);
            else if (obj instanceof byte[])
                b = (byte[]) obj;
        }
        return b;
    }

    /**
     * Get bytes byte [ ].
     *
     * @param fpath the fpath
     * @return the byte [ ]
     */
    public byte[] getBytes(String fpath) {
        byte[] b = null;

        Object obj = getValue(fpath);
        if (obj instanceof String)
            b = ((String) obj).getBytes(ISOUtil.CHARSET);
        else if (obj instanceof byte[])
            b = (byte[]) obj;

        return b;
    }

    /**
     * Has field boolean.
     *
     * @param fldno the fldno
     * @return the boolean
     */
    public boolean hasField(int fldno) {
        return fields.get(fldno) != null;
    }

    /**
     * Has fields boolean.
     *
     * @param fields the fields
     * @return the boolean
     */
    public boolean hasFields(int[] fields) {
        for (int field : fields)
            if (!hasField(field))
                return false;
        return true;
    }

    /**
     * Has any boolean.
     *
     * @param fields the fields
     * @return the boolean
     */
    public boolean hasAny(int[] fields) {
        for (int field : fields)
            if (hasField(field))
                return true;
        return false;
    }

    /**
     * Has any boolean.
     *
     * @param fields the fields
     * @return the boolean
     */
    public boolean hasAny(String... fields) {
        for (String field : fields)
            if (hasField(field))
                return true;
        return false;
    }

    /**
     * Has field boolean.
     *
     * @param fpath the fpath
     * @return the boolean
     */
    public boolean hasField(String fpath) {
        StringTokenizer st = new StringTokenizer(fpath, ".");
        ISOMsg m = this;
        for (; ; ) {
            int fldno = Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) {
                Object obj = m.getValue(fldno);
                if (obj instanceof ISOMsg) {
                    m = (ISOMsg) obj;
                } else {
                    // No real way of checking for further subfields, return false, perhaps should be ?
                    return false;
                }
            } else {
                return m.hasField(fldno);
            }
        }
    }

    /**
     * Has fields boolean.
     *
     * @return the boolean
     */
    public boolean hasFields() {
        return !fields.isEmpty();
    }

    @Override
    public Object clone() {
        try {
            ISOMsg m = (ISOMsg) super.clone();
            m.setRawBuffer(null);
            m.setISOHeader(getISOHeader());

            m.fields = (TreeMap) ((TreeMap) fields).clone();
            if (isoHeader != null)
                m.isoHeader = isoHeader;
            if (trailer != null)
                m.trailer = trailer.clone();
            for (Integer k : fields.keySet()) {
                ISOComponent c = (ISOComponent) m.fields.get(k);
//                setOriginalFieldValue(k, null);
                if (c instanceof ISOMsg)
                    m.fields.put(k, (ISOMsg) ((ISOMsg) c).clone());
            }
            return m;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Clone object.
     *
     * @param fields the fields
     * @return the object
     */
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public Object clone(int[] fields) {
        try {
            ISOMsg m = (ISOMsg) super.clone();
            m.fields = new TreeMap();
            for (int field : fields) {
                if (hasField(field)) {

                    m.set(field, getComponent(field));

                }
            }
            return m;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Merge.
     *
     * @param m the m
     */
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public void merge(ISOMsg m) {
        for (int i = 0; i <= m.getMaxField(); i++)

            if (m.hasField(i))
                set(i, m.getComponent(i));

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
        if (!hasField(0))
            throw new RuntimeException("MTI not available");
        return (String) getValue(0);
    }

    /**
     * Sets mti.
     *
     * @param mti the mti
     */
    public void setMTI(String mti) {
        set(0, new ISOStringField(mti));
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
        char c1 = mti.charAt(3);
        char c2 = '0';
        switch (c1) {
            case '0':
            case '1':
                c2 = '0';
                break;
            case '2':
            case '3':
                c2 = '2';
                break;
            case '4':
            case '5':
                c2 = '4';
                break;

        }
        set(0, new ISOStringField(
                        mti.substring(0, 2)
                                + (Character.getNumericValue(getMTI().charAt(2)) + 1) + c2
                )
        );
        return this;
    }

    /**
     * Sets retransmission mti.
     */
    public void setRetransmissionMTI() {
        if (!isRequest())
            throw new RuntimeException("not a request");

        set(0, new ISOStringField(getMTI().substring(0, 3) + "1"));
    }

    /**
     * Gets packager.
     *
     * @return the packager
     */
    public ISOMsgPackager getPackager() {
        return packager;
    }

    /**
     * Sets packager.
     *
     * @param packager the packager
     * @return the packager
     */
    public ISOMsg setPackager(ISOMsgPackager packager) {
        this.packager = packager;
        return this;
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
