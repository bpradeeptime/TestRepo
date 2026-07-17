@isTest
private class A1Test {
    @isTest
    static void testGetGreetingReturnsExpectedValue() {
        A1 instance = new A1();
        String result = instance.getGreeting();
        Assert.areEqual('Hello from A1', result, 'getGreeting should return the fixed greeting message');
    }

    @isTest
    static void testGetGreetingIsNotNullOrEmpty() {
        A1 instance = new A1();
        String result = instance.getGreeting();
        Assert.isNotNull(result, 'getGreeting should never return null');
        Assert.isTrue(String.isNotBlank(result), 'getGreeting should not return a blank value');
    }

    @isTest
    static void testGetGreetingNegativePath() {
        A1 instance = new A1();
        String result = instance.getGreeting();
        Assert.areNotEqual('Goodbye', result, 'getGreeting should not return an unexpected value');
        Assert.isFalse(result.contains('Goodbye'), 'greeting should not contain unexpected text');
    }
}