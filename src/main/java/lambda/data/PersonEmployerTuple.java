package lambda.data;

import lombok.Value;

@Value
public class PersonEmployerTuple {
    Person person;
    String employer;
}
