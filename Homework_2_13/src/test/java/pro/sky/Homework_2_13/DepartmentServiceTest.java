package pro.sky.Homework_2_13;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.Homework_2_13.exception.DepartmentNotFoundException;
import pro.sky.Homework_2_13.model.Employee;
import pro.sky.Homework_2_13.service.DepartmentService;
import pro.sky.Homework_2_13.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

  @Mock
  private EmployeeService employeeService;

  @InjectMocks
  private DepartmentService departmentService;

  public static Stream<Arguments> maxSalaryFromDepartmentParams() {
    return Stream.of(
        Arguments.of(1, 17000),
        Arguments.of(2, 16000),
        Arguments.of(3, 19000)
    );
  }

  public static Stream<Arguments> minSalaryFromDepartmentTestParams() {
    return Stream.of(
        Arguments.of(1, 12000),
        Arguments.of(2, 13000),
        Arguments.of(3, 19000)
    );
  }

  public static Stream<Arguments> sumSalaryFromDepartmentTestParams() {
    return Stream.of(
        Arguments.of(1, 29000),
        Arguments.of(2, 29000),
        Arguments.of(3, 19000),
        Arguments.of(4, 0)
    );
  }

  public static Stream<Arguments> employeesFromDepartmentTestParams() {
    return Stream.of(
        Arguments.of(
            1,
            List.of(
                new Employee("Екатерина", "Сидорова", 1, 12000),
                new Employee("Ольга", "Иванова", 1, 17000)
            )
        ),
        Arguments.of(
            2,
            List.of(
                new Employee("Мария", "Иванова", 2, 13000),
                new Employee("Анна", "Павлова", 2, 16000)
            )
        ),
        Arguments.of(
            3,
            Collections.singletonList(new Employee("Мария", "Киселева", 3, 19000))
        ),
        Arguments.of(
            4,
            Collections.emptyList()
        )
    );
  }

  @BeforeEach
  public void beforeEach() {
    Mockito.when(employeeService.getAll()).thenReturn(
        List.of(
            new Employee("Екатерина", "Сидорова", 1, 12000),
            new Employee("Мария", "Иванова", 2, 13000),
            new Employee("Ольга", "Иванова", 1, 17000),
            new Employee("Анна", "Павлова", 2, 16000),
            new Employee("Мария", "Киселева", 3, 19000)
        )
    );
  }

  @ParameterizedTest
  @MethodSource("maxSalaryFromDepartmentParams")
  public void maxSalaryFromDepartmentTest(int departmentId, double expected) {
    Assertions.assertThat(departmentService.maxSalaryFromDepartment(departmentId))
        .isEqualTo(expected);
  }

  @Test
  public void maxSalaryFromDepartmentWhenNotFoundTest() {
    Assertions.assertThatExceptionOfType(DepartmentNotFoundException.class)
        .isThrownBy(() -> departmentService.maxSalaryFromDepartment(4));
  }

  @ParameterizedTest
  @MethodSource("minSalaryFromDepartmentTestParams")
  public void minSalaryFromDepartmentTest(int departmentId, double expected) {
    Assertions.assertThat(departmentService.minSalaryFromDepartment(departmentId))
        .isEqualTo(expected);
  }

  @Test
  public void minSalaryFromDepartmentWhenNotFoundTest() {
    Assertions.assertThatExceptionOfType(DepartmentNotFoundException.class)
        .isThrownBy(() -> departmentService.minSalaryFromDepartment(4));
  }

  @ParameterizedTest
  @MethodSource("sumSalaryFromDepartmentTestParams")
  public void sumSalaryFromDepartmentTest(int departmentId, double expected) {
    Assertions.assertThat(departmentService.sumSalaryFromDepartment(departmentId))
        .isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("employeesFromDepartmentTestParams")
  public void employeesFromDepartmentTest(int departmentId, List<Employee> expected) {
    Assertions.assertThat(departmentService.employeesFromDepartment(departmentId))
        .containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  public void employeesGroupedByDepartmentTest() {
    Map<Integer, List<Employee>> expected = Map.of(
        1,
        List.of(
            new Employee("Екатерина", "Сидорова", 1, 12000),
            new Employee("Ольга", "Иванова", 1, 17000)
        ),
        2,
        List.of(
            new Employee("Мария", "Иванова", 2, 13000),
            new Employee("Анна", "Павлова", 2, 16000)
        ),
        3,
        Collections.singletonList(new Employee("Мария", "Киселева", 3, 19000))
    );
    Assertions.assertThat(departmentService.employeesGroupedByDepartment())
        .containsExactlyInAnyOrderEntriesOf(expected);
  }

}
