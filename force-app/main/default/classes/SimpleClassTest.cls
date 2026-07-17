/**
 * SimpleClassTest
 *
 * Unit tests for SimpleClass. Exercises every public method with positive-path
 * and negative/edge-case assertions to satisfy the >=75% code coverage bar.
 */
@IsTest
private class SimpleClassTest {

    @IsTest
    static void testGetGreeting() {
        SimpleClass sc = new SimpleClass();

        Test.startTest();
        String result = sc.getGreeting();
        Test.stopTest();

        Assert.areEqual('Hello, World!', result, 'getGreeting should return the fixed greeting');
    }

    @IsTest
    static void testAddPositive() {
        SimpleClass sc = new SimpleClass();

        Assert.areEqual(5, sc.add(2, 3), 'add should sum two positive integers');
        Assert.areEqual(-1, sc.add(2, -3), 'add should handle negative operands');
        Assert.areEqual(0, sc.add(0, 0), 'add of zeros should be zero');
    }

    @IsTest
    static void testAddNullOperands() {
        SimpleClass sc = new SimpleClass();

        // Negative/edge path: null operands are treated as 0.
        Assert.areEqual(4, sc.add(null, 4), 'null first operand should be treated as 0');
        Assert.areEqual(7, sc.add(7, null), 'null second operand should be treated as 0');
        Assert.areEqual(0, sc.add(null, null), 'both null operands should sum to 0');
    }

    @IsTest
    static void testEcho() {
        SimpleClass sc = new SimpleClass();

        Assert.areEqual('abc', sc.echo('abc'), 'echo should return the input unchanged');
        // Negative/edge path: null input returns empty string.
        Assert.areEqual('', sc.echo(null), 'echo of null should return an empty string');
    }
}
