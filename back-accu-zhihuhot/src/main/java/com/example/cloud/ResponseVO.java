package com.example.cloud;

import lombok.Data;

import java.util.List;

@Data
public class ResponseVO {
    private List<String> names;
    private List<Integer> values_0;
    private List<Integer> values_1;
    private List<Integer> values_2;
    private List<String> times;
}
