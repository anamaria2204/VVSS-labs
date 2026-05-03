package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Integration Step 2 - Scenario (1): V <--- S ---> R  (top-down breadth-first)
 *
 * S (ProductService) is integrated with the REAL V (ProductValidator).
 * R (Repository) is still mocked.
 * E (Product) is used as a real object.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceIntegrationStep2Test {

    @Mock
    private Repository<Integer, Product> productRepo;

    // Real ProductValidator - integrated in this step
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepo, new ProductValidator());
    }

    // --- Test 1: valid product passes real validation and is saved via mock repo ---
    @Test
    void addProduct_validProduct_savesViaRepo() {
        Product p = new Product(1, "Espresso", 10.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);
        when(productRepo.save(p)).thenReturn(p);

        productService.addProduct(p);

        verify(productRepo, times(1)).save(p);
    }

    // --- Test 2: product with invalid name rejected by real validator; repo.save never called ---
    @Test
    void addProduct_invalidName_throwsValidationException_repoNotCalled() {
        Product p = new Product(1, "ab", 10.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC); // name too short

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        verify(productRepo, never()).save(any());
    }

    // --- Test 3: product with invalid price rejected by real validator ---
    @Test
    void addProduct_invalidPrice_throwsValidationException_repoNotCalled() {
        Product p = new Product(2, "Limonada", 3.0, CategorieBautura.JUICE, TipBautura.WATER_BASED); // price too low

        assertThrows(ValidationException.class, () -> productService.addProduct(p));

        verify(productRepo, never()).save(any());
    }

    // --- Test 4: getAllProducts delegates to mock repo and returns its result ---
    @Test
    void getAllProducts_returnsAllFromMockRepo() {
        Product p1 = new Product(1, "Latte", 12.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        Product p2 = new Product(2, "Limonada", 8.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        when(productRepo.findAll()).thenReturn(List.of(p1, p2));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepo, times(1)).findAll();
    }
}
