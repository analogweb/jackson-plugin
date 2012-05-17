package org.analogweb.jackson;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.analogweb.RequestContext;
import org.junit.Before;
import org.junit.Test;

public class JacksonJsonFormatterTest {

    private JacksonJsonFormatter formatter;
    private RequestContext context;

    @Before
    public void setUp() throws Exception {
        formatter = new JacksonJsonFormatter();
        context = mock(RequestContext.class);
    }

    @Test
    public void test() throws Exception {
        Bean source = new Bean("snowgoose",true,new SimpleDateFormat("yyyy/MM/dd").parse("1978/4/20"));
        formatter.formatAndWriteInto(context, "UTF-8", source);
        fail("Not yet implemented");
    }

    private static class Bean {
        private String name;
        private boolean alive;
        private Date date;
        public Bean(String name, boolean alive, Date date) {
            super();
            this.name = name;
            this.alive = alive;
            this.date = date;
        }
        public String getName() {
            return name;
        }
        public boolean isAlive() {
            return alive;
        }
        public Date getDate() {
            return date;
        }
        
    }
}
