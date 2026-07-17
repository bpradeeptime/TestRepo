/**
 * HelloWorld
 * Minimal Apex class used to validate that custom Apex can be built and deployed.
 * Exposes a static method returning the string 'Hello World'.
 */
public with sharing class HelloWorld {

    /**
     * Returns the greeting message.
     * @return the string 'Hello World'
     */
    public static String sayHello() {
        return 'Hello World';
    }
}
