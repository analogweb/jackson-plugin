package org.analogweb.jackson;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.analogweb.RequestContext;
import org.analogweb.exception.FormatFailureException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class JacksonJsonFormatterTest {

    private JacksonJsonFormatter formatter;
    private RequestContext context;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        formatter = new JacksonJsonFormatter();
        context = mock(RequestContext.class);
    }

    @Test
    public void testFormatAndWriteInto() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        when(context.getResponseBody()).thenReturn(out);
        Date expectedDate = new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20");
        Bean source = new Bean("snowgoose", true, expectedDate);
        formatter.formatAndWriteInto(context, "UTF-8", source);
        String actual = new String(out.toByteArray(), "UTF-8");
        assertThat(actual,
                is("{\"name\":\"snowgoose\",\"alive\":true,\"date\":" + expectedDate.getTime()
                        + "}"));
    }

    @Test
    public void testFormatAndWriteIntoOccursIOException() throws Exception {
        thrown.expect(FormatFailureException.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        when(context.getResponseBody()).thenReturn(out);
        ObjectMapper alwaysIOError = new ObjectMapper() {
            @Override
            public void writeValue(OutputStream out, Object value) throws IOException,
                    JsonGenerationException, JsonMappingException {
                throw new IOException();
            }
        };
        Bean source = new Bean("snowgoose", true,
                new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20"));
        formatter.setObjectMapper(alwaysIOError);
        formatter.formatAndWriteInto(context, "UTF-8", source);
    }

}
