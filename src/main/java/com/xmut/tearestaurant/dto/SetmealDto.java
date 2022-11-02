package com.xmut.tearestaurant.dto;

import com.xmut.tearestaurant.entity.Setmeal;
import com.xmut.tearestaurant.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
