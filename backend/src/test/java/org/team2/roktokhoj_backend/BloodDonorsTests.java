package org.team2.roktokhoj_backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.team2.roktokhoj_backend.controllers.BloodDonorController;

@DataJpaTest
@SpringBootTest
// TODO: Add mock tests...
@ExtendWith(MockitoExtension.class)
public class BloodDonorsTests {
    @Mock
    private BloodDonorController controller;

    @Test
    public void registerBloodDonor() {

    }
}