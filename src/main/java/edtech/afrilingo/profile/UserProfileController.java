package edtech.afrilingo.profile;

import edtech.afrilingo.dto.ApiResponse;
import edtech.afrilingo.exception.ResourceNotFoundException;
import edtech.afrilingo.language.Language;
import edtech.afrilingo.language.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import edtech.afrilingo.user.User;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Endpoints for user profile management")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final LanguageService languageService;

    @Operation(summary = "Get current user's profile", description = "Returns the profile of the currently authenticated user")
    @GetMapping
    public ResponseEntity<ApiResponse<UserProfile>> getCurrentUserProfile() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        UserProfile userProfile = userProfileService.getUserProfileByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userId", currentUser.getId()));
        
        return ResponseEntity.ok(ApiResponse.success(userProfile));
    }

    @Operation(summary = "Check if user has profile", description = "Checks if the current user has already set up a profile")
    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> checkUserProfileExists() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasProfile = userProfileService.hasUserProfile(currentUser.getId());
        
        return ResponseEntity.ok(ApiResponse.success(hasProfile));
    }

    @Operation(summary = "Create or update user profile", description = "Creates a new profile or updates existing profile for the current user")
    @PostMapping
    public ResponseEntity<ApiResponse<UserProfile>> createOrUpdateUserProfile(@RequestBody UserProfileRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        UserProfile profile = new UserProfile();
        profile.setCountry(request.getCountry());
        profile.setFirstLanguage(request.getFirstLanguage());
        profile.setReasonToLearn(request.getReasonToLearn());
        profile.setProfilePicture(request.getProfilePicture());
        
        if (request.getDailyReminders() != null) {
            profile.setDailyReminders(request.getDailyReminders());
        }
        
        if (request.getDailyGoalMinutes() != null) {
            profile.setDailyGoalMinutes(request.getDailyGoalMinutes());
        }
        
        if (request.getPreferredLearningTime() != null) {
            profile.setPreferredLearningTime(request.getPreferredLearningTime());
        }
        
        // Set languages to learn if provided
        if (request.getLanguagesToLearnIds() != null && !request.getLanguagesToLearnIds().isEmpty()) {
            List<Language> languages = request.getLanguagesToLearnIds().stream()
                    .map(id -> languageService.getLanguageById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Language", "id", id)))
                    .collect(Collectors.toList());
            
            profile.setLanguagesToLearn(languages);
        }
        
        UserProfile savedProfile = userProfileService.createOrUpdateUserProfile(currentUser.getId(), profile);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedProfile, "User profile created/updated successfully"));
    }

    @Operation(summary = "Update languages to learn", description = "Updates the list of languages the user wants to learn")
    @PutMapping("/languages")
    public ResponseEntity<ApiResponse<UserProfile>> updateLanguagesToLearn(@RequestBody List<Long> languageIds) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        UserProfile updatedProfile = userProfileService.updateLanguagesToLearn(currentUser.getId(), languageIds);
        
        return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Languages to learn updated successfully"));
    }

    @Operation(summary = "Update learning preferences", description = "Updates the user's learning preferences")
    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<UserProfile>> updateLearningPreferences(
            @RequestParam(required = false, defaultValue = "false") boolean dailyReminders,
            @RequestParam(required = false, defaultValue = "15") int dailyGoalMinutes,
            @RequestParam(required = false) String preferredLearningTime) {
        
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        UserProfile updatedProfile = userProfileService.updateLearningPreferences(
                currentUser.getId(), dailyReminders, dailyGoalMinutes, preferredLearningTime);
        
        return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Learning preferences updated successfully"));
    }

    @Operation(summary = "Update profile picture", description = "Updates the user's profile picture")
    @PutMapping("/picture")
    public ResponseEntity<ApiResponse<UserProfile>> updateProfilePicture(@RequestBody String profilePicture) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        UserProfile updatedProfile = userProfileService.updateProfilePicture(currentUser.getId(), profilePicture);
        
        return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Profile picture updated successfully"));
    }

    @Operation(summary = "Delete user profile", description = "Deletes the current user's profile")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUserProfile() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        UserProfile userProfile = userProfileService.getUserProfileByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", "userId", currentUser.getId()));
        
        userProfileService.deleteUserProfile(userProfile.getId());
        
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(200)
                        .message("User profile deleted successfully")
                        .build()
        );
    }
}