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
     * Emit bit map boolean.
     *
     * @param fld the fld
     * @return the boolean
     */
    static boolean hasBitMapField(ISOComponentMapper[] fld) {
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
            return 1;
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
        try {
            ISOComponent c = m.getIsoComponent(0);
            int first = getFirstField(fld);
            if (first > 0 && c != null && !(fld[0] instanceof ISOBitMapMapper)) {
                fld[0].write(c, byteBuffer);
            }
            BitSet bmap3 = null;
            if (hasBitMapField(fld)) {
                c = m.getIsoComponent(getBitMapfieldIndex(fld));
                getBitMapfieldMapper(fld).write(c, byteBuffer);
            }
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
        readInternal(fld, isoComponentDumperMap, m, byteBuffer);

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
        readInternal(fld, null, m, byteBuffer);
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
    private static void readInternal(ISOComponentMapper[] fld, Map<Integer, ISOComponentDumper> isoComponentDumperMap, ISOMsg m, ByteBuffer byteBuffer) throws ISOFieldException {
        if (logger.isDebugEnabled())
            logger.debug("unpack");
        if (!(fld[0] == null) && !(fld[0] instanceof ISOBitMapMapper)) {
            ISOComponent<String> mti = fld[0].read(byteBuffer);
            m.set(0, mti);
        }
        BitSet bmap = null;
        int maxField = fld.length - 1;
        if (hasBitMapField(fld)) {
            ISOBitMap bitmap = (ISOBitMap) getBitMapfieldMapper(fld).read(byteBuffer);
            bmap = bitmap.getValue();
            if (logger.isDebugEnabled())
                logger.debug("bitmap={}", bmap.toString());
            m.set(getBitMapfieldIndex(fld), bitmap);
            maxField = Math.min(maxField, bmap.length() - 1);
        }
        for (int i = getFirstField(fld); i <= maxField; i++) {
            try {
                if (bmap == null && fld[i] == null)
                    continue;
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
                }
            } catch (Exception e) {
                if (logger.isErrorEnabled())
                    logger.error("exception in unpack field id=" + i + " bitmap=" + bmap.toString(), e);
                throw new ISOFieldException(0, IIsoRejectCodes.InvalidIsoMsgField, e);
            }
        }
        if (byteBuffer.hasRemaining()) {
            if (logger.isWarnEnabled())
                logger.warn("WARNING: unpack len=" + byteBuffer.position() + " consumed=" + byteBuffer.limit());
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
        ISOMsgBaseMapper.write(fld, m, byteBuffer);
    }

    @Override
    public ISOMsg read(ByteBuffer byteBuffer) throws ISOFieldException {
        ISOMsg m = new ISOMsg();
        readInternal(fld, isoComponentDumperMap, m, byteBuffer);
        return m;
    }

}
