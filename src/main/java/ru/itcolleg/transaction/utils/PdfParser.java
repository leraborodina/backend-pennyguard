package ru.itcolleg.transaction.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.repository.CategoryRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class PdfParser {

    private final CategoryRepository categoryRepository;

    private List<Category> categories;

    @Autowired
    public PdfParser(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<TransactionDTO> parseTransactions(InputStream inputStream) throws IOException {
        List<TransactionDTO> transactions = new ArrayList<>();

        // Load PDF document from InputStream
        PDDocument document = PDDocument.load(inputStream);

        // Create PDFTextStripper
        PDFTextStripper stripper = new PDFTextStripper();

        // Get text from PDF
        String text = stripper.getText(document);
        text = text.replace('\u00A0', ' ');

        transactions = parseTransactions(text);

        // Close the document
        document.close();

        return transactions;
    }

    public List<TransactionDTO> parseTransactions(String text) {
        List<TransactionDTO> transactions = new ArrayList<>();
        // Define the pattern to match dates, amounts, and signs
        //Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})\\s+.*?([+-]?\\d{1,3}(?:\\s*\\d{1,3})*(?:[,.]\\d{2})?)\\s*RUB.*?([+-]?\\d{1,3}(?:\\s*\\d{1,3})*(?:[,.]\\d{2})?)\\s*RUB");
        String dateRegex = "(\\d{2}\\.\\d{2}\\.\\d{4})\\s+.*?"; // Part 1: Date
        String amount1Regex = "([-+]?\\s*\\d{1,3}(?:[\\s.,]\\d{3})*(?:[,.]\\d{2})?)\\s*RUB\\s*.*?";
        String amount2Regex = "([-+]?\\s*\\d{1,3}(?:[\\s.,]\\d{3})*(?:[,.]\\d{2})?)\\s*RUB"; // Part 3: Amount 2
        String descriptionRegex = "([^\\d.]+(?:\\s*[*].*?)?)";

        Pattern pattern = Pattern.compile(dateRegex + amount1Regex + amount2Regex + descriptionRegex);

        // Create a matcher to find the pattern in the text
        Matcher matcher = pattern.matcher(text);


        // Iterate through matches and create transactions
        while (matcher.find()) {
            LocalDate date = LocalDate.parse(matcher.group(1), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            String amountStr = matcher.group(2);
            String sign = amountStr.contains("+") ? "+" : "-";
            Double amount = Double.parseDouble(matcher.group(2).replaceAll("\\s", "").replace(",", "."));
            String details = matcher.group(4).trim();

            // Find category for the transaction details
            Long categoryId = findCategory(details);

            // Create a Transaction object and add it to the list
            TransactionDTO transaction = new TransactionDTO();
            //transaction.setCreatedAt(date);
            transaction.setAmount(amount);
            transaction.setRegular(false);
            transaction.setCategoryId(categoryId);
            transaction.setTypeId(sign.equals("-") ? 1L : 2L);

            transactions.add(transaction);
        }

        for (TransactionDTO transactionDTO : transactions) {
            System.out.println(transactionDTO);
        }
        return transactions;
    }

    private Long findCategory(String transactionStr) {
        // Define categories and their associated keywords
        Map<String, String[]> categoryKeywords = new HashMap<>();
        categoryKeywords.put("Food", new String[]{"ПРОДУКТЫ", "КОФЕ", "COFFEE", "РЕСТОРАН", "ЕДА", "ПИТАНИЕ", "ФУД", "ПЕКАРНЯ", "ВКУСНО"});
        categoryKeywords.put("Travel", new String[]{"ПУТЕШЕСТВИЕ", "БИЛЕТ", "АВИА", "ПОЕЗДКА", "ОТПУСК"});
        categoryKeywords.put("Health", new String[]{"ДОКТОР", "ЗДРАВ", "БОЛЬНИЦА", "ЛЕЧЕНИЕ", "МЕДИЦИНА", "ЗДОРОВЬЕ", "АПТЕКА", "ХЕЛС"});
        categoryKeywords.put("Beauty", new String[]{"КОСМЕТИК", "МАГНИТ", "САЛОН", "КРАСОТА", "УХОД", "МАКИЯЖ", "ПАРИКМАХЕР", "БЬЮТИ", "ПАРФЮМ"});
        categoryKeywords.put("Transport", new String[]{"ТРАНСПОРТ", "ТАКСИ", "АВТОБУС", "МЕТРО", "ТРАНСП"});
        categoryKeywords.put("Entertainment", new String[]{"КИНО", "ТЕАТР", "КЛУБ", "КОНЦЕРТ", "ИГРА", "РАЗВЛЕЧЕНИЕ"});
        categoryKeywords.put("Shopping", new String[]{"МАГАЗИН", "ПОКУПКА", "ТОВАР", "СКИДКА", "ПОКУПОК", "ПЛАТИТЬ", "ШОППИНГ"});
        categoryKeywords.put("Education", new String[]{"ШКОЛА", "УНИВЕРСИТЕТ", "ОБУЧЕНИЕ", "КУРС", "УЧЕБА", "УЧИТЬ", "ОБРАЗОВАНИЕ"});
        categoryKeywords.put("Finance", new String[]{"СЧЕТ", "КРЕДИТ"});
        categoryKeywords.put("Personal", new String[]{"ЛИЧНЫЙ", "ПЕРЕВОД", "СБП", "ПЕРЕВЕЛИ", "ПОЛУЧАТЕЛЬ", "ПЕРСОНАЛЬНЫЙ"});

        // Iterate through category keywords
        for (Map.Entry<String, String[]> entry : categoryKeywords.entrySet()) {
            String categoryName = entry.getKey();
            String[] keywords = entry.getValue();
            // Check if any keyword is present in the transaction string
            for (String keyword : keywords) {
                if (containsKeyword(transactionStr.toLowerCase(), keyword.toLowerCase())) {
                    // Find the category ID in the database based on the category name
                    return findCategoryInDatabase(categoryName);
                }
            }
        }

        // If no specific category is identified, return a default category
        return findCategoryInDatabase("Other");
    }

    static boolean containsKeyword(String transactionStr, String keyword) {
        return transactionStr.contains(keyword);
    }

    private Long findCategoryInDatabase(String categoryName) {
        // Search for the category in the database by its value
        Category category = categoryRepository.findByNameContainingIgnoreCase(categoryName).orElse(null);
        if (category != null) {
            return category.getId();
        } else {
            // If category is not found, return a default value or handle accordingly
            return getDefaultCategoryId();
        }
    }

    private Long getDefaultCategoryId() {
        return 0L; // Example: Return category ID 0 as the default
    }
}
