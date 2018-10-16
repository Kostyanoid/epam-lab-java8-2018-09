package streams.part1.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"ConstantConditions", "unused"})
class Exercise2 {

    private static final int BASE_SALARY = 75_000;
    private static final int PERK_PERCENT = 20;

    @Test
    void calcAverageAgeOfEmployees() {
        List<Employee> employees = getEmployees();

        Double expected = employees.stream()
                .filter(Objects::nonNull)
                .map(Employee::getPerson)
                .filter(Objects::nonNull)
                .mapToInt(Person::getAge)
                .summaryStatistics()
                .getAverage();

        assertThat(expected, Matchers.closeTo(33.66, 0.1));
    }

    @Test
    void findPersonWithLongestFullName() {
        List<Employee> employees = getEmployees();

        Person expected = employees.stream()
                .filter(Objects::nonNull)
                .map(Employee::getPerson)
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(p -> p.getFullName().length()))
                .orElseGet(null);


        assertThat(expected, Matchers.is(employees.get(1).getPerson()));
    }

    @Test
    void findEmployeeWithMaximumDurationAtOnePosition() {
        List<Employee> employees = getEmployees();

        Employee expected = employees.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(e -> e.getJobHistory().stream()
                                .mapToInt(JobHistoryEntry::getDuration)
                                .max()
                                .orElse(Integer.MIN_VALUE)))
                .orElseGet(null);

        assertThat(expected, Matchers.is(employees.get(4)));
    }

    /**
     * Вычислить общую сумму заработной платы для сотрудников.
     * Базовая ставка каждого сотрудника составляет 75_000.
     * Если на текущей позиции (последняя в списке) он работает больше трех лет - ставка увеличивается на 20%
     */
    @Test
    void calcTotalSalaryWithCoefficientWorkExperience() {
        List<Employee> employees = getEmployees();

        Double expected = employees.stream()
                .filter(Objects::nonNull)
                .map(Employee::getJobHistory)
                .filter(jhl -> jhl.size() > 0)
                .map(jhl -> jhl.get(0))
                .filter(Objects::nonNull)
                .mapToInt(JobHistoryEntry::getDuration)
                .mapToDouble(d -> d <= 3 ? BASE_SALARY : BASE_SALARY / 100 * (100 + PERK_PERCENT))
                .sum();

        assertThat(expected, Matchers.closeTo(465000.0, 0.001));
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