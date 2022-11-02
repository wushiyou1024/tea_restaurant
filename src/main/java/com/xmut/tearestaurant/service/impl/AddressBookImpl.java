package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.entity.AddressBook;
import com.xmut.tearestaurant.mapper.AddressBookMapper;
import com.xmut.tearestaurant.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-11-01 21:34
 */
@Service
public class AddressBookImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
