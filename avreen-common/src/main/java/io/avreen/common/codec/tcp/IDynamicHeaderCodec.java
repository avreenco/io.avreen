package io.avreen.common.codec.tcp;

public interface IDynamicHeaderCodec
{

    IMessageLenCodec getHeaderLenCodec();

}
