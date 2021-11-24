package io.avreen.tlv;

import io.avreen.common.util.CodecUtil;

/**
 * The class Test tlv.
 */
public class testTlv {


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
//        HashMap<String,IValuePackager>  hashMap = new HashMap<>();
//        hashMap.putIfAbsent("6F.A5.5F2D", AsciiValuePackager.INSTANCE);
//
//
//
        byte[] allBytes = CodecUtil.hex2byte("E022C218252A468F51E28F24EC75B3ED51E81C1C0E7E7D60A5485BD3C303BBE32CC40102E022C2184233DB1BE34A771B93292B8E6E1458606CCB4D2AB3DE571AC3031DA7A5C40101E022C2188D8CCF97CF13F068DA8C9D741C093E6DB9A809E75BCF797BC303FE0815C40100");
        TLVList tlvList = new TLVList();
        tlvList.unpack(allBytes);
        tlvList.dump(System.out, "");

//        TLVList tlvList = new TLVList();
//        TLVList tlvList2 = new TLVList();
//        TLVMsg tlvMsg = new TLVMsg(0xC0, CodecUtil.hex2byte("0000"));
//
//
//        tlvList.append(new TLVMsg(0xE1, tlvList2));
//
//
//        tlvList2.append(tlvMsg);
//        TLVMsg tlvMsg1 = new TLVMsg(0xC1, CodecUtil.hex2byte("1111"));
//        tlvList2.append(tlvMsg1);
//
//        tlvList.rebuildTagsHierarchy();
//        System.out.println(tlvMsg1.getCompleteTagString());
//        tlvList.dump(System.out, "");
//        tlvList.unpackValues(hashMap);
//        tlvList.dump(System.out,"");
//        tlvList.packValues(hashMap);


        // tlvList.unpack(allBytes);
////        TLVUtil.unpackToTlvMessage(null, ByteBuffer.wrap(allBytes),null,tlvMessage);
//        byte[]  pp = tlvList.pack();
//        TLVList tlvList1 = new TLVList();
//        tlvList1.unpack(pp);
//        tlvList1.unpackValues(hashMap);
//        tlvList1.dump(System.out,"");
//
//
////
//        System.out.println(CodecUtil.hexString(pp));
//
//        System.out.println(CodecUtil.hexString(TLVMsg.getL(435)));
    }
}
