package io.avreen.iso8583.common;


import io.avreen.common.IMessageKeyProvider;
import io.avreen.common.util.CodecUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * The class Iso msg key provider.
 */
public class ISOMsgKeyProvider implements IMessageKeyProvider<ISOMsg> {


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println(new ISOMsgKeyProvider().mapMTI("0200"));
    }

    /**
     * The Nomap.
     */
    static final String nomap = "0123456789";
    /**
     * The Mti mapping.
     */
    protected String[] mtiMapping = new String[]{nomap, nomap, "0022446789"};
    /**
     * The Key.
     */
    protected Integer[] key = new Integer[]{41, 11};
    private Map<String, Integer[]> mtiKey = new HashMap<>();

    /**
     * Instantiates a new Iso msg key provider.
     *
     * @param key the key
     */
    public ISOMsgKeyProvider(Integer[] key) {
        this.key = key;
    }


    /**
     * Sets mti override.
     *
     * @param mtiOverride the mti override
     */
    public void setMtiOverride(Map<String, Integer[]> mtiOverride) {
        if (mtiOverride == null)
            return;
        mtiOverride.forEach((mti, keys) -> mtiKey.put(mapMTI(mti.trim()), keys));
    }

    /**
     * Instantiates a new Iso msg key provider.
     */
    public ISOMsgKeyProvider() {
    }

    /**
     * Get key string [ ].
     *
     * @return the string [ ]
     */
    public Integer[] getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(Integer[] key) {
        this.key = key;
    }

    public String getKey(ISOMsg m, String ownerName, boolean outgoing) {
        StringBuilder sb = new StringBuilder(ownerName);
        sb.append('.');
        String mapMTI = mapMTI(m.getMTI());
        sb.append(mapMTI);
//        if (headerIsKey && m.getISOHeader().getDynamicHeader() != null) {
//            sb.append('.');
//            sb.append(ISOUtil.hexString(m.getISOHeader().getDynamicHeader()));
//            sb.append('.');
//        }
        boolean hasFields = false;
        Integer[] k = mtiKey.getOrDefault(mapMTI, key);
        for (Integer f : k) {
            String v = m.getString(f);
            if (v != null) {
                if ("11".equals(f)) {
                    String vt = v.trim();
                    int l = m.getMTI().charAt(0) == '2' ? 12 : 6;
                    if (vt.length() < l)
                        v = CodecUtil.zeroPad(vt, l);
                }
                if ("41".equals(f)) {
                    v = CodecUtil.zeroPad(v.trim(), 16); // BIC ANSI to ISO hack
                }
                hasFields = true;
                sb.append(v);
            }
        }
        if (!hasFields)
            throw new RuntimeException("Key fields not found - not sending " + sb.toString());
        return sb.toString();
    }


    private String mapMTI(String mti) {
        StringBuilder sb = new StringBuilder();
        if (mti != null) {
            if (mti.length() < 4)
                mti = CodecUtil.zeroPad(mti, 4);
            if (mti.length() == 4) {
                for (int i = 0; i < mtiMapping.length; i++) {
                    int c = mti.charAt(i) - '0';
                    if (c >= 0 && c < 10)
                        sb.append(mtiMapping[i].charAt(c));
                }
            }
        }
        return sb.toString();
    }

}
