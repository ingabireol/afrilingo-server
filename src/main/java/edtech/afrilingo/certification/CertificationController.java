package edtech.afrilingo.certification;

import edtech.afrilingo.certification.dto.CertificateResponseDTO;
import edtech.afrilingo.dto.ApiResponse;
import edtech.afrilingo.question.Question;
import edtech.afrilingo.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    
    @Operation(summary = "Get certification session", description = "Get session details by ID")
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<ApiResponse<CertificationSession>> getSession(@PathVariable Long sessionId) {
        try {
            CertificationSession session = certificationService.getSessionById(sessionId);
            return ResponseEntity.ok(ApiResponse.success(session));
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
                    request.getSelectedOptionId(), request.getTextAnswer(), request.getScore(), request.getTimeSpentMs());
            
            return ResponseEntity.ok(ApiResponse.success("Answer recorded successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Complete certification", description = "Complete certification and generate certificate")
    @PostMapping("/sessions/{sessionId}/complete")
    public ResponseEntity<ApiResponse<CertificateResponseDTO>> completeCertification(@PathVariable Long sessionId) {
        try {
            CertificateResponseDTO certificate = certificationService.completeSessionAndGenerateCertificate(sessionId);
            
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

    // New: Retrieve proctor events for a specific session
    @Operation(summary = "Get proctor events for session", description = "Retrieve all proctor events for a certification session")
    @GetMapping("/sessions/{sessionId}/proctor-events")
    public ResponseEntity<ApiResponse<List<ProctorEvent>>> getProctorEventsForSession(@PathVariable Long sessionId) {
        try {
            List<ProctorEvent> events = certificationService.getProctorEventsBySessionId(sessionId);
            return ResponseEntity.ok(ApiResponse.success(events));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    // New: Retrieve proctor events for a specific user
    @Operation(summary = "Get proctor events for user", description = "Retrieve all proctor events across certification sessions for a specific user")
    @GetMapping("/users/{userId}/proctor-events")
    public ResponseEntity<ApiResponse<List<ProctorEvent>>> getProctorEventsForUser(@PathVariable Long userId) {
        try {
            List<ProctorEvent> events = certificationService.getProctorEventsByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(events));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    // New: Retrieve proctor events across all users
    @Operation(summary = "Get proctor events for all users", description = "Retrieve all proctor events across all certification sessions and users")
    @GetMapping("/proctor-events")
    public ResponseEntity<ApiResponse<List<ProctorEvent>>> getAllProctorEvents() {
        try {
            List<ProctorEvent> events = certificationService.getAllProctorEvents();
            return ResponseEntity.ok(ApiResponse.success(events));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Get user certificates", description = "Get all certificates for current user")
    @GetMapping("/certificates")
    public ResponseEntity<ApiResponse<List<Certificate>>> getUserCertificates() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        List<Certificate> certificates = certificationService.getUserCertificates(currentUser);
        return ResponseEntity.ok(ApiResponse.success(certificates));
    }

    // New: Retrieve all certificates across all users
    @Operation(summary = "Get all certificates", description = "Retrieve all issued certificates across all users")
    @GetMapping("/certificates/all")
    public ResponseEntity<ApiResponse<List<Certificate>>> getAllCertificates() {
        List<Certificate> certificates = certificationService.getAllCertificates();
        return ResponseEntity.ok(ApiResponse.success(certificates));
    }
    
    @Operation(summary = "Verify certificate", description = "Verify certificate by ID")
    @GetMapping("/certificates/{certificateId}/verify")
    public ResponseEntity<ApiResponse<Certificate>> verifyCertificate(@PathVariable String certificateId) {
        try {
            Certificate certificate = certificationService.getCertificateById(certificateId);
            return ResponseEntity.ok(ApiResponse.success(certificate));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Clear ongoing sessions", description = "Clear ongoing certification sessions for testing (ADMIN only)")
    @DeleteMapping("/sessions/clear")
    public ResponseEntity<ApiResponse<String>> clearOngoingSessions() {
        try {
            certificationService.clearOngoingSessions();
            return ResponseEntity.ok(ApiResponse.success("All ongoing sessions cleared successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    @Operation(summary = "Download certificate", description = "Download certificate PDF file")
    @GetMapping("/certificates/download/{fileName}")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable String fileName) {
        try {
            // Get the configured certificate storage path from the PDF service
            String storagePath = certificationService.getCertificateStoragePath();
            String filePath = storagePath + "/" + fileName;
            Path path = Paths.get(filePath);
            
            // Log the file path for debugging
            System.out.println("Attempting to download certificate from path: " + filePath);
            
            if (!Files.exists(path)) {
                System.out.println("Certificate file not found at path: " + filePath);
                return ResponseEntity.notFound().build();
            }
            
            // Create resource from file
            Resource resource = new FileSystemResource(path.toFile());
            
            // Set headers for file download
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .header("Content-Type", "application/pdf")
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("Expires", "0")
                    .body(resource);
                    
        } catch (Exception e) {
            System.out.println("Error downloading certificate: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Helper endpoint to check if a certificate file exists
    @Operation(summary = "Check certificate file", description = "Check if certificate PDF file exists")
    @GetMapping("/certificates/check/{fileName}")
    public ResponseEntity<ApiResponse<String>> checkCertificateFile(@PathVariable String fileName) {
        try {
            // Get the configured certificate storage path from the PDF service
            String storagePath = certificationService.getCertificateStoragePath();
            String filePath = storagePath + "/" + fileName;
            Path path = Paths.get(filePath);
            
            if (Files.exists(path)) {
                long fileSize = Files.size(path);
                return ResponseEntity.ok(ApiResponse.success(
                    "Certificate file exists with size: " + fileSize + " bytes"));
            } else {
                return ResponseEntity.ok(ApiResponse.error(404, "Certificate file not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "Error checking certificate: " + e.getMessage()));
        }
    }
}