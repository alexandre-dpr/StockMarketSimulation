package portefeuille.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import portefeuille.service.impl.CachedPriceService;
import portefeuille.service.impl.DirectPriceService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CachedPriceServiceTest {

    @Mock
    private DirectPriceService directPriceService;


    @InjectMocks
    private CachedPriceService service;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getPrice_OK_noCache() throws InterruptedException {
        String ticker = "ticker";
        when(directPriceService.getPrice(ticker)).thenReturn(10.);
        double price = service.getPrice(ticker);
        assertEquals(price, 10., 0);
    }

    @Test
    public void getPrice_OK_cache_vide() throws InterruptedException {
        String ticker = "ticker";
        double price = 10.;
        when(directPriceService.getPrice(ticker)).thenReturn(10.);
        double result = service.getPrice(ticker);
        assertEquals(price, result, 0);
    }

    @Test
    public void getPrice_OK_cache_rempli() throws InterruptedException {
        String ticker = "ticker";
        double price = 10.;
        when(directPriceService.getPrice(ticker)).thenReturn(10.);
        service.getPrice(ticker);
        double result = service.getPrice(ticker);
        assertEquals(price, result, 0);
    }
}