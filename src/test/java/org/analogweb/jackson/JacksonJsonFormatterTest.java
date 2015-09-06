package org.analogweb.jackson;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.analogweb.RequestContext;
import org.analogweb.ResponseContext;
import org.analogweb.core.FormatFailureException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonFormatterTest {

    private JacksonJsonFormatter formatter;
    private RequestContext context;
    private ResponseContext response;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        formatter = new JacksonJsonFormatter();
        context = mock(RequestContext.class);
        response = mock(ResponseContext.class);
    }

    @Test
    public void testFormatAndWriteInto() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        Date expectedDate = new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20");
        Bean source = new Bean("snowgoose", true, expectedDate);
        formatter.formatAndWriteInto(context, response, "UTF-8", source).writeInto(out);

        String actual = new String(out.toByteArray(), "UTF-8");
        assertThat(actual,
                is("{\"name\":\"snowgoose\",\"alive\":true,\"date\":" + expectedDate.getTime()
                        + "}"));
    }

    @Test
    public void testFormatAndWriteIntoOccursIOException() throws Exception {
        thrown.expect(FormatFailureException.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectMapper alwaysIOError = new ObjectMapper() {
			private static final long serialVersionUID = 1L;

			@Override
            public void writeValue(OutputStream out, Object value) throws IOException,
                    JsonGenerationException, JsonMappingException {
                throw new IOException();
            }
        };
        Bean source = new Bean("snowgoose", true,
                new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20"));
        formatter.setObjectMapper(alwaysIOError);
        formatter.formatAndWriteInto(context, response, "UTF-8", source).writeInto(out);
    }

}
