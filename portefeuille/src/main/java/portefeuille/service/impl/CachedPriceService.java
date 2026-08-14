package portefeuille.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import portefeuille.service.IPriceService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CachedPriceService implements IPriceService {

    private final DirectPriceService directPriceService;

    Map<String, Double> cache = new HashMap<>();

    @Override
    public double getPrice(String ticker) throws InterruptedException {
        if (!cache.containsKey(ticker)) {
            double price = directPriceService.getPrice(ticker);
            cache.put(ticker, price);
            return price;
        } else {
            return cache.get(ticker);
        }
    }

    public void clearCache() {
        cache.clear();
    }
}
