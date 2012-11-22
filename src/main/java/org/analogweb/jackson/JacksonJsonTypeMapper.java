package org.analogweb.jackson;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import org.analogweb.Headers;
import org.analogweb.InvocationMetadata;
import org.analogweb.RequestContext;
import org.analogweb.TypeMapper;
import org.analogweb.core.AbstractAttributesHandler;
import org.analogweb.util.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Jacksonによる変換により、リクエストされたJSONを任意のオブジェクト
 * のインスタンスに変換する{@link TypeMapper}の実装です。<br/>
 * 変換元の値として、リクエストされたJSONを保持する{@link InputStream}
 * または{@link Reader}(読み込み可能なリクエストボディ)が指定されている
 * 必要があります。
 * @author snowgoose
 */
public class JacksonJsonTypeMapper extends AbstractAttributesHandler {

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
        if (isJsonType(context)) {
            try {
                return jsonToObject(context.getRequestBody(), requiredType);
            } catch (IOException e) {
                return null;
            }
            /*
            if (InputStream.class.isInstance(from)) {
                return jsonToObject((InputStream) from, requiredType);
            } else if (Reader.class.isInstance(from)) {
                return jsonToObject((Reader) from, requiredType);
            }
            */
        }
        return null;
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

    protected boolean isJsonType(RequestContext context) {
        Headers headers = context.getRequestHeaders();
        List<String> contentTypes = headers.getValues("Content-Type");
        if (contentTypes == null || contentTypes.isEmpty()) {
            return false;
        }
        String contentType = contentTypes.get(0);
        return StringUtils.isNotEmpty(contentType) && (contentType.startsWith("application/json"));
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
