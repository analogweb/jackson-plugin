Analog Web Framework Jackson Plugin
===============================================

[![Build Status](https://travis-ci.org/analogweb/jackson-plugin.svg)](https://travis-ci.org/analogweb/jackson-plugin)

This plugin enables parse JSON format request.(application/json)

Also rendering JSON using Jackson's ObjectMapper.

## Example

```java
import static org.analogweb.core.response.BasicResponses.json;

import org.analogweb.annotation.Route;
import org.analogweb.annotation.Get;
import org.analogweb.annotation.Post;
import org.analogweb.annotation.RequestFormats;
import org.analogweb.core.MediaTypes;
import org.analogweb.core.response.Json;
import org.analogweb.jackson.JsonType;

@Route("/")
public class Hello {

  @Route("json")
  // Add Content-Type limitation. (Response 415 when invalid Content-Type detected.)
  // Without this annotation there is no Content-Type limitation. (Argument is null.)
  @RequestFormats(MediaTypes.APPLICATION_JSON)
  @Post
  public String postJson(@JsonType Bean bean) {
     // Obtain requested JSON as object.
     ...
  }

  @Route("json")
  @Get
  public Json getJson() {
    ...
    Bean bean
    ...
    // Response JSON using Jackson's ObjectMapper.
    return json(bean)
  }

}
```
