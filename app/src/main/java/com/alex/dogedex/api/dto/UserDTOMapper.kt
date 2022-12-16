package com.alex.dogedex.api.dto

import com.alex.dogedex.model.User

class UserDTOMapper {
    fun fromUserDTOToUserDomain(userDTO: UserDTO) =
        User(userDTO.id, userDTO.email, userDTO.authenticationToken)

}