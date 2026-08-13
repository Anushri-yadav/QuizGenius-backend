package com.QuizGen.Master.controller;

import com.QuizGen.Master.model.QuizResponse;
import com.QuizGen.Master.service.QuizService;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "https://quiz-genius-frontend-gamma.vercel.app/")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/generate-from-pdf")
    public ResponseEntity<?> generateQuizFromPdf(
            @RequestParam("file") MultipartFile file) {

        try {

            // 1. Check if file exists
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Please upload a PDF file.");
            }

            // 2. Check file extension
            String filename = file.getOriginalFilename();

            if (filename == null ||
                    !filename.toLowerCase().endsWith(".pdf")) {

                return ResponseEntity.badRequest()
                        .body("Only PDF files are allowed.");
            }

            // 3. Read PDF
            byte[] pdfBytes = file.getBytes();

            try (PDDocument document = Loader.loadPDF(pdfBytes)) {

                // 4. Extract text from PDF
                PDFTextStripper stripper = new PDFTextStripper();

                String extractedText =
                        stripper.getText(document);

                // Print PDF text in terminal
                System.out.println("========== PDF TEXT ==========");
                System.out.println(extractedText);
                System.out.println("==============================");

                // 5. Check if PDF contains text
                if (extractedText == null ||
                        extractedText.trim().isEmpty()) {

                    return ResponseEntity.badRequest()
                            .body("Could not extract text from this PDF.");
                }

                // 6. Send PDF text to AI
                QuizResponse response =
                        quizService.generateQuiz(extractedText);

                // 7. Return generated quiz to React
                return ResponseEntity.ok(response);
            }

        } catch (IOException e) {

            return ResponseEntity.internalServerError()
                    .body("Error while reading PDF: "
                            + e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("Error while generating quiz: "
                            + e.getMessage());
        }
    }
}
