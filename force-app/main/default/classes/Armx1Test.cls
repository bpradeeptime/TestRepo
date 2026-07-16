@isTest
private class Armx1Test {

    @isTest
    static void testAddPositive() {
        Armx1 calc = new Armx1();
        Assert.areEqual(5, calc.add(2, 3), 'add of two positives should return their sum');
    }

    @isTest
    static void testAddNegativeAndMixed() {
        Armx1 calc = new Armx1();
        Assert.areEqual(-5, calc.add(-2, -3), 'add of two negatives should return negative sum');
        Assert.areEqual(1, calc.add(-2, 3), 'add of mixed signs should return correct sum');
    }

    @isTest
    static void testAddZero() {
        Armx1 calc = new Armx1();
        Assert.areEqual(7, calc.add(7, 0), 'adding zero should return the original value');
        Assert.areEqual(0, calc.add(0, 0), 'adding two zeros should return zero');
    }

    @isTest
    static void testSubtractPositive() {
        Armx1 calc = new Armx1();
        Assert.areEqual(2, calc.subtract(5, 3), 'subtract should return first minus second');
    }

    @isTest
    static void testSubtractNegativeResult() {
        Armx1 calc = new Armx1();
        // Negative path: result should be negative when second > first
        Assert.areEqual(-4, calc.subtract(3, 7), 'subtract should return a negative result when second is larger');
        Assert.areEqual(1, calc.subtract(-2, -3), 'subtract of two negatives should be correct');
    }

    @isTest
    static void testSubtractZero() {
        Armx1 calc = new Armx1();
        Assert.areEqual(9, calc.subtract(9, 0), 'subtracting zero should return the original value');
        Assert.areEqual(-9, calc.subtract(0, 9), 'zero minus value should return the negated value');
    }
}
