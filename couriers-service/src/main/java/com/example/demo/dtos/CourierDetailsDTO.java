package com.example.demo.dtos;

import com.example.demo.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierDetailsDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String personalIdNumber;
    private List<Comment> comments;
    private int yearsOfExperience;
}
