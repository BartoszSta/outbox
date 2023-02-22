package com.stasikowski.playground.outbox.repository

import com.stasikowski.playground.outbox.model.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: CrudRepository<Customer, Long>  {
}