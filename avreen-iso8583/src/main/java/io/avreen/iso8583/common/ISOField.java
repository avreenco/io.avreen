package io.avreen.iso8583.common;

/**
 * The class Iso field.
 *
 * @param <T> the type parameter
 */
public abstract class ISOField<T>
        implements ISOComponent<T>, ISOComponentDumperAware {

    private ISOComponentDumper isoComponentDumper;

    /**
     * Instantiates a new Iso field.
     */
    public ISOField() {
    }

    @Override
    public ISOComponentDumper getDumper() {
        return isoComponentDumper;
    }

    @Override
    public void setISOComponentDumper(ISOComponentDumper isoComponentDumper) {
        this.isoComponentDumper = isoComponentDumper;
    }


}
