@isTest
private class ArmxArmx1Test {
    @isTest
    static void testSubtractPositiveResult() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.subtract(5, 3);
        Assert.areEqual(2, result, 'Subtracting 3 from 5 should return 2');
    }

    @isTest
    static void testSubtractNegativeResult() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.subtract(3, 5);
        Assert.areEqual(-2, result, 'Subtracting 5 from 3 should return -2');
    }

    @isTest
    static void testSubtractZeroResult() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.subtract(7, 7);
        Assert.areEqual(0, result, 'Subtracting equal values should return 0');
    }

    @isTest
    static void testSubtractZeroOperand() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.subtract(9, 0);
        Assert.areEqual(9, result, 'Subtracting zero should return the original value');
    }

    @isTest
    static void testSubtractNegativeValues() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.subtract(-4, -6);
        Assert.areEqual(2, result, 'Subtracting -6 from -4 should return 2');
    }

    @isTest
    static void testSubtractDecimalValues() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.subtract(5.5, 2.25);
        Assert.areEqual(3.25, result, 'Decimal subtraction should be precise');
    }

    @isTest
    static void testSubtractNullFirstOperand() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.subtract(null, 3);
        Assert.isNull(result, 'Null first operand should return null');
    }

    @isTest
    static void testSubtractNullSecondOperand() {
        ArmxArmx1 instance = new ArmxArmx1();
        Decimal result = instance.subtract(3, null);
        Assert.isNull(result, 'Null second operand should return null');
    }
}
