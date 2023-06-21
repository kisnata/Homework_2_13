package pro.sky.Homework_2_13;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pro.sky.Homework_2_13.exception.*;
import pro.sky.Homework_2_13.model.Employee;
import pro.sky.Homework_2_13.service.EmployeeService;
import pro.sky.Homework_2_13.service.ValidatorService;

public class EmployeeServiceTest {

  private final EmployeeService employeeService = new EmployeeService(new ValidatorService());

  @BeforeEach
  public void beforeEach() {
    employeeService.add("Светлана", "Светина", 1, 13000);
    employeeService.add("Людмила", "Иванова", 2, 10000);
    employeeService.add("Нина", "Кукушкина", 3, 20000);
  }

  @AfterEach
  public void afterEach() {
    employeeService.getAll()
        .forEach(employee -> employeeService.remove(employee.getName(), employee.getSurname()));
  }

  public static Stream<Arguments> addWithIncorrectNameTestParams() {
    return Stream.of(
        Arguments.of("Светлана1"),
        Arguments.of("Светлана!"),
        Arguments.of("Светлана@")
    );
  }

  public static Stream<Arguments> addWithIncorrectSurnameTestParams() {
    return Stream.of(
        Arguments.of("Светина1"),
        Arguments.of("Светина!"),
        Arguments.of("Светина@")
    );
  }

  @Test
  public void addTest() {
    int beforeCount = employeeService.getAll().size();
    Employee expected = new Employee("Svetlana", "Svetina", 1, 13000);

    Assertions.assertThat(employeeService.add("Svetlana", "Svetina", 1, 13000))
        .isEqualTo(expected)
        .isIn(employeeService.getAll());
    Assertions.assertThat(employeeService.getAll()).hasSize(beforeCount + 1);
    Assertions.assertThat(employeeService.find("Svetlana", "Svetina")).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("addWithIncorrectNameTestParams")
  public void addWithIncorrectNameTest(String incorrectName) {
    Assertions.assertThatExceptionOfType(IncorrectNameException.class)
        .isThrownBy(() -> employeeService.add(incorrectName, "Светина", 1, 130000));
  }

  @ParameterizedTest
  @MethodSource("addWithIncorrectSurnameTestParams")
  public void addWithIncorrectSurnameTest(String incorrectSurname) {
    Assertions.assertThatExceptionOfType(IncorrectSurnameException.class)
        .isThrownBy(() -> employeeService.add("Светлана", incorrectSurname, 1, 130000));
  }

  @Test
  public void addWhenAlreadyExistsTest() {
    Assertions.assertThatExceptionOfType(EmployeeAlreadyAddedException.class)
        .isThrownBy(() -> employeeService.add("Светлана", "Светина", 1, 13000));
  }

  @Test
  public void addWhenStorageIsFullTest() {
    Stream.iterate(1, i -> i + 1)
        .limit(7)
        .map(number -> new Employee(
                "Светлана" + ((char) ('а' + number)),
                "Светина" + ((char) ('а' + number)),
                number,
                13000 + number
            )
        )
        .forEach(employee ->
            employeeService.add(
                employee.getName(),
                employee.getSurname(),
                employee.getDepartment(),
                employee.getSalary()
            )
        );

    Assertions.assertThatExceptionOfType(EmployeeStorageIsFullException.class)
        .isThrownBy(() -> employeeService.add("Мария", "Киселева", 1, 10000));
  }

  @Test
  public void removeTest() {
    int beforeCount = employeeService.getAll().size();
    Employee expected = new Employee("Светлана", "Светина", 1, 13000);

    Assertions.assertThat(employeeService.remove("Светлана", "Светина"))
        .isEqualTo(expected)
        .isNotIn(employeeService.getAll());
    Assertions.assertThat(employeeService.getAll()).hasSize(beforeCount - 1);
    Assertions.assertThatExceptionOfType(EmployeeNotFoundException.class)
        .isThrownBy(() -> employeeService.find("Светлана", "Светина"));
  }

  @Test
  public void removeWhenNotFoundTest() {
    Assertions.assertThatExceptionOfType(EmployeeNotFoundException.class)
        .isThrownBy(() -> employeeService.find("Мария", "Киселева"));
  }

  @Test
  public void findTest() {
    int beforeCount = employeeService.getAll().size();
    Employee expected = new Employee("Светлана", "Светина", 1, 13000);

    Assertions.assertThat(employeeService.find("Светлана", "Светина"))
        .isEqualTo(expected)
        .isIn(employeeService.getAll());
    Assertions.assertThat(employeeService.getAll()).hasSize(beforeCount);
  }

  @Test
  public void findWhenNotFoundTest() {
    Assertions.assertThatExceptionOfType(EmployeeNotFoundException.class)
        .isThrownBy(() -> employeeService.find("Мария", "Киселева"));
  }

  @Test
  public void getAllTest() {
    Assertions.assertThat(employeeService.getAll())
        .hasSize(3)
        .containsExactlyInAnyOrder(
            new Employee("Ольга", "Иванова", 1, 17000),
            new Employee("Мария", "Киселева", 3, 19000),
            new Employee("ЕСветлана", "Светина", 1, 13000)
        );
  }

}
