package hyun.service;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TtsService {
    private static final Logger logger = LoggerFactory.getLogger(TtsService.class);
    private final String URL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts";
    private final String ID = "eotqdfinnh";  // Your Client ID
    private final String Secret = "9BoN7n7d6TcEdiQ3eGOJSdIJwruYoY24ao9ZmRCS"; // Your Client Secret

    public ResponseEntity<byte[]> convertTextToSpeech(String text) {
        try {
            HttpHeaders ttsHeaders = new HttpHeaders();
            ttsHeaders.set("Content-Type", "application/x-www-form-urlencoded");
            ttsHeaders.set("X-NCP-APIGW-API-KEY-ID", ID);
            ttsHeaders.set("X-NCP-APIGW-API-KEY", Secret);

            // Encode text and set parameters
            String requestBody = String.format("speaker=dara-danna&text=%s&volume=0&speed=0&pitch=0",
                    URLEncoder.encode(text, StandardCharsets.UTF_8.toString()));
            System.out.println(text);

            HttpEntity<String> ttsRequestEntity = new HttpEntity<>(requestBody, ttsHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> ttsResponse = restTemplate.exchange(URL, HttpMethod.POST, ttsRequestEntity, byte[].class);

            logger.info("TTS Response Status: {}", ttsResponse.getStatusCode());
            return ttsResponse;
        } catch (HttpClientErrorException e) {
            logger.error("HTTP Error: StatusCode: {}, ResponseBody: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Failed to convert text to speech", e);
            throw new RuntimeException("Failed to convert text to speech", e);
        }
    }
}
