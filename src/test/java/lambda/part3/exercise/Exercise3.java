package lambda.part3.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.theInstance;

@SuppressWarnings({"unused", "ConstantConditions"})
class Exercise3 {

    private static class LazyMapHelper<T, R> {

        private List<T> source;
        private Function<T, R> mapper;

        public static <T> LazyMapHelper<T, T> from(List<T> list) {
            LazyMapHelper<T, T> lm = new LazyMapHelper<>();
            lm.source = list;
            lm.mapper = Function.identity();
            return lm;
        }

        public List<R> force() {
            List<R> result = new ArrayList<>();
            source.forEach(e -> result.add(mapper.apply(e)));
            return result;
        }

        public <R2> LazyMapHelper<T, R2> map(Function<R, R2> mapping) {
            LazyMapHelper<T, R2> lm = new LazyMapHelper<>();
            lm.source = source;
            lm.mapper = mapper.andThen(mapping);
            return lm;
        }
    }

    @Test
    void mapEmployeesToLengthOfTheirFullNamesUsingLazyMapHelper() {
        List<Employee> employees = getEmployees();

        List<Integer> lengths = LazyMapHelper.from(employees)
                                        .map(Employee::getPerson)
                                        .map(Person::getFullName)
                                        .map(String::length)
                                        .force();

        assertThat(lengths, contains(14, 19, 14, 15, 14, 16));
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
