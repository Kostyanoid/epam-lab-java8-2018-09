package lambda.data;

import lombok.Value;

@Value
public class PersonEmployerDurationTuple {
    Person person;
    String employer;
    int duration;
}
