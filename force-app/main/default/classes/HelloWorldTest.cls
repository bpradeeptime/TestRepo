@isTest
private class HelloWorldTest {

    @isTest
    static void sayHelloReturnsExpectedString() {
        Test.startTest();
        String result = HelloWorld.sayHello();
        Test.stopTest();

        Assert.areEqual('Hello World', result, 'sayHello() should return the exact string \'Hello World\'');
    }

    @isTest
    static void sayHelloDoesNotReturnNullOrEmpty() {
        String result = HelloWorld.sayHello();

        Assert.isNotNull(result, 'sayHello() should never return null');
        Assert.areNotEqual('', result, 'sayHello() should not return an empty string');
    }

    @isTest
    static void sayHelloIsNotArbitraryValue() {
        String result = HelloWorld.sayHello();

        // Negative path: confirm the method does not return an unexpected value
        Assert.areNotEqual('Goodbye', result, 'sayHello() should not return an unrelated greeting');
    }
}
