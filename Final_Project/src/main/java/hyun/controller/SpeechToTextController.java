package hyun.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@RestController
@RequestMapping("/api/stt")
public class SpeechToTextController {

    private final String URL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=Kor";
    private final String ID = "eotqdfinnh"; // 인증 정보의 Client ID
    private final String Secret = "9BoN7n7d6TcEdiQ3eGOJSdIJwruYoY24ao9ZmRCS"; // 인증 정보의 Client Secret


    @PostMapping("/convert")
    public ResponseEntity<String> convertSpeechToText(@RequestParam("file") MultipartFile file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/octet-stream");
            headers.set("X-NCP-APIGW-API-KEY-ID", ID);
            headers.set("X-NCP-APIGW-API-KEY", Secret);

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, String.class);

            return response.getStatusCode() == HttpStatus.OK
                ? new ResponseEntity<>(response.getBody(), HttpStatus.OK)
                : new ResponseEntity<>("Error: " + response.getBody(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
