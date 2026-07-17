/**
 * HelloWorldTest
 * Unit tests for HelloWorld.
 */
@isTest
private class HelloWorldTest {

    @isTest
    static void sayHello_returnsExpectedMessage() {
        Test.startTest();
        String result = HelloWorld.sayHello();
        Test.stopTest();

        Assert.areEqual('Hello World', result, 'sayHello() should return the exact greeting');
    }

    @isTest
    static void sayHello_isNotEmptyOrUnexpected() {
        String result = HelloWorld.sayHello();

        // Negative path: ensure it never returns null, blank, or an unexpected value.
        Assert.isNotNull(result, 'sayHello() must never return null');
        Assert.isTrue(String.isNotBlank(result), 'sayHello() must not return a blank string');
        Assert.areNotEqual('Goodbye', result, 'sayHello() must not return an unexpected greeting');
    }
}
