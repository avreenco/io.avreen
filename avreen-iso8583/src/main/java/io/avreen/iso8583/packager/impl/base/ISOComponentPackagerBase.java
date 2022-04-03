package io.avreen.iso8583.packager.impl.base;

import io.avreen.iso8583.common.ISOComponent;
import io.avreen.iso8583.packager.api.ISOComponentPackager;

import java.nio.ByteBuffer;

/**
 * The class Iso component packager base.
 *
 * @param <VT> the type parameter
 * @param <C>  the type parameter
 */
public abstract class ISOComponentPackagerBase<VT, C extends ISOComponent<VT>> implements ISOComponentPackager<C> {
    private int len;
    private String description;
    private IValueCodec<VT> valueCodec;
    private Padder<VT> padder;
    private IValueLengthCodec valueLengthCodec;

    /**
     * Instantiates a new Iso component packager base.
     *
     * @param len         the len
     * @param description the description
     */
    public ISOComponentPackagerBase(int len, String description) {
        this.len = len;
        this.description = description;
    }

    /**
     * Sets interpreter.
     *
     * @param valueCodec the interpreter
     */
    public void setValueCodec(IValueCodec<VT> valueCodec) {
        this.valueCodec = valueCodec;
    }

    /**
     * Sets padder.
     *
     * @param padder the padder
     */
    public void setPadder(Padder<VT> padder) {
        this.padder = padder;
    }

    /**
     * Sets ValueLengthCodec.
     *
     * @param valueLengthCodec the ValueLengthCodec
     */
    public void setValueLengthCodec(IValueLengthCodec valueLengthCodec) {
        this.valueLengthCodec = valueLengthCodec;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets interpreter.
     *
     * @return the interpreter
     */
    public IValueCodec<VT> getValueCodec() {
        return valueCodec;
    }

    /**
     * Gets length.
     *
     * @return the length
     */
    public int getLength() {
        return len;
    }

    /**
     * Sets length.
     *
     * @param len the len
     */
    public void setLength(int len) {
        this.len = len;
    }


    /**
     * Gets value length.
     *
     * @param value the value
     * @return the value length
     */
    protected abstract int getValueLength(VT value);


    @Override
    public void pack(C isoComponent, ByteBuffer byteBuffer) {
        VT data = isoComponent.getValue();
        if (getValueLength(data) > getLength()) {
            throw new RuntimeException("Field length " + getValueLength(data) + " too long. Max: " + getLength());
        }
        VT paddedData = data;
        if (padder != null)
            paddedData = padder.pad(data, getLength());
        if (valueLengthCodec != null)
             valueLengthCodec.encodeLength(getValueLength(paddedData), byteBuffer);
        valueCodec.encodeValue(paddedData, byteBuffer);
    }


    @Override
    public void unpack(C isoComponent, ByteBuffer byteBuffer) {

        int len = -1;
        if (valueLengthCodec != null)
            len = valueLengthCodec.decodeLength(byteBuffer);
        if (len == -1) {
            len = getLength();
        } else if (getLength() > 0 && len > getLength())
            throw new RuntimeException("Field length " + len + " too long. Max: " + getLength());
        isoComponent.setValue(valueCodec.decodeValue(byteBuffer, len));
    }

}

