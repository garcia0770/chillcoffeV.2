package com.ChillCoffeV1_4.servicio;



import com.ChillCoffeV1_4.modelo.Venta;

import com.ChillCoffeV1_4.repositorio.VentaRepository;

import org.springframework.stereotype.Service;



@Service

public class VentaService {



    private final VentaRepository ventaRepository;



    public VentaService(VentaRepository ventaRepository) {

        this.ventaRepository = ventaRepository;

    }



    /**

     * Registra la venta final en la base de datos con el método de pago.

     * @param pedido La entidad Venta que se está procesando.

     * @param metodoPago El método de pago seleccionado (Tarjeta, Efectivo, Nequi).

     * @return La Venta finalizada con su ID.

     */

    public Venta finalizarVenta(Venta pedido, String metodoPago) {

        // ⭐ Lógica de negocio:

       

        // 1. Asignar el método de pago al pedido

        pedido.setMetodoPago(metodoPago);

       

        // 2. Asignar la fecha de la transacción

        pedido.setFecha(new java.util.Date());

       

        // 3. Guardar la Venta en la tabla 'ventas' (y posiblemente sus items en otra tabla).

        Venta ventaGuardada = ventaRepository.save(pedido);



        // 4. (Opcional) Realizar cualquier otra acción (ej. actualizar inventario)

       

        return ventaGuardada;

    }

   

    // ... otros métodos de servicio

}