package edtech.afrilingo.certification;

import lombok.Data;

@Data
public class AnswerSubmissionRequest {
    private Long questionId;
    private Long selectedOptionId;
    private long timeSpentMs;
}
