package portefeuille.service;

import org.springframework.stereotype.Service;
import portefeuille.dto.rabbitMq.TickerInfoDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class CacheService {
    static final private List<TickerInfoDto> cache = new ArrayList<>();

    public void addTickerInfo(TickerInfoDto tickerInfoDto) {
        cache.add(tickerInfoDto);
    }

    public TickerInfoDto getTickerInfo(String uuid) {
        return cache.stream().filter(tickerInfoDto -> uuid.equals(tickerInfoDto.uuid())).findAny().get();
    }

    public void removeTickerInfo(String uuid) {
        cache.removeIf(tickerInfoDto -> uuid.equals(tickerInfoDto.uuid()));
    }

    public boolean isPresentTickerInfo(String uuid) {
        return cache.stream().anyMatch(tickerInfoDto -> uuid.equals(tickerInfoDto.uuid()));
    }
}
