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
    private Interpreter<VT> interpreter;
    private Padder<VT> padder;
    private Prefixer prefixer;

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
     * @param interpreter the interpreter
     */
    public void setInterpreter(Interpreter<VT> interpreter) {
        this.interpreter = interpreter;
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
     * Sets prefixer.
     *
     * @param prefixer the prefixer
     */
    public void setPrefixer(Prefixer prefixer) {
        this.prefixer = prefixer;
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
    public Interpreter<VT> getInterpreter() {
        return interpreter;
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
    public int pack(C isoComponent, ByteBuffer byteBuffer) {
        VT data = isoComponent.getValue();
        if (getValueLength(data) > getLength()) {
            throw new RuntimeException("Field length " + getValueLength(data) + " too long. Max: " + getLength());
        }
        VT paddedData = data;
        if (padder != null)
            paddedData = padder.pad(data, getLength());
        int packedLength = 0;
        if (prefixer != null)
            packedLength = prefixer.encodeLength(getValueLength(paddedData), byteBuffer);
        int il = interpreter.interpret(paddedData, byteBuffer);
        return il + packedLength;

    }


    @Override
    public int unpack(C isoComponent, ByteBuffer byteBuffer) {

        int len = -1;
        if (prefixer != null)
            len = prefixer.decodeLength(byteBuffer);
        if (len == -1) {
            len = getLength();
        } else if (getLength() > 0 && len > getLength())
            throw new RuntimeException("Field length " + len + " too long. Max: " + getLength());
        int lenLen = 0;
        if (prefixer != null)
            lenLen = prefixer.getPackedLength();
        isoComponent.setValue(interpreter.uninterpret(byteBuffer, len));
        return lenLen + interpreter.getPackedLength(len);

    }

    /**
     * Check length.
     *
     * @param len       the len
     * @param maxLength the max length
     * @throws IllegalArgumentException the illegal argument exception
     */
    protected void checkLength(int len, int maxLength) throws IllegalArgumentException {
        if (len > maxLength) {
            throw new IllegalArgumentException("Length " + len + " too long for " + getClass().getName());
        }
    }


}

