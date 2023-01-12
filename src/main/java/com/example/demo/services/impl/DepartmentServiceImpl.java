package com.example.demo.services.impl;

import com.example.demo.exceptions.ErrorDataAccessException;
import com.example.demo.exceptions.ErrorNotFound;
import com.example.demo.models.Department;
import com.example.demo.models.dto.DepartmentDTO;
import com.example.demo.repositories.DeparmentRepository;
import com.example.demo.services.IDepartmentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements IDepartmentService {
    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DeparmentRepository departmentRepository;
    private final ModelMapper modelMapper = new ModelMapper();

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
            log.info("Success in create");
            Department department = this.modelMapper.map(departmentDTO, Department.class); // Convierte DTO en Department
            return this.departmentRepository.save(department);
        } catch (DataAccessException e) {
            log.info("Failed in create");
            log.info(e.getMessage());
            throw new ErrorDataAccessException("Error creating department.");
        }
    }

    @Override
    public Department update(Department department, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }
}
