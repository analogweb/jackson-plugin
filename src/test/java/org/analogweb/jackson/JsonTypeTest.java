package org.analogweb.jackson;

import static org.analogweb.core.fake.FakeApplication.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.analogweb.annotation.Post;
import org.analogweb.annotation.RequestFormats;
import org.analogweb.annotation.Route;
import org.analogweb.core.MediaTypes;
import org.analogweb.core.fake.FakeApplication;
import org.analogweb.core.fake.ResponseResult;
import org.analogweb.util.Maps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author snowgooseyk
 */
@Route
public class JsonTypeTest {

    private FakeApplication app;

    @Before
    public void setUp() {
        app = fakeApplication();
    }

    @After
    public void tearDown() {
        app.shutdown();
    }

    @Route
    @RequestFormats(MediaTypes.APPLICATION_JSON)
    @Post
    public String test(@JsonType Bean bean) {
        return bean.getName();
    }

    @Test
    public void testOk() {
        Map<String, List<String>> header = Maps.newHashMap("Content-Type",
                Arrays.asList("application/json"));
        ResponseResult result = app.request("jsontypetest/test", "POST", header,
                new ByteArrayInputStream("{\"name\":\"snowgooseyk\"}".getBytes()));
        assertThat(result.getStatus(), is(200));
        assertThat(result.getResponseBody().toString(), is("snowgooseyk"));
    }

    @Test
    public void testUnsupportedMediaType() {
        Map<String, List<String>> header = Maps.newHashMap("Content-Type",
                Arrays.asList("plain/text"));
        ResponseResult result = app.request("jsontypetest/test", "POST", header,
                new ByteArrayInputStream("{\"name\":\"snowgooseyk\"}".getBytes()));
        assertThat(result.getStatus(), is(415));
    }
}
