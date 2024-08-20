package hyun.controller;

import hyun.service.ChatbotService;
import hyun.service.TtsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/tts")
public class TtsController {

    private final String URL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=Kor";
    private final String ID = "eotqdfinnh";
    private final String Secret = "9BoN7n7d6TcEdiQ3eGOJSdIJwruYoY24ao9ZmRCS";

    private final TtsService ttsService;
    private final ChatbotService chatbotService;

    public TtsController(TtsService ttsService,ChatbotService chatbotService) {
        this.ttsService = ttsService;
        this.chatbotService=chatbotService;
        
    }

    @PostMapping("/convert")
    public Object convertSpeechToText(@RequestParam("file") MultipartFile file) {
        try {
            // Prepare headers for STT request
            HttpHeaders sttHeaders = new HttpHeaders();
            sttHeaders.set("Content-Type", "application/octet-stream");
            sttHeaders.set("X-NCP-APIGW-API-KEY-ID", ID);
            sttHeaders.set("X-NCP-APIGW-API-KEY", Secret);

            // Create request entity for STT
            HttpEntity<byte[]> sttRequestEntity = new HttpEntity<>(file.getBytes(), sttHeaders);

            // Initialize RestTemplate and make STT request
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> sttResponse = restTemplate.exchange(URL, HttpMethod.POST, sttRequestEntity, String.class);
//
//            // Check STT response
//            if (sttResponse.getStatusCode() == HttpStatus.OK) {
//                String recognizedText = sttResponse.getBody();
//                // Process recognized text and call TTS service
//                if (recognizedText != null && !recognizedText.isEmpty()) {
//                    ResponseEntity<byte[]> ttsResponse = ttsService.convertTextToSpeech(recognizedText);
//
//                    System.out.println(ttsResponse.getBody());
//                    return new ResponseEntity<>(ttsResponse.getBody(), HttpStatus.OK);
//                } else {
//                    // Return an empty audio file or a suitable response
//                }
//            } else {
//                // Return an empty audio file or a suitable response
//                return new ResponseEntity<>(("STT Error: " + sttResponse.getBody()).getBytes(), HttpStatus.BAD_REQUEST);
//            }

            // Check STT response
            if (sttResponse.getStatusCode() == HttpStatus.OK) {
                String recognizedText = sttResponse.getBody();
                // Process recognized text and call Chatbot service
                if (recognizedText != null && !recognizedText.isEmpty()) {
                    String chatbotResponse = chatbotService.main(recognizedText);

                    // Call TTS service with chatbot response
                    if (chatbotResponse != null && !chatbotResponse.isEmpty()) {
                        ResponseEntity<byte[]> ttsResponse = ttsService.convertTextToSpeech(chatbotResponse);

                        return new ResponseEntity<>(ttsResponse.getBody(), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Chatbot response is empty", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity<>("STT recognized text is empty", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(("STT Error: " + sttResponse.getBody()).getBytes(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(("Internal Server Error: " + e.getMessage()).getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
