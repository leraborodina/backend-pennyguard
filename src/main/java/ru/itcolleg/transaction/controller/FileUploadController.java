package ru.itcolleg.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itcolleg.auth.service.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.service.TransactionService;
import ru.itcolleg.transaction.utils.PdfParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final TokenService tokenService;

    private final PdfParser pdfParser;

    @Autowired
    public FileUploadController(TokenService tokenService, PdfParser pdfParser) {
        this.tokenService = tokenService;
        this.pdfParser = pdfParser;
    }

    @RequiresTokenValidation
    @PostMapping("/pdf")
    public ResponseEntity<?> uploadPDF(@RequestParam("pdfFile") MultipartFile pdfFile, @RequestHeader("Authorization") String token) {
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to upload transactions");
        }

        try {
            // Parse PDF file to extract transaction data
            List<TransactionDTO> transactions = pdfParser.parseTransactions(pdfFile.getInputStream());

            // Return a success response along with the list of transactions
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Transactions uploaded successfully");
            responseBody.put("transactions", transactions);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload transactions: " + e.getMessage());
        }
    }
}

