package io.avreen.iso8583.mapper.impl.base;

import io.avreen.common.log.LoggerDomain;
import io.avreen.iso8583.common.*;
import io.avreen.iso8583.mapper.api.ISOComponentMapper;
import io.avreen.iso8583.mapper.api.ISOMsgMapper;
import io.avreen.iso8583.mapper.impl.ISOBitMapMapper;
import io.avreen.iso8583.util.DumpUtil;
import io.avreen.iso8583.util.ISOFieldException;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The class Iso msg base mapper.
 */
public abstract class ISOMsgBaseMapper implements ISOMsgMapper {
    private static final int thirdBitmapFieldDefault = -999;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".iso8583.mapper.impl.base.ISOMsgBaseMapper");
    /**
     * The Fld.
     */
    protected ISOComponentMapper[] fld;
    /**
     * The Iso component dumper map.
     */
    protected Map<Integer, ISOComponentDumper> isoComponentDumperMap = new HashMap<>();
    /**
     * The Third bitmap field.
     */
    protected int thirdBitmapField = thirdBitmapFieldDefault;       // for implementations where the tertiary bitmap is inside a Data Element

    /**
     * Emit bit map boolean.
     *
     * @param fld the fld
     * @return the boolean
     */
    static boolean emitBitMap(ISOComponentMapper[] fld) {
        return getBitMapfieldIndex(fld) >= 0;
    }

    /**
     * Gets first field.
     *
     * @param fld the fld
     * @return the first field
     */
    static int getFirstField(ISOComponentMapper[] fld) {
        int bitmapIndex = getBitMapfieldIndex(fld);
        if (bitmapIndex < 0)
            return 1; /* MTI or field 0 unpack first */
        return bitmapIndex + 1;
    }

    /**
     * Pack int.
     *
     * @param fld        the fld
     * @param m          the m
     * @param byteBuffer the byte buffer
     * @return the int
     */
    public static void write(ISOComponentMapper[] fld, ISOMsg m, ByteBuffer byteBuffer) {
        write(fld, thirdBitmapFieldDefault, m, byteBuffer);
    }

    /**
     * Pack int.
     *
     * @param fld              the fld
     * @param thirdBitmapField the third bitmap field
     * @param m                the m
     * @param byteBuffer       the byte buffer
     * @return the int
     */
    public static void write(ISOComponentMapper[] fld, int thirdBitmapField, ISOMsg m, ByteBuffer byteBuffer) {
        try {
            byte[] b;
            byte[] hdr = null;

            Set<Integer> fields = m.fieldSet();
            ISOComponent c = m.getIsoComponent(0);
            int first = getFirstField(fld);


            if (first > 0 && c != null && !(fld[0] instanceof ISOBitMapMapper)) {
                fld[0].write(c, byteBuffer);
            }

            BitSet bmap12 = null;                            // will store primary and secondary part of bitmap
            BitSet bmap3 = null;                             // will store tertiary part of bitmap
            if (emitBitMap(fld)) {   // The ISOComponent stores a single bitmap in field -1, which could be up to
                // 192 bits long. If we have a thirdBitmapField, we may need to split the full
                // bitmap into 1 & 2 at the beginning (16 bytes), and 3rd inside the Data Element
                c = (ISOComponent) m.getIsoComponent(getBitMapfieldIndex(fld));
                bmap12 = (BitSet) c.getValue();               // the full bitmap (up to 192 bits long)

                if (thirdBitmapField >= 0 &&                // we may need to split it!
                        fld[thirdBitmapField] instanceof ISOBitMapMapper) {
                    if (bmap12.length() - 1 > 128)          // some bits are set in the high part (3rd bitmap)
                    {
                        bmap3 = bmap12.get(128, 193);        // new bitmap, with the high 3rd bitmap (use 128 as dummy bit0)
                        bmap3.clear(0);                     // don't really need to clear dummy bit0 I guess...
                        bmap12.set(thirdBitmapField);       // indicate presence of field that will hold the 3rd bitmap
                        bmap12.clear(129, 193);             // clear high part, so that the field's write() method will not use it

                        // Now create add-hoc ISOBitMap in position thirdBitmapField to hold 3rd bitmap
                        ISOBitMap bmField = new ISOBitMap();
                        bmField.setValue(bmap3);
                        m.set(thirdBitmapField, bmField);
                        //fields.put(thirdBitmapField, bmField);    // fields is a clone of m's inner map, so we store it here as well

                        // bit65 should only be set if there's a data-containing DE-65 (which should't happen!)
                        bmap12.set(65, m.getIsoComponent(65) == null ? false : true);
                    } else {   // else: No bits/fields above 128 in this message.
                        // In case there's an old (residual/garbage) field `thirdBitmapField` in the message
                        // we need to clear the bit and the data
                        m.remove(thirdBitmapField);                // remove from ISOMsg
                        bmap12.clear(thirdBitmapField);           // remove from inner bitmap
                        fields.remove(thirdBitmapField);          // remove from fields clone
                    }
                }
                // now will emit the 1st and 2nd bitmaps, and the loop below will remove care of 3rd
                // when emitting field `thirdBitmapField`
                getBitMapfieldMapper(fld).write(c, byteBuffer);
            }

            // if Field 1 is a BitMap then we are packing an
            // ISO-8583 message so next field is fld#2.
            // else we are packing an ANSI X9.2 message, first field is 1
            int tmpMaxField = Math.min(m.calcMaxField(), (bmap3 != null || fld.length > 129) ? 192 : 128);

            for (int i = first; i <= tmpMaxField; i++) {
                if ((c = m.getIsoComponent(i)) != null) {
                    try {
                        ISOComponentMapper fp = fld[i];
                        if (fp == null)
                            throw new RuntimeException("null field " + i + "mapper");
                        fp.write(c, byteBuffer);


                    } catch (Exception e) {
                        if (logger.isErrorEnabled())
                            logger.error("error packing field ={} and component={} ", i, c);
                        if (logger.isErrorEnabled())
                            logger.error(e.getMessage(), e);
                        throw new RuntimeException("error packing field " + i, e);
                    }
                }
            }

        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
        }
    }

    /**
     * Gets bit mapfield mapper.
     *
     * @param fld the fld
     * @return the bit mapfield mapper
     */
    static ISOComponentMapper getBitMapfieldMapper(ISOComponentMapper[] fld) {
        return fld[getBitMapfieldIndex(fld)];
    }

    /**
     * Gets bit mapfield index.
     *
     * @param fld the fld
     * @return the bit mapfield index
     */
    static int getBitMapfieldIndex(ISOComponentMapper[] fld) {
        if (fld[0] instanceof ISOBitMapMapper)
            return 0;
        if (fld[1] instanceof ISOBitMapMapper)
            return 1;
        return -1;
    }

    /**
     * Unpack int.
     *
     * @param fld                   the fld
     * @param isoComponentDumperMap the iso component dumper map
     * @param m                     the m
     * @param byteBuffer            the byte buffer
     * @return the int
     */
    public static void read(ISOComponentMapper[] fld, Map<Integer, ISOComponentDumper> isoComponentDumperMap, ISOMsg m, ByteBuffer byteBuffer) throws ISOFieldException {
        read(fld, thirdBitmapFieldDefault, isoComponentDumperMap, m, byteBuffer);

    }

    /**
     * Unpack int.
     *
     * @param fld        the fld
     * @param m          the m
     * @param byteBuffer the byte buffer
     * @return the int
     */
    public static void read(ISOComponentMapper[] fld, ISOMsg m, ByteBuffer byteBuffer) throws ISOFieldException {
        read(fld, thirdBitmapFieldDefault, null, m, byteBuffer);
    }

    /**
     * Unpack int.
     *
     * @param fld                   the fld
     * @param thirdBitmapField      the third bitmap field
     * @param isoComponentDumperMap the iso component dumper map
     * @param m                     the m
     * @param byteBuffer            the byte buffer
     * @return the int
     */
    public static void read(ISOComponentMapper[] fld, int thirdBitmapField, Map<Integer, ISOComponentDumper> isoComponentDumperMap, ISOMsg m, ByteBuffer byteBuffer) throws ISOFieldException {
        if (logger.isDebugEnabled())
            logger.debug("unpack");
        try {
            if (!(fld[0] == null) && !(fld[0] instanceof ISOBitMapMapper)) {
                ISOComponent<String> mti = fld[0].read(byteBuffer);
                m.set(0, mti);
            }
            BitSet bmap = null;
            int bmapBytes = 0;                                   // bitmap length in bytes (usually 8, 16, 24)
            int maxField = fld.length - 1;                       // array length counts position 0!

            if (emitBitMap(fld)) {
                ISOBitMap bitmap = (ISOBitMap) getBitMapfieldMapper(fld).read(byteBuffer);
                bmap = bitmap.getValue();
                bmapBytes = (bmap.length() - 1 + 63) >> 6 << 3;
                if (logger.isDebugEnabled())
                    logger.debug("bitmap={}", bmap.toString());
                m.set(getBitMapfieldIndex(fld), bitmap);

                maxField = Math.min(maxField, bmap.length() - 1); // bmap.length behaves similarly to fld.length
            }

            for (int i = getFirstField(fld); i <= maxField; i++) {
                try {
                    if (bmap == null && fld[i] == null)
                        continue;

                    // maxField is computed above as min(fld.length-1, bmap.length()-1), therefore
                    // "maxField > 128" means fld[] has mappers defined above 128, *and*
                    // the bitmap's length is greater than 128 (i.e., a contiguous tertiary bitmap exists).
                    // In this case, bit 65 simply indicates a 3rd bitmap contiguous to the 2nd one.
                    // Therefore, there MUST NOT be a DE-65 with data payload to read.
                    if (maxField > 128 && i == 65)
                        continue;   // ignore extended bitmap

                    if (bmap == null || bmap.get(i)) {
                        if (fld[i] == null)
                            throw new RuntimeException("field mapper '" + i + "' is null");

                        ISOComponent c = fld[i].read(byteBuffer);
                        if (c instanceof ISOComponentDumperAware) {
                            if (isoComponentDumperMap != null) {
                                if (isoComponentDumperMap.containsKey(i))
                                    ((ISOComponentDumperAware) c).setISOComponentDumper(isoComponentDumperMap.get(i));
                            }
                        }
                        if (logger.isDebugEnabled()) {
                            ISOComponentDumper isoComponentDumper = DumpUtil.getIsoComponentDumper(c);
                            ByteArrayOutputStream result = new ByteArrayOutputStream();
                            PrintStream printWriter = new PrintStream(result);
                            isoComponentDumper.dump(i, c, printWriter, "");
                            logger.debug(result.toString());
                        }
                        m.set(i, c);

                        if (i == thirdBitmapField && fld.length > 129 &&          // fld[128] is at pos 129
                                bmapBytes == 16 &&
                                fld[thirdBitmapField] instanceof ISOBitMapMapper) {   // We have a weird case of tertiary bitmap implemented inside a Data Element
                            BitSet bs3rd = (BitSet) (m.getIsoComponent(thirdBitmapField)).getValue();
                            maxField = 128 + (bs3rd.length() - 1);                 // update loop end condition
                            for (int bit = 1; bit <= 64; bit++)
                                bmap.set(bit + 128, bs3rd.get(bit));                // extend bmap with new bits above 128
                        }
                    }
                } catch (Exception e) {
                    if (logger.isErrorEnabled())
                        logger.error("exception in unpack field id=" + i + " bitmap=" + bmap.toString(), e);
                    throw new ISOFieldException(0, IIsoRejectCodes.InvalidIsoMsgField, e);
                }
            } // for each field

            if (byteBuffer.hasRemaining()) {
                if (logger.isWarnEnabled())
                    logger.warn("WARNING: unpack len=" + byteBuffer.position() + " consumed=" + byteBuffer.limit());
            }

        } finally {
        }
    }

    /**
     * Sets field mapper.
     *
     * @param fld the fld
     */
    public void setFieldsMapper(ISOComponentMapper[] fld) {
        this.fld = fld;
    }

    private int getThirdBitmapField() {
        return thirdBitmapField;
    }

    private void setThirdBitmapField(int f) {
        if (f < 0 || f > 128)
            throw new RuntimeException("thirdBitmapField should be >= 0 and <= 128");
        thirdBitmapField = f;
    }

    /**
     * Sets iso component dumper.
     *
     * @param isoComponentDumper the iso component dumper
     * @param fieldNumbers       the field numbers
     */
    public void setISOComponentDumper(ISOComponentDumper isoComponentDumper, int... fieldNumbers) {
        for (int fieldNumber : fieldNumbers)
            isoComponentDumperMap.put(fieldNumber, isoComponentDumper);
    }

    /**
     * Sets iso component dumper.
     *
     * @param dumperMap the dumper map
     */
    public void setISOComponentDumper(Map<Integer, ISOComponentDumper> dumperMap) {
        Set<Integer> fieldNumbers = dumperMap.keySet();
        for (int fieldNumber : fieldNumbers) {
            isoComponentDumperMap.put(fieldNumber, dumperMap.get(fieldNumber));
        }
    }


    @Override
    public void write(ISOMsg m, ByteBuffer byteBuffer) throws ISOFieldException {
        write(fld, thirdBitmapField, m, byteBuffer);
    }

    @Override
    public ISOMsg read(ByteBuffer byteBuffer) throws ISOFieldException {
        ISOMsg m = new ISOMsg();
        read(fld, thirdBitmapField, isoComponentDumperMap, m, byteBuffer);
        return m;
    }

}
