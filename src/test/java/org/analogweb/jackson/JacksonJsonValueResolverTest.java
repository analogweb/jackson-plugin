package org.analogweb.jackson;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.analogweb.Headers;
import org.analogweb.RequestContext;
import org.analogweb.core.MediaTypes;
import org.junit.Before;
import org.junit.Test;

/**
 * @author snowgoose
 */
public class JacksonJsonValueResolverTest {

    private JacksonJsonValueResolver mapper;
    private RequestContext requestContext;
    private Headers headers;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        mapper = new JacksonJsonValueResolver();
        requestContext = mock(RequestContext.class);
        headers = mock(Headers.class);
        when(requestContext.getRequestHeaders()).thenReturn(headers);
    }

    @Test
    public void testMapToType() throws Exception {
        when(headers.getValues("Content-Type")).thenReturn(Arrays.asList("application/json"));
        Date expectedDate = new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20");
        InputStream from = new ByteArrayInputStream(String.valueOf(
                "{\"name\":\"snowgoose\",\"alive\":true,\"date\":" + expectedDate.getTime() + "}")
                .getBytes());
        when(requestContext.getRequestBody()).thenReturn(from);
        Bean actual = (Bean) mapper.resolveValue(requestContext, null, null, Bean.class);
        assertThat(actual.getName(), is("snowgoose"));
        assertThat(actual.isAlive(), is(true));
        assertThat(actual.getDate(), is(expectedDate));
    }

    @Test
    public void testMapToTypeWithCharset() throws Exception {
        when(headers.getValues("Content-Type")).thenReturn(
                Arrays.asList("application/json ;charset=utf-8"));
        Date expectedDate = new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20");
        InputStream from = new ByteArrayInputStream(String.valueOf(
                "{\"name\":\"snowgoose\",\"alive\":true,\"date\":" + expectedDate.getTime() + "}")
                .getBytes());
        when(requestContext.getRequestBody()).thenReturn(from);
        Bean actual = (Bean) mapper.resolveValue(requestContext, null, null, Bean.class);
        assertThat(actual.getName(), is("snowgoose"));
        assertThat(actual.isAlive(), is(true));
        assertThat(actual.getDate(), is(expectedDate));
    }

    @Test
    public void testMapToTypeWithNullSource() throws Exception {
        when(headers.getValues("Content-Type")).thenReturn(Arrays.asList("application/json"));
        when(requestContext.getRequestBody()).thenThrow(new IOException());
        Bean actual = (Bean) mapper.resolveValue(requestContext, null, null, Bean.class);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void testSupports() throws Exception {
        assertThat(mapper.supports(MediaTypes.APPLICATION_JSON_TYPE), is(true));
        assertThat(mapper.supports(MediaTypes.TEXT_PLAIN_TYPE), is(false));
        assertThat(mapper.supports(MediaTypes.valueOf("text/javascript")), is(false));
        assertThat(mapper.supports(MediaTypes.valueOf("aplication/javascript")), is(false));
    }
}
