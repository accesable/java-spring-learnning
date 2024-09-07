package com.example.learningjava.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class JsonResponseMessage {
    private String code;
    private String Msg;

}
