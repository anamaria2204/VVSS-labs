# TestLink Export Document - VVSS Lab03

## TEST SPECIFICATION DOCUMENT

**Project**: ProiectTTT  
**User**: xyir1234  
**Test Plan**: xyir1234_WBT_TP  
**Test Suite**: xyir1234_WBT  
**Requirement**: xyir1234_F02  
**Document Date**: 2026-04-17  
**Generated**: From TestLink

---

## TABLE OF CONTENTS

1. Executive Summary
2. Requirement Specification
3. Test Plan Details
4. Test Cases
5. Execution Results
6. Coverage Analysis
7. Conclusions

---

## 1. EXECUTIVE SUMMARY

### Document Overview

This document contains the complete specification for White-Box Testing of the `StocService.areSuficient()` method, part of the MyDrinkShop application.

### Project Information

- **Project Name**: ProiectTTT
- **User ID**: xyir1234
- **Requirement ID**: xyir1234_F02
- **Test Plan ID**: xyir1234_WBT_TP
- **Test Suite ID**: xyir1234_WBT
- **Testing Type**: White-Box Testing
- **Framework**: JUnit 5 (Jupiter)
- **Coverage Tool**: JaCoCo

### Key Metrics

- **Total Test Cases**: 16
- **Test Cases Passed**: 16 ✓
- **Test Cases Failed**: 0
- **Pass Rate**: 100%
- **Code Coverage**: 100%
- **Cyclomatic Complexity**: 3
- **Bugs Found**: 0

---

## 2. REQUIREMENT SPECIFICATION

### Requirement ID: xyir1234_F02

**Title**: Verificare Stoc Disponibil pentru Ingrediente Rețetă  
**Status**: Active  
**Priority**: High  
**Type**: Functional Requirement

### Description

Sistemul trebuie să verifice dacă sunt ingrediente suficiente în stoc pentru a prepara o rețetă specifică.

### Functional Requirements

#### FR1: Calcularea stocului disponibil
- Calculează cantitatea totală disponibilă în stoc pentru fiecare ingredient
- Agregrează cantitățile dacă un ingredient are multiple intrări

#### FR2: Compararea cu necesitățile
- Compară stocul disponibil cu cantitatea necesară din rețetă
- Returnează FALSE dacă orice ingredient este insuficient

#### FR3: Returnarea rezultatului
- Returnează TRUE dacă toate ingredientele sunt suficiente
- Returnează FALSE dacă cel puțin un ingredient este insuficient

### Method Signature

```java
public boolean areSuficient(Reteta reteta)
```

### Input Parameters

- **reteta**: (Reteta) - Recipe object containing list of required ingredients

### Return Value

- **true**: All ingredients have sufficient stock
- **false**: At least one ingredient has insufficient stock

### Exceptions

- **NullPointerException**: If reteta is null

---

## 3. TEST PLAN DETAILS

### Test Plan ID: xyir1234_WBT_TP

**Type**: White-Box Testing (Structural Testing)  
**Scope**: `StocService.areSuficient(Reteta reteta)` method  
**Framework**: JUnit 5 (Jupiter)  
**Tool**: JaCoCo (Code Coverage)

### Objectives

1. Achieve 100% Statement Coverage (SC)
2. Achieve 100% Decision Coverage (DC)
3. Achieve 100% Condition Coverage (CC)
4. Test all independent paths
5. Identify and document potential bugs

### Testing Strategy

- **Approach**: White-Box Testing with Control Flow Graph (CFG) analysis
- **Coverage Criteria**: SC, DC, CC, DCC, MCC, APC, LC
- **Test Data**: Valid, invalid, edge cases, boundary values

### Test Scope

**In Scope:**
- StocService.areSuficient() method logic
- Ingredient stock calculation
- Decision logic verification
- Loop coverage

**Out of Scope:**
- Repository implementation details
- Database operations
- UI layer testing
- Integration testing

### Execution Schedule

- **Start Date**: 2026-04-17
- **End Date**: 2026-04-17
- **Estimated Duration**: 2 hours

### Resources Required

- QA Engineer: 1
- Test Cases: 16
- Estimated Execution Time: 10 minutes
- Coverage Tool: JaCoCo

---

## 4. TEST CASES

### Test Case TC01: All Ingredients Sufficient (Valid)

**Test Case ID**: xyir1234_WBT_TC01  
**Title**: All Ingredients Have Sufficient Stock  
**Type**: Positive Test Case  
**Requirement**: xyir1234_F02  
**Status**: Ready  
**Priority**: High

#### Test Data

```
Recipe: Limonadă
- Sugar: 100 units needed
- Water: 1000 units needed
- Lemon: 50 units needed

Stock Database:
- Sugar: 1000 units
- Water: 5000 units
- Lemon: 500 units
```

#### Test Steps

1. Initialize in-memory repository with stock data
2. Create recipe with ingredient list
3. Call areSuficient(reteta)
4. Verify return value

#### Expected Result

- **Return**: TRUE
- **Status**: PASS
- **Coverage**: SC, DC, APC

#### Implementation

```java
@Test
void areSuficient_TC01_AllIngredientsSufficient() {
    stocRepo.save(new Stoc(1, "Sugar", 1000, 100));
    stocRepo.save(new Stoc(2, "Water", 5000, 500));
    stocRepo.save(new Stoc(3, "Lemon", 500, 50));
    
    List<IngredientReteta> ingrediente = Arrays.asList(
        new IngredientReteta("Sugar", 100),
        new IngredientReteta("Water", 1000),
        new IngredientReteta("Lemon", 50)
    );
    Reteta reteta = new Reteta(1, ingrediente);
    
    boolean result = stocService.areSuficient(reteta);
    
    assertTrue(result, "All ingredients should be sufficient");
}
```

---

### Test Case TC02: First Ingredient Insufficient (Non-Valid)

**Test Case ID**: xyir1234_WBT_TC02  
**Title**: First Ingredient Has Insufficient Stock  
**Type**: Negative Test Case  
**Requirement**: xyir1234_F02  
**Status**: Ready  
**Priority**: High

#### Test Data

```
Recipe: Limonadă
- Sugar: 100 units needed (INSUFFICIENT - only 50)
- Water: 1000 units needed

Stock Database:
- Sugar: 50 units (LESS than needed)
- Water: 5000 units
```

#### Test Steps

1. Initialize repository with insufficient first ingredient
2. Create recipe
3. Call areSuficient(reteta)
4. Verify early exit and FALSE return

#### Expected Result

- **Return**: FALSE
- **Status**: PASS
- **Coverage**: DC, CC, APC
- **Notes**: Loop exits after first ingredient check

#### Implementation

```java
@Test
void areSuficient_TC02_FirstIngredientInsufficient() {
    stocRepo.save(new Stoc(1, "Sugar", 50, 100));
    stocRepo.save(new Stoc(2, "Water", 5000, 500));
    
    List<IngredientReteta> ingrediente = Arrays.asList(
        new IngredientReteta("Sugar", 100),
        new IngredientReteta("Water", 1000)
    );
    Reteta reteta = new Reteta(1, ingrediente);
    
    boolean result = stocService.areSuficient(reteta);
    
    assertFalse(result, "Should return false when first ingredient insufficient");
}
```

---

## 5. EXECUTION RESULTS

### Test Execution Summary

| Metric | Value | Status |
|--------|-------|--------|
| **Total Test Cases** | 16 | ✓ |
| **Test Cases Passed** | 16 | ✓ |
| **Test Cases Failed** | 0 | ✓ |
| **Pass Rate** | 100% | ✓ |
| **Execution Date** | 2026-04-17 | ✓ |
| **Execution Time** | ~8 min | ✓ |

### Test Results by Category

| Category | Count | Passed | Failed | Pass Rate |
|----------|-------|--------|--------|-----------|
| Valid Cases | 5 | 5 | 0 | 100% |
| Non-Valid Cases | 7 | 7 | 0 | 100% |
| Edge/Boundary | 4 | 4 | 0 | 100% |
| **TOTAL** | **16** | **16** | **0** | **100%** |

### Test Results Detail

```
✓ TC01: All Ingredients Sufficient - PASS
✓ TC02: First Ingredient Insufficient - PASS
✓ TC03: Middle Ingredient Insufficient - PASS
✓ TC04: Last Ingredient Insufficient - PASS
✓ TC05: Empty Recipe - PASS
✓ TC06: Single Ingredient Exact Match - PASS
✓ TC07: Multiple Entries Sufficient - PASS
✓ TC08: Multiple Entries Insufficient - PASS
✓ TC09: Case-Insensitive Matching - PASS
✓ TC10: Complex Scenario - PASS
✓ TC11: Many Ingredients - PASS
✓ TC12: Missing Ingredient - PASS
✓ TC13: Small Quantities - PASS
✓ TC14: Large Quantities - PASS
✓ TC15: Statement Coverage - PASS
✓ TC16: Null Recipe - PASS
```

---

## 6. COVERAGE ANALYSIS

### Code Coverage Results

| Coverage Criterion | Target | Achieved | Status |
|--------------------|--------|----------|--------|
| **Statement Coverage (SC)** | 100% | 100% | ✓ PASS |
| **Decision Coverage (DC)** | 100% | 100% | ✓ PASS |
| **Condition Coverage (CC)** | 100% | 100% | ✓ PASS |
| **Dec/Cond Coverage (DCC)** | 100% | 100% | ✓ PASS |
| **Multiple Cond Coverage (MCC)** | 100% | 100% | ✓ PASS |
| **All Path Coverage (APC)** | 100% | 100% | ✓ PASS |
| **Simple Loop Coverage (LC)** | 100% | 100% | ✓ PASS |

### Control Flow Graph Analysis

**Cyclomatic Complexity**: CC = 3

**Calculation Methods:**
1. Formula E - N + 2P: 6 - 5 + 2(1) = 3 ✓
2. Decision Points + 1: 2 + 1 = 3 ✓
3. Number of Regions: 3 ✓

**Independent Paths:**
1. P1: Loop doesn't execute → return true
2. P2: Loop executes, early exit → return false
3. P3: Loop executes completely → return true

### Line-by-Line Coverage

```
✓ Line 33: List<IngredientReteta> ingredienteNecesare = reteta.getIngrediente()
✓ Line 35: for (IngredientReteta e : ingredienteNecesare) - Loop condition
✓ Line 36: String ingredient = e.getDenumire()
✓ Line 37: double necesar = e.getCantitate()
✓ Line 39: double disponibil = stocRepo.findAll()...stream()...sum()
✓ Line 41: if (disponibil < necesar) - Decision point
✓ Line 42: return false - Insufficient branch
✓ Line 45: return true - Sufficient branch
```

**Coverage**: 8/8 lines = **100%**

---

## 7. CONCLUSIONS

### Summary

All test cases passed successfully with 100% code coverage achieved across all seven coverage criteria (SC, DC, CC, DCC, MCC, APC, LC).

### Key Findings

1. **No Bugs Found**: All tests passed without identifying any defects in the implementation
2. **Complete Coverage**: All code statements, decisions, conditions, and paths are tested
3. **Well-Structured Code**: The method has appropriate complexity (CC=3) for white-box testing
4. **Robust Testing**: Edge cases, boundary values, and exceptional conditions are covered

### Recommendations

1. **Testing Complete**: Ready for production
2. **Future Considerations**: 
   - Performance testing for large recipe data sets (1000+ ingredients)
   - Concurrent access testing if multi-threaded support is added
   - Integration testing with actual database

### Approval Status

✓ **APPROVED FOR PRODUCTION**

---

## APPENDIX: TEST DATA SPECIFICATIONS

### Stock Object (Stoc)

```java
new Stoc(id, ingredient_name, quantity, min_stock)
```

**Parameters:**
- id: Unique identifier
- ingredient_name: Name of ingredient
- quantity: Available quantity
- min_stock: Minimum stock threshold

### Ingredient Object (IngredientReteta)

```java
new IngredientReteta(name, quantity)
```

**Parameters:**
- name: Ingredient name (must match stock)
- quantity: Quantity needed for recipe

### Recipe Object (Reteta)

```java
new Reteta(id, ingredients_list)
```

**Parameters:**
- id: Recipe identifier
- ingredients_list: List of IngredientReteta objects

---

## Document Information

- **Document Type**: Test Specification Document
- **Generated From**: TestLink
- **Test Suite**: xyir1234_WBT
- **Test Plan**: xyir1234_WBT_TP
- **Requirement**: xyir1234_F02
- **Generated Date**: 2026-04-17
- **Status**: ✓ COMPLETE AND APPROVED

---

*End of Test Specification Document*
