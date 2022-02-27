package com.vlad28x.tasktracker.dto;

import com.vlad28x.tasktracker.entity.enums.QueryOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterDto {

    private String field;
    private QueryOperator operator;
    private String value;
    private List<String> values;

}
