/**
 * Rahul
 *
 * Created for TEST-6: "Create a new Apex class named Rahul and accept the automatic calculations."
 *
 * NOTE: The work item does not define what the "automatic calculations" should compute.
 * As a minimal, testable placeholder this class exposes a simple deterministic calculation
 * (sum of two integers) so the class exists in the org and has verifiable behaviour.
 */
public with sharing class Rahul {
    /**
     * Performs a simple automatic calculation: the sum of two integers.
     * @param a first operand (null treated as 0)
     * @param b second operand (null treated as 0)
     * @return the sum of a and b
     */
    public Integer calculate(Integer a, Integer b) {
        Integer left = (a == null) ? 0 : a;
        Integer right = (b == null) ? 0 : b;
        return left + right;
    }

    /**
     * Convenience method returning a computed default value.
     * @return a fixed computed value used as a placeholder result
     */
    public Integer computeDefault() {
        return calculate(1, 1);
    }
}
