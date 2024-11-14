package com.booking.servicebookingsys.entity;

import com.booking.servicebookingsys.dto.UserDto;
import com.booking.servicebookingsys.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

// 告訴Spring這是數據模型層的宣告
@Entity
/*
當一個物件的生命期開始被你追蹤時，你就需要為它建立身份標誌 (identity)，而這個物件我們就稱為 Entity。
Entity 最大的特徵就是有 Identity 的概念，所以常會搭配一個擁有唯一值的 ID 欄位。
這裡的 Entity 是一個物件，或者說是一個 Class，跟 Clean Architecture 中的 Entity Layer 是不同的概念。

我們總結一下 Entity 的特徵：

Entity 最重要的是他的 ID。
兩個 Entity 不論其他狀態，只要 ID 相同就是相同的物件。
除了 ID，他們其他的狀態是可變的 (mutable)。
他們可能擁有很長的壽命，甚至不會被刪除。
一個 Entity 是可變的、長壽的，所以通常會有複雜的生命週期變化，如一套 CRUD 的操作。
 */

// 標註這個object映射的資料庫，指定為"user" database
@Table(name="users")
@Data
public class User {
    // 標註 id 為主key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String lastName;

    private String phone;

    private UserRole role;

    public UserDto getDto(){
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setRole(role);
        return userDto;
    }
}
