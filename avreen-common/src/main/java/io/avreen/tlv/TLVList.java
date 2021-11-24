package io.avreen.tlv;

import io.avreen.common.packager.IValuePackager;
import io.avreen.common.util.CodecUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * The class Tlv list.
 */
public class TLVList implements Serializable {

    private List<TLVMsg> tags = new ArrayList<>();

    /**
     * Instantiates a new Tlv list.
     */
    public TLVList() {
        super();
    }


    private static String buildItem(String key, Object value) {
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

    private static void rebuildTagsHierarchy(TLVList tlvList, TLVMsg parent) {
        for (TLVMsg tlvMsg : tlvList.getTags()) {
            tlvMsg.setParent(parent);
            if (tlvMsg.getValue() instanceof TLVList) {
                rebuildTagsHierarchy((TLVList) tlvMsg.getValue(), tlvMsg);
            }
        }
    }

    /**
     * Unpack.
     *
     * @param buf the buf
     */
    public void unpack(byte[] buf) {
        unpack(buf, 0);
    }

    /**
     * Gets tags.
     *
     * @return the tags
     */
    public List<TLVMsg> getTags() {
        return tags;
    }

    /**
     * Elements enumeration.
     *
     * @return the enumeration
     */
    public Enumeration<TLVMsg> elements() {
        return Collections.enumeration(tags);
    }

    /**
     * Unpack.
     *
     * @param buf    the buf
     * @param offset the offset
     */
    public void unpack(byte[] buf, int offset) {
        ByteBuffer buffer = ByteBuffer.wrap(buf, offset, buf.length - offset);
        unpack(buffer);

    }

    /**
     * Unpack.
     *
     * @param buffer the buffer
     */
    public void unpack(ByteBuffer buffer) {
        while (hasNext(buffer)) {
            TLVMsg currentNode = getTLVMsg(buffer);    // null is returned if no tag found (trailing padding)
            if (currentNode != null)
                append(currentNode);
        }
    }

    /**
     * Append.
     *
     * @param tlvToAppend the tlv to append
     */
    public void append(TLVMsg tlvToAppend) {
        tags.add(tlvToAppend);


    }

    /**
     * Append.
     *
     * @param tag   the tag
     * @param value the value
     */
    public void append(int tag, byte[] value) {
        append(new TLVMsg(tag, value));
    }

    /**
     * Append.
     *
     * @param tag   the tag
     * @param value the value
     */
    public void append(int tag, String value) {
        append(new TLVMsg(tag, CodecUtil.hex2byte(value)));
    }

    /**
     * Delete by index.
     *
     * @param index the index
     */
    public void deleteByIndex(int index) {
        tags.remove(index);
    }

    /**
     * Delete by tag.
     *
     * @param tag the tag
     */
    public void deleteByTag(int tag) {
        List<TLVMsg> t = new ArrayList<TLVMsg>();
        for (TLVMsg tlv2 : tags) {
            if (tlv2.getTag() == tag)
                t.add(tlv2);
        }
        tags.removeAll(t);
    }

    /**
     * Find tlv list.
     *
     * @param tag the tag
     * @return the tlv list
     */
    public TLVList find(int tag) {
        TLVList tlvList = null;
        for (TLVMsg tlv : tags) {
            if (tlv.getTag() == tag) {
                if (tlvList == null)
                    tlvList = new TLVList();
                tlvList.append(tlv);
            }
        }
        return tlvList;
    }

    /**
     * Find first tlv msg.
     *
     * @param tag the tag
     * @return the tlv msg
     */
    public TLVMsg findFirst(int tag) {
        TLVList tlvList = find(tag);
        if (tlvList != null) {
            if (tlvList.getTags().size() > 0)
                return tlvList.getTags().get(0);
        }
        return null;
    }

    /**
     * Index tlv msg.
     *
     * @param index the index
     * @return the tlv msg
     */
    public TLVMsg index(int index) {
        return tags.get(index);
    }

    /**
     * Unpack values.
     *
     * @param tlv_codec the tlv codec
     */
    public void unpackValues(HashMap<String, IValuePackager> tlv_codec) {

        unpackValues(tlv_codec, true);
    }

    private void unpackValues(HashMap<String, IValuePackager> tlv_codec, boolean rebuild_ierarchy) {
        if (rebuild_ierarchy)
            rebuildTagsHierarchy();
        for (TLVMsg tlvMsg : getTags()) {
            if (tlvMsg.getValue() instanceof TLVList) {
                ((TLVList) tlvMsg.getValue()).unpackValues(tlv_codec, false);
            } else {
                String fulltag = tlvMsg.getCompleteTagString();
                if (tlv_codec.containsKey(fulltag)) {
                    byte[] bytes = tlvMsg.getBytes();
                    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                    tlvMsg.setValue(tlv_codec.get(fulltag).unpack(byteBuffer, bytes.length));
                }
            }

        }
    }

    /**
     * Rebuild tags hierarchy.
     */
    public void rebuildTagsHierarchy() {
        rebuildTagsHierarchy(this, null);
    }


    /**
     * Dump.
     *
     * @param p      the p
     * @param indent the indent
     */
    public void dump(PrintStream p, String indent) {
        p.println();
        p.print(indent + "[");
        String newIndent = indent + " ";
        int count = getTags().size();
        int idx = 0;
        for (TLVMsg tlvMsg : getTags()) {
            {
                if (tlvMsg.getValue() == null)
                    return;
                p.println();
                p.print(newIndent);
                p.print("{ ");
                p.print(buildItem("tag", Integer.toHexString(tlvMsg.getTag())).toUpperCase());
                p.print(" , ");
                if (tlvMsg.getValue() instanceof TLVList)
                    p.print(buildItem("type", "tlv"));
                else if (tlvMsg.getValue() instanceof byte[]) {
                    p.print(buildItem("type", "binary"));
                    p.print(" , ");
                } else {
                    p.print(buildItem("type", "string"));
                    p.print(" , ");
                }


                String strValue = null;
                if (tlvMsg.getValue() instanceof TLVList) {
                    p.print(" ,\"value\": ");
                    ((TLVList) tlvMsg.getValue()).dump(p, newIndent + " ");
                    p.println();
                    p.print(newIndent);
                    p.print("}");
                } else {
                    if (tlvMsg.getValue() instanceof byte[])
                        strValue = CodecUtil.hexString((byte[]) tlvMsg.getValue());
                    else
                        strValue = tlvMsg.getValue().toString();
                    p.print(buildItem("value", strValue));
                    p.print("}");
                }
                idx++;
                if (idx < count)
                    p.print(",");

            }


        }
        p.println();
        p.print(indent + "]");
    }

    public String toString() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        PrintStream printWriter = new PrintStream(result);
        this.dump(printWriter, "");
        return result.toString();
    }

    /**
     * Pack values.
     *
     * @param tlv_codec the tlv codec
     */
    public void packValues(HashMap<String, IValuePackager> tlv_codec) {
        packValues(tlv_codec, true);
    }

    private void packValues(HashMap<String, IValuePackager> tlv_codec, boolean rebuild_hirerchy) {
        if (rebuild_hirerchy)
            rebuildTagsHierarchy();
        for (TLVMsg tlvMsg : getTags()) {
            if (tlvMsg.getValue() instanceof TLVList) {
                ((TLVList) tlvMsg.getValue()).packValues(tlv_codec, false);
            } else {
                String fulltag = tlvMsg.getCompleteTagString();
                if (tlv_codec.containsKey(fulltag)) {
                    tlvMsg.setValue(tlv_codec.get(fulltag).pack(tlvMsg.getValue()));
                }
            }
        }
    }


    /**
     * Pack values.
     */
    public void packValues() {

    }


    /**
     * Pack byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] pack() {
        ByteBuffer buffer = ByteBuffer.allocate(9999);
        pack(buffer);
        byte[] b = new byte[buffer.position()];
        buffer.flip();
        buffer.get(b);
        return b;
    }

    /**
     * Pack.
     *
     * @param buffer the buffer
     */
    public void pack(ByteBuffer buffer) {
        for (TLVMsg tlv : tags)
            buffer.put(tlv.getTLV());
    }

    private TLVMsg getTLVMsg(ByteBuffer buffer) {
        int tag = getTAG(buffer);  // tag = 0 if tag not found
        if (tag == 0)
            return null;
        // Get Length if buffer remains!
        if (!buffer.hasRemaining())
            throw new RuntimeException(String.format("BAD TLV FORMAT - tag (%x)"
                    + " without length or value", tag));

        int length = getValueLength(buffer);
        if (length > buffer.remaining())
            throw new RuntimeException(String.format("BAD TLV FORMAT - tag (%x)"
                    + " length (%d) exceeds available data.", tag, length));

        byte[] arrValue = new byte[length];
        buffer.get(arrValue);

        if (TLVMsg.isConstructedTag(tag)) {

            TLVList tlvList = new TLVList();
            tlvList.unpack(arrValue);
            return new TLVMsg(tag, tlvList);

        } else {
            return new TLVMsg(tag, arrValue);
        }

    }

    /**
     * Gets tlv msg.
     *
     * @param tag      the tag
     * @param arrValue the arr value
     * @return the tlv msg
     */
    protected TLVMsg getTLVMsg(int tag, byte[] arrValue) {
        return new TLVMsg(tag, arrValue);
    }

    private boolean hasNext(ByteBuffer buffer) {
        return buffer.hasRemaining();
    }

    private int getTAG(ByteBuffer buffer) {
        int b;
        int tag;
        b = buffer.get() & 0xff;
        // Skip padding chars
        if (b == 0xFF || b == 0x00) {
            do {
                if (hasNext(buffer)) {
                    b = buffer.get() & 0xff;
                } else {
                    break;
                }
            } while (b == 0xFF || b == 0x00);
        }
        // Get first byte of Tag Identifier
        tag = b;
        // Get rest of Tag identifier if required
        if ((b & 0x1F) == 0x1F) {
            do {
                tag <<= 8;
                b = buffer.get();
                tag |= b & 0xFF;

            } while ((b & 0x80) == 0x80);
        }
        return tag;
    }

    /**
     * Gets value length.
     *
     * @param buffer the buffer
     * @return the value length
     */
    protected int getValueLength(ByteBuffer buffer) {
        byte b = buffer.get();
        int count = b & 0x7f;
        // check first byte for more bytes to follow
        if ((b & 0x80) == 0 || count == 0)
            return count;

        //fetch rest of bytes
        byte[] bb = new byte[count];
        buffer.get(bb);
        //adjust buffer if first bit is turn on
        //important for BigInteger reprsentation
        if ((bb[0] & 0x80) > 0)
            bb = CodecUtil.concat(new byte[1], bb);
        return new BigInteger(bb).intValue();
    }

    /**
     * Has tag boolean.
     *
     * @param tag the tag
     * @return the boolean
     */
    public boolean hasTag(int tag) {
        for (TLVMsg tlv : tags) {
            if (tlv.getTag() == tag)
                return true;
        }
        return false;
    }

    /**
     * Has tag boolean.
     *
     * @param tag the tag
     * @return the boolean
     */
    public boolean hasTag(String tag) {
        return hasTag(Integer.parseInt(tag, 16));
    }

}
