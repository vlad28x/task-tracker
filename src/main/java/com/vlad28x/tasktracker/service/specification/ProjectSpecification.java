package com.vlad28x.tasktracker.service.specification;

import com.vlad28x.tasktracker.dto.FilterNodeDto;
import com.vlad28x.tasktracker.entity.Project;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProjectSpecification {

    Specification<Project> getSpecificationFromFilters(List<FilterNodeDto.Filter> filters);

}
