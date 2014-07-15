package org.kiwi;

import org.glassfish.jersey.moxy.json.MoxyJsonConfig;

import javax.ws.rs.ext.ContextResolver;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
        final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
        namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
        return moxyJsonConfig.resolver();
    }
}
