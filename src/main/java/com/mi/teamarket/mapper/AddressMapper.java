package com.mi.teamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mi.teamarket.entity.Address;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AddressMapper extends BaseMapper<Address> {

    @Select("select * from tea.address where address.user_id = #{id};")
    List<Address> getUserAddresses(@Param("id") Integer id);

    @Update("update user set user.address = #{address} where user.user_id = #{userId};")
    void updateUserAddressUserId(@Param("userId") Integer userId, @Param("address") String address);

    @Update(
            "update user set user.address = (" +
                    "    select address.address from address where address.id = #{id}" +
                    ") where user.user_id = (" +
                    "    select address.user_id from address where address.id = #{id}" +
                    ");"
    )
    void updateUserAddressByAddressId(@Param("id") Integer id);

}
