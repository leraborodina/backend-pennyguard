package ru.itcolleg.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.service.TransactionService;
import ru.itcolleg.transaction.utils.PdfParser;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final TransactionService transactionService;

    private final TokenService tokenService;

    private final PdfParser pdfParser;

    @Autowired
    public FileUploadController(TransactionService transactionService, TokenService tokenService, PdfParser pdfParser) {
        this.transactionService = transactionService;
        this.tokenService = tokenService;
        this.pdfParser = pdfParser;
    }

    @PostMapping("/pdf")
    public ResponseEntity<?> uploadPDF(@RequestParam("pdfFile") MultipartFile pdfFile, @RequestHeader("Authorization") String token) {
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to upload transactions");
        }

        try {
            Long userId = tokenService.extractUserIdFromToken(token);

            // Parse PDF file to extract transaction data
            List<TransactionDTO> transactions = pdfParser.parseTransactions(pdfFile.getInputStream());

            // Save each transaction
            for (TransactionDTO transaction : transactions) {
                transactionService.saveTransaction(transaction, userId);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload transactions: " + e.getMessage());
        }
    }

}

