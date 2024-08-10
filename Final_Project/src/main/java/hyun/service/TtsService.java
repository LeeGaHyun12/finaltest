package hyun.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TtsService {

    @Value("${naver.tts.api-url}")
    private String apiUrl;

    @Value("${naver.tts.client-id}")
    private String clientId;

    @Value("${naver.tts.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public TtsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public File synthesizeTextToFile(String text) throws IOException {
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        String body = "speaker=nara&volume=0&speed=0&pitch=0&format=mp3&text=" + encodedText;

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                URI.create(apiUrl),
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            // 파일 이름을 현재 시간 기반으로 생성
            String fileName = "output_" + new Date().getTime() + ".mp3";
            File file = new File(fileName);

            try (OutputStream os = new FileOutputStream(file)) {
                os.write(response.getBody());
                System.out.println("File written successfully: " + fileName);
            }

            return file;
        } else {
            // 상세한 오류 메시지 포함
            throw new IOException("Failed to synthesize text. HTTP Status: " + response.getStatusCode() + ". Response Body: " + response.getBody());
        }
    }
}
