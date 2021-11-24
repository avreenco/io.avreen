package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.common.ISOStringField;
import io.avreen.iso8583.packager.impl.base.ISOComponentPackagerBase;
import io.avreen.iso8583.packager.impl.base.Interpreter;
import io.avreen.iso8583.packager.impl.base.Padder;
import io.avreen.iso8583.packager.impl.base.Prefixer;

/**
 * The class Iso string field packager.
 */
public class ISOStringFieldPackager extends ISOComponentPackagerBase<String, ISOStringField> {
    /**
     * Instantiates a new Iso string field packager.
     *
     * @param maxLength   the max length
     * @param description the description
     * @param padder      the padder
     * @param interpreter the interpreter
     * @param prefixer    the prefixer
     */
    public ISOStringFieldPackager(int maxLength, String description, Padder<String> padder,
                                  Interpreter<String> interpreter, Prefixer prefixer) {
        super(maxLength, description);
        setPadder(padder);
        setInterpreter(interpreter);
        setPrefixer(prefixer);
    }

    public ISOStringField createComponent() {
        return new ISOStringField();
    }

    @Override
    protected int getValueLength(String value) {
        return value.length();
    }

}
