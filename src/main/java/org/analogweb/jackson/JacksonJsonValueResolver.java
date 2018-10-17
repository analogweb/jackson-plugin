package org.analogweb.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;

import org.analogweb.InvocationMetadata;
import org.analogweb.MediaType;
import org.analogweb.RequestContext;
import org.analogweb.core.ApplicationRuntimeException;
import org.analogweb.core.InvalidRequestFormatException;
import org.analogweb.core.MediaTypes;
import org.analogweb.core.SpecificMediaTypeRequestValueResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link SpecificMediaTypeRequestValueResolver} implementation for Jackson.
 * Parse JSON formatted request body using Jackson's {@link ObjectMapper}.
 * @author y2k2mt
 */
public class JacksonJsonValueResolver implements SpecificMediaTypeRequestValueResolver {

    private ObjectMapper mapper;

    protected ObjectMapper initObjectMapper() {
        ObjectMapper newMapper = new ObjectMapper();
        newMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return newMapper;
    }

    @Override
    public Object resolveValue(RequestContext context, InvocationMetadata metadata, String key,
            Class<?> requiredType, Annotation[] annotations) {
        try {
            return jsonToObject(context.getRequestBody().asInputStream(), requiredType);
        } catch (IOException e) {
            throw new InvalidRequestFormatException(e, getClass());
        }
    }

    protected Object jsonToObject(InputStream in, Class<?> requiredType) {
        try {
            return getObjectMapper().readValue(in, requiredType);
        } catch (JsonMappingException e) {
            throw new ApplicationRuntimeException(e) {
                private static final long serialVersionUID = 1L;
            };
        } catch (JsonParseException e) {
            throw new InvalidRequestFormatException(e, JacksonJsonValueResolver.class);
        } catch (IOException e){
            throw new InvalidRequestFormatException(e, getClass());
        }
    }

    protected Object jsonToObject(Reader reader, Class<?> requiredType) {
        try {
            return getObjectMapper().readValue(reader, requiredType);
        } catch (JsonMappingException e) {
            throw new InvalidRequestFormatException(e, getClass());
        } catch (JsonParseException e) {
            throw new InvalidRequestFormatException(e, getClass());
        } catch (IOException e){
            throw new InvalidRequestFormatException(e, getClass());
        }
    }

    @Override
    public boolean supports(MediaType mediaType) {
        return MediaTypes.APPLICATION_JSON_TYPE.isCompatible(mediaType);
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
