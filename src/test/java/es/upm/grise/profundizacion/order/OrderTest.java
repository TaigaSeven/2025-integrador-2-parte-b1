package es.upm.grise.profundizacion.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.upm.grise.exceptions.IncorrectItemException;

public class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void addItem_throwsException_whenPriceIsNegative() throws IncorrectItemException {
        Item item = mock(Item.class);
        when(item.getPrice()).thenReturn(-1.0);
        when(item.getQuantity()).thenReturn(1);

        assertThrows(IncorrectItemException.class, () -> order.addItem(item));
    }

    @Test
    void addItem_throwsException_whenQuantityIsZeroOrNegative() throws IncorrectItemException {
        Item item = mock(Item.class);
        when(item.getPrice()).thenReturn(10.0);
        when(item.getQuantity()).thenReturn(0);

        assertThrows(IncorrectItemException.class, () -> order.addItem(item));
    }

    @Test
    void addItem_addsNewItem_whenProductDoesNotExist() throws IncorrectItemException {
        Item item = mock(Item.class);
        Product product = mock(Product.class);

        when(product.getId()).thenReturn(1L);
        when(item.getPrice()).thenReturn(10.0);
        when(item.getQuantity()).thenReturn(2);
        when(item.getProduct()).thenReturn(product);

        order.addItem(item);

        Collection<Item> items = order.getItems();
        assertEquals(1, items.size());
        assertTrue(items.contains(item));
    }

    @Test
    void addItem_incrementsQuantity_whenProductExistsAndPriceIsSame() throws IncorrectItemException {
        Item existing = mock(Item.class);
        Item incoming = mock(Item.class);
        Product product = mock(Product.class);

        when(product.getId()).thenReturn(1L);

        when(existing.getProduct()).thenReturn(product);
        when(existing.getPrice()).thenReturn(5.0);
        when(existing.getQuantity()).thenReturn(3);

        when(incoming.getProduct()).thenReturn(product);
        when(incoming.getPrice()).thenReturn(5.0);
        when(incoming.getQuantity()).thenReturn(2);

        order.getItems().add(existing);

        order.addItem(incoming);

        verify(existing).setQuantity(3 + 2);
        assertEquals(1, order.getItems().size());
    }

    @Test
    void addItem_addsNewItem_whenProductExistsButPriceDiffers() throws IncorrectItemException {
        Item existing = mock(Item.class);
        Item incoming = mock(Item.class);
        Product product = mock(Product.class);

        when(product.getId()).thenReturn(1L);

        when(existing.getProduct()).thenReturn(product);
        when(existing.getPrice()).thenReturn(5.0);

        when(incoming.getProduct()).thenReturn(product);
        when(incoming.getPrice()).thenReturn(7.0);
        when(incoming.getQuantity()).thenReturn(4);

        order.getItems().add(existing);

        order.addItem(incoming);

        assertEquals(2, order.getItems().size());
        assertTrue(order.getItems().contains(incoming));
    }
}