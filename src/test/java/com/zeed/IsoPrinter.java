package com.zeed;

import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsoPrinter {
    private static final int NUMBER_OF_ISO_FIELDS = 129;
    private static final Logger logger = LoggerFactory.getLogger(IsoPrinter.class);
    public static final String SOURCE_NODE_SENT_MESSAGE = "SOURCE_NODE_SENT_MESSAGE";
    public static final String SINK_NODE_SENT_MESSAGE = "SINK_NODE_SENT_MESSAGE";
    public static final String SINK_NODE_RECEIVED_MESSAGE = "SINK_NODE_RECIEVED_MESSAGE";
    public static final String SOURCE_NODE_RECEIVED_MESSAGE = "SOURCE_NODE_RECEIVED_MESSAGE";

    public IsoPrinter() {
    }

    public static void printISOMsg(ISOMsg isoMsg, String source) {
        if (isoMsg == null) {
            logger.info(source + " is null");
        } else {
            logger.info("Printing " + source + "\nUnpacked Iso Message is: \n" + dumpIsoMsg(isoMsg));
        }
    }

    public static String dump(Object message) {
        return message instanceof ISOMsg ? dumpIsoMsg((ISOMsg)message) : "";
    }

    private static String dumpIsoMsg(ISOMsg isoMsg) {
        try {
            StringBuilder isoString = new StringBuilder();
            isoString.append("<iso8583>\n");
            isoString.append("<isomsg>\n");

            for(int i = 0; i <= isoMsg.getMaxField(); ++i) {
                try {
                    if (i == 127) {
                        if (isoMsg.hasField(127)) {
                            ISOMsg field127Msg = (ISOMsg)isoMsg.getComponent(127);
                            isoString.append(formatPrivateIsoFields(field127Msg));
                        }
                    } else {
                        isoString.append(createIsoField(i, isoMsg));
                    }
                } catch (ISOException var4) {
                    throw new IllegalArgumentException("Unable to extract iso field ", var4);
                }
            }

            isoString.append("</isomsg>\n");
            isoString.append("</iso8583>\n");
            return isoString.toString();
        } catch (Exception var5) {
            return "";
        }
    }

    private static String formatPrivateIsoFields(ISOMsg isoMsg) throws ISOException {
        StringBuilder builder = new StringBuilder();

        for(int i = 1; i <= isoMsg.getMaxField(); ++i) {
            builder.append(createPrivateIsoField(i, isoMsg));
        }

        return builder.toString();
    }

    private static String createPrivateIsoField(int field, ISOMsg isoMsg) throws ISOException {
        if (isoMsg.hasField(field)) {
            String value = isoMsg.getString(field);
            if (field == 22) {
                value = (new String(new char[value.length()])).replace("\u0000", "*");
            }

            String fieldId = String.format("127.%03d", field);
            return "<field id=\"" + fieldId + "\" value=\"" + value + "\"/>\n";
        } else {
            return "";
        }
    }

    private static String createIsoField(int field, ISOMsg isoMsg) throws ISOException {
        if (isoMsg.hasField(field)) {
            String value = isoMsg.getString(field);
            switch(field) {
            case 2:
                value = CardUtils.maskPan(isoMsg.getString(field));
                break;
            case 35:
                value = CardUtils.maskTrack2(isoMsg.getString(field));
                break;
            case 52:
                value = (new String(new char[value.length()])).replace("\u0000", "*");
                break;
            case 53:
                value = (new String(new char[value.length()])).replace("\u0000", "*");
                break;
            case 55:
                value = (new String(Hex.encodeHex(isoMsg.getBytes(55)))).toUpperCase();
                break;
            case 125:
                value = (new String(new char[value.length()])).replace("\u0000", "*");
            }

            return "<field id=\"" + field + "\" value=\"" + value + "\"/>\n";
        } else {
            return "";
        }
    }
}
