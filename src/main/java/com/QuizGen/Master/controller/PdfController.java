package com.QuizGen.Master.controller;


import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "http://localhost:5173")
public class PdfController {

    @PostMapping(
            value = "/extract",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String extractText(@RequestParam("file") MultipartFile file)
            throws IOException {

        if (file.isEmpty()) {
            return "Please upload a PDF file.";
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return "Only PDF files are allowed.";
        }

        byte[] pdfBytes = file.getBytes();

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {

            PDFTextStripper stripper = new PDFTextStripper();

            String text = stripper.getText(document);

            return text;
        }
    }

    @RestController
    @RequestMapping("/api/quiz")
    @CrossOrigin(origins = "http://localhost:5173")
    public static class QuizController {

        @PostMapping("/upload")
        public ResponseEntity<String> uploadPdf(
                @RequestParam("file") MultipartFile file) {

            try {

                if (file.isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body("Please upload a PDF file.");
                }

                if (!file.getOriginalFilename()
                        .toLowerCase()
                        .endsWith(".pdf")) {

                    return ResponseEntity.badRequest()
                            .body("Only PDF files are allowed.");
                }

                byte[] pdfBytes = file.getBytes();

                try (PDDocument document = Loader.loadPDF(pdfBytes)) {

                    PDFTextStripper pdfTextStripper =
                            new PDFTextStripper();

                    String extractedText =
                            pdfTextStripper.getText(document);

                    System.out.println("========== PDF TEXT ==========");
                    System.out.println(extractedText);
                    System.out.println("==============================");

                    return ResponseEntity.ok(
                            "PDF uploaded successfully!\n\n"
                                    + extractedText
                    );
                }

            } catch (IOException e) {

                return ResponseEntity.internalServerError()
                        .body("Error while reading PDF: " + e.getMessage());
            }
        }
    }
}


