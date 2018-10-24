package lambda.data;

import lombok.Value;

@Value
public class PersonPositionTuple {
    Person person;
    String position;
}
