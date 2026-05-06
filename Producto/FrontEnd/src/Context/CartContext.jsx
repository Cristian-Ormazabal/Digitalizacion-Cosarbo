import React, { createContext, useState, useEffect } from 'react';

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
  // Se inicializa el estado con lo que haya en LocalStorage o un array vacío
  const [cart, setCart] = useState(() => {
    const savedCart = localStorage.getItem('cosarbo_cart');
    return savedCart ? JSON.parse(savedCart) : [];
  });

  // Cada vez que el carrito cambie, se guarda en LocalStorage
  useEffect(() => {
    localStorage.setItem('cosarbo_cart', JSON.stringify(cart));
  }, [cart]);

  // Función para añadir productos
  const addToCart = (product, quantity) => {
    setCart((prevCart) => {
      const existingProduct = prevCart.find(item => item.id === product.id);
      
      if (existingProduct) {
        // Si ya existe, se suma la cantidad
        return prevCart.map(item =>
          item.id === product.id 
          ? { ...item, quantity: item.quantity + parseInt(quantity) } 
          : item
        );
      }
      // Si es nuevo, se añade
      return [...prevCart, { ...product, quantity: parseInt(quantity) }];
    });
  };

  // Función para eliminar productos
  const removeFromCart = (id) => {
    setCart(cart.filter(item => item.id !== id));
  };

  // Función para vaciar el carrito
  const clearCart = () => setCart([]);

  return (
    <CartContext.Provider value={{ cart, addToCart, removeFromCart, clearCart }}>
      {children}
    </CartContext.Provider>
  );
};