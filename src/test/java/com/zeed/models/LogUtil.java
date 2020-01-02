package com.zeed.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.jpos.util.Loggeable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tosineniolorunda on 7/3/16.
 */
public class LogUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger logger = Logger.getLogger(LogUtil.class.getName());

    static {
        OBJECT_MAPPER.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String dump(Loggeable loggeable) {
        if (loggeable == null) {
            return "<nothing>";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        loggeable.dump(ps, "    ");
        return baos.toString();
    }

    public static String dump(Loggeable loggeable, String indent) {
        if (loggeable == null) {
            return "<nothing>";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        loggeable.dump(ps, indent);
        return baos.toString();
    }

    public static String dump(Object object) {
        if (object == null) {
            return "<nothing>";
        }
        try {
            String[] ignorableFieldNames = {"pan", "sourceInterchange", "sourceMessageBytes", "pinData", "cardData","chequeFrontImage","chequeRearImage"};
            FilterProvider filterProvider = new SimpleFilterProvider()
                    .addFilter("sensitiveDataFilter", SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames))
                    .setFailOnUnknownId(false);
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().with(filterProvider).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return "";
        }
    }
}
