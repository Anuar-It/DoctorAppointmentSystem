@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Source {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                .requestMatchers("/etc/**").hasRole("ETC")
                .anyRequest().authenticated()
            )
            .httpBasic();

        return http.build();
    }

    // Временные пользователи
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(encoder.encode("admin123"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("manager")
                        .password(encoder.encode("manager123"))
                        .roles("MANAGER")
                        .build(),
                User.withUsername("user")
                        .password(encoder.encode("user123"))
                        .roles("USER")
                        .build(),
                User.withUsername("etc")
                        .password(encoder.encode("etc123"))
                        .roles("ETC")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
