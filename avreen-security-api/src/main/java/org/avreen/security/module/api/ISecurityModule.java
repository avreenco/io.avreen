package org.avreen.security.module.api;

import org.avreen.security.module.api.request.*;
import org.avreen.security.module.api.response.*;

/**
 * The interface Security module.
 */
public interface ISecurityModule {

    /**
     * Advance random key gen response advance random key gen.
     *
     * @param request the request
     * @return the response advance random key gen
     */
    Response_ADVANCE_RANDOM_KEY_GEN ADVANCE_RANDOM_KEY_GEN(Request_ADVANCE_RANDOM_KEY_GEN request);

    /**
     * Pin tran 2 response pin tran 2.
     *
     * @param request the request
     * @return the response pin tran 2
     */
    Response_PIN_TRAN_2 PIN_TRAN_2(Request_PIN_TRAN_2 request);

    /**
     * Encipher 2 response encipher 2.
     *
     * @param request the request
     * @return the response encipher 2
     */
    Response_ENCIPHER_2 ENCIPHER_2(Request_ENCIPHER_2 request);

    /**
     * Decipher 2 response decipher 2.
     *
     * @param request the request
     * @return the response decipher 2
     */
    Response_DECIPHER_2 DECIPHER_2(Request_DECIPHER_2 request);

    /**
     * Decipher 4 response decipher 4.
     *
     * @param request the request
     * @return the response decipher 4
     */
    Response_DECIPHER_4 DECIPHER_4(Request_DECIPHER_4 request);

    /**
     * Mac gen final response mac gen final.
     *
     * @param request the request
     * @return the response mac gen final
     */
    Response_MAC_GEN_FINAL MAC_GEN_FINAL(Request_MAC_GEN_FINAL request);

    /**
     * Mac ver final response mac ver final.
     *
     * @param request the request
     * @return the response mac ver final
     */
    Response_MAC_VER_FINAL MAC_VER_FINAL(Request_MAC_VER_FINAL request);

    /**
     * Pin ver ibm multi response pin ver ibm multi.
     *
     * @param request the request
     * @return the response pin ver ibm multi
     */
    Response_PIN_VER_IBM_MULTI PIN_VER_IBM_MULTI(Request_PIN_VER_IBM_MULTI request);

    /**
     * Ii key gen response ii key gen.
     *
     * @param request the request
     * @return the response ii key gen
     */
    Response_II_KEY_GEN II_KEY_GEN(Request_II_KEY_GEN request);

    /**
     * Ii key rcv response ii key rcv.
     *
     * @param request the request
     * @return the response ii key rcv
     */
    Response_II_KEY_RCV II_KEY_RCV(Request_II_KEY_RCV request);

    /**
     * It key gen response it key gen.
     *
     * @param request the request
     * @return the response it key gen
     */
    Response_IT_KEY_GEN IT_KEY_GEN(Request_IT_KEY_GEN request);

    /**
     * Dukpt ik derive 2 response dukpt ik derive 2.
     *
     * @param request the request
     * @return the response dukpt ik derive 2
     */
    Response_DUKPT_IK_Derive_2 DUKPT_IK_Derive_2(Request_DUKPT_IK_Derive_2 request);

    /**
     * Pin off response pin off.
     *
     * @param request the request
     * @return the response pin off
     */
    Response_PIN_OFF PIN_OFF(Request_PIN_OFF request);

    /**
     * Emv script crypto emv 2000 response emv script crypto emv 2000.
     *
     * @param request the request
     * @return the response emv script crypto emv 2000
     */
    Response_EMV_SCRIPT_CRYPTO_EMV2000 EMV_SCRIPT_CRYPTO_EMV2000(Request_EMV_SCRIPT_CRYPTO_EMV2000 request);

    /**
     * Emv verify ac emv 2000 response emv verify ac emv 2000.
     *
     * @param request the request
     * @return the response emv verify ac emv 2000
     */
    Response_EMV_VERIFY_AC_EMV2000 EMV_VERIFY_AC_EMV2000(Request_EMV_VERIFY_AC_EMV2000 request);

    /**
     * Emv verify ac visa response emv verify ac visa.
     *
     * @param request the request
     * @return the response emv verify ac visa
     */
    Response_EMV_VERIFY_AC_VISA EMV_VERIFY_AC_VISA(Request_EMV_VERIFY_AC_VISA request);

    /**
     * Emv generate arpc response emv generate arpc.
     *
     * @param request the request
     * @return the response emv generate arpc
     */
    Response_EMV_GENERATE_ARPC EMV_GENERATE_ARPC(Request_EMV_GENERATE_ARPC request);

    /**
     * Cvv generate response cvv generate.
     *
     * @param request the request
     * @return the response cvv generate
     */
    Response_CVV_GENERATE CVV_GENERATE(Request_CVV_GENERATE request);

    /**
     * Cvv verify response cvv verify.
     *
     * @param request the request
     * @return the response cvv verify
     */
    Response_CVV_VERIFY CVV_VERIFY(Request_CVV_VERIFY request);

    /**
     * Generate hash response generate hash.
     *
     * @param request the request
     * @return the response generate hash
     */
    Response_GENERATE_HASH GENERATE_HASH(Request_GENERATE_HASH request);

    Response_DERIVE_ICC_MASTER_KEY DERIVE_ICC_MASTER_KEY(Request_DERIVE_ICC_MASTER_KEY request);


}