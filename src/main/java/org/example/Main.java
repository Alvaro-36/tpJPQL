
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

            Factura factura2 = Factura.builder()
                    .puntoVenta(2025)
                    .fechaAlta(new Date())
                    .fechaComprobante(LocalDate.now())
                    .cliente(cliente)
                    .nroComprobante(FuncionApp.generateRandomNumber())
                    .build();
            factura.addDetalleFactura(detalle1);
            factura.addDetalleFactura(detalle2);
            factura.addDetalleFactura(detalle3);
            factura.calcularTotal();

            em.persist(factura2);

            em.getTransaction().commit();

            // Crear la consulta SQL nativa
            // Crear la consulta JPQL

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


            /// ARREGLAR EJERCICIO 7
            //Ejercicio7
            String articulosFactura = "SELECT FacturaDetalle.articulo FROM FacturaDetalle WHERE FacturaDetalle.factura.nroComprobante = :nroComprobante";
            query = em.createQuery(articulosFactura);
            query.setParameter("nroComprobante", 769910L);
            List<Articulo> articulos = query.getResultList();
            System.out.println("Articulos de la factura 769910L");
            for (Articulo a : articulos)
                System.out.println(a.getDenominacion());











            em.close();
            emf.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

/*

Manejo del Ciclo de Estados en JPA
El ciclo de estados en JPA (Java Persistence API) define los diferentes estados que puede tener una entidad en relación con el contexto de persistencia (EntityManager). Comprender y manejar correctamente estos estados es crucial para trabajar eficazmente con JPA. Los estados del ciclo de vida de una entidad en JPA son:

New (Nuevo):

Una entidad está en estado "New" cuando ha sido creada pero aún no ha sido persistida en la base de datos.
Managed (Gestionado):

Una entidad está en estado "Managed" cuando está asociada con un contexto de persistencia (EntityManager) y cualquier cambio en la entidad se reflejará automáticamente en la base de datos.
Detached (Desconectado):

Una entidad está en estado "Detached" cuando ya no está asociada con un contexto de persistencia. Los cambios en la entidad no se reflejarán automáticamente en la base de datos.
Removed (Eliminado):

Una entidad está en estado "Removed" cuando ha sido marcada para su eliminación en la base de datos.
*/


