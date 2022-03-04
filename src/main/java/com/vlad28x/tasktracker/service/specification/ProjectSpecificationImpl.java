package com.vlad28x.tasktracker.service.specification;

import com.vlad28x.tasktracker.dto.FilterNodeDto;
import com.vlad28x.tasktracker.entity.Project;
import com.vlad28x.tasktracker.entity.enums.ProjectStatus;
import com.vlad28x.tasktracker.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class ProjectSpecificationImpl implements ProjectSpecification {

    private static final Logger log = LoggerFactory.getLogger(ProjectSpecificationImpl.class);

    @Override
    public Specification<Project> getSpecificationFromFilters(List<FilterNodeDto.Filter> filters) {
        if (filters == null || filters.size() == 0) {
            return where(null);
        }
        Specification<Project> specification = where(createSpecification(filters.remove(0)));
        for (FilterNodeDto.Filter filter : filters) {
            specification = specification.and(createSpecification(filter));
        }
        return specification;
    }

    private Specification<Project> createSpecification(FilterNodeDto.Filter filter) {
        if (filter.getField() == null || filter.getOperator() == null || (filter.getValue() == null && filter.getValues() == null)) {
            log.error("Arguments in filter must not null");
            throw new BadRequestException("Arguments in filter must not null");
        }
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
                        criteriaBuilder.greaterThan(root.get(filter.getField()),
                                (Comparable) castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValue()));
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get(filter.getField()),
                                (Comparable) castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValue()));
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(filter.getField()), "%" + filter.getValue() + "%");
            case BETWEEN:
                return (root, query, criteriaBuilder) -> {
                    if (filter.getValues().size() != 2) {
                        log.error("Bad arguments in values");
                        throw new BadRequestException("Bad arguments in values");
                    }
                    return criteriaBuilder.between(root.get(filter.getField()),
                            (Comparable) castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValues().get(0)),
                            (Comparable) castToRequiredType(root.get(filter.getField()).getJavaType(), filter.getValues().get(1)));
                };
            default:
                throw new BadRequestException("Unsupported operation");
        }
    }

    private <T> Object castToRequiredType(Class<T> fieldType, String value) {
        if (fieldType.isAssignableFrom(String.class)) {
            return value;
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (fieldType.isAssignableFrom(Long.class)) {
            return Long.valueOf(value);
        } else if (fieldType.isAssignableFrom(LocalDate.class)) {
            return LocalDate.parse(value);
        } else if (fieldType.isAssignableFrom(ProjectStatus.class)) {
            return ProjectStatus.valueOf(value);
        }
        return null;
    }

}
