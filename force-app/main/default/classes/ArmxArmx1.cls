public with sharing class ArmxArmx1 {
    /**
     * Sums the provided values and returns the result.
     * Null or empty input returns 0. Null elements are treated as 0.
     *
     * @param values the collection of Decimal values to sum
     * @return the arithmetic sum of the provided values as a Decimal
     */
    public Decimal sumValues(List<Decimal> values) {
        Decimal total = 0;
        if (values == null || values.isEmpty()) {
            return total;
        }
        for (Decimal value : values) {
            if (value != null) {
                total += value;
            }
        }
        return total;
    }
}
