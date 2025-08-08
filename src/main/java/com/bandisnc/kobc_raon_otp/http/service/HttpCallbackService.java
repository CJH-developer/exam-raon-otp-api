package com.bandisnc.kobc_raon_otp.http.service;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.http.util.HttpConService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HttpCallbackService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${ONE_PASS_URL}")
    private String onePassUrl;

    @Value("${CONTENT_TYPE}")
    private String content_type;

    @Value("${METHOD}")
    private String method;


    //private final RestTemplate restTemplate;

    /*public ResponseDTO callback(Map<String, String> map) {

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, header);

        try {
            ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(onePassUrl, httpEntity, ResponseDTO.class);
            ResponseDTO responseDTO = response.getBody();

            if (responseDTO != null && "000".equals(responseDTO.getResultCode())) {
                return responseDTO;
            }
            return responseDTO;
        } catch (Exception e) {
            System.err.println("One Pass API 호출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }*/
    private final ObjectMapper objectMapper;

    @Autowired
    private final HttpConService httpConService;

    public ResponseDTO callback(Map<String, String> map) {

        try{
            String wrap = objectMapper.writeValueAsString(map);
            HttpConService.Result result = httpConService.callHttp(onePassUrl, method, wrap, content_type, null, null);

            if(result.getHttpStatusCode() == 200){
                String content = result.getContent();
                ResponseDTO responseDTO = objectMapper.readValue(content, ResponseDTO.class);

                if( "000".equals(responseDTO.getResultCode())){
                    return responseDTO;
                }else{
                    logger.info("[FAIL_API_RESULT]" + responseDTO.getResultMsg());
                    return responseDTO;
                }
            }else{
                logger.info("[HTTP_STATUS_ERROR]" + result.getHttpStatusCode());
            }
        }catch (Exception e){
            logger.info("[ONEPASS_API_REQUEST_ERROR]" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
