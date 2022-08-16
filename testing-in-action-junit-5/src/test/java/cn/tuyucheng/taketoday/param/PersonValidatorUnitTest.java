package cn.tuyucheng.taketoday.param;

import cn.tuyucheng.taketoday.param.PersonValidator.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing PersonValidator")
public class PersonValidatorUnitTest {

    /**
     * Nested class, uses ExtendWith
     * {@link ValidPersonParameterResolver ValidPersonParameterResolver}
     * to feed Test methods with "good" data.
     */
    @Nested
    @DisplayName("When using Valid data")
    @ExtendWith(ValidPersonParameterResolver.class)
    public class ValidDataTest {
        /**
         * Repeat the test ten times, that way we have a good shot at
         * running all the data through at least once.
         * @param person A valid Person object to validate.
         */
        @RepeatedTest(value = 10)
        @DisplayName("All first name are valid")
        public void validateFirstName(Person person) {
            try {
                assertTrue(PersonValidator.validateFirstName(person));
            } catch (ValidationException e) {
                fail("Exception not expected: " + e.getLocalizedMessage());
            }
        }

        /**
         * Repeat the test ten times, that way we have a good shot at
         * running all the data through at least once.
         * @param person A valid Person object to validate.
         */
        @RepeatedTest(value = 10)
        @DisplayName("All last name are valid")
        public void validateLastName(Person person) {
            try {
                assertTrue(PersonValidator.validateLastName(person));
            } catch (ValidationException e) {
                fail("Exception not expected: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Nested class, uses ExtendWith
     * {@link InvalidPersonParameterResolver InvalidPersonParameterResolver}
     * to feed Test methods with "bad" data.
     */
    @Nested
    @DisplayName("When using Invalid data")
    @ExtendWith(InvalidPersonParameterResolver.class)
    public class InvalidDataTest {
        /**
         * Repeat the test ten times, that way we have a good shot at
         * running all the data through at least once.
         * @param person An invalid Person object to validate.
         */
        @RepeatedTest(value = 10)
        @DisplayName("All first name are invalid")
        public void validateFirstName(Person person) {
            assertThrows(ValidationException.class, () -> PersonValidator.validateFirstName(person));
        }

        /**
         * Repeat the test ten times, that way we have a good shot at
         * running all the data through at least once.
         * @param person An invalid Person object to validate.
         */
        @RepeatedTest(value = 10)
        @DisplayName("All last name are invalid")
        public void validateLastName(Person person) {
            assertThrows(ValidationException.class, () -> PersonValidator.validateLastName(person));
        }
    }
}