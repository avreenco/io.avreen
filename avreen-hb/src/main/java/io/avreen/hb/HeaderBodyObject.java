package io.avreen.hb;

import io.avreen.common.context.IMsgContextAware;
import io.avreen.common.context.IRejectSupportObject;
import io.avreen.common.context.MessageTypes;
import io.avreen.common.context.MsgContext;
import io.avreen.common.netty.IMessageTypeSupplier;
import io.avreen.common.util.CodecUtil;

/**
 * The class Header body object.
 */
public class HeaderBodyObject implements IRejectSupportObject ,
                                         IMsgContextAware<HeaderBodyObject>,
                                         IMessageTypeSupplier
{
    private byte[] header;
    private byte[] body;
    private Integer rejectCode;
    private transient MsgContext<HeaderBodyObject> msgContext;

    /**
     * Get header byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getHeader() {
        return header;
    }

    /**
     * Sets header.
     *
     * @param header the header
     */
    public void setHeader(byte[] header) {
        this.header = header;
    }

    /**
     * Get body byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{ header=" + CodecUtil.hexString(getHeader()) + ((rejectCode != null) ? " reject Code=" + rejectCode : "") + " body=" + CodecUtil.hexString(getBody()) + "}";
    }

    public boolean isReject() {
        return rejectCode != null;
    }

    public Integer getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(int rejectCode) {
        this.rejectCode = rejectCode;
    }

    @Override
    public void setMsgContext(MsgContext<HeaderBodyObject> msgContext)
    {
        this.msgContext = msgContext;
    }

    @Override
    public MsgContext<HeaderBodyObject> getMsgContext()
    {
        return this.msgContext;
    }

    @Override
    public MessageTypes getMessageTypes()
    {
        if(this.isReject())
            return MessageTypes.Reject;
        return MessageTypes.Request;
    }

    @Override
    public void setResponse()
    {

    }

}
