package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.common.ISOBinaryField;
import io.avreen.iso8583.packager.impl.base.ISOComponentPackagerBase;
import io.avreen.iso8583.packager.impl.base.Interpreter;
import io.avreen.iso8583.packager.impl.base.Padder;
import io.avreen.iso8583.packager.impl.base.Prefixer;

/**
 * The class Iso binary field packager.
 */
public class ISOBinaryFieldPackager extends ISOComponentPackagerBase<byte[], ISOBinaryField> {
    /**
     * Instantiates a new Iso binary field packager.
     *
     * @param maxLength   the max length
     * @param description the description
     * @param padder      the padder
     * @param interpreter the interpreter
     * @param prefixer    the prefixer
     */
    public ISOBinaryFieldPackager(int maxLength, String description, Padder<byte[]> padder,
                                  Interpreter<byte[]> interpreter, Prefixer prefixer) {
        super(maxLength, description);
        setPadder(padder);
        setInterpreter(interpreter);
        setPrefixer(prefixer);
    }

    /**
     * Instantiates a new Iso binary field packager.
     *
     * @param maxLength   the max length
     * @param description the description
     * @param interpreter the interpreter
     * @param prefixer    the prefixer
     */
    public ISOBinaryFieldPackager(int maxLength, String description,
                                  Interpreter<byte[]> interpreter, Prefixer prefixer) {
        super(maxLength, description);
        setInterpreter(interpreter);
        setPrefixer(prefixer);
    }

    public ISOBinaryField createComponent() {
        return new ISOBinaryField();
    }

    @Override
    protected int getValueLength(byte[] value) {
        return value.length;
    }
}
