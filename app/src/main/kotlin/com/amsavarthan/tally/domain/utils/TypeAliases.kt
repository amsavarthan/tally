package com.amsavarthan.tally.domain.utils

import com.amsavarthan.tally.domain.entity.CategoryType

typealias TransactionType = CategoryType
typealias UseCaseResult = Pair<Boolean, String>
typealias UseCaseResultWithData<T> = Pair<T, UseCaseResult>