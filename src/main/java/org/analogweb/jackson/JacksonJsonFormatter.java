package org.analogweb.jackson;

import java.io.IOException;

import org.analogweb.DirectionFormatter;
import org.analogweb.RequestContext;
import org.analogweb.exception.FormatFailureException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author snowgoose
 */
public class JacksonJsonFormatter implements DirectionFormatter {

    @Override
    public void formatAndWriteInto(RequestContext writeTo, String charset, Object source) {
        // TODO Auto-generated method stub
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(writeTo.getResponse().getOutputStream(), source);
        } catch (IOException e) {
            throw new FormatFailureException(e, source, getClass().getName());
        }
    }

}
