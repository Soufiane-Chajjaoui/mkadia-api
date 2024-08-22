package fr.mkadia.mkadiaapi.services.verification;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import fr.mkadia.mkadiaapi.exceptions.VerificationException;
import fr.mkadia.mkadiaapi.models.CodeOTP;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerificationService {

    @Value("${twilio.service.friendly_name}")
    private String friendlyName;
    @Value("${twilio.service.ssid}")
    private String serviceSid;


    public ResponseMessage sendVerification(String to){
        try {
            Verification verification = Verification.creator(serviceSid, to, "sms")
                    .create();
            log.info("Send AuthMSG has Successfully");
        }catch (Exception exception){
            throw new VerificationException(exception.getMessage());
        }
        return ResponseMessage.builder().message("has been Send Verification Code").status(HttpStatus.OK.value()).build();
    }

    public ResponseMessage checkVerification(CodeOTP codeOTP){

        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid)
                    .setCode(codeOTP.getCode())
                    .setTo(codeOTP.getTo())
                    .create();
            if (verificationCheck.getValid())
                return ResponseMessage.builder().message("Code Otp has been Verified").status(HttpStatus.OK.value()).build();
            throw new VerificationException("Code Otp has not verified");
        }catch (Exception e){
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new VerificationException(e.getMessage());
        }

    }
}
