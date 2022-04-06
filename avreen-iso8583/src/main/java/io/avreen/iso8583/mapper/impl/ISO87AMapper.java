package io.avreen.iso8583.mapper.impl;

import io.avreen.common.util.CodecUtil;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.mapper.api.ISOComponentMapper;
import io.avreen.iso8583.mapper.impl.base.ISOMsgBaseMapper;

import java.nio.ByteBuffer;

/**
 * The class Iso 87 a mapper.
 */
public class ISO87AMapper extends ISOMsgBaseMapper {
    /**
     * The Fld.
     */
    protected ISOComponentMapper fieldsMapper[] = {
            /*000*/ IFA.NUM(4, "MESSAGE TYPE INDICATOR"),
            /*001*/ IFA.BITMAP(16, "BIT MAP"),
            /*002*/ IFA.VARNUM(19, "PAN - PRIMARY ACCOUNT NUMBER"),
            /*003*/ IFA.NUM(6, "PROCESSING CODE"),
            /*004*/ IFA.NUM(12, "AMOUNT, TRANSACTION"),
            /*005*/ IFA.NUM(12, "AMOUNT, SETTLEMENT"),
            /*006*/ IFA.NUM(12, "AMOUNT, CARDHOLDER BILLING"),
            /*007*/ IFA.NUM(10, "TRANSMISSION DATE AND TIME"),
            /*008*/ IFA.NUM(8, "AMOUNT, CARDHOLDER BILLING FEE"),
            /*009*/ IFA.NUM(8, "CONVERSION RATE, SETTLEMENT"),
            /*010*/ IFA.NUM(8, "CONVERSION RATE, CARDHOLDER BILLING"),
            /*011*/ IFA.NUM(6, "SYSTEM TRACE AUDIT NUMBER"),
            /*012*/ IFA.NUM(6, "TIME, LOCAL TRANSACTION"),
            /*013*/ IFA.NUM(4, "DATE, LOCAL TRANSACTION"),
            /*014*/ IFA.NUM(4, "DATE, EXPIRATION"),
            /*015*/ IFA.NUM(4, "DATE, SETTLEMENT"),
            /*016*/ IFA.NUM(4, "DATE, CONVERSION"),
            /*017*/ IFA.NUM(4, "DATE, CAPTURE"),
            /*018*/ IFA.NUM(4, "MERCHANTS TYPE"),
            /*019*/ IFA.NUM(3, "ACQUIRING INSTITUTION COUNTRY CODE"),
            /*020*/ IFA.NUM(3, "PAN EXTENDED COUNTRY CODE"),
            /*021*/ IFA.NUM(3, "FORWARDING INSTITUTION COUNTRY CODE"),
            /*022*/ IFA.NUM(3, "POINT OF SERVICE ENTRY MODE"),
            /*023*/ IFA.NUM(3, "CARD SEQUENCE NUMBER"),
            /*024*/ IFA.NUM(3, "NETWORK INTERNATIONAL IDENTIFIEER"),
            /*025*/ IFA.NUM(2, "POINT OF SERVICE CONDITION CODE"),
            /*026*/ IFA.NUM(2, "POINT OF SERVICE PIN CAPTURE CODE"),
            /*027*/ IFA.NUM(1, "AUTHORIZATION IDENTIFICATION RESP LEN"),
            /*028*/ IFA.NUM(9, "AMOUNT, TRANSACTION FEE"),
            /*029*/ IFA.NUM(9, "AMOUNT, SETTLEMENT FEE"),
            /*030*/ IFA.NUM(9, "AMOUNT, TRANSACTION PROCESSING FEE"),
            /*031*/ IFA.NUM(9, "AMOUNT, SETTLEMENT PROCESSING FEE"),
            /*032*/ IFA.VARNUM(11, "ACQUIRING INSTITUTION IDENT CODE"),
            /*033*/ IFA.VARNUM(11, "FORWARDING INSTITUTION IDENT CODE"),
            /*034*/ IFA.VARCHAR(28, "PAN EXTENDED"),
            /*035*/ IFA.VARCHAR(37, "TRACK 2 DATA"),
            /*036*/ IFA.VARCHAR(104, "TRACK 3 DATA"),
            /*037*/ IFA.CHAR(12, "RETRIEVAL REFERENCE NUMBER"),
            /*038*/ IFA.CHAR(6, "AUTHORIZATION IDENTIFICATION RESPONSE"),
            /*039*/ IFA.CHAR(2, "RESPONSE CODE"),
            /*040*/ IFA.CHAR(3, "SERVICE RESTRICTION CODE"),
            /*041*/ IFA.CHAR(8, "CARD ACCEPTOR TERMINAL IDENTIFICACION"),
            /*042*/ IFA.CHAR(15, "CARD ACCEPTOR IDENTIFICATION CODE"),
            /*043*/ IFA.CHAR(40, "CARD ACCEPTOR NAME/LOCATION"),
            /*044*/ IFA.VARCHAR(99, "ADITIONAL RESPONSE DATA"),
            /*045*/ IFA.VARCHAR(76, "TRACK 1 DATA"),
            /*046*/ IFA.VARCHAR(999, "ADITIONAL DATA - ISO"),
            /*047*/ IFA.VARCHAR(999, "ADITIONAL DATA - NATIONAL"),
            /*048*/ IFA.VARCHAR(999, "ADITIONAL DATA - PRIVATE"),
            /*049*/ IFA.CHAR(3, "CURRENCY CODE, TRANSACTION"),
            /*050*/ IFA.CHAR(3, "CURRENCY CODE, SETTLEMENT"),
            /*051*/ IFA.CHAR(3, "CURRENCY CODE, CARDHOLDER BILLING"),
            /*052*/ IFA.BIN(8, "PIN DATA"),
            /*053*/ IFA.NUM(16, "SECURITY RELATED CONTROL INFORMATION"),
            /*054*/ IFA.VARCHAR(120, "ADDITIONAL AMOUNTS"),
            /*055*/ IFA.VARCHAR(999, "RESERVED ISO"),
            /*056*/ IFA.VARCHAR(999, "RESERVED ISO"),
            /*057*/ IFA.VARCHAR(999, "RESERVED NATIONAL"),
            /*058*/ IFA.VARCHAR(999, "RESERVED NATIONAL"),
            /*059*/ IFA.VARCHAR(999, "RESERVED NATIONAL"),
            /*060*/ IFA.VARCHAR(999, "RESERVED PRIVATE"),
            /*061*/ IFA.VARCHAR(999, "RESERVED PRIVATE"),
            /*062*/ IFA.VARCHAR(999, "RESERVED PRIVATE"),
            /*063*/ IFA.VARCHAR(999, "RESERVED PRIVATE"),
            /*064*/ IFA.BIN(8, "MESSAGE AUTHENTICATION CODE FIELD"),
            /*065*/ IFA.BIN(1, "BITMAP, EXTENDED"),
            /*066*/ IFA.NUM(1, "SETTLEMENT CODE"),
            /*067*/ IFA.NUM(2, "EXTENDED PAYMENT CODE"),
            /*068*/ IFA.NUM(3, "RECEIVING INSTITUTION COUNTRY CODE"),
            /*069*/ IFA.NUM(3, "SETTLEMENT INSTITUTION COUNTRY CODE"),
            /*070*/ IFA.NUM(3, "NETWORK MANAGEMENT INFORMATION CODE"),
            /*071*/ IFA.NUM(4, "MESSAGE NUMBER"),
            /*072*/ IFA.NUM(4, "MESSAGE NUMBER LAST"),
            /*073*/ IFA.NUM(6, "DATE ACTION"),
            /*074*/ IFA.NUM(10, "CREDITS NUMBER"),
            /*075*/ IFA.NUM(10, "CREDITS REVERSAL NUMBER"),
            /*076*/ IFA.NUM(10, "DEBITS NUMBER"),
            /*077*/ IFA.NUM(10, "DEBITS REVERSAL NUMBER"),
            /*078*/ IFA.NUM(10, "TRANSFER NUMBER"),
            /*079*/ IFA.NUM(10, "TRANSFER REVERSAL NUMBER"),
            /*080*/ IFA.NUM(10, "INQUIRIES NUMBER"),
            /*081*/ IFA.NUM(10, "AUTHORIZATION NUMBER"),
            /*082*/ IFA.NUM(12, "CREDITS, PROCESSING FEE AMOUNT"),
            /*083*/ IFA.NUM(12, "CREDITS, TRANSACTION FEE AMOUNT"),
            /*084*/ IFA.NUM(12, "DEBITS, PROCESSING FEE AMOUNT"),
            /*085*/ IFA.NUM(12, "DEBITS, TRANSACTION FEE AMOUNT"),
            /*086*/ IFA.NUM(16, "CREDITS, AMOUNT"),
            /*087*/ IFA.NUM(16, "CREDITS, REVERSAL AMOUNT"),
            /*088*/ IFA.NUM(16, "DEBITS, AMOUNT"),
            /*089*/ IFA.NUM(16, "DEBITS, REVERSAL AMOUNT"),
            /*090*/ IFA.NUM(42, "ORIGINAL DATA ELEMENTS"),
            /*091*/ IFA.CHAR(1, "FILE UPDATE CODE"),
            /*092*/ IFA.CHAR(2, "FILE SECURITY CODE"),
            /*093*/ IFA.CHAR(6, "RESPONSE INDICATOR"),
            /*094*/ IFA.CHAR(7, "SERVICE INDICATOR"),
            /*095*/ IFA.CHAR(42, "REPLACEMENT AMOUNTS"),
            /*096*/ IFA.BIN(8, "MESSAGE SECURITY CODE"),
            /*097*/ IFA.NUM(17, "AMOUNT, NET SETTLEMENT"),
            /*098*/ IFA.CHAR(25, "PAYEE"),
            /*099*/ IFA.VARNUM(11, "SETTLEMENT INSTITUTION IDENT CODE"),
            /*100*/ IFA.VARNUM(11, "RECEIVING INSTITUTION IDENT CODE"),
            /*101*/ IFA.VARCHAR(17, "FILE NAME"),
            /*102*/ IFA.VARCHAR(28, "ACCOUNT IDENTIFICATION 1"),
            /*103*/ IFA.VARCHAR(28, "ACCOUNT IDENTIFICATION 2"),
            /*104*/ IFA.VARCHAR(100, "TRANSACTION DESCRIPTION"),
            /*105*/ IFA.VARCHAR(999, "RESERVED ISO USE"),
            /*106*/ IFA.VARCHAR(999, "RESERVED ISO USE"),
            /*107*/ IFA.VARCHAR(999, "RESERVED ISO USE"),
            /*108*/ IFA.VARCHAR(999, "RESERVED ISO USE"),
            /*109*/ IFA.VARCHAR(999, "RESERVED ISO USE"),
            /*110*/ IFA.VARCHAR(999, "RESERVED ISO USE"),
            /*111*/ IFA.VARCHAR(999, "RESERVED ISO USE"),
            /*112*/ IFA.VARCHAR(999, "RESERVED NATIONAL USE"),
            /*113*/ IFA.VARCHAR(999, "RESERVED NATIONAL USE"),
            /*114*/ IFA.VARCHAR(999, "RESERVED NATIONAL USE"),
            /*115*/ IFA.VARCHAR(999, "RESERVED NATIONAL USE"),
            /*116*/ IFA.VARCHAR(999, "RESERVED NATIONAL USE"),
            /*117*/ IFA.VARCHAR(999, "RESERVED NATIONAL USE"),
            /*118*/ IFA.VARCHAR(999, "RESERVED NATIONAL USE"),
            /*119*/ IFA.VARCHAR(999, "RESERVED NATIONAL USE"),
            /*120*/ IFA.VARCHAR(999, "RESERVED PRIVATE USE"),
            /*121*/ IFA.VARCHAR(999, "RESERVED PRIVATE USE"),
            /*122*/ IFA.VARCHAR(999, "RESERVED PRIVATE USE"),
            /*123*/ IFA.VARCHAR(999, "RESERVED PRIVATE USE"),
            /*124*/ IFA.VARCHAR(999, "RESERVED PRIVATE USE"),
            /*125*/ IFA.VARCHAR(999, "RESERVED PRIVATE USE"),
            /*126*/ IFA.VARCHAR(999, "RESERVED PRIVATE USE"),
            /*127*/ IFA.VARCHAR(999, "RESERVED PRIVATE USE"),
            /*128*/ IFA.BIN(8, "MAC 2"),
    };

    /**
     * Instantiates a new Iso 87 a mapper.
     */
    public ISO87AMapper() {
        super();
        setFieldsMapper(fieldsMapper);
    }

    @Override
    public String toString() {
        return "ISO87AMapper";
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        byte[] bytes = CodecUtil.hex2byte("303830303832333830303030383030313038303030343030303030313030303030303030303933303036353635383030303030313130323635383039333030393530323830363030303031382020453341424635384646394634463946463030333030303030303130333030313631303235383536313131443339383737304343");

        ISO87AMapper iso87AMapper = new ISO87AMapper();
        ISOMsg isoMsg = iso87AMapper.read(ByteBuffer.wrap(bytes));
        System.out.println(isoMsg);
        {
            ByteBuffer byteBuffer = ByteBuffer.allocate(9999);
            iso87AMapper.write(isoMsg, byteBuffer);
            byteBuffer.flip();
            byte[] bb = new byte[byteBuffer.limit()];
            byteBuffer.get(bb);
            System.out.println(CodecUtil.hexString(bb));


        }

    }


}
