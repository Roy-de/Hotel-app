package com.example.hotelapp.DTO.Comments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String username;
    private String rating;
    private String comment;
    private int hotel_id;
    private Date created_at;
}