package com.xmut.tearestaurant.dto;


import com.xmut.tearestaurant.entity.Dish;
import com.xmut.tearestaurant.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
