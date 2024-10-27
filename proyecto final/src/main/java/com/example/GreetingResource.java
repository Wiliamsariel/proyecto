package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/carrito")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String hello() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Carrito de Compras</title>
                <style>
                    img {
                        width: 250px;
                        height: 155px;
                        margin: 10px;
                    }
                    .producto {
                        display: inline-block;
                        text-align: center;
                        margin: 20px;
                    }
                    #carrito {
                        margin-top: 20px;
                    }
                </style>
                <script>
                    const selectedProducts = [];

                    function addToCart(product, quantity) {
                        const productInfo = `${product};${quantity}`;
                        selectedProducts.push(encodeURIComponent(productInfo));
                        alert(productInfo + " ha sido agregado al carrito.");
                    }

                    function goToCart() {
                        const queryString = selectedProducts.join(',');
                        window.location.href = "/carrito/listado?productos=" + queryString;
                    }

                    function vaciarCarrito() {
                        selectedProducts.length = 0; // Vaciar el array en JavaScript
                        alert("El carrito ha sido vaciado.");
                    }
                </script>
            </head>
            <body>
                <h1>Bienvenido a tu Carrito de Compras</h1>
                <div>
                    <div class="producto">
                        <img src="/images/camera.jpg" alt="Cámara">
                        <p>Cámara: $199.99</p>
                        <input type="number" id="cantidadCamera" value="1" min="1">
                        <button onclick="addToCart('Cámara', document.getElementById('cantidadCamera').value)">Agregar al carrito</button>
                    </div>
                    <div class="producto">
                        <img src="/images/external-hard-drive.jpg" alt="Disco Duro Externo">
                        <p>Disco Duro Externo: $89.99</p>
                        <input type="number" id="cantidadExternalHardDrive" value="1" min="1">
                        <button onclick="addToCart('Disco Duro Externo', document.getElementById('cantidadExternalHardDrive').value)">Agregar al carrito</button>
                    </div>
                    <div class="producto">
                        <img src="/images/laptop.jpg" alt="Laptop">
                        <p>Laptop: $899.99</p>
                        <input type="number" id="cantidadLaptop" value="1" min="1">
                        <button onclick="addToCart('Laptop', document.getElementById('cantidadLaptop').value)">Agregar al carrito</button>
                    </div>
                    <div class="producto">
                        <img src="/images/watch.jpg" alt="Reloj">
                        <p>Reloj: $49.99</p>
                        <input type="number" id="cantidadWatch" value="1" min="1">
                        <button onclick="addToCart('Reloj', document.getElementById('cantidadWatch').value)">Agregar al carrito</button>
                    </div>
                </div>
                <div id="carrito">
                    <button onclick="goToCart()">Ver Carrito</button>
                </div>
            </body>
            </html>
            """;
    }
}
//http://localhost:8080/carrito
