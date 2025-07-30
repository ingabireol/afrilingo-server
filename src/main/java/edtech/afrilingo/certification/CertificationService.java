package edtech.afrilingo.certification;

import edtech.afrilingo.question.Question;
import edtech.afrilingo.question.QuestionService;
import edtech.afrilingo.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificationService {
    
    private final CertificationSessionRepository sessionRepository;
    private final CertificateRepository certificateRepository;
    private final ProctorEventRepository proctorEventRepository;
    private final CertificationQuestionResponseRepository responseRepository;
    private final QuestionService questionService;
    private final CertificatePDFService pdfService;
    private final ProctorAnalysisService proctorAnalysisService;
    
    @Transactional
    public CertificationSession initiateCertificationSession(User user, String languageCode, String testLevel) {
        // Validate user eligibility
        validateUserEligibility(user, languageCode);
        
        // Create session
        CertificationSession session = CertificationSession.builder()
                .sessionId(generateSessionId())
                .user(user)
                .languageCode(languageCode)
                .testLevel(testLevel)
                .startTime(LocalDateTime.now())
                .completed(false)
                .passed(false)
                .cameraVerified(false)
                .environmentVerified(false)
                .suspiciousActivityCount(0)
                .build();
        
        return sessionRepository.save(session);
    }
    
    @Transactional
    public void verifyTestEnvironment(Long sessionId, boolean cameraVerified, boolean environmentVerified) {
        CertificationSession session = getSessionById(sessionId);
        session.setCameraVerified(cameraVerified);
        session.setEnvironmentVerified(environmentVerified);
        
        // Log environment setup event
        recordProctorEvent(session, ProctorEventType.SESSION_START, 
                "Test environment verified. Camera: " + cameraVerified + ", Environment: " + environmentVerified);
        
        sessionRepository.save(session);
    }
    
    public List<Question> getCertificationQuestions(Long sessionId) {
        CertificationSession session = getSessionById(sessionId);
        
        // Get questions based on language and level
        List<Question> questions = questionService.getCertificationQuestions(
                session.getLanguageCode(), session.getTestLevel(), 50); // 50 questions for certification
        
        session.setTotalQuestions(questions.size());
        sessionRepository.save(session);
        
        return questions;
    }
    
    @Transactional
    public void recordAnswer(Long sessionId, Long questionId, Long selectedOptionId, long timeSpentMs) {
        CertificationSession session = getSessionById(sessionId);
        Question question = questionService.getQuestionById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        
        boolean isCorrect = questionService.isAnswerCorrect(questionId, selectedOptionId);
        
        CertificationQuestionResponse response = CertificationQuestionResponse.builder()
                .session(session)
                .question(question)
                .answeredAt(LocalDateTime.now())
                .timeSpentMs(timeSpentMs)
                .correct(isCorrect)
                .build();
        
        responseRepository.save(response);
        
        // Update session statistics
        if (isCorrect) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
        }
        
        sessionRepository.save(session);
    }
    
    @Transactional
    public Certificate completeSessionAndGenerateCertificate(Long sessionId) {
        CertificationSession session = getSessionById(sessionId);
        
        // Validate session completion
        if (session.isCompleted()) {
            throw new RuntimeException("Session already completed");
        }
        
        // Calculate final score
        int finalScore = calculateFinalScore(session);
        boolean passed = finalScore >= 70; // 70% pass threshold
        
        // Update session
        session.setCompleted(true);
        session.setPassed(passed);
        session.setFinalScore(finalScore);
        session.setEndTime(LocalDateTime.now());
        
        // Analyze proctoring data
        AnalysisResult proctorAnalysis = proctorAnalysisService.analyzeSession(session);
        
        // Generate certificate if passed and no major violations
        Certificate certificate = null;
        if (passed && proctorAnalysis.isCertifiable()) {
            certificate = generateCertificate(session, finalScore);
        }
        
        // Record completion event
        recordProctorEvent(session, ProctorEventType.SESSION_END, 
                "Session completed. Score: " + finalScore + "%, Passed: " + passed);
        
        sessionRepository.save(session);
        
        return certificate;
    }
    
    private Certificate generateCertificate(CertificationSession session, int finalScore) {
        String certificateId = generateCertificateId();
        String proficiencyLevel = determineProficiencyLevel(finalScore);
        
        Certificate certificate = Certificate.builder()
                .certificateId(certificateId)
                .user(session.getUser())
                .languageTested(session.getLanguageCode())
                .proficiencyLevel(proficiencyLevel)
                .finalScore(finalScore)
                .completedAt(session.getEndTime())
                .issuedAt(LocalDateTime.now())
                .verified(true)
                .session(session)
                .build();
        
        certificate = certificateRepository.save(certificate);
        
        // Generate PDF certificate
        String pdfUrl = pdfService.generateCertificatePDF(certificate);
        certificate.setCertificateUrl(pdfUrl);
        
        return certificateRepository.save(certificate);
    }
    
    public void recordProctorEvent(CertificationSession session, ProctorEventType eventType, 
                                   String description, double confidenceScore) {
        ProctorEvent event = ProctorEvent.builder()
                .session(session)
                .eventType(eventType)
                .description(description)
                .timestamp(LocalDateTime.now())
                .confidenceScore(confidenceScore)
                .flagged(confidenceScore > 0.8) // Flag high-confidence suspicious events
                .build();
        
        proctorEventRepository.save(event);
        
        // Update session suspicious activity count
        if (event.isFlagged()) {
            session.setSuspiciousActivityCount(session.getSuspiciousActivityCount() + 1);
            sessionRepository.save(session);
        }
    }
    
    public void recordProctorEvent(CertificationSession session, ProctorEventType eventType, String description) {
        recordProctorEvent(session, eventType, description, 0.0);
    }
    
    // Helper methods
    private void validateUserEligibility(User user, String languageCode) {
        // Check if user has completed prerequisite courses
        // Check if user has any ongoing certification sessions
        Optional<CertificationSession> ongoingSession = sessionRepository
                .findByUserAndCompletedFalse(user);
        
        if (ongoingSession.isPresent()) {
            throw new RuntimeException("User has an ongoing certification session");
        }
    }
    
    public CertificationSession getSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Certification session not found"));
    }
    
    private int calculateFinalScore(CertificationSession session) {
        if (session.getTotalQuestions() == 0) return 0;
        return (int) Math.round((double) session.getCorrectAnswers() / session.getTotalQuestions() * 100);
    }
    
    private String determineProficiencyLevel(int score) {
        if (score >= 90) return "ADVANCED";
        if (score >= 80) return "INTERMEDIATE";
        return "BEGINNER";
    }
    
    private String generateSessionId() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private String generateCertificateId() {
        return "AFL-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
    public List<Certificate> getUserCertificates(User user) {
        return certificateRepository.findByUserOrderByIssuedAtDesc(user);
    }
    
    public Certificate getCertificateById(String certificateId) {
        return certificateRepository.findByCertificateId(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
    }
    
    @Transactional
    public void clearOngoingSessions() {
        List<CertificationSession> ongoingSessions = sessionRepository.findByCompletedFalse();
        sessionRepository.deleteAll(ongoingSessions);
        log.info("Cleared {} ongoing certification sessions", ongoingSessions.size());
    }

}