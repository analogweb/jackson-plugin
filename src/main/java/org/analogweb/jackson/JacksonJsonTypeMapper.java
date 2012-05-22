package org.analogweb.jackson;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import org.analogweb.RequestAttributes;
import org.analogweb.RequestContext;
import org.analogweb.TypeMapper;
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
public class JacksonJsonTypeMapper implements TypeMapper {

    private ObjectMapper mapper = initObjectMapper();

    protected ObjectMapper initObjectMapper() {
        ObjectMapper newMapper = new ObjectMapper();
        newMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return newMapper;
    }

    @Override
    public Object mapToType(RequestContext context, RequestAttributes attributes, Object from,
            Class<?> requiredType, String[] formats) {
        if (isJsonType(context)) {
            if (InputStream.class.isInstance(from)) {
                return jsonToObject((InputStream) from, requiredType);
            } else if (Reader.class.isInstance(from)) {
                return jsonToObject((Reader) from, requiredType);
            }
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
        HttpServletRequest request = context.getRequest();
        String contentType = request.getContentType();
        return StringUtils.isNotEmpty(contentType) && (contentType.equals("application/json"));
    }

    protected ObjectMapper getObjectMapper() {
        return this.mapper;
    }

}
