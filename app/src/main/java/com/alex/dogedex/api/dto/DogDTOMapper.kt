package com.alex.dogedex.api.dto

import com.alex.dogedex.Dog

class DogDTOMapper {
    private fun fromDogDTOToDogDomain(dogDTO: DogDTO):Dog{
        return Dog(
            dogDTO.id,
            dogDTO.index,
            dogDTO.name,
            dogDTO.type,
            dogDTO.heightFemale,
            dogDTO.heightMale,
            dogDTO.imageUrl,
            dogDTO.lifeExpectancy,
            dogDTO.temperament,
            dogDTO.weightFemale,
            dogDTO.weightMale
        )
    }


    fun fromDogDTOListToDogDomainList(dogDTOList: List<DogDTO>): List<Dog>{
        //El map itera para cada uno de los elementos de la lista de
        // dogDTO y aplica una transformación de cada elemento regresandonos el resultado de todas
        // las transformaciones de cada uno de los elementos
        return dogDTOList.map {
            fromDogDTOToDogDomain(it) }
    }
}