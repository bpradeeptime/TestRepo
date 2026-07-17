/**
 * RahulTest
 *
 * Unit tests for the Rahul class (TEST-6).
 * Verifies the placeholder automatic calculation behaviour, including edge cases
 * (nulls, negatives) and a negative-path assertion.
 */
@IsTest
private class RahulTest {

    @IsTest
    static void testCalculatePositiveNumbers() {
        Rahul r = new Rahul();
        Integer result = r.calculate(2, 3);
        Assert.areEqual(5, result, 'calculate(2,3) should return 5');
    }

    @IsTest
    static void testCalculateWithNulls() {
        Rahul r = new Rahul();
        Assert.areEqual(0, r.calculate(null, null), 'calculate(null,null) should return 0');
        Assert.areEqual(4, r.calculate(4, null), 'calculate(4,null) should return 4');
        Assert.areEqual(7, r.calculate(null, 7), 'calculate(null,7) should return 7');
    }

    @IsTest
    static void testCalculateWithNegatives() {
        Rahul r = new Rahul();
        Assert.areEqual(-1, r.calculate(2, -3), 'calculate(2,-3) should return -1');
    }

    @IsTest
    static void testComputeDefault() {
        Rahul r = new Rahul();
        Assert.areEqual(2, r.computeDefault(), 'computeDefault() should return 2');
    }

    @IsTest
    static void testCalculateNegativePath() {
        Rahul r = new Rahul();
        Integer result = r.calculate(2, 3);
        // Negative-path assertion: confirm an incorrect expectation does NOT hold.
        Assert.areNotEqual(6, result, 'calculate(2,3) must not equal 6');
    }
}
