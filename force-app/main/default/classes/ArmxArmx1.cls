public with sharing class ArmxArmx1 {
    /**
     * Subtracts the second value from the first and returns the result.
     *
     * @param a the minuend
     * @param b the subtrahend
     * @return the difference (a - b); returns null if either input is null
     */
    public Decimal subtract(Decimal a, Decimal b) {
        if (a == null || b == null) {
            return null;
        }
        return a - b;
    }
}
