package com.example.hotelapp.DTO.Comments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String username;
    private String rating;
    private String hotel_name;
    private String comment;
    private int user_id;
}
