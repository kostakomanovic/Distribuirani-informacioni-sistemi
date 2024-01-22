package com.example.demo.utils;

import com.example.demo.dtos.CommentDTO;
import com.example.demo.dtos.CourierBasicInfoDTO;
import com.example.demo.dtos.CourierDetailsDTO;
import com.example.demo.models.Comment;
import com.example.demo.models.Courier;

import java.util.ArrayList;

public class Mapper {
    public static CourierBasicInfoDTO entityToBasicInfoDto(Courier courier) {
        return new CourierBasicInfoDTO(courier.getId(), courier.getFirstName(), courier.getLastName());
    }

    public static CourierDetailsDTO entityToDetailsDto(Courier courier) {
        return new CourierDetailsDTO(courier.getId(), courier.getFirstName(), courier.getLastName(),
                courier.getPersonalIdNumber(), courier.getComments(), courier.getYearsOfExperience());
    }

    public static Courier detailsDtoToEntity(CourierDetailsDTO courierDetailsDTO) {
        return new Courier(null, courierDetailsDTO.getFirstName(), courierDetailsDTO.getLastName(),
                courierDetailsDTO.getPersonalIdNumber(), new ArrayList<>(), courierDetailsDTO.getYearsOfExperience());
    }

    public static Comment commentDtoToEntity(CommentDTO commentDTO) {
        return new Comment(null, null, commentDTO.getText(), commentDTO.isPositive());
    }
}
