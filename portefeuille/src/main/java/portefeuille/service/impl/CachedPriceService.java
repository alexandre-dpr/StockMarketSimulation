package portefeuille.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portefeuille.service.IPriceService;

import java.util.HashMap;
import java.util.Map;

@Service
public class CachedPriceService implements IPriceService {

    @Autowired
    private DirectPriceService directPriceService;

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
