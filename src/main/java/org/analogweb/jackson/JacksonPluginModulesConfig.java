package org.analogweb.jackson;

import org.analogweb.ModulesBuilder;
import org.analogweb.PluginModulesConfig;
import org.analogweb.core.response.Json;
import org.analogweb.util.MessageResource;
import org.analogweb.util.PropertyResourceBundleMessageResource;
import org.analogweb.util.logging.Log;
import org.analogweb.util.logging.Logs;

/**
 * <a href="http://jackson.codehaus.org/">Jackson</a>フレームワークを
 * <a href="https://github.com/analogweb">Analog Web Framework</a>
 * に統合する{@link PluginModulesConfig}です。<br/>
 * このプラグインを使用することで、{@link Json}使用時に、
 * Jacksonを利用したJsonの生成とレスポンスを行う事が可能になります。
 * @author snowgoose
 */
public class JacksonPluginModulesConfig implements PluginModulesConfig {

    /**
     * Jacksonプラグインで使用する{@link MessageResource}です。
     */
    public static final MessageResource PLUGIN_MESSAGE_RESOURCE = new PropertyResourceBundleMessageResource(
            "org.analogweb.jackson.analog-messages");
    private static final Log log = Logs.getLog(JacksonPluginModulesConfig.class);

    @Override
    public ModulesBuilder prepare(ModulesBuilder builder) {
        log.log(PLUGIN_MESSAGE_RESOURCE, "IJKB000001");
        builder.addResponseFormatterClass(Json.class, JacksonJsonFormatter.class);
        builder.addAttributesHandlerClass(JacksonJsonTypeMapper.class);
        return builder;
    }

}
