package cn.tuyucheng.taketoday.param;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Random;

public class InvalidPersonParameterResolver implements ParameterResolver {

    /**
     * The "bad" (invalid) data for testing purposes has to go somewhere, right?
     */
    public static Person[] INVALID_PERSONS = {
            new Person().setId(1L).setLastName("Ad_ams").setFirstName("Jill,"),
            new Person().setId(2L).setLastName(",Baker").setFirstName(""),
            new Person().setId(3L).setLastName(null).setFirstName(null),
            new Person().setId(4L).setLastName("Daniel&").setFirstName("{Joseph}"),
            new Person().setId(5L).setLastName("").setFirstName("English, Jane"),
            new Person()/* .setId(6L).setLastName("Fontana").setFirstName("Enrique") */,
    };

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Object ret = null;
        if (parameterContext.getParameter().getType() == Person.class) {
            ret = INVALID_PERSONS[new Random().nextInt(INVALID_PERSONS.length)];
        }
        return ret;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Person.class;
    }
}