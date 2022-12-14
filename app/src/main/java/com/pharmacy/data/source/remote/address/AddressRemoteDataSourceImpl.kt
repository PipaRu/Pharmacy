package com.pharmacy.data.source.remote.address

import com.pharmacy.common.extensions.flowOf
import com.pharmacy.data.model.Address
import kotlinx.coroutines.flow.Flow

class AddressRemoteDataSourceImpl : AddressRemoteDataSource {

    override fun search(query: String): Flow<List<Address>> {
        return flowOf {
            mockedAddresses.filter { address ->
                address.name.contains(other = query, ignoreCase = true)
            }
        }
    }

    private companion object {
        val mockedAddresses = listOf(
            "843397, Тверская область, город Серебряные Пруды, пл. Домодедовская, 40",
            "186878, Нижегородская область, город Павловский Посад, наб. Сталина, 77",
            "824673, Магаданская область, город Чехов, въезд Домодедовская, 65",
            "651272, Кировская область, город Павловский Посад, въезд Ломоносова, 33",
            "810639, Костромская область, город Шатура, пер. Домодедовская, 69",
            "602806, Московская область, город Домодедово, наб. Балканская, 11",
            "775569, Вологодская область, город Ногинск, наб. Гоголя, 24",
            "675150, Курганская область, город Коломна, пл. Ладыгина, 63",
            "204240, Нижегородская область, город Волоколамск, пл. 1905 года, 02",
            "970125, Ленинградская область, город Видное, пер. 1905 года, 14",
            "626142, Калужская область, город Солнечногорск, пр. Чехова, 33",
            "045650, Ярославская область, город Шаховская, ул. Будапештсткая, 87",
            "093719, Тверская область, город Орехово-Зуево, проезд Будапештсткая, 68",
            "304796, Новгородская область, город Щёлково, пр. Славы, 84",
            "380031, Сахалинская область, город Талдом, ул. Ломоносова, 75",
            "777670, Мурманская область, город Шаховская, пер. Ладыгина, 68",
            "952083, Пензенская область, город Балашиха, пер. Чехова, 35",
            "308336, Тульская область, город Щёлково, пер. Сталина, 79",
            "346423, Московская область, город Воскресенск, проезд Ломоносова, 49",
            "563612, Тверская область, город Кашира, пер. Ленина, 95",
            "185330, Пензенская область, город Коломна, бульвар Бухарестская, 38",
            "270355, Самарская область, город Клин, въезд Славы, 94",
            "895002, Иркутская область, город Коломна, проезд Ломоносова, 83",
            "699339, Орловская область, город Солнечногорск, шоссе Домодедовская, 77",
            "825689, Нижегородская область, город Солнечногорск, ул. Чехова, 96",
            "885207, Иркутская область, город Раменское, пер. Сталина, 91",
            "237889, Нижегородская область, город Солнечногорск, пр. Ленина, 16",
            "604456, Курганская область, город Наро-Фоминск, наб. 1905 года, 98",
            "428097, Омская область, город Орехово-Зуево, проезд Космонавтов, 46",
            "935704, Магаданская область, город Дорохово, ул. Чехова, 34",
            "316914, Ульяновская область, город Егорьевск, пр. Бухарестская, 68",
            "328763, Сахалинская область, город Одинцово, бульвар Космонавтов, 74",
            "467140, Калужская область, город Чехов, ул. Домодедовская, 45",
            "488965, Омская область, город Волоколамск, ул. Будапештсткая, 97",
        ).mapIndexed { index, address ->
            Address(
                id = index.toLong(),
                name = address
            )
        }
    }

}