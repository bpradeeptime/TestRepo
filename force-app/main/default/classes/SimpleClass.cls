/**
 * SimpleClass
 *
 * Minimal, syntactically valid Apex class created as a starting point/placeholder
 * for future logic (Jira TEST-13). Exposes a couple of simple, deterministic
 * methods so the class can be exercised by unit tests and satisfy Salesforce
 * code coverage requirements.
 */
public with sharing class SimpleClass {

    /**
     * Returns a fixed greeting string.
     *
     * @return the constant greeting 'Hello, World!'
     */
    public String getGreeting() {
        return 'Hello, World!';
    }

    /**
     * Adds two integers.
     *
     * @param a first operand (null treated as 0)
     * @param b second operand (null treated as 0)
     * @return the sum of a and b
     */
    public Integer add(Integer a, Integer b) {
        Integer left = (a == null) ? 0 : a;
        Integer right = (b == null) ? 0 : b;
        return left + right;
    }

    /**
     * Echoes the supplied value back to the caller.
     *
     * @param input any string (may be null)
     * @return the same string that was passed in, or an empty string when null
     */
    public String echo(String input) {
        return (input == null) ? '' : input;
    }
}
