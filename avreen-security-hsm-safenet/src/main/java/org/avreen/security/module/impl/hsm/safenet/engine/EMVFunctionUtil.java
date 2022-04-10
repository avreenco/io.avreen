package org.avreen.security.module.impl.hsm.safenet.engine;


/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Emv function util.
 */
public class EMVFunctionUtil {
    /**
     * Prepare command data byte [ ].
     *
     * @param commandData the command data
     * @return the byte [ ]
     */
    public static byte[] prepareCommandData(byte[] commandData) {
        int plainCommandDataLength;
        byte plainCommandDataBytes[] = new byte[256];
        for (int idx = 0; idx < plainCommandDataBytes.length; idx++)
            plainCommandDataBytes[idx] = 0;
        System.arraycopy(commandData, 0, plainCommandDataBytes, 0, commandData.length);
        if ((commandData.length % 8) != 0) {
            plainCommandDataBytes[commandData.length] = (byte) 0x80;
            plainCommandDataLength = (8 - (commandData.length % 8)) + commandData.length;
        } else {
            plainCommandDataLength = commandData.length;
        }
        byte[] temp = new byte[plainCommandDataLength];
        System.arraycopy(plainCommandDataBytes, 0, temp, 0, plainCommandDataLength);
        return temp;
    }

    /**
     * Prepare script data byte [ ].
     *
     * @param commandDataBytes the command data bytes
     * @param headerDataBytes  the header data bytes
     * @return the byte [ ]
     */
    public static byte[] prepareScriptData(byte[] commandDataBytes, byte[] headerDataBytes) {
        int plainCommandDataLength, scriptDataLength;
        byte plainCommandDataBytes[] = new byte[256];
        for (int idx = 0; idx < plainCommandDataBytes.length; idx++)
            plainCommandDataBytes[idx] = 0;

        byte scriptDataBytes[] = new byte[256];
        for (int idx = 0; idx < scriptDataBytes.length; idx++)
            scriptDataBytes[idx] = 0;
        System.arraycopy(commandDataBytes, 0, plainCommandDataBytes, 0, commandDataBytes.length);
        if ((commandDataBytes.length % 8) != 0) {
            plainCommandDataBytes[commandDataBytes.length] = (byte) 0x80;
            plainCommandDataLength = (8 - (commandDataBytes.length % 8)) + commandDataBytes.length;
        } else {
            plainCommandDataLength = commandDataBytes.length;
        }

        System.arraycopy(headerDataBytes, 0, scriptDataBytes, 0, 4);
        System.arraycopy(plainCommandDataBytes, 0, scriptDataBytes, 4, plainCommandDataLength);
        scriptDataBytes[4 + plainCommandDataLength] = (byte) 0x80;
        scriptDataLength = 8 + plainCommandDataLength;

        byte[] temp = new byte[scriptDataLength];
        System.arraycopy(scriptDataBytes, 0, temp, 0, scriptDataLength);
        return temp;
    }

    /**
     * Prepare script result byte [ ].
     *
     * @param encryptedcommandDataBytes the encryptedcommand data bytes
     * @param MACBytes                  the mac bytes
     * @param headerDataBytes           the header data bytes
     * @param MAClen                    the ma clen
     * @return the byte [ ]
     */
    public static byte[] prepareScriptResult(byte[] encryptedcommandDataBytes, byte[] MACBytes,
                                             byte[] headerDataBytes, int MAClen) {
        int scriptResultLength = encryptedcommandDataBytes.length + MAClen + 5;
        byte[] script = new byte[scriptResultLength];
        System.arraycopy(headerDataBytes, 0, script, 0, 4);
        System.arraycopy(encryptedcommandDataBytes, 0, script, 5, encryptedcommandDataBytes.length);
        System.arraycopy(MACBytes, 0, script, 5 + encryptedcommandDataBytes.length, MAClen);
        script[4] = (byte) (encryptedcommandDataBytes.length + MAClen);
        return script;
    }


}

