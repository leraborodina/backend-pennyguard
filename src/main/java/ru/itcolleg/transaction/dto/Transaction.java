package ru.itcolleg.transaction.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transaction {
    private String date;
    private String time;
    private String description;
    private double amount;

    public Transaction(String date, String time, String description, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static List<Transaction> parseTransactions(String text) {
        List<Transaction> transactions = new ArrayList<>();
        // Define the pattern to match dates, amounts, and signs
        //Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})\\s+.*?([+-]?\\d{1,3}(?:\\s*\\d{1,3})*(?:[,.]\\d{2})?)\\s*RUB.*?([+-]?\\d{1,3}(?:\\s*\\d{1,3})*(?:[,.]\\d{2})?)\\s*RUB");
        String dateRegex = "(\\d{2}\\.\\d{2}\\.\\d{4})\\s+.*?"; // Part 1: Date
        String amount1Regex = "([-+]?\\s*\\d{1,3}(?:[\\s.,]\\d{3})*(?:[,.]\\d{2})?)\\s*RUB\\s*.*?";
        String amount2Regex = "([-+]?\\s*\\d{1,3}(?:[\\s.,]\\d{3})*(?:[,.]\\d{2})?)\\s*RUB"; // Part 3: Amount 2
        String descriptionRegex = "([^\\d.]+(?:\\s*[*].*?)?)";

        Pattern pattern = Pattern.compile(dateRegex + amount1Regex + amount2Regex + descriptionRegex);

        // Create a matcher to find the pattern in the text
        Matcher matcher = pattern.matcher(text);
        int count = 0;

        // Iterate through matches and create transactions
        while (matcher.find()) {
            String date = matcher.group(1);
            String amount1 = matcher.group(2).trim();
            String amount2 = matcher.group(3).trim();
            String details = matcher.group(4).trim();
            String transactionInfo = amount1 + " " + amount2 + " " + details;

            // Create a Transaction object and add it to the list
            System.out.println(date + " " + transactionInfo);
            count++;
        }
        System.out.println(count);
        return transactions;
    }
}
