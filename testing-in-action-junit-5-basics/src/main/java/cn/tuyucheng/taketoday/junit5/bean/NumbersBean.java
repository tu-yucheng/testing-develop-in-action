package cn.tuyucheng.taketoday.junit5.bean;

/**
 * Bean that contains utility methods to work with numbers.
 * @author tuyucheng
 */
public class NumbersBean {

    /**
     * Returns true if a number is even, false otherwise.
     * @param number the number to check
     * @return true if the argument is even, false otherwise
     */
    public boolean isNumberEven(Integer number) {
        return number % 2 == 0;
    }
}