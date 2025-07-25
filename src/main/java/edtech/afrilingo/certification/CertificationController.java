package edtech.afrilingo.certification;

import edtech.afrilingo.dto.ApiResponse;
import edtech.afrilingo.question.Question;
import edtech.afrilingo.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certification")
@RequiredArgsConstructor
@Tag(name = "Certification", description = "Automated Language Certification System")
public class CertificationController {
    
    private final CertificationService certificationService;
    
    @Operation(summary = "Initiate certification session", description = "Start a new certification test session")
    @PostMapping("/sessions/initiate")
    public ResponseEntity<ApiResponse<CertificationSession>> initiateCertification(
            @RequestParam String languageCode,
            @RequestParam String testLevel) {
        
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        try {
            CertificationSession session = certificationService.initiateCertificationSession(
                    currentUser, languageCode, testLevel);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(session, "Certification session initiated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Verify test environment", description = "Verify camera and environment setup")
    @PostMapping("/sessions/{sessionId}/verify-environment")
    public ResponseEntity<ApiResponse<String>> verifyEnvironment(
            @PathVariable Long sessionId,
            @RequestParam boolean cameraVerified,
            @RequestParam boolean environmentVerified) {
        
        try {
            certificationService.verifyTestEnvironment(sessionId, cameraVerified, environmentVerified);
            return ResponseEntity.ok(ApiResponse.success("Environment verified successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Get certification questions", description = "Retrieve questions for certification test")
    @GetMapping("/sessions/{sessionId}/questions")
    public ResponseEntity<ApiResponse<List<Question>>> getCertificationQuestions(@PathVariable Long sessionId) {
        try {
            List<Question> questions = certificationService.getCertificationQuestions(sessionId);
            return ResponseEntity.ok(ApiResponse.success(questions));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Submit answer", description = "Submit answer for a certification question")
    @PostMapping("/sessions/{sessionId}/answers")
    public ResponseEntity<ApiResponse<String>> submitAnswer(
            @PathVariable Long sessionId,
            @RequestBody AnswerSubmissionRequest request) {
        
        try {
            certificationService.recordAnswer(sessionId, request.getQuestionId(), 
                    request.getSelectedOptionId(), request.getTimeSpentMs());
            
            return ResponseEntity.ok(ApiResponse.success("Answer recorded successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Complete certification", description = "Complete certification and generate certificate")
    @PostMapping("/sessions/{sessionId}/complete")
    public ResponseEntity<ApiResponse<Certificate>> completeCertification(@PathVariable Long sessionId) {
        try {
            Certificate certificate = certificationService.completeSessionAndGenerateCertificate(sessionId);
            
            if (certificate != null) {
                return ResponseEntity.ok(ApiResponse.success(certificate, "Certification completed successfully"));
            } else {
                return ResponseEntity.ok(ApiResponse.success(null, "Certification completed but certificate not issued due to policy violations"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Record proctoring event", description = "Record suspicious activity during test")
    @PostMapping("/sessions/{sessionId}/proctor-events")
    public ResponseEntity<ApiResponse<String>> recordProctorEvent(
            @PathVariable Long sessionId,
            @RequestBody ProctorEventRequest request) {
        
        try {
            CertificationSession session = certificationService.getSessionById(sessionId);
            certificationService.recordProctorEvent(session, request.getEventType(), 
                    request.getDescription(), request.getConfidenceScore());
            
            return ResponseEntity.ok(ApiResponse.success("Proctor event recorded"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Get user certificates", description = "Get all certificates for current user")
    @GetMapping("/certificates")
    public ResponseEntity<ApiResponse<List<Certificate>>> getUserCertificates() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        List<Certificate> certificates = certificationService.getUserCertificates(currentUser);
        return ResponseEntity.ok(ApiResponse.success(certificates));
    }
    
    @Operation(summary = "Verify certificate", description = "Verify certificate by ID")
    @GetMapping("/certificates/{certificateId}/verify")
    public ResponseEntity<ApiResponse<Certificate>> verifyCertificate(@PathVariable String certificateId) {
        try {
            Certificate certificate = certificationService.verifyCertificate(certificateId);
            return ResponseEntity.ok(ApiResponse.success(certificate, "Certificate verified"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Certificate not found or invalid"));
        }
    }
}