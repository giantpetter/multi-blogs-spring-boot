package MySpring.integrationTest;

import MySpring.Application;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class MyIntegrationTest {
    @Inject
    private Environment environment;

    @Test
    public void isLoginByDefault() throws IOException {
        String port = environment.getProperty("local.server.port");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://127.0.0.1:" + port + "/auth");
        CloseableHttpResponse response = httpClient.execute(httpGet);

        HttpEntity responseEntity = response.getEntity();
        String str = EntityUtils.toString(responseEntity);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        Assertions.assertEquals(200, statusCode);

        Assertions.assertTrue(str.contains("用户没有登录"));
    }

}

