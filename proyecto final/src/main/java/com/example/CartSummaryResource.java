package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/carrito/listado")
public class CartSummaryResource {

    private static final List<CartItem> cartItems = new ArrayList<>();
    private static final Map<String, String> productImages = new HashMap<>();

    static {
        productImages.put("Cámara", "/images/camera.jpg");
        productImages.put("Disco Duro Externo", "/images/external-hard-drive.jpg");
        productImages.put("Laptop", "/images/laptop.jpg");
        productImages.put("Reloj", "/images/watch.jpg");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String listado(@QueryParam("productos") String productos) {
        if (productos != null) {
            saveCartInMemory(productos);
        }

        double totalPrice = 0.0;
        StringBuilder html = new StringBuilder("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Resumen del Carrito</title>
                <style>
                    body { font-family: Arial, sans-serif; }
                    img { width: 250px; height: 155px; margin: 10px; }
                    .producto-container { display: flex; flex-wrap: wrap; justify-content: center; }
                    .producto { text-align: center; margin: 20px; }
                    .empty-cart { text-align: center; margin-top: 50px; }
                    .balloon {
                        position: absolute;
                        bottom: 0;
                        width: 50px;
                        animation: float 5s infinite;
                    }
                    @keyframes float {
                        0% { transform: translateY(0); }
                        100% { transform: translateY(-500px); }
                    }
                    .balloon:nth-child(1) { left: 10%; animation-duration: 6s; }
                    .balloon:nth-child(2) { left: 30%; animation-duration: 4s; }
                    .balloon:nth-child(3) { left: 50%; animation-duration: 5s; }
                    .balloon:nth-child(4) { left: 70%; animation-duration: 3s; }
                    .balloon:nth-child(5) { left: 90%; animation-duration: 7s; }
                </style>
            </head>
            <body>
                <h1>Resumen del Carrito</h1>
                <div class='producto-container'>
        """);

        if (!cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
                double itemTotal = item.getPrice() * item.getQuantity();
                totalPrice += itemTotal;
                String imageUrl = productImages.get(item.getName());

                html.append("<div class='producto'>")
                        .append("<img src='").append(imageUrl).append("' alt='").append(item.getName()).append("'>")
                        .append("<p>").append(item.getName())
                        .append("<br>Cantidad: ").append(item.getQuantity())
                        .append("<br>Precio Total: $").append(itemTotal).append("</p>")
                        .append("</div>");
            }
            html.append("<h2>Precio Total del Carrito: $").append(totalPrice).append("</h2>");
        } else {
            html.append("""
                <div class='empty-cart'>
                    <p>El carrito está vacío.</p>
                    <p>¡Agrega productos!</p>
                    <div class="balloon"><img src="/images/balloon.png" alt="Globo"></div>
                    <div class="balloon"><img src="/images/balloon.png" alt="Globo"></div>
                    <div class="balloon"><img src="/images/balloon1.jpeg" alt="Globo"></div>
                    <div class="balloon"><img src="/images/balloon.png" alt="Globo"></div>
                    <div class="balloon"><img src="/images/balloon.png" alt="Globo"></div>
                </div>
            """);
        }

        html.append("""
                </div>
                <div>
                    <button onclick="window.location.href='/carrito'">Regresar</button>
                    <button onclick="window.location.href='/carrito/listado?productos='">Vaciar Carrito</button>
                </div>
            </body>
            </html>
        """);

        return html.toString();
    }

    private void saveCartInMemory(String productos) {
        if (productos.isEmpty()) {
            cartItems.clear(); // Vaciar el carrito si se recibe cadena vacía
            return;
        }

        String[] items = productos.split(",");
        for (String item : items) {
            String decodedItem = URLDecoder.decode(item, java.nio.charset.StandardCharsets.UTF_8);
            String[] details = decodedItem.split(";");
            String name = details[0];
            int quantity = Integer.parseInt(details[1]);
            double price = getPrice(name);
            addOrUpdateCartItem(name, quantity, price);
        }
    }

    private void addOrUpdateCartItem(String name, int quantity, double price) {
        for (CartItem item : cartItems) {
            if (item.getName().equals(name)) {
                item.addQuantity(quantity); // Aumentar la cantidad si el producto ya existe
                return;
            }
        }
        cartItems.add(new CartItem(name, quantity, price)); // Agregar nuevo producto si no existe
    }

    private double getPrice(String productName) {
        switch (productName) {
            case "Cámara":
                return 199.99;
            case "Disco Duro Externo":
                return 89.99;
            case "Laptop":
                return 899.99;
            case "Reloj":
                return 49.99;
            default:
                return 0.0;
        }
    }

    private static class CartItem {
        private final String name;
        private int quantity;
        private final double price;

        public CartItem(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        public void addQuantity(int quantity) {
            this.quantity += quantity; // Sumar la cantidad
        }
    }
}

