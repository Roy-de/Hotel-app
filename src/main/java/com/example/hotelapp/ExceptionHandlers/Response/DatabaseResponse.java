package com.example.hotelapp.ExceptionHandlers.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseResponse {
    private String errCode;
    private String errMessage;
    private LocalDateTime timestamp;
}
