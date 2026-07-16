@IsTest
private class ArmxArmx1Test {
    @IsTest
    static void testSumMultiplePositiveValues() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.sumValues(new List<Decimal>{ 1, 2, 3 });
        Assert.areEqual(6, result, 'Sum of 1, 2, 3 should be 6');
    }

    @IsTest
    static void testSumWithNegativeValues() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.sumValues(new List<Decimal>{ 10, -4, -1 });
        Assert.areEqual(5, result, 'Sum of 10, -4, -1 should be 5');
    }

    @IsTest
    static void testSumSingleValue() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.sumValues(new List<Decimal>{ 42.5 });
        Assert.areEqual(42.5, result, 'Single value should be returned as the sum');
    }

    @IsTest
    static void testEmptyListReturnsZero() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.sumValues(new List<Decimal>());
        Assert.areEqual(0, result, 'Empty list should return 0');
    }

    @IsTest
    static void testNullInputReturnsZero() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.sumValues(null);
        Assert.areEqual(0, result, 'Null input should return 0');
    }

    @IsTest
    static void testNullElementsIgnored() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.sumValues(new List<Decimal>{ 5, null, 3 });
        Assert.areEqual(8, result, 'Null elements should be treated as 0');
    }

    @IsTest
    static void testLargeValues() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.sumValues(new List<Decimal>{ 1000000000, 2000000000 });
        Assert.areEqual(3000000000L, result, 'Large values should sum without overflow');
    }
}
