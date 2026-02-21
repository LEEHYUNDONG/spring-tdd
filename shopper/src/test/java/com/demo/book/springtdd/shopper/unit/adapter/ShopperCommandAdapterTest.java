package com.demo.book.springtdd.shopper.unit.adapter;

import com.demo.book.springtdd.shopper.adapter.out.persistence.ShopperCommandAdapter;
import com.demo.book.springtdd.shopper.adapter.out.persistence.entity.ShopperJpaEntity;
import com.demo.book.springtdd.shopper.adapter.out.persistence.repository.JpaShopperRepository;
import com.demo.book.springtdd.shopper.domain.Shopper;
import com.demo.book.springtdd.shopper.domain.ShopperId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShopperCommandAdapterTest {

    @Mock
    private JpaShopperRepository jpaShopperRepository;

    private ShopperCommandAdapter shopperCommandAdapter;

    @BeforeEach
    void setup() {
        shopperCommandAdapter = new ShopperCommandAdapter(jpaShopperRepository);
    }

    @Test
    void create_shouldConvertDomainToJpaEntityAndSave() {
        // arrange
        Shopper shopper = new Shopper();
        shopper.setId(ShopperId.generate());
        shopper.setEmail("test@email.com");
        shopper.setUsername("testuser");
        shopper.setHashedPassword("hashedPassword123");

        // act
        shopperCommandAdapter.create(shopper);

        // assert
        ArgumentCaptor<ShopperJpaEntity> captor = ArgumentCaptor.forClass(ShopperJpaEntity.class);
        verify(jpaShopperRepository).save(captor.capture());

        ShopperJpaEntity savedEntity = captor.getValue();
        assertThat(savedEntity.getId()).isEqualTo(shopper.getId());
        assertThat(savedEntity.getEmail()).isEqualTo(shopper.getEmail());
        assertThat(savedEntity.getUsername()).isEqualTo(shopper.getUsername());
        assertThat(savedEntity.getHashedPassword()).isEqualTo(shopper.getHashedPassword());
    }

}
