package com.example.kbocombo.mock.infra

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.infra.ComboRepository

class FakeComboRepository : BaseFakeRepository<Combo>(Combo::class), ComboRepository {


}
