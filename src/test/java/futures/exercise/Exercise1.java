package futures.exercise;

import lambda.data.Employee;
import lambda.data.Person;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.lang.System.lineSeparator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings({"unused", "UnnecessaryLocalVariable", "ConstantConditions", "UnusedAssignment"})
class Exercise1 {

    @Test
    void vanillaFutureExample() throws Exception {
        test(() -> {
            Employee result = null;

            // TODO использовать Executors.newFixedThreadPool(4), Future<T> и метод getEmployee: Person -> Employee
            ExecutorService executor = Executors.newFixedThreadPool(4);

            Future<Employee> employeeFuture = executor.submit(() -> {
                String personFullName[] = getPersonNameAndSurnameFromUser().split(" ");
                if (personFullName == null || personFullName.length < 2) return null;

                return getEmployee(getPerson(personFullName[0], personFullName[1]));
            });

            while (!employeeFuture.isDone()) {
                TimeUnit.SECONDS.sleep(1);
            }

            return employeeFuture.get();
        });
    }

    @Test
    void completableFutureExample() throws Exception {
        test(() -> {
            Employee result = null;

            // TODO использовать CompletableFuture<T> и метод getEmployeeInFuture: Person -> CompletableFuture<Employee>

            String personFullName[] = getPersonNameAndSurnameFromUser().split(" ");
            if (personFullName == null || personFullName.length < 2) return null;

            CompletableFuture<Employee> employeeCompletableFuture
                    = CompletableFuture.supplyAsync(() -> getPerson(personFullName[0], personFullName[1]))
                    .thenApplyAsync(Exercise1::getEmployee);

            employeeCompletableFuture.join();

            return employeeCompletableFuture.get();
        });
    }

    private static void test(Callable<Employee> task) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(("Дмитрий Сашков" + lineSeparator()).getBytes());

        Employee result = performWithCustomSystemIn(task, input);

        assertThat(result, is(new Employee(new Person("Дмитрий", "Сашков", 24), Collections.emptyList())));
    }

    private static <T> T performWithCustomSystemIn(Callable<T> task, InputStream input) throws Exception {
        InputStream original = System.in;
        try {
            System.setIn(input);
            return task.call();
        } finally {
            System.setIn(original);
        }
    }

    @SneakyThrows
    private static String getPersonNameAndSurnameFromUser() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return reader.readLine();
        }
    }

    @SneakyThrows
    private static Person getPerson(String name, String surname) {
        Person person;
        // For example load from another service
        TimeUnit.SECONDS.sleep(2);
        return person = new Person(name, surname, 24);
    }

    // TODO использовать в vanillaFutureExample
    @SneakyThrows
    private static Employee getEmployee(Person person) {
        Employee employee;
        TimeUnit.SECONDS.sleep(2);         // For example load from another service
        return employee = new Employee(person, Collections.emptyList());
    }

    // TODO использовать в completableFutureExample
    @SneakyThrows
    private static CompletableFuture<Employee> getEmployeeInFuture(Person person) {
        Employee employee;
        TimeUnit.SECONDS.sleep(2);         // For example load from another service
        throw new UnsupportedOperationException();
    }
}
