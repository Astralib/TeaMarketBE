package com.mi.teamarket.controller;

import com.mi.teamarket.entity.Address;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressMapper addressMapper;

    @GetMapping("/getAddresses/{userId}")
    List<Address> getAddresses(@PathVariable Integer userId) {
        return addressMapper.getUserAddresses(userId);
    }

    @PostMapping("/addAddress")
    Status addAddress(@RequestParam Integer userId, @RequestParam String address) {
        var addressObj = new Address();
        addressObj.setUserId(userId);
        addressObj.setAddress(address);
        addressMapper.insertOrUpdate(addressObj);
        return Status.getSuccessInstance("地址添加成功");
    }

    @PostMapping("/delAddress")
    Status delAddress(@RequestParam Integer addressId) {
        addressMapper.deleteById(addressId);
        return Status.getSuccessInstance("地址删除成功");
    }

    @PostMapping("/chooseAddress")
    Status chooseAddress(@RequestParam Integer addressId) {
        addressMapper.updateUserAddressByAddressId(addressId);
        return Status.getSuccessInstance("地址选择成功");
    }
}
