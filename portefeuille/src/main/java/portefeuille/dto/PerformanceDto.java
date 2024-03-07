package portefeuille.dto;

import lombok.Getter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Getter
public class PerformanceDto {

    public PerformanceDto(String percentage, double value) {
        this.percentage = percentage;
        this.value = value;
    }

    private final String percentage;

    private final double value;

    public static PerformanceDto createPerformanceDto(double prixAchat, double prixActuel) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00%", DecimalFormatSymbols.getInstance(Locale.US));
        return new PerformanceDto(
                decimalFormat.format(prixActuel / prixAchat),
                prixActuel - prixAchat
        );
    }
}
