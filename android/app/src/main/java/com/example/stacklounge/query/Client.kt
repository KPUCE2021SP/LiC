package com.example.stacklounge.query

import android.content.Context
import com.apollographql.apollo.ApolloClient

val apolloClient = ApolloClient.builder()
    .serverUrl("http://3.35.118.152:8000/graphql")
    .build()