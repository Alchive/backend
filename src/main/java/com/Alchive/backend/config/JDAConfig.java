package com.Alchive.backend.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class JDAConfig {
    @Value("${DISCORD_BOT_TOKEN}")
    private String discordBotToken;
    @Bean
    public JDA jda() throws LoginException, RateLimitedException {
        return JDABuilder.createDefault(discordBotToken)
                .build();
    }
}
