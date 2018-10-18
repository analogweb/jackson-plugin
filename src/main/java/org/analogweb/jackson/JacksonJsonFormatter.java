package org.analogweb.jackson;

import java.io.IOException;
import java.io.OutputStream;

import org.analogweb.ResponseFormatter;
import org.analogweb.RequestContext;
import org.analogweb.ResponseContext;
import org.analogweb.ResponseEntity;
import org.analogweb.core.FormatFailureException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * {@link ResponseFormatter} implementation for deserialise or serialise JSON with 
 * <a href="https://github.com/FasterXML/jackson">Jackson</a><br/>
 * @author y2k2mt
 */
public class JacksonJsonFormatter implements ResponseFormatter {

    private ObjectMapper mapper;

    protected ObjectMapper initObjectMapper() {
        ObjectMapper newMapper = new ObjectMapper();
        newMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return newMapper;
    }

    @Override
    public ResponseEntity formatAndWriteInto(RequestContext context, ResponseContext writeTo,
            String charset, final Object source) {
        final ObjectMapper mapper = getObjectMapper();
        return new ResponseEntity<byte[]>() {
        	
        	private byte[] bytes;

            @Override
            public byte[] entity() {
                return getByteContents();
            }

            @Override
            public long getContentLength() {
                return getByteContents().length;
            }
            
            private byte[] getByteContents(){
                try {
                	if(this.bytes == null){
                        this.bytes = mapper.writeValueAsBytes(source);
                	}
                	return this.bytes;
                } catch (IOException e) {
                    throw new FormatFailureException(e, source, getClass().getName());
                }
            }
        };
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
