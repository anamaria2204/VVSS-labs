package drinkshop.service;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * White-Box Test Cases for StocService.areSuficient(Reteta reteta)
 *
 * Method Tested: StocService.areSuficient()
 *
 * Test Coverage Criteria:
 * - Statement Coverage (SC)
 * - Decision/Condition Coverage (DC/CC/DCC)
 * - Multiple Condition Coverage (MCC)
 * - All Path Coverage (APC)
 * - Simple Loop Coverage (LC)
 *
 * Cyclomatic Complexity (CC): 3
 * CC Calculation:
 * - Formula 1: E - N + 2P = 6 - 5 + 2(1) = 3
 * - Formula 2: decision points + 1 = 2 + 1 = 3
 * - Formula 3: regions = 3
 */
class StocServiceTest {

    private StocService stocService;
    private Repository<Integer, Stoc> stocRepo;

    @BeforeEach
    void setUp() {
        // Create in-memory repository for testing
        stocRepo = new AbstractRepository<>() {
            @Override
            protected Integer getId(Stoc entity) {
                return entity.getId();
            }
        };
        stocService = new StocService(stocRepo);
    }

    @AfterEach
    void tearDown() {
        stocRepo = null;
        stocService = null;
    }

    // ============================================================================
    // PATH 1: All ingredients have sufficient stock
    // VALID: All required ingredients are available in stock
    // ============================================================================
    @Test
    void areSuficient_TC01_AllIngredientsSufficient() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 1000, 100));
        stocRepo.save(new Stoc(2, "Water", 5000, 500));
        stocRepo.save(new Stoc(3, "Lemon", 500, 50));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 100),
                new IngredientReteta("Water", 1000),
                new IngredientReteta("Lemon", 50)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "Should return true when all ingredients have sufficient stock");
    }

    // ============================================================================
    // PATH 2: First ingredient has insufficient stock (loop exits early)
    // NON-VALID: First ingredient insufficient
    // ============================================================================
    @Test
    void areSuficient_TC02_FirstIngredientInsufficient() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 50, 100));      // Only 50, need 100
        stocRepo.save(new Stoc(2, "Water", 5000, 500));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 100),
                new IngredientReteta("Water", 1000)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertFalse(result, "Should return false when first ingredient is insufficient");
    }

    // ============================================================================
    // PATH 3: Second/Middle ingredient has insufficient stock
    // NON-VALID: Middle ingredient insufficient
    // ============================================================================
    @Test
    void areSuficient_TC03_MiddleIngredientInsufficient() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 1000, 100));
        stocRepo.save(new Stoc(2, "Water", 500, 500));      // Only 500, need 1000
        stocRepo.save(new Stoc(3, "Lemon", 500, 50));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 100),
                new IngredientReteta("Water", 1000),
                new IngredientReteta("Lemon", 50)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertFalse(result, "Should return false when middle ingredient is insufficient");
    }

    // ============================================================================
    // PATH 4: Last ingredient has insufficient stock
    // NON-VALID: Last ingredient insufficient
    // ============================================================================
    @Test
    void areSuficient_TC04_LastIngredientInsufficient() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 1000, 100));
        stocRepo.save(new Stoc(2, "Water", 5000, 500));
        stocRepo.save(new Stoc(3, "Lemon", 30, 50));        // Only 30, need 50

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 100),
                new IngredientReteta("Water", 1000),
                new IngredientReteta("Lemon", 50)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertFalse(result, "Should return false when last ingredient is insufficient");
    }

    // ============================================================================
    // BOUNDARY VALUE: Empty recipe (0 ingredients)
    // EDGE CASE: Loop executes 0 times
    // ============================================================================
    @Test
    void areSuficient_TC05_EmptyRecipe() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 1000, 100));

        List<IngredientReteta> ingrediente = new ArrayList<>();
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "Should return true for empty recipe (no ingredients needed)");
    }

    // ============================================================================
    // BOUNDARY VALUE: Single ingredient - exact match
    // VALID: Exactly enough stock
    // ============================================================================
    @Test
    void areSuficient_TC06_SingleIngredientExactMatch() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 100, 100));

        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta("Sugar", 100));
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "Should return true when stock exactly matches requirement");
    }

    // ============================================================================
    // CONDITION COVERAGE: Multiple stock entries for same ingredient
    // VALID: Aggregated stock is sufficient
    // ============================================================================
    @Test
    void areSuficient_TC07_MultipleEntriesSameIngredientSufficient() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 60, 100));
        stocRepo.save(new Stoc(2, "Sugar", 60, 100));        // Total: 120, need 100
        stocRepo.save(new Stoc(3, "Water", 5000, 500));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 100),
                new IngredientReteta("Water", 1000)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "Should return true when aggregated stock is sufficient");
    }

    // ============================================================================
    // CONDITION COVERAGE: Multiple stock entries for same ingredient - insufficient
    // NON-VALID: Aggregated stock is insufficient
    // ============================================================================
    @Test
    void areSuficient_TC08_MultipleEntriesSameIngredientInsufficient() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 40, 100));
        stocRepo.save(new Stoc(2, "Sugar", 50, 100));        // Total: 90, need 100
        stocRepo.save(new Stoc(3, "Water", 5000, 500));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 100),
                new IngredientReteta("Water", 1000)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertFalse(result, "Should return false when aggregated stock is insufficient");
    }

    // ============================================================================
    // CONDITION COVERAGE: Case-insensitive ingredient matching
    // VALID: Matching should be case-insensitive
    // ============================================================================
    @Test
    void areSuficient_TC09_CaseInsensitiveMatching() {
        // Arrange
        stocRepo.save(new Stoc(1, "SUGAR", 1000, 100));
        stocRepo.save(new Stoc(2, "Water", 5000, 500));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("sugar", 100),          // lowercase in recipe
                new IngredientReteta("water", 1000)          // lowercase in recipe
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "Should match ingredients case-insensitively");
    }

    // ============================================================================
    // MULTIPLE CONDITION COVERAGE: Complex scenario
    // Testing decision outcome combinations
    // ============================================================================
    @Test
    void areSuficient_TC10_ComplexScenarioMultipleIngredients() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 500, 100));
        stocRepo.save(new Stoc(2, "Water", 3000, 500));
        stocRepo.save(new Stoc(3, "Lemon", 200, 50));
        stocRepo.save(new Stoc(4, "Honey", 150, 50));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 200),
                new IngredientReteta("Water", 1500),
                new IngredientReteta("Lemon", 100),
                new IngredientReteta("Honey", 75)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "All ingredients should be sufficient in complex scenario");
    }

    // ============================================================================
    // LOOP COVERAGE: Testing loop with maximum iterations
    // ============================================================================
    @Test
    void areSuficient_TC11_ManyIngredientsAllSufficient() {
        // Arrange
        for (int i = 1; i <= 10; i++) {
            stocRepo.save(new Stoc(i, "Ingredient" + i, 1000, 100));
        }

        List<IngredientReteta> ingrediente = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ingrediente.add(new IngredientReteta("Ingredient" + i, 500));
        }
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "Should handle 10 ingredients correctly");
    }

    // ============================================================================
    // DECISION COVERAGE: Missing ingredient (not in stock at all)
    // NON-VALID: Ingredient not found in stock returns 0 quantity
    // ============================================================================
    @Test
    void areSuficient_TC12_MissingIngredient() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 1000, 100));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 100),
                new IngredientReteta("NonExistentIngredient", 50)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertFalse(result, "Should return false when required ingredient is not in stock at all");
    }

    // ============================================================================
    // BOUNDARY VALUE: Very small quantities
    // VALID: Minimal amounts
    // ============================================================================
    @Test
    void areSuficient_TC13_VerySmallQuantities() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 1, 1));
        stocRepo.save(new Stoc(2, "Water", 2, 1));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 1),
                new IngredientReteta("Water", 2)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "Should work with small quantities");
    }

    // ============================================================================
    // BOUNDARY VALUE: Large quantities
    // VALID: Large amounts
    // ============================================================================
    @Test
    void areSuficient_TC14_LargeQuantities() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 1000000, 100));
        stocRepo.save(new Stoc(2, "Water", 5000000, 500));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Sugar", 500000),
                new IngredientReteta("Water", 2500000)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "Should handle large quantities correctly");
    }

    // ============================================================================
    // STATEMENT COVERAGE: Ensure all statements are executed
    // Testing the filter and sum operations
    // ============================================================================
    @Test
    void areSuficient_TC15_StatementCoverage() {
        // Arrange: Setup to ensure all statements in areSuficient are executed
        stocRepo.save(new Stoc(1, "Coffee", 100, 50));
        stocRepo.save(new Stoc(2, "Milk", 200, 75));
        stocRepo.save(new Stoc(3, "Sugar", 150, 100));

        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Coffee", 50),
                new IngredientReteta("Milk", 100),
                new IngredientReteta("Sugar", 75)
        );
        Reteta reteta = new Reteta(1, ingrediente);

        // Act
        boolean result = stocService.areSuficient(reteta);

        // Assert
        assertTrue(result, "All statements in areSuficient should be executed");
    }

    // ============================================================================
    // NULL CONDITION: Null recipe
    // EDGE CASE: Should handle null gracefully or throw exception
    // ============================================================================
    @Test
    void areSuficient_TC16_NullRecipe() {
        // Arrange
        stocRepo.save(new Stoc(1, "Sugar", 1000, 100));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> stocService.areSuficient(null));
    }

}

