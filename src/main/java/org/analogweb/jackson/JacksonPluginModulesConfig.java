package org.analogweb.jackson;

import org.analogweb.ModulesBuilder;
import org.analogweb.PluginModulesConfig;
import org.analogweb.core.response.Json;
import org.analogweb.util.MessageResource;
import org.analogweb.util.PropertyResourceBundleMessageResource;
import org.analogweb.util.logging.Log;
import org.analogweb.util.logging.Logs;

/**
 * This {@link PluginModulesConfig} integrates <a href=""https://github.com/FasterXML/jackson"">Jackson</a> into
 * <a href="http://analogweb.org">Analog Web Framework</a>
 * @author y2k2mt
 */
public class JacksonPluginModulesConfig implements PluginModulesConfig {

    /**
     * {@link MessageResource} for this plugin.
     */
    public static final MessageResource PLUGIN_MESSAGE_RESOURCE = new PropertyResourceBundleMessageResource(
            "org.analogweb.jackson.analog-messages");
    private static final Log log = Logs.getLog(JacksonPluginModulesConfig.class);

    @Override
    public ModulesBuilder prepare(ModulesBuilder builder) {
        log.log(PLUGIN_MESSAGE_RESOURCE, "IJKB000001");
        builder.addResponseFormatterClass(Json.class, JacksonJsonFormatter.class);
        builder.addRequestValueResolverClass(JacksonJsonValueResolver.class);
        return builder;
    }
}
