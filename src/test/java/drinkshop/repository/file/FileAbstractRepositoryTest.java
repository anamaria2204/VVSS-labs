package drinkshop.repository.file;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileAbstractRepositoryTest {

    private FileProductRepository repo;
    private String testFile = "test_abstract.txt";

    @BeforeEach
    void setUp() {
        try {
            new File(testFile).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        repo = new FileProductRepository(testFile);
    }

    @AfterEach
    void tearDown() {
        new File(testFile).delete();
    }

    @Test
    void loadFromFile() {
        // Write to file
        try (FileWriter fw = new FileWriter(testFile)) {
            fw.write("1,Limonada,10.0,JUICE,WATER_BASED\n");
            fw.write("2,Cafea,5.0,CLASSIC_COFFEE,BASIC\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Load
        repo.loadFromFile();
        assertNotNull(repo.findOne(1));
        assertNotNull(repo.findOne(2));
        assertEquals(2, repo.findAll().size());
    }

    @Test
    void save() {
        Product p = new Product(1, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        repo.save(p);
        // Check in memory
        assertEquals(p, repo.findOne(1));
        // Check file written (but since writeToFile is private, hard to test directly)
        // Perhaps reload or check file content
    }
}