package pro.sky.Homework_2_13.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.Homework_2_13.model.Employee;
import pro.sky.Homework_2_13.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {

  private final DepartmentService departmentService;

  public DepartmentController(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }

  @GetMapping(value = "/{id}/employees")
  public List<Employee> employeesFromDepartment(@PathVariable int id) {
    return departmentService.employeesFromDepartment(id);
  }

  @GetMapping("/{id}/salary/sum")
  public double sumSalaryFromDepartment(@PathVariable int id) {
    return departmentService.sumSalaryFromDepartment(id);
  }

  @GetMapping("/{id}/salary/max")
  public double maxSalaryFromDepartment(@PathVariable int id) {
    return departmentService.maxSalaryFromDepartment(id);
  }

  @GetMapping("/{id}/salary/min")
  public double minSalaryFromDepartment(@PathVariable int id) {
    return departmentService.minSalaryFromDepartment(id);
  }

  @GetMapping("/employees")
  public Map<Integer, List<Employee>> employeesGroupedByDepartment() {
    return departmentService.employeesGroupedByDepartment();
  }

}
