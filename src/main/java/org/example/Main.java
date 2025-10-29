
package org.example;

import funciones.FuncionApp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");
            EntityManager em = emf.createEntityManager();

            // Persistir la entidad UnidadMedida en estado "gestionada"
            em.getTransaction().begin();
            // Crear una nueva entidad UnidadMedida en estado "nueva"
            UnidadMedida unidadMedida = UnidadMedida.builder()
                    .denominacion("Kilogramo")
                    .build();
            UnidadMedida unidadMedidapote = UnidadMedida.builder()
                    .denominacion("pote")
                    .build();

            em.persist(unidadMedida);
            em.persist(unidadMedidapote);


            // Crear una nueva entidad Categoria en estado "nueva"
            Categoria categoria = Categoria.builder()
                    .denominacion("Frutas")
                    .esInsumo(true)
                    .build();

            // Crear una nueva entidad Categoria en estado "nueva"
            Categoria categoriaPostre = Categoria.builder()
                    .denominacion("Postre")
                    .esInsumo(false)
                    .build();

            // Persistir la entidad Categoria en estado "gestionada"

            em.persist(categoria);
            em.persist(categoriaPostre);


            // Crear una nueva entidad ArticuloInsumo en estado "nueva"
            ArticuloInsumo articuloInsumo = ArticuloInsumo.builder()
                    .denominacion("Manzana").codigo(UUID.randomUUID().toString()) // Código único
                    .precioCompra(1.5)
                    .precioVenta(5d)
                    .stockActual(100)
                    .stockMaximo(200)
                    .esParaElaborar(true)
                    .unidadMedida(unidadMedida)
                    .build();


            ArticuloInsumo articuloInsumoPera = ArticuloInsumo.builder()
                    .denominacion("Pera").codigo(UUID.randomUUID().toString())
                    .precioCompra(2.5)
                    .precioVenta(10d)
                    .stockActual(130)
                    .stockMaximo(200)
                    .esParaElaborar(true)
                    .unidadMedida(unidadMedida)
                    .build();
            ArticuloInsumo articuloInsumoCigarrillo = ArticuloInsumo.builder()
                    .denominacion("Cigarrillo").codigo(UUID.randomUUID().toString())
                    .precioCompra(2.5)
                    .precioVenta(300d)
                    .stockActual(130)
                    .stockMaximo(20000)
                    .esParaElaborar(false)
                    .unidadMedida(unidadMedida)
                    .build();

            // Persistir la entidad ArticuloInsumo en estado "gestionada"

            em.persist(articuloInsumo);
            em.persist(articuloInsumoPera);

            Imagen manza1 = Imagen.builder().denominacion("Manzana Verde").
                    build();
            Imagen manza2 = Imagen.builder().denominacion("Manzana Roja").
                    build();

            Imagen pera1 = Imagen.builder().denominacion("Pera Verde").
                    build();
            Imagen pera2 = Imagen.builder().denominacion("Pera Roja").
                    build();




            // Agregar el ArticuloInsumo a la Categoria
            categoria.getArticulos().add(articuloInsumo);
            categoria.getArticulos().add(articuloInsumoPera);
            // Actualizar la entidad Categoria en la base de datos

         // em.merge(categoria);

            // Crear una nueva entidad ArticuloManufacturadoDetalle en estado "nueva"
            ArticuloManufacturadoDetalle detalleManzana = ArticuloManufacturadoDetalle.builder()
                    .cantidad(2)
                    .articuloInsumo(articuloInsumo)
                    .build();


            ArticuloManufacturadoDetalle detallePera = ArticuloManufacturadoDetalle.builder()
                    .cantidad(2)
                    .articuloInsumo(articuloInsumoPera)
                    .build();

            // Crear una nueva entidad ArticuloManufacturado en estado "nueva"
            ArticuloManufacturado articuloManufacturado = ArticuloManufacturado.builder()
                    .denominacion("Ensalada de frutas")
                    .descripcion("Ensalada de manzanas y peras ")
                    .precioVenta(150d)
                    .tiempoEstimadoMinutos(10)
                    .preparacion("Cortar las frutas en trozos pequeños y mezclar")
                    .unidadMedida(unidadMedidapote)
                    .build();

            articuloManufacturado.getImagenes().add(manza1);
            articuloManufacturado.getImagenes().add(pera1);

            categoriaPostre.getArticulos().add(articuloManufacturado);
            // Crear una nueva entidad ArticuloManufacturadoDetalle en estado "nueva"

            // Agregar el ArticuloManufacturadoDetalle al ArticuloManufacturado
            articuloManufacturado.getDetalles().add(detalleManzana);
            articuloManufacturado.getDetalles().add(detallePera);
            // Persistir la entidad ArticuloManufacturado en estado "gestionada"
            categoriaPostre.getArticulos().add(articuloManufacturado);
            em.persist(articuloManufacturado);
            em.getTransaction().commit();

            // modificar la foto de manzana roja
            em.getTransaction().begin();
            articuloManufacturado.getImagenes().add(manza2);
            em.merge(articuloManufacturado);
            em.getTransaction().commit();

            //creo y guardo un cliente
            em.getTransaction().begin();
            Cliente cliente = Cliente.builder()
                    .cuit(FuncionApp.generateRandomCUIT())
                    .razonSocial("Juan Perez")
                    .build();

            Cliente cliente2 = Cliente.builder()
                    .cuit(FuncionApp.generateRandomCUIT())
                    .razonSocial("Valentina Alvarez")
                    .build();
            em.persist(cliente);
            em.persist(cliente2);
            em.getTransaction().commit();

            //creo y guardo una factura
            em.getTransaction().begin();

            FacturaDetalle detalle1 = new FacturaDetalle(30, articuloInsumo);
            detalle1.calcularSubTotal();
            FacturaDetalle detalle2 = new FacturaDetalle(31, articuloInsumoPera);
            detalle2.calcularSubTotal();
            FacturaDetalle detalle3 = new FacturaDetalle(3, articuloManufacturado);
            detalle3.calcularSubTotal();

            Factura factura = Factura.builder()
                    .puntoVenta(2024)
                    .fechaAlta(new Date())
                    .fechaComprobante(FuncionApp.generateRandomDate())
                    .cliente(cliente)
                    .nroComprobante(769910L)
                    .build();
            factura.addDetalleFactura(detalle1);
            factura.addDetalleFactura(detalle2);
            factura.addDetalleFactura(detalle3);
            factura.calcularTotal();

            em.persist(factura);

            FacturaDetalle detalleFactura2_1 = new FacturaDetalle(10, articuloInsumo); // Manzana
            detalleFactura2_1.calcularSubTotal();

            FacturaDetalle detalleFactura2_2 = new FacturaDetalle(5, articuloInsumoPera); // Pera
            detalleFactura2_2.calcularSubTotal();

            FacturaDetalle detalleFactura2_3 = new FacturaDetalle(2, articuloManufacturado); // Ensalada
            detalleFactura2_3.calcularSubTotal();

            Factura factura2 = Factura.builder()
                    .puntoVenta(2025)
                    .fechaAlta(new Date())
                    .fechaComprobante(LocalDate.now())
                    .cliente(cliente) // Asumo que 'cliente' está disponible en este alcance
                    .nroComprobante(FuncionApp.generateRandomNumber())
                    .build();

            factura2.addDetalleFactura(detalleFactura2_1);
            factura2.addDetalleFactura(detalleFactura2_2);
            factura2.addDetalleFactura(detalleFactura2_3);
            factura2.calcularTotal();

            em.persist(factura2);

            em.getTransaction().commit();

            String jpql = "SELECT am FROM ArticuloManufacturado am LEFT JOIN FETCH am.detalles d WHERE am.id = :id";
            Query query = em.createQuery(jpql);
            query.setParameter("id", 3L);
            ArticuloManufacturado articuloManufacturadoCon = (ArticuloManufacturado) query.getSingleResult();

            System.out.println("Artículo manufacturado: " + articuloManufacturado.getDenominacion());
            System.out.println("Descripción: " + articuloManufacturado.getDescripcion());
            System.out.println("Tiempo estimado: " + articuloManufacturado.getTiempoEstimadoMinutos() + " minutos");
            System.out.println("Preparación: " + articuloManufacturado.getPreparacion());

            System.out.println("Líneas de detalle:");
            for (ArticuloManufacturadoDetalle detalle : articuloManufacturado.getDetalles()) {
                System.out.println("- " + detalle.getCantidad() + " unidades de " + detalle.getArticuloInsumo().getDenominacion());

            }



                //   em.getTransaction().begin();
        //   em.remove(articuloManufacturado);
       //    em.getTransaction().commit();



            // Cerrar el EntityManager y el EntityManagerFactory

            //==============================================
            //      CONSULTAS TP
            //==============================================



            //String jpql = "SELECT am FROM ArticuloManufacturado am LEFT JOIN FETCH am.detalles d WHERE am.id = :id";
            //Query query = em.createQuery(jpql);
            //query.setParameter("id", 3L);
            //ArticuloManufacturado articuloManufacturadoCon = (ArticuloManufacturado) query.getSingleResult();

            //Ejercicio1
            String obtenerTodosClientes = "FROM Cliente";
            query = em.createQuery(obtenerTodosClientes);
            List<Cliente> clientes = query.getResultList();
            System.out.println("Todos los clientes:");
            for (Cliente c : clientes)
                System.out.println(c.getRazonSocial());

            //Ejercicio2
            LocalDate fechaInicio = LocalDate.now().minusDays(30);
            String queryFacturasUltimoMes = "SELECT f FROM Factura f WHERE f.fechaComprobante >= :fechaInicio";
            query = em.createQuery(queryFacturasUltimoMes);
            query.setParameter("fechaInicio", fechaInicio);
            List<Factura> facturas = query.getResultList();
            System.out.println("Facturas del ultimo mes:");
            for (Factura f : facturas)
                System.out.println(f.getNroComprobante());

            //Ejercicio3
            String queryClienteMasFacturas = "SELECT f.cliente FROM Factura f GROUP BY f.cliente ORDER BY COUNT(f) DESC";
            query = em.createQuery(queryClienteMasFacturas);
            List<Cliente> ClienteMasFacturas  = query.getResultList();
            for (Cliente c: ClienteMasFacturas)
                System.out.println("El cliente con mas facturas es: "+c.getRazonSocial());

            //Ejercicio4
            String queryProdMasVendidos = "SELECT d.articulo FROM FacturaDetalle d GROUP BY d.articulo ORDER BY SUM(d.cantidad) DESC";
            query = em.createQuery(queryProdMasVendidos);
            List<Articulo> prodMasVendidos = query.getResultList();
            System.out.println("Los articulos mas vendidos son: \n");
            for (Articulo articulo: prodMasVendidos)
                System.out.println(articulo.getDenominacion());

            //Ejercicio5
            String factClienteUltMes = "FROM Factura f WHERE f.cliente.id = :cliente AND f.fechaComprobante >= :fechaInicio";
            query = em.createQuery(factClienteUltMes);
            query.setParameter("cliente", 1L);
            query.setParameter("fechaInicio", fechaInicio);
            List<Factura> facturasCliente = query.getResultList();
            System.out.println("\nLas facturas del cliente con id=1, en el ultimo mes son: \n");
            for (Factura f : facturasCliente)
                System.out.println(f.getNroComprobante());


            //Ejercicio6
            String totalFacturadoCliente = "SELECT SUM(f.total) FROM Factura f WHERE f.cliente.id = :cliente";
            query = em.createQuery(totalFacturadoCliente);
            query.setParameter("cliente", 1L);
            Double total = (Double) query.getSingleResult();
            System.out.println("El monto total facturado a el cliente con ID=1 es de:");
            System.out.println(total);



            //Ejercicio7
            String articulosFactura = "SELECT fd.articulo FROM FacturaDetalle fd WHERE fd.factura.nroComprobante = :nroComprobante";
            query = em.createQuery(articulosFactura);
            query.setParameter("nroComprobante", 769910L);
            List<Articulo> articulos = query.getResultList();
            System.out.println("Articulos de la factura 769910L");
            for (Articulo a : articulos)
                System.out.println(a.getDenominacion());


            //Ejercicio 8
            String articulosFacturaMasCaro = "SELECT fd.articulo FROM FacturaDetalle fd WHERE fd.factura.nroComprobante = :nroComprobante ORDER BY fd.articulo.precioVenta DESC ";
            query = em.createQuery(articulosFacturaMasCaro);
            query.setParameter("nroComprobante", 769910L);
            query.setMaxResults(1);
            List<Articulo> articuloCaro = query.getResultList();
            System.out.println("Articulo mas caro:");
            for (Articulo a : articuloCaro)
                System.out.println(a.getDenominacion());

            //Ejercicio 9
            String cantFacturas = "SELECT COUNT(f) FROM Factura f";
            query = em.createQuery(cantFacturas);
            Long cant = (Long) query.getSingleResult();
            System.out.println("Cantidad total de facturas en el sistema: " + cant);

            //Ejercicio 10
            Query  queryPrimerElemento;
            String factMayorXMonto = "SELECT f FROM Factura f JOIN f.detallesFactura d GROUP BY f.nroComprobante HAVING SUM(d.subTotal) > :monto ";
            queryPrimerElemento = em.createQuery(factMayorXMonto);
            queryPrimerElemento.setParameter("monto", 1.0);
            List<Factura> facturasMayorXMonto = queryPrimerElemento.getResultList();
            System.out.println("Facturas con monto mayor a 1:");
            for(Factura f :facturasMayorXMonto)
                System.out.println(f.getNroComprobante());

            //Ejercicio 11
            String factConArt = "SELECT DISTINCT f FROM Factura f JOIN f.detallesFactura d JOIN d.articulo a WHERE a.denominacion = :nombre";
            query = em.createQuery(factConArt);
            query.setParameter("nombre", "Manzana");
            List<Factura> facturasConManzana = query.getResultList();
            System.out.println("Facturas con Manzana:");
            for(Factura f :facturasConManzana)
                System.out.println(f.getNroComprobante());


            //EJERCICIO 12

            String codParcial = "FROM Articulo a WHERE a.codigo LIKE :codigo";
            query = em.createQuery(codParcial);
            query.setParameter("codigo", "M%");
            List<Articulo> articulosCodParcial = query.getResultList();
            System.out.println("Articulos con codigo que empieza con M:");
            for(Articulo a : articulosCodParcial)
                System.out.println(a.getDenominacion());

            //EJERCICIO 13
            String precioMayorPromedio = "FROM Articulo a WHERE a.precioVenta  > (SELECT AVG(a2.precioVenta) FROM Articulo a2)";
            query = em.createQuery(precioMayorPromedio);
            List<Articulo> articulosPrecioMayorPromedio = query.getResultList();
            System.out.println("Articulos con precio mayor al promedio");
            for(Articulo a : articulosPrecioMayorPromedio)
                System.out.println(a.getDenominacion());

            //EJERCICIO 14
            String consultaEXISTS = "SELECT a FROM Articulo a\n" +
                    "WHERE EXISTS (\n" +
                    "    SELECT 1 FROM FacturaDetalle d WHERE d.articulo = a\n" +
                    ")";
            query = em.createQuery(consultaEXISTS);
            List<Articulo> articulosConDetalle = query.getResultList();
            System.out.println("Articulos que estan en al menos una factura");
            for(Articulo a : articulosConDetalle)
                System.out.println(a.getDenominacion());

//La clausula EXISTS devuelve TRUE si existe al menos una fila con la condicion indicada, en este caso devuelve true si el articulo forma
// parte de un detalle factura y por ende de una factura







            em.close();
            emf.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}




