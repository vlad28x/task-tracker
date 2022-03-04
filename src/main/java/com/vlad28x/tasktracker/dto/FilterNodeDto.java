package com.vlad28x.tasktracker.dto;

import com.vlad28x.tasktracker.entity.enums.QueryOperator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
public class FilterNodeDto {

    private List<Filter> filters;
    private Sorting sort;

    @Getter
    @Setter
    public static class Filter {

        private String field;
        private QueryOperator operator;
        private String value;
        private List<String> values;

    }

    @Getter
    @Setter
    public static class Sorting {

        private String field;
        private Sort.Direction direction;

    }
}
