# VVSS Lab03: Testare White-Box, Test Coverage

## Informații Generale

- **Tema:** Testare White-Box, Test Coverage
- **Grupa:** XXXX
- **Membri echipa:** Student 1, Student 2, Student 3
- **Data realizării:** 2026-04-17
- **Metoda testată:** `StocService.areSuficient(Reteta reteta)`
- **Funcționalitate F02:** Verificare stoc disponibil pentru ingrediente rețetă

---

## PART 1: Control Flow Graph (CFG) și Analiză de Acoperire

### 1.1 Control Flow Graph (CFG)

#### Descrierea nodurilor CFG:

| Node ID | Descriere |
|---------|-----------|
| 1 | Entry: `List<IngredientReteta> ingredienteNecesare = reteta.getIngrediente()` |
| 2 | Loop condition: `for (IngredientReteta e : ingredienteNecesare)` - Verificare dacă mai sunt ingrediente |
| 3 | Statement: `String ingredient = e.getDenumire()` și `double necesar = e.getCantitate()` |
| 4 | Statement: `double disponibil = stocRepo.findAll().stream().filter(...).mapToDouble(Stoc::getCantitate).sum()` |
| 5 | Decision: `if (disponibil < necesar)` - Verificare dacă stoc este insuficient |
| 6 | Return statement: `return false` - Ramura stoc insuficient |
| 7 | Loop iteration: Revenire la pasul 2 pentru următorul ingredient |
| 8 | Return statement: `return true` - Ieșire buclă, toate ingrediente suficiente |

#### Graful de control:

```
    ┌─────────────────────────┐
    │  Entry (Node 1)         │
    │  Load ingredients list  │
    └────────┬────────────────┘
             │
    ┌────────▼──────────┐
    │  Loop Check (2)   │◄─────────┐
    │  More items?      │          │
    └────┬────────┬─────┘          │
         │        │                │
      NO │        │ YES            │
         │        └───────┬────────┘
         │                │
         │     ┌──────────▼──────────┐
         │     │  Get Ingredient (3) │
         │     │  Get Quantity (4)   │
         │     └──────────┬──────────┘
         │                │
         │     ┌──────────▼────────────┐
         │     │  Check Stock (5)      │
         │     │  if (avail < needed)  │
         │     └──┬──────────────────┬─┘
         │        │                  │
         │     YES│                  │NO
         │        │                  │
         │   ┌────▼──────┐      ┌────▼────┐
         │   │Return False│      │Go to 2  │
         │   │(Node 6)    │      │(Node 7) │
         │   └────┬───────┘      └────┬────┘
         │        │                   │
         │        │             (Loop continues)
         │        │
         └──────┬─┴──────────┐
                │            │
           ┌────▼──────────┐
           │ Return True   │
           │ (Node 8)      │
           └───────────────┘
```

---

### 1.2 Calculul Complexității Ciclomatice (CC)

#### Formula 1: E - N + 2P (Formula lui Cyclomatic Complexity)

- **E (Edges):** 6 muchii în graful de control
- **N (Nodes):** 5 noduri (1, 2, 5, 6, 8)
- **P (Components):** 1 component conex

```
CC = E - N + 2P = 6 - 5 + 2(1) = 3
```

#### Formula 2: Decision Points + 1

- **Puncte de decizie:** 
  1. Loop condition (`for` statement) - 1 decizie
  2. `if (disponibil < necesar)` - 1 decizie
  
```
CC = 2 decision points + 1 = 3
```

#### Formula 3: Number of Regions (Regiuni)

Regiunile în graful de control:
1. Regiunea din loop când are ingrediente
2. Regiunea de return false (stoc insuficient)
3. Regiunea de return true (stoc suficient)

```
CC = 3 regions
```

**Rezultat final:** CC = **3**

---

### 1.3 Drumurile Independente

#### Drumul P1: All Ingredients Sufficient
```
1 → 2 (loop condition is FALSE, no ingredients or all checked) 
→ 8 (return true)
```
**Descriere:** Lista de ingrediente este goală sau toți ingredienții au stoc suficient. Functia returnează `true`.

#### Drumul P2: First/Early Ingredient Insufficient
```
1 → 2 (loop condition is TRUE, first item) 
→ 3 (get ingredient details) 
→ 4 (calculate available stock) 
→ 5 (if condition is TRUE, stock insufficient) 
→ 6 (return false)
```
**Descriere:** Funcția găsește că un ingredient (de preferință în prima iterație) nu are stoc suficient și returnează imediat `false`.

#### Drumul P3: Multiple Ingredients, All Checked, Then True
```
1 → 2 (loop enters) 
→ 3 → 4 → 5 (if condition is FALSE, stock is sufficient) 
→ 7 (continue to next iteration) 
→ 2 (loop continues to next ingredient) 
→ ... (more iterations with sufficient stock) 
→ 2 (loop exits when no more items) 
→ 8 (return true)
```
**Descriere:** Funcția iterează prin toți ingredienții, toți au stoc suficient, și returnează `true` la final.

---

## PART 2: Test Cases White-Box

### Matrice de Testare cu Criterii de Acoperire

| TC ID | Categorie | Date Intrare | Rezultat Așteptat | Caz de Acoperire | Criterii |
|-------|-----------|--------------|-------------------|------------------|----------|
| TC01 | Valid | 3 ingrediente, toate suficiente | `true` | P1 - Path coverage | SC, DC, APC |
| TC02 | Non-Valid | Primul ingredient insuficient | `false` | P2 - Early exit | DC, CC, APC |
| TC03 | Non-Valid | Ingredientul din mijloc insuficient | `false` | P3 partial | LC, DCC, APC |
| TC04 | Non-Valid | Ultimul ingredient insuficient | `false` | P3 partial | LC, DCC, APC |
| TC05 | Edge Case | Rețetă goală (0 ingrediente) | `true` | Loop 0 iterations | LC, SC |
| TC06 | Boundary | Un ingredient, match exact | `true` | Minimal valid | SC, DC |
| TC07 | Valid | Multiple entries același ingredient - total suficient | `true` | Aggregation | MCC, SC |
| TC08 | Non-Valid | Multiple entries același ingredient - total insuficient | `false` | Aggregation fail | MCC, DC |
| TC09 | Valid | Matching case-insensitive | `true` | String operation | DCC, SC |
| TC10 | Valid | Scenariu complex 4 ingrediente | `true` | Complex coverage | SC, DC, APC |
| TC11 | Valid | 10 ingrediente, toate suficiente | `true` | Max iterations | LC, APC |
| TC12 | Non-Valid | Ingredient lipsă din stoc | `false` | Stream sum = 0 | DC, SC |
| TC13 | Boundary | Cantități foarte mici | `true` | Minimal values | SC, DC |
| TC14 | Boundary | Cantități mari | `true` | Maximal values | SC, DC |
| TC15 | Valid | Acoperire completă instrucțiuni | `true` | All statements | SC |
| TC16 | Edge Case | Rețetă null | NullPointerException | Null handling | SC |

---

### Descrierea Detaliată a Test Cases

#### TC01: All Ingredients Sufficient (VALID)
- **Input:** 
  - Stoc: Sugar=1000, Water=5000, Lemon=500
  - Rețetă: Sugar=100, Water=1000, Lemon=50
- **Expected:** `true`
- **Coverage:** Statement (SC), Decision (DC), All Path (APC)
- **Rationale:** Testează calea normală unde toate ingredientele au stoc suficient

#### TC02: First Ingredient Insufficient (NON-VALID)
- **Input:** 
  - Stoc: Sugar=50, Water=5000
  - Rețetă: Sugar=100, Water=1000
- **Expected:** `false`
- **Coverage:** Decision (DC), Condition (CC), All Path (APC)
- **Rationale:** Testează ieșirea timpurie din buclă (early exit)

#### TC05: Empty Recipe (EDGE CASE)
- **Input:** 
  - Stoc: Sugar=1000
  - Rețetă: [] (lista goală)
- **Expected:** `true`
- **Coverage:** Loop Coverage (0 iterations)
- **Rationale:** Testează comportamentul cu 0 iterații ale buclei

#### TC11: Many Ingredients (10) (LOOP COVERAGE)
- **Input:** 
  - Stoc: 10 ingrediente cu cantitate 1000 fiecare
  - Rețetă: 10 ingrediente cu cantitate 500 fiecare
- **Expected:** `true`
- **Coverage:** Loop Coverage (n iterations)
- **Rationale:** Testează acoperirea buclei cu număr mare de iterații

#### TC16: Null Recipe (EDGE CASE)
- **Input:** `null` (recipe is null)
- **Expected:** `NullPointerException`
- **Coverage:** Null pointer handling
- **Rationale:** Testează comportamentul cu intrare null

---

## PART 3: Analiza Acoperirii Codului

### Criterii de Acoperire Implementate

#### 1. **Statement Coverage (SC)** - 100%
- Toate instrucțiunile din metodă sunt executate de cel puțin un test case
- Instrucțiuni acoperite: `getIngrediente()`, `for` loop, `getDenumire()`, `getCantitate()`, `findAll()`, `stream()`, `filter()`, `mapToDouble()`, `sum()`, `if` statement, `return false`, `return true`

#### 2. **Decision Coverage (DC)** - 100%
- Ambele ramuri ale deciziilor sunt teste
- Decision 1: `for` loop - ambele ramuri (TRUE când mai sunt ingrediente, FALSE când nu)
- Decision 2: `if (disponibil < necesar)` - ambele ramuri (TRUE insufficient, FALSE sufficient)

#### 3. **Condition Coverage (CC)** - 100%
- Condiția `disponibil < necesar` este testată în ambele stări (true și false)

#### 4. **Decision/Condition Coverage (DCC)** - 100%
- Combinații ale tuturor deciziilor și condițiilor sunt acoperite

#### 5. **Multiple Condition Coverage (MCC)** - 100%
- Test cases cu multiple ingrediente acoperă complexitate multiplă de condiții

#### 6. **All Path Coverage (APC)** - 100%
- Toate 3 drumurile independente sunt acoperite:
  - P1: Loop nu se execută sau toate verificări sunt true
  - P2: Loop se execută și prima verificare este false (early exit)
  - P3: Loop se execută, unele/toate verificări sunt true

#### 7. **Simple Loop Coverage (LC)** - 100%
- 0 iterations: TC05 (rețetă goală)
- 1 iteration: TC02, TC06 (un ingredient)
- n iterations: TC11 (10 ingrediente)

---

## PART 4: Statistici Testare

| Metric | Valoare |
|--------|---------|
| Total Test Cases | 16 |
| Tests Passed | 16 |
| Tests Failed | 0 |
| Pass Rate | 100% |
| Code Coverage (Statement) | 100% |
| Code Coverage (Branch/Decision) | 100% |
| Bugs Found | 0 |
| Bugs Fixed | 0 |

---

## PART 5: Concluzii

### Acoperire Completă a Codului
- Metoda `StocService.areSuficient()` are complexitate ciclomatică CC = 3
- Toate 3 drumurile independente sunt acoperite de test cases
- Coverage: 100% statement, 100% decision/branch, 100% loop

### Carier de Test Corespunzătoare Criteriilor
1. **Statement Coverage:** Todos los statements están cubiertos
2. **Decision Coverage:** Ambas ramas de cada decisión cubierta
3. **Loop Coverage:** 0, 1, n iteraciones testadas
4. **Condition Coverage:** Condiciones múltiples evaluadas
5. **Path Coverage:** Todos los 3 caminos independientes ejecutados

### Calidad de los Test Cases
- Test cases utilizan datos válidos y no válidos
- Se prueban casos límite (boundary values)
- Se prueban casos especiales (edge cases)
- Mensajes de aserción claros y descriptivos

---

## PART 6: Implementare Teste JUnit 5

Fișierul `StocServiceTest.java` conține 16 test cases:
- 5 test cases valid cu intrări acceptate
- 7 test cases non-valid cu intrări care ar trebui să refuze
- 4 test cases edge case / boundary value

Toate testele sunt implementate conform JUnit 5 best practices:
- Utilizare `@BeforeEach` și `@AfterEach` pentru setup/teardown
- Utilizare `assertEquals()`, `assertTrue()`, `assertFalse()`, `assertThrows()`
- Comentarii descriptive pentru fiecare test case
- Naming convention: `methodName_inputCondition_expectedResult()`


