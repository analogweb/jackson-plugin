package org.analogweb.jackson;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.analogweb.Headers;
import org.analogweb.RequestContext;
import org.junit.Before;
import org.junit.Test;

/**
 * @author snowgoose
 */
public class JacksonJsonTypeMapperTest {

    private JacksonJsonTypeMapper mapper;
    private RequestContext requestContext;
    private Headers headers;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        mapper = new JacksonJsonTypeMapper();
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
        Bean actual = (Bean) mapper.mapToType(requestContext, from, Bean.class, null);
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
        Bean actual = (Bean) mapper.mapToType(requestContext, from, Bean.class, null);
        assertThat(actual.getName(), is("snowgoose"));
        assertThat(actual.isAlive(), is(true));
        assertThat(actual.getDate(), is(expectedDate));
    }

    @Test
    public void testMapToTypeWithReader() throws Exception {
        when(headers.getValues("Content-Type")).thenReturn(Arrays.asList("application/json"));
        Date expectedDate = new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20");
        Reader from = new StringReader(
                String.valueOf("{\"name\":\"snowgoose2\",\"alive\":false,\"date\":"
                        + expectedDate.getTime() + "}"));
        Bean actual = (Bean) mapper.mapToType(requestContext, from, Bean.class, null);
        assertThat(actual.getName(), is("snowgoose2"));
        assertThat(actual.isAlive(), is(false));
        assertThat(actual.getDate(), is(expectedDate));
    }

    @Test
    public void testMapToTypeWithInvalidSource() throws Exception {
        when(headers.getValues("Content-Type")).thenReturn(Arrays.asList("application/json"));
        Bean actual = (Bean) mapper.mapToType(requestContext, mock(Serializable.class), Bean.class,
                null);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void testMapToTypeWithNullSource() throws Exception {
        when(headers.getValues("Content-Type")).thenReturn(Arrays.asList("application/json"));
        Bean actual = (Bean) mapper.mapToType(requestContext, null, Bean.class, null);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void testMapToTypeWithInvalidContentType() throws Exception {
        when(headers.getValues("Content-Type")).thenReturn(Arrays.asList("text/javascript"));
        InputStream from = new ByteArrayInputStream(
                "{\"name\":\"snowgoose\",\"alive\":true,\"date\":261846000000}".getBytes());
        Bean actual = (Bean) mapper.mapToType(requestContext, from, Bean.class, null);
        assertThat(actual, is(nullValue()));
    }

}
