package com.vlad28x.tasktracker.service.specification;

import com.vlad28x.tasktracker.dto.FilterDto;
import com.vlad28x.tasktracker.entity.Project;
import com.vlad28x.tasktracker.entity.enums.ProjectStatus;
import com.vlad28x.tasktracker.exception.BadRequestException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class ProjectSpecificationImpl implements ProjectSpecification {

    @Override
    public Specification<Project> getSpecificationFromFilters(List<FilterDto> filters) {
        Specification<Project> specification = where(createSpecification(filters.remove(0)));
        for (FilterDto filter : filters) {
            specification = specification.and(createSpecification(filter));
        }
        return specification;
    }

    private Specification<Project> createSpecification(FilterDto filter) {
        switch (filter.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(filter.getField()),
                                castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValue()));
            case NOT_EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(filter.getField()),
                                castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValue()));
            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(filter.getField()),
                                (Number) castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValue()));
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(filter.getField()),
                                (Number) castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValue()));
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(filter.getField()), "%" + filter.getValue() + "%");
                /*case BETWEEN:
                    return (root, query, criteriaBuilder) ->
                            criteriaBuilder.between(root.get(filter.getField()),
                                    (String) castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValues().get(0)),
                                    (String) castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValues().get(1)));*/
            default:
                throw new BadRequestException("Unsupported operation");
        }
    }

    private <T> Object castToRequiredType(Class<T> fieldType, String value) {
        if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (fieldType.isAssignableFrom(LocalDate.class)) {
            return LocalDate.parse(value);
        } else if (fieldType.isAssignableFrom(ProjectStatus.class)) {
            return ProjectStatus.valueOf(value);
        }
        return null;
    }

}
