package lambda.part1.exercise;

import com.google.common.collect.FluentIterable;
import lambda.data.Person;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.arrayContaining;

class Exercise1 {

    @Test
    void sortPersonsByAgeUsingArraysSortLocalComparator() {
        Person[] persons = getPersons();

        // TODO use Arrays.sort
        class PersonComparator implements Comparator<Person> {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1 == null && o2 == null) return 0;
                if (o1 != null && o2 == null) return 1;
                if (o1 == null) return -1;

                return o1.getAge() - o2.getAge();
            }
        }

        Arrays.sort(persons, new PersonComparator());

        assertThat(persons, is(arrayContaining(
                new Person("Иван", "Мельников", 20),
                new Person("Николай", "Зимов", 30),
                new Person("Алексей", "Доренко", 40),
                new Person("Артем", "Зимов", 45)
        )));
    }

    @Test
    void sortPersonsByAgeUsingArraysSortAnonymousComparator() {
        Person[] persons = getPersons();

        // TODO use Arrays.sort
        Comparator<Person> personComparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1 == null && o2 == null) return 0;
                if (o1 != null && o2 == null) return 1;
                if (o1 == null) return -1;

                return o1.getAge() - o2.getAge();
            }
        };

        Arrays.sort(persons, personComparator);

        assertThat(persons, is(arrayContaining(
                new Person("Иван", "Мельников", 20),
                new Person("Николай", "Зимов", 30),
                new Person("Алексей", "Доренко", 40),
                new Person("Артем", "Зимов", 45)
        )));
    }

    @Test
    void sortPersonsByLastNameThenFirstNameUsingArraysSortAnonymousComparator() {
        Person[] persons = getPersons();

        // TODO use Arrays.sort
        Comparator<Person> personByLastNameComparator = (p1, p2) -> p1.getLastName().compareToIgnoreCase(p2.getFirstName());
        Comparator<Person> personByFirstNameComparator = (p1, p2) -> p1.getFirstName().compareToIgnoreCase(p2.getFirstName());

        Arrays.sort(persons, personByLastNameComparator.thenComparing(personByFirstNameComparator));

        assertThat(persons, is(arrayContaining(
                new Person("Алексей", "Доренко", 40),
                new Person("Артем", "Зимов", 45),
                new Person("Николай", "Зимов", 30),
                new Person("Иван", "Мельников", 20)
        )));
    }

    @Test
    void findFirstWithAge30UsingGuavaPredicate() {
        List<Person> persons = Arrays.asList(getPersons());

        // TODO use FluentIterable
        Predicate<Person> personIs30yoPredicate = new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                return person.getAge() == 30;
            }
        };

        Person person = FluentIterable.from(persons)
                            .firstMatch(personIs30yoPredicate::test)
                            .orNull();

        assertThat(person, is(new Person("Николай", "Зимов", 30)));
    }

    @Test
    void findFirstWithAge30UsingGuavaAnonymousPredicate() {
        List<Person> persons = Arrays.asList(getPersons());

        // TODO use FluentIterable
        Person person = FluentIterable.from(persons)
                .firstMatch(new com.google.common.base.Predicate<Person>() {
                    @Override
                    public boolean apply(Person person) {
                        return person.getAge() == 30;
                    }
                })
                .orNull();

        assertThat(person, is(new Person("Николай", "Зимов", 30)));
    }

    private Person[] getPersons() {
        return new Person[]{
                new Person("Иван", "Мельников", 20),
                new Person("Алексей", "Доренко", 40),
                new Person("Николай", "Зимов", 30),
                new Person("Артем", "Зимов", 45)
        };
    }
}
