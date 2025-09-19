package com.currenjin.account.repository

import com.currenjin.account.domain.Account
import java.util.Optional

interface AccountRepository {
    fun findById(id: Long): Optional<Account>

    fun save(account: Account): Account
}
