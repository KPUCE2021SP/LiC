package com.example.stacklounge.query

import android.content.Context
import com.apollographql.apollo.ApolloClient

val apolloClient = ApolloClient.builder()
    .serverUrl("http://3.35.48.232:8000/graphql")
    .build()