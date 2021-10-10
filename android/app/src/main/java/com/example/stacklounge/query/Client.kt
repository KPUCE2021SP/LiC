package com.example.stacklounge.query

import android.content.Context
import com.apollographql.apollo.ApolloClient

val apolloClient = ApolloClient.builder()
    .serverUrl("http://15.164.18.220:8000/graphql")
    .build()