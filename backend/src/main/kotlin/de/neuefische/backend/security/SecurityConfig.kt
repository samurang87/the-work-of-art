package de.neuefische.backend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${client.url}")
    private val clientUrl: String,
) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/api/woa", "/api/woa/**", "/api/user/**", "/api/cloudinary/**").authenticated()
                it.requestMatchers("/api/status").permitAll()
                it.anyRequest().permitAll()
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            }.exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }.oauth2Login {
                it.defaultSuccessUrl("$clientUrl/feed", true)
            }.logout {
                it.logoutSuccessUrl(clientUrl)
            }
        return http.build()
    }
}
