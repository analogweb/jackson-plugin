package org.analogweb.jackson;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.analogweb.RequestAttributes;
import org.analogweb.RequestContext;
import org.junit.Before;
import org.junit.Test;

/**
 * @author snowgoose
 */
public class JacksonJsonTypeMapperTest {

    private JacksonJsonTypeMapper mapper;
    private RequestContext requestContext;
    private RequestAttributes attributes;
    private HttpServletRequest request;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        mapper = new JacksonJsonTypeMapper();
        requestContext = mock(RequestContext.class);
        request = mock(HttpServletRequest.class);
        when(requestContext.getRequest()).thenReturn(request);
        attributes = mock(RequestAttributes.class);
    }

    @Test
    public void testMapToType() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        InputStream from = new ByteArrayInputStream(
                "{\"name\":\"snowgoose\",\"alive\":true,\"date\":261846000000}".getBytes());
        Bean actual = (Bean) mapper.mapToType(requestContext, attributes, from, Bean.class, null);
        Date expectedDate = new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20");
        assertThat(actual.getName(), is("snowgoose"));
        assertThat(actual.isAlive(), is(true));
        assertThat(actual.getDate(), is(expectedDate));
    }

    @Test
    public void testMapToTypeWithReader() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        Reader from = new StringReader(
                "{\"name\":\"snowgoose2\",\"alive\":false,\"date\":261846000000}");
        Bean actual = (Bean) mapper.mapToType(requestContext, attributes, from, Bean.class, null);
        Date expectedDate = new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20");
        assertThat(actual.getName(), is("snowgoose2"));
        assertThat(actual.isAlive(), is(false));
        assertThat(actual.getDate(), is(expectedDate));
    }

    @Test
    public void testMapToTypeWithInvalidSource() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        Bean actual = (Bean) mapper.mapToType(requestContext, attributes, mock(Serializable.class),
                Bean.class, null);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void testMapToTypeWithNullSource() throws Exception {
        when(request.getContentType()).thenReturn("application/json");
        Bean actual = (Bean) mapper.mapToType(requestContext, attributes, null, Bean.class, null);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void testMapToTypeWithInvalidContentType() throws Exception {
        when(request.getContentType()).thenReturn("text/javascript");
        InputStream from = new ByteArrayInputStream(
                "{\"name\":\"snowgoose\",\"alive\":true,\"date\":261846000000}".getBytes());
        Bean actual = (Bean) mapper.mapToType(requestContext, attributes, from, Bean.class, null);
        assertThat(actual, is(nullValue()));
    }

}
