package org.analogweb.jackson;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertThat;

import org.analogweb.ModulesBuilder;
import org.analogweb.core.direction.Json;
import org.junit.Before;
import org.junit.Test;

public class JacksonPluginModulesConfigTest {

    private JacksonPluginModulesConfig config;
    private ModulesBuilder builder;

    @Before
    public void setUp() throws Exception {
        config = new JacksonPluginModulesConfig();
        builder = mock(ModulesBuilder.class);
    }

    @Test
    public void test() {
        when(builder.addDirectionFormatterClass(Json.class, JacksonJsonFormatter.class))
                .thenReturn(builder);

        ModulesBuilder actual = config.prepare(builder);

        assertThat(actual, is(builder));
        verify(builder).addDirectionFormatterClass(Json.class, JacksonJsonFormatter.class);
    }

}
