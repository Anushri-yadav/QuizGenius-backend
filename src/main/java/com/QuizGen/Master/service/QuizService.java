package com.QuizGen.Master.service;

import com.QuizGen.Master.model.QuizResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    private final ChatClient chatClient;

    public QuizService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public QuizResponse generateQuiz(String pdfText) {

        String prompt = """
                You are QuizGenius, an AI quiz generator.

                Your task is to create a multiple-choice quiz ONLY from
                the information contained in the provided PDF text.

                PDF TEXT:
                ----------------
                %s
                ----------------

                Generate exactly 5 questions.

                Each question must have:
                1. A clear question.
                2. Exactly 4 answer options.
                3. One correct answer.
                4. A short helpful hint.

                IMPORTANT RULES:

                - Do NOT use Markdown.
                - Do NOT write "Question 1:", "Question 2:", etc.
                - Do NOT write "A:", "B:", "C:", or "D:" inside the options.
                - Do NOT provide explanations outside the quiz.
                - Do NOT add any text before or after the quiz.
                - correctAnswer must be a number:
                  0 = first option
                  1 = second option
                  2 = third option
                  3 = fourth option.
                - Questions must be based ONLY on the PDF.
                - Do not invent information that is not present in the PDF.
                - Return the result as a QuizResponse object.

                Example structure:

                {
                  "title": "Quiz",
                  "questions": [
                    {
                      "question": "What is ...?",
                      "options": [
                        "Option 1",
                        "Option 2",
                        "Option 3",
                        "Option 4"
                      ],
                      "correctAnswer": 0,
                      "hint": "Think about ..."
                    }
                  ]
                }
                """.formatted(pdfText);

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .entity(
                        QuizResponse.class,
                        spec -> spec.useProviderStructuredOutput()
                );
    }
}