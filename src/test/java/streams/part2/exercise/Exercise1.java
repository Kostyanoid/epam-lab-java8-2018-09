package streams.part2.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import lambda.data.PersonPositionTuple;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

@SuppressWarnings({"ConstantConditions", "unused"})
class Exercise1 {

    private final static String EPAM = "EPAM";
    private final static String QA_POSITION = "QA";

    @Test
    void calcTotalYearsSpentInEpam() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Long hours = employees.stream()
                .filter(Objects::nonNull)
                .flatMap(employee -> employee.getJobHistory().stream())
                .filter(Objects::nonNull)
                .filter(jh -> EPAM.equals(jh.getEmployer()))
                .mapToLong(JobHistoryEntry::getDuration)
                .sum();

        assertThat(hours, is(19L));
    }

    @Test
    void findPersonsWithQaExperience() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Set<Person> workedAsQa = employees.stream()
                .filter(Objects::nonNull)
                .flatMap(Exercise1::toPersonPosition)
                .filter(pp -> QA_POSITION.equals(pp.getPosition()))
                .map(PersonPositionTuple::getPerson)
                .collect(toSet());

        assertThat(workedAsQa, containsInAnyOrder(
                employees.get(2).getPerson(),
                employees.get(4).getPerson(),
                employees.get(5).getPerson()
        ));
    }

    private static Stream<PersonPositionTuple> toPersonPosition(Employee employee) {
        Person person = employee.getPerson();
        return employee.getJobHistory()
                .stream()
                .map(jh -> new PersonPositionTuple(person, jh.getPosition()));
    }

    @Test
    void composeFullNamesOfEmployeesUsingLineSeparatorAsDelimiter() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        String result = employees
                .stream()
                .filter(Objects::nonNull)
                .map(Employee::getPerson)
                .filter(Objects::nonNull)
                .map(Person::getFullName)
                .collect(Collectors.joining("\n"));

        assertThat(result, is(
                "Иван Мельников\n"
                + "Александр Дементьев\n"
                + "Дмитрий Осинов\n"
                + "Анна Светличная\n"
                + "Игорь Толмачёв\n"
                + "Иван Александров"));
    }

    @Test
    @SuppressWarnings("Duplicates")
    void groupPersonsByFirstPositionUsingToMap() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Map<String, Set<Person>> result = employees.stream()
                .filter(Objects::nonNull)
                .map(Exercise1::toPersonFirstPosition)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(HashMap::new, Exercise1::addToMapMutable, Exercise1::mergeMapsMutable);

        assertThat(result, hasEntry(is("dev"), contains(employees.get(0).getPerson())));
        assertThat(result, hasEntry(is("QA"), containsInAnyOrder(employees.get(2).getPerson(), employees.get(5).getPerson())));
        assertThat(result, hasEntry(is("tester"), containsInAnyOrder(employees.get(1).getPerson(), employees.get(3).getPerson(), employees.get(4).getPerson())));
    }

    private static Optional<PersonPositionTuple> toPersonFirstPosition(Employee employee) {
        Person person = employee.getPerson();
        String firstPosition = employee.getJobHistory() != null && !employee.getJobHistory().isEmpty()
                ? employee.getJobHistory().get(0).getPosition()
                : null;
        return Optional.ofNullable(firstPosition != null ? new PersonPositionTuple(person, firstPosition) : null);
    }

    private static void addToMapMutable(HashMap<String, Set<Person>> container, PersonPositionTuple tuple) {
        container.compute(tuple.getPosition(), (position, set) -> {
            set = Optional.ofNullable(set).orElseGet(HashSet::new);
            set.add(tuple.getPerson());
            return set;
        });
    }

    private static void mergeMapsMutable(HashMap<String, Set<Person>> left, HashMap<String, Set<Person>> right) {
        right.forEach((position, people) -> left.merge(position, people, (resultSet, rightSet) -> {
            resultSet.addAll(rightSet);
            return resultSet;
        }));
    }

    @Test
    @SuppressWarnings("Duplicates")
    void groupPersonsByFirstPositionUsingGroupingByCollector() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Map<String, Set<Person>> result = employees.stream()
                .filter(Objects::nonNull)
                .map(Exercise1::toPersonFirstPosition)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(groupingBy(PersonPositionTuple::getPosition, mapping(PersonPositionTuple::getPerson, toSet())));

        assertThat(result, hasEntry(is("dev"), contains(employees.get(0).getPerson())));
        assertThat(result, hasEntry(is("QA"), containsInAnyOrder(employees.get(2).getPerson(), employees.get(5).getPerson())));
        assertThat(result, hasEntry(is("tester"), containsInAnyOrder(employees.get(1).getPerson(), employees.get(3).getPerson(), employees.get(4).getPerson())));
    }

    private static List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        new Person("Иван", "Мельников", 30),
                        Arrays.asList(
                                new JobHistoryEntry(2, "dev", "EPAM"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Александр", "Дементьев", 28),
                        Arrays.asList(
                                new JobHistoryEntry(1, "tester", "EPAM"),
                                new JobHistoryEntry(1, "dev", "EPAM"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Дмитрий", "Осинов", 40),
                        Arrays.asList(
                                new JobHistoryEntry(3, "QA", "yandex"),
                                new JobHistoryEntry(1, "QA", "mail.ru"),
                                new JobHistoryEntry(1, "dev", "mail.ru")
                        )),
                new Employee(
                        new Person("Анна", "Светличная", 21),
                        Collections.singletonList(
                                new JobHistoryEntry(1, "tester", "T-Systems")
                        )),
                new Employee(
                        new Person("Игорь", "Толмачёв", 50),
                        Arrays.asList(
                                new JobHistoryEntry(5, "tester", "EPAM"),
                                new JobHistoryEntry(6, "QA", "EPAM")
                        )),
                new Employee(
                        new Person("Иван", "Александров", 33),
                        Arrays.asList(
                                new JobHistoryEntry(2, "QA", "T-Systems"),
                                new JobHistoryEntry(3, "QA", "EPAM"),
                                new JobHistoryEntry(1, "dev", "EPAM")
                        ))
        );
    }
}
