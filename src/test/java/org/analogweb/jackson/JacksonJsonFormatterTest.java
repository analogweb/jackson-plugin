package org.analogweb.jackson;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
    private HttpServletResponse response;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        formatter = new JacksonJsonFormatter();
        context = mock(RequestContext.class);
        response = mock(HttpServletResponse.class);
        when(context.getResponse()).thenReturn(response);
    }

    @Test
    public void testFormatAndWriteInto() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override
            public void write(int arg0) throws IOException {
                out.write(arg0);
            }
        });
        Bean source = new Bean("snowgoose",true,new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20"));
        formatter.formatAndWriteInto(context, "UTF-8", source);
        String actual = new String(out.toByteArray(),"UTF-8");
        assertThat(actual,is("{\"name\":\"snowgoose\",\"alive\":true,\"date\":261846000000}"));
    }

    @Test
    public void testFormatAndWriteIntoOccursIOException() throws Exception {
        thrown.expect(FormatFailureException.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override
            public void write(int arg0) throws IOException {
                out.write(arg0);
            }
        });
        ObjectMapper alwaysIOError = new ObjectMapper(){
            @Override
            public void writeValue(OutputStream out, Object value) throws IOException,
                    JsonGenerationException, JsonMappingException {
                throw new IOException();
            }
        };
        Bean source = new Bean("snowgoose",true,new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20"));
        formatter.setObjectMapper(alwaysIOError);
        formatter.formatAndWriteInto(context, "UTF-8", source);
    }

}
