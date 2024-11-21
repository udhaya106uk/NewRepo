package com.onward.hrservice.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onward.hrservice.entity.EmployeeMaster;

public interface EmployeeMasterRepository extends JpaRepository<EmployeeMaster, String> {

	EmployeeMaster findByEmpNumber(String empNumber);

	EmployeeMaster findByEmpCode(Integer empCode);

	@Query(value = "SELECT * FROM emp_master ORDER BY emp_code DESC LIMIT 1", nativeQuery = true)
	EmployeeMaster getLastRecord();

	@Query("SELECT A FROM EmployeeMaster A WHERE empCode in (:empNumber)")
	List<EmployeeMaster> getHrpb(List<Integer> empNumber);

	@Query("SELECT A FROM EmployeeMaster A WHERE empName like %:reportingManagerName%")
	List<EmployeeMaster> getReportingManager(String reportingManagerName);

	@Query("SELECT empName FROM EmployeeMaster A WHERE empNumber =:hrbpId")
	String getHrbpName(String hrbpId);

	@Query(value = "SELECT * FROM emp_master WHERE DATE(created_on) >= CURDATE() - INTERVAL 1 DAY", nativeQuery = true)
	List<EmployeeMaster> findAllDetails();

	@Query(value = "SELECT * FROM emp_master WHERE emp_doj BETWEEN :fromDate AND :toDate", nativeQuery = true)
	List<EmployeeMaster> findByDoj1(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

	@Query(value = "SELECT d from EmployeeMaster d where CAST( d.empDoj  as LocalDate) BETWEEN :fromDate AND :toDate")
	List<EmployeeMaster> findByDoj(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

	@Query(value = "select * from emp_master where (emp_number = :employeeNumber OR emp_name like %:employeeName%)", nativeQuery = true)
	List<EmployeeMaster> findBySearchEmployee(@Param("employeeNumber") String employeeNumber,
			@Param("employeeName") String employeeName);

	@Query(value = "select * from emp_master where emp_name like %:employeeName%", nativeQuery = true)
	List<EmployeeMaster> findBySearchEmployeeName(@Param("employeeName") String employeeName);

	@Query(value = "select * from emp_master where emp_number = :employeeNumber ", nativeQuery = true)
	List<EmployeeMaster> findBySearchEmployeeNumber(@Param("employeeNumber") String employeeNumber);

	@Query(value = "select * from emp_master where (emp_number = :employeeNumber AND emp_name like %:employeeName%)", nativeQuery = true)
	List<EmployeeMaster> findBySearchEmployeeNumberAndName(@Param("employeeNumber") String employeeNumber,
			@Param("employeeName") String employeeName);
}
