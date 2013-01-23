package org.analogweb.jackson;

import java.io.IOException;
import java.io.OutputStream;

import org.analogweb.DirectionFormatter;
import org.analogweb.RequestContext;
import org.analogweb.ResponseContext;
import org.analogweb.ResponseContext.ResponseEntity;
import org.analogweb.exception.FormatFailureException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

/**
 * <a href="http://jackson.codehaus.org/">Jackson</a>を使用して
 * レスポンスする対象のオブジェクトをJSONにフォーマットする
 * {@link DirectionFormatter}の実装です。<br/>
 * @author snowgoose
 */
public class JacksonJsonFormatter implements DirectionFormatter {

    private ObjectMapper mapper;

    protected ObjectMapper initObjectMapper() {
        ObjectMapper newMapper = new ObjectMapper();
        newMapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
        return newMapper;
    }

    @Override
    public void formatAndWriteInto(RequestContext context, ResponseContext writeTo, String charset,
            final Object source) {
        final ObjectMapper mapper = getObjectMapper();
        writeTo.getResponseWriter().writeEntity(new ResponseEntity() {
            @Override
            public void writeInto(OutputStream responseBody) throws IOException {
                try {
                    mapper.writeValue(responseBody, source);
                } catch (IOException e) {
                    throw new FormatFailureException(e, source, getClass().getName());
                }
            }
        });
    }

    protected ObjectMapper getObjectMapper() {
        if (this.mapper == null) {
            this.mapper = initObjectMapper();
        }
        return this.mapper;
    }

    public synchronized void setObjectMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

}
