package com.example.demo.services.impl;

import com.example.demo.exceptions.ErrorDataAccessException;
import com.example.demo.exceptions.ErrorNotFound;
import com.example.demo.models.Department;
import com.example.demo.models.dto.DepartmentDTO;
import com.example.demo.repositories.DepartmentRepository;
import com.example.demo.services.IDepartmentService;
import com.example.demo.utils.ListDepartment;
import com.example.demo.utils.NativeQuerys;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements IDepartmentService {
    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final JdbcTemplate jdbcTemplate;

    /**
     * Obtener listado de los departamentos
     *
     * @param page    Numero de pagina
     * @param size    Cantidad de departamentos a listar
     * @param orderBy Ordenar por, (key_department, name)
     * @return
     */
    @Override
    public Page<Department> findAll(int page, int size, String orderBy) {
        log.info("Init findAll()");
        return this.departmentRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, orderBy)));
    }

    /**
     * Buscar departamento por ID
     *
     * @param id
     * @return
     */
    @Override
    public Department findById(Long id) {
        log.info("Init findById()");
        return this.departmentRepository.findById(id)
                .orElseThrow(() -> new ErrorNotFound("Department not found."));
    }

    /**
     * Crea un nuevo departamento
     *
     * @param departmentDTO
     * @return
     */
    @Override
    public Department create(DepartmentDTO departmentDTO) {
        log.info("Init create()");
        try {
            Department department = this.modelMapper.map(departmentDTO, Department.class); // Convierte DTO en Department
            return this.departmentRepository.save(department);
        } catch (DataAccessException e) {
            log.error("Failed in create");
            log.error(e.getMessage());
            throw new ErrorDataAccessException("Error creating department.");
        }
    }

    /**
     * Actualizar Departamento
     *
     * @param departmentDTO
     * @param id
     * @return
     */
    @Override
    public Department update(DepartmentDTO departmentDTO, Long id) {
        log.info("Init update()");
        Optional<Department> department = this.departmentRepository.findById(id);
        if (department.isPresent()) {
            Department departmentToPersist = department.get();
            departmentToPersist.setName(departmentDTO.getName());
            departmentToPersist.setKeyDepartment(departmentDTO.getKeyDepartment());
            return this.departmentRepository.save(departmentToPersist);
        } else {
            log.error("Department not found.");
            throw new ErrorNotFound("Department not found.");
        }
    }

    /**
     * Eliminar un departamento
     * @param id
     */
    @Override
    public void delete(Long id) {
        log.info("Init delete()");
        Optional<Department> department = this.departmentRepository.findById(id);
        if (department.isPresent()) {
            this.departmentRepository.delete(department.get());
        } else {
            log.error("Department not found.");
            throw new ErrorNotFound("Department not found.");
        }
    }

    /**
     * Listar departamento para los combos de filtros (select)
     * @return
     */
    @Override
    public List<ListDepartment> getAllDepartments() {
        log.info("Init getAllDepartments()");
        return jdbcTemplate.query(NativeQuerys.GET_ALL_DEPARTMENTS, (rs, rowNum) ->
                new ListDepartment(rs.getLong("id"), rs.getString("name"), rs.getString("key_department")));
    }
}
