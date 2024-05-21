package ru.itcolleg.transaction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itcolleg.auth.aspect.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.utils.PdfParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for uploading PDF files and extracting transaction data.
 * Контроллер REST для загрузки PDF-файлов и извлечения данных о транзакциях.
 */
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final TokenService tokenService;
    private final PdfParser pdfParser;

    @Autowired
    public FileUploadController(TokenService tokenService, PdfParser pdfParser) {
        this.tokenService = tokenService;
        this.pdfParser = pdfParser;
    }

    /**
     * Handles PDF file upload and extracts transaction data.
     * Обрабатывает загрузку PDF-файла и извлекает данные о транзакциях.
     *
     * @param pdfFile The PDF file to upload. PDF-файл для загрузки.
     * @param token   The authorization token. Токен авторизации.
     * @return ResponseEntity containing the extracted transactions if successful, otherwise returns an error message.
     * ResponseEntity, содержащий извлеченные транзакции в случае успеха, в противном случае возвращает сообщение об ошибке.
     */
    @RequiresTokenValidation
    @PostMapping("/pdf")
    public ResponseEntity<?> uploadPDF(@RequestParam("pdfFile") MultipartFile pdfFile, @RequestHeader("Authorization") String token) {
        logger.info("Uploading PDF file");
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to upload transactions");
        }

        try {
            List<TransactionDTO> transactions = pdfParser.parseTransactions(pdfFile.getInputStream());
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Transactions uploaded successfully");
            responseBody.put("transactions", transactions);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            logger.error("Failed to upload transactions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload transactions: " + e.getMessage());
        }
    }
}
