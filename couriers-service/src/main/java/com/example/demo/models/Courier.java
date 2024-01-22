package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courier {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String personalIdNumber;
    private List<Comment> comments;
    private int yearsOfExperience;

}
