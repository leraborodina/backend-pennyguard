package ru.itcolleg.transaction.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(PdfParser.class);

    @Autowired
    public PdfParser(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Parses transactions from the input stream of a PDF document.
     * Разбирает транзакции из потока ввода PDF-документа.
     *
     * @param inputStream The input stream of the PDF document.
     * @return List of TransactionDTO objects parsed from the PDF document.
     * @throws IOException If an I/O error occurs.
     */
    public List<TransactionDTO> parseTransactions(InputStream inputStream) throws IOException {
        logger.info("Начало разбора транзакций из потока ввода PDF-документа.");
        List<TransactionDTO> transactions = new ArrayList<>();
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            text = text.replace('\u00A0', ' ');
            transactions = parseTransactions(text);
        } finally {
            logger.info("Завершение разбора транзакций из потока ввода PDF-документа.");
        }
        return transactions;
    }

    /**
     * Parses transactions from the provided text.
     * Разбирает транзакции из предоставленного текста.
     *
     * @param text The text containing transaction information.
     * @return List of TransactionDTO objects parsed from the text.
     */
    public List<TransactionDTO> parseTransactions(String text) {
        logger.info("Начало разбора транзакций из предоставленного текста.");
        List<TransactionDTO> transactions = new ArrayList<>();
        String dateRegex = "(\\d{2}\\.\\d{2}\\.\\d{4})\\s+.*?";
        String amount1Regex = "([-+]?\\s*\\d{1,3}(?:[\\s.,]\\d{3})*(?:[,.]\\d{2})?)\\s*RUB\\s*.*?";
        String amount2Regex = "([-+]?\\s*\\d{1,3}(?:[\\s.,]\\d{3})*(?:[,.]\\d{2})?)\\s*RUB";
        String descriptionRegex = "([^\\d.]+(?:\\s*[*].*?)?)";

        Pattern pattern = Pattern.compile(dateRegex + amount1Regex + amount2Regex + descriptionRegex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            LocalDate date = LocalDate.parse(matcher.group(1), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String amountStr = matcher.group(2);
            String sign = amountStr.contains("+") ? "+" : "-";
            Double amount = Double.parseDouble(amountStr.replaceAll("\\s", "").replace(",", "."));
            String details = matcher.group(4).trim();

            Long categoryId = findCategory(details);

            TransactionDTO transaction = new TransactionDTO();
            transaction.setAmount(amount);
            transaction.setRegular(false);
            transaction.setCategoryId(categoryId);
            transaction.setTypeId(sign.equals("-") ? 1L : 2L);

            transactions.add(transaction);
        }

        logger.info("Завершение разбора транзакций из предоставленного текста.");
        return transactions;
    }

    /**
     * Finds the category for a transaction based on keywords in the transaction string.
     * Находит категорию для транзакции на основе ключевых слов в строке транзакции.
     *
     * @param transactionStr The transaction string.
     * @return The category ID.
     */
    private Long findCategory(String transactionStr) {
        Map<String, String[]> categoryKeywords = new HashMap<>();
        categoryKeywords.put("Еда", new String[]{"ПРОДУКТЫ", "КОФЕ", "COFFEE", "РЕСТОРАН", "ЕДА", "ПИТАНИЕ", "ФУД", "ПЕКАРНЯ", "ВКУСНО"});
        categoryKeywords.put("Путешествия", new String[]{"ПУТЕШЕСТВИЕ", "БИЛЕТ", "АВИА", "ПОЕЗДКА", "ОТПУСК"});
        categoryKeywords.put("Здоровье", new String[]{"ДОКТОР", "ЗДРАВ", "БОЛЬНИЦА", "ЛЕЧЕНИЕ", "МЕДИЦИНА", "ЗДОРОВЬЕ", "АПТЕКА", "ХЕЛС"});
        categoryKeywords.put("Красота", new String[]{"КОСМЕТИК", "МАГНИТ", "САЛОН", "КРАСОТА", "УХОД", "МАКИЯЖ", "ПАРИКМАХЕР", "БЬЮТИ", "ПАРФЮМ"});
        categoryKeywords.put("Транспорт", new String[]{"ТРАНСПОРТ", "ТАКСИ", "АВТОБУС", "МЕТРО", "ТРАНСП"});
        categoryKeywords.put("Развлечения", new String[]{"КИНО", "ТЕАТР", "КЛУБ", "КОНЦЕРТ", "ИГРА", "РАЗВЛЕЧЕНИЕ"});
        categoryKeywords.put("Покупки", new String[]{"МАГАЗИН", "ПОКУПКА", "ТОВАР", "СКИДКА", "ПОКУПОК", "ПЛАТИТЬ", "ШОППИНГ"});
        categoryKeywords.put("Образование", new String[]{"ШКОЛА", "УНИВЕРСИТЕТ", "ОБУЧЕНИЕ", "КУРС", "УЧЕБА", "УЧИТЬ", "ОБРАЗОВАНИЕ"});
        categoryKeywords.put("Банк", new String[]{"СЧЕТ", "КРЕДИТ"});
        categoryKeywords.put("Персональное", new String[]{"ЛИЧНЫЙ", "ПЕРЕВОД", "СБП", "ПЕРЕВЕЛИ", "ПОЛУЧАТЕЛЬ", "ПЕРСОНАЛЬНЫЙ"});

        for (Map.Entry<String, String[]> entry : categoryKeywords.entrySet()) {
            String categoryName = entry.getKey();
            String[] keywords = entry.getValue();
            for (String keyword : keywords) {
                if (containsKeyword(transactionStr.toLowerCase(), keyword.toLowerCase())) {
                    return findCategoryInDatabase(categoryName);
                }
            }
        }
        return findCategoryInDatabase("Other");
    }

    /**
     * Checks if a string contains a keyword.
     * Проверяет, содержит ли строка ключевое слово.
     *
     * @param transactionStr The string to check.
     * @param keyword        The keyword to search for.
     * @return True if the string contains the keyword, otherwise false.
     */
    static boolean containsKeyword(String transactionStr, String keyword) {
        return transactionStr.contains(keyword);
    }

    /**
     * Finds a category in the database based on its name.
     * Находит категорию в базе данных по ее имени.
     *
     * @param categoryName The name of the category.
     * @return The ID of the category if found, otherwise the default category ID.
     */
    private Long findCategoryInDatabase(String categoryName) {
        Category category = categoryRepository.findByNameContainingIgnoreCase(categoryName).orElse(null);
        return category != null ? category.getId() : getDefaultCategoryId();
    }

    /**
     * Retrieves the default category ID.
     * Возвращает идентификатор категории по умолчанию.
     *
     * @return The default category ID.
     */
    private Long getDefaultCategoryId() {
        return 0L;
    }
}
