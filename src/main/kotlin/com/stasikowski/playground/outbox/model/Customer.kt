package com.stasikowski.playground.outbox.model

import jakarta.persistence.*


@Entity
class Customer(@Column(name = "first_name") var firstname: String,
               @Column(name = "last_name") var lastname: String,
               @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null)
