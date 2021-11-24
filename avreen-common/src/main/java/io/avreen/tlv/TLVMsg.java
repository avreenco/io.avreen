package io.avreen.tlv;

import io.avreen.common.util.CodecUtil;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * The class Tlv msg.
 */
public class TLVMsg implements Serializable {
    /**
     * The Value.
     */
    protected Object value;
    private int tag;
    private TLVMsg parent;

    /**
     * Instantiates a new Tlv msg.
     */
    public TLVMsg() {
        super();
    }

    /**
     * Instantiates a new Tlv msg.
     *
     * @param tag   the tag
     * @param value the value
     */
    public TLVMsg(int tag, byte[] value) {
        this.tag = tag;
        this.setValue(value);
    }

    /**
     * Instantiates a new Tlv msg.
     *
     * @param tag   the tag
     * @param value the value
     */
    public TLVMsg(int tag, Integer value) {
        this.tag = tag;
        this.setValue(value.toString());
    }

    /**
     * Instantiates a new Tlv msg.
     *
     * @param tag   the tag
     * @param value the value
     */
    public TLVMsg(int tag, String value) {
        this.tag = tag;
        this.setValue(value);
    }

    /**
     * Instantiates a new Tlv msg.
     *
     * @param tag   the tag
     * @param value the value
     */
    public TLVMsg(int tag, TLVList value) {
        this.tag = tag;
        this.setValue(value);
    }

    /**
     * Get l byte [ ].
     *
     * @param len the len
     * @return the byte [ ]
     */
    public static byte[] getL(int len) {

        if (len == 0)
            return new byte[1];

        // if Length is less than 128
        // set the 8bit as 0 indicating next 7 bits is the length
        // of the message
        // if length is more than 127 then, set the first bit as 1 indicating
        // next 7 bits will indicate the length of following bytes used for
        // length

        BigInteger bi = BigInteger.valueOf(len);
        /* Value to be encoded on multiple bytes */
        byte[] rBytes = bi.toByteArray();
        /* If value can be encoded on one byte */
        if (len < 0x80)
            return rBytes;

        //we need 1 byte to indicate the length
        //for that is used sign byte (first 8-bits equals 0),
        //if it is not present it is added
        if (rBytes[0] > 0)
            rBytes = CodecUtil.concat(new byte[1], rBytes);
        rBytes[0] = (byte) (0x80 | rBytes.length - 1);

        return rBytes;
    }

    /**
     * Gets tag string.
     *
     * @param tag the tag
     * @return the tag string
     */
    public static String getTagString(int tag) {
        return Integer.toHexString(tag).toUpperCase();
    }

    /**
     * Is constructed tag boolean.
     *
     * @param tag the tag
     * @return the boolean
     */
    public static boolean isConstructedTag(int tag) {
        String hexVal = Integer.toHexString(tag);
        byte[] bytes = CodecUtil.hex2byte(hexVal);
        return (bytes[0] & 0x20) != 0;
    }


    /**
     * Gets complete tag string.
     *
     * @return the complete tag string
     */
    public String getCompleteTagString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (parent != null) {
            stringBuilder.append(parent.getCompleteTagString());
            stringBuilder.append(".");
        }
        String hexString = getTagString(tag);
        stringBuilder.append(hexString);
        return stringBuilder.toString();
    }


    /**
     * Gets tag.
     *
     * @return the tag
     */
    public int getTag() {
        return tag;
    }

    /**
     * Sets tag.
     *
     * @param tag the tag
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Object value) {
//        if (value != null) {
//            if (value instanceof TLVList) {
//                for (TLVMsg tlvMsg : ((TLVList) value).getTags()) {
//                    tlvMsg.parent = this;
//                }
//            }
//        }
        this.value = value;
    }

    /**
     * Gets tlv list.
     *
     * @return the tlv list
     */
    public TLVList getTLVList() {
        if (value == null)
            return null;
        if (value instanceof TLVList)
            return (TLVList) value;
        throw new RuntimeException("value is not tlv list. tag=" + getTagString(tag));
    }

    /**
     * Get bytes byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getBytes() {
        if (value == null)
            return null;
        if (value instanceof byte[])
            return (byte[]) value;
        throw new RuntimeException("value is not byte array. tag=" + getTagString(tag));
    }

    /**
     * Gets string.
     *
     * @return the string
     */
    public String getString() {
        if (value == null)
            return null;
        if (value instanceof String)
            return (String) value;
        throw new RuntimeException("value is not string. tag=" + getTagString(tag));
    }

    /**
     * Gets int.
     *
     * @return the int
     */
    public int getInt() {
        if (value instanceof String)
            return Integer.parseInt((String) value);
        throw new RuntimeException("value is not int. tag=" + getTagString(tag));
    }

    /**
     * Sets parent.
     *
     * @param parent the parent
     */
    protected void setParent(TLVMsg parent) {
        this.parent = parent;
    }


    /**
     * Get tlv byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getTLV() {
        String hexVal = Integer.toHexString(tag);
        byte[] bTag = CodecUtil.hex2byte(hexVal);
        if (value == null) {
            byte[] bLen = getL(0);
            int tLength = bTag.length + bLen.length;
            byte[] out = new byte[tLength];
            System.arraycopy(bTag, 0, out, 0, bTag.length);
            System.arraycopy(bLen, 0, out, bTag.length, bLen.length);
            return out;
        }

        byte[] packValue;
        if (value instanceof TLVList) {
            setValue(value); /* rebuild parents */
            packValue = ((TLVList) value).pack();
        } else {
            packValue = (byte[]) value;
        }

        byte[] bLen = getL(packValue.length);
        int tLength = bTag.length + bLen.length + packValue.length;
        byte[] out = new byte[tLength];
        System.arraycopy(bTag, 0, out, 0, bTag.length);
        System.arraycopy(bLen, 0, out, bTag.length, bLen.length);
        System.arraycopy(packValue, 0, out, bTag.length + bLen.length,
                packValue.length);
        return out;

    }
}
