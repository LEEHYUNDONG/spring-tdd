package com.demo.book.springtdd.shopper.unit.application.usecase;

import com.demo.book.springtdd.shopper.application.port.out.CreateShopperPort;
import com.demo.book.springtdd.shopper.application.port.out.ReadShopperPort;
import com.demo.book.springtdd.shopper.application.usecase.ShopperService;
import com.demo.book.springtdd.shopper.domain.CreateShopperCommand;
import com.demo.book.springtdd.shopper.domain.ReadShopperQuery;
import com.demo.book.springtdd.shopper.domain.Shopper;
import com.demo.book.springtdd.shopper.domain.ShopperInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ShopperServiceTest {

    @Mock
    private ReadShopperPort readShopperPort;

    @Mock
    private CreateShopperPort createShopperPort;

    private ShopperService shopperService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        shopperService = new ShopperService(passwordEncoder, createShopperPort, readShopperPort);
    }

    @Test
    void signUp() {
        var command = new CreateShopperCommand("test@email.com", "leehyundong", "password123");

        Shopper shopper = shopperService.signUp(command);

        verify(createShopperPort, times(1)).create(shopper);
    }

    @Test
    void ifDuplicatedEmailExistThenThrowException() {
        var command = new CreateShopperCommand("test123@email.com", "leehyundong", "password");
        Shopper shopper = shopperService.signUp(command);

        when(readShopperPort.findByEmail(command.email())).thenReturn(shopper);

        assertThatThrownBy(() -> shopperService.signUp(command)).isInstanceOf(DuplicateKeyException.class);
    }


    @Test
    void shopperRead() {
        var command = new CreateShopperCommand("test123@email.com", "leehyundong", "password");
        Shopper shopper = Shopper.register(command, passwordEncoder);

        var request = new ReadShopperQuery(shopper.getId());

        when(readShopperPort.findById(shopper.getId())).thenReturn(Optional.of(shopper));

        ShopperInfo result = shopperService.read(request);

        assertThat(result.id()).isEqualTo(shopper.getId());
        assertThat(result.email()).isEqualTo(command.email());
        assertThat(result.username()).isEqualTo(command.username());
    }


}