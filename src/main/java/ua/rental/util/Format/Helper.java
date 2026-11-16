package ua.rental.util.Format;

class FormatHelper {

    private FormatHelper() {}

    static String formatMoney(double amount) {
        return String.format("%.2f грн", amount);
    }
}
