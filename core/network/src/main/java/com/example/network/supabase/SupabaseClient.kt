package com.example.network.supabase

import com.example.network.utils.ApiConstants
import com.example.network.utils.NetworkConstants
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    val supabase = createSupabaseClient(
        supabaseUrl = ApiConstants.Supabase.BASE_URL,
        supabaseKey = NetworkConstants.API_KEY
    ) {

        install(Auth)

        install(Postgrest)
    }
}