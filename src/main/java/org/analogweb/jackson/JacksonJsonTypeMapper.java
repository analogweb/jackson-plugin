package org.analogweb.jackson;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.analogweb.InvocationMetadata;
import org.analogweb.MediaType;
import org.analogweb.RequestContext;
import org.analogweb.TypeMapper;
import org.analogweb.core.AbstractAttributesHandler;
import org.analogweb.core.MediaTypes;
import org.analogweb.core.SpecificMediaTypeAttirbutesHandler;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Jacksonによる変換により、リクエストされたJSONを任意のオブジェクト
 * のインスタンスに変換する{@link TypeMapper}の実装です。<br/>
 * 変換元の値として、リクエストされたJSONを保持する{@link InputStream}
 * または{@link Reader}(読み込み可能なリクエストボディ)が指定されている
 * 必要があります。
 * @author snowgoose
 */
public class JacksonJsonTypeMapper extends AbstractAttributesHandler implements
        SpecificMediaTypeAttirbutesHandler {

    private ObjectMapper mapper;

    protected ObjectMapper initObjectMapper() {
        ObjectMapper newMapper = new ObjectMapper();
        newMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return newMapper;
    }

    @Override
    public String getScopeName() {
        return "json";
    }

    @Override
    public Object resolveAttributeValue(RequestContext context, InvocationMetadata metadata,
            String key, Class<?> requiredType) {
        try {
            return jsonToObject(context.getRequestBody(), requiredType);
        } catch (IOException e) {
            return null;
        }
    }

    protected Object jsonToObject(InputStream in, Class<?> requiredType) {
        try {
            return getObjectMapper().readValue(in, requiredType);
        } catch (IOException e) {
            return null;
        }
    }

    protected Object jsonToObject(Reader reader, Class<?> requiredType) {
        try {
            return getObjectMapper().readValue(reader, requiredType);
        } catch (IOException e) {
            return null;
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
