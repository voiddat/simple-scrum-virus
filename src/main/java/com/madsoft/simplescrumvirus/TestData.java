package com.madsoft.simplescrumvirus;

import com.madsoft.simplescrumvirus.model.User;
import com.madsoft.simplescrumvirus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestData implements InitializingBean {
    private final UserRepository userRepository;

    @Override
    public void afterPropertiesSet() {
        userRepository.save(
                User.builder()
                        .username("Kowalski")
                        .build()
        );

        userRepository.save(
                User.builder()
                        .username("Nowak")
                        .build()
        );

        userRepository.save(
                User.builder()
                        .username("Smith")
                        .build()
        );

        userRepository.save(
                User.builder()
                        .username("Nowicki")
                        .build()
        );
    }
}
