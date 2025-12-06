# 🛒 API de Gestión para Tienda Híbrida

> **Backend en Java para una tienda moderna que vende productos físicos y digitales en una misma transacción.**  
> Proyecto final de *Programación Orientada a Objetos*, demostrando dominio de **herencia**, **polimorfismo**, **encapsulamiento** y **abstracción** en un caso de uso realista.

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=java&logoColor=white)](https://openjdk.org)
[![Spring Boot 3.5.6](https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![H2 Database](https://img.shields.io/badge/H2-2.3.232-5282C4?logo=h2&logoColor=white)](https://www.h2database.com)
[![JUnit 5](https://img.shields.io/badge/JUnit_5-5.12.2-25A162?logo=junit&logoColor=white)](https://junit.org)
[![Swagger UI](https://img.shields.io/badge/Swagger-3.0-85EA2D?logo=swagger&logoColor=white)](https://swagger.io)

---


### 🚀 Características Técnicas
* **Modelo de Dominio Polimórfico:** Implementación de herencia con estrategia `JOINED` en JPA para distinguir entre `ProductoFisico` y `ProductoDigital`.
* **Lógica de Negocio Dinámica:** Cálculo de precios finales con reglas específicas (descuentos por licencia digital vs. recargos por peso/flete en físicos).
* **Gestión de Inventario:** Control de stock atómico con validaciones de estado y manejo de excepciones personalizadas.
* **Persistencia Relacional:** Mapeo ORM avanzado con relaciones bidireccionales, carga diferida (`Lazy Loading`) y operaciones en cascada.
* **Integridad de Datos:** Snapshots de precios en el momento de la venta (`ItemVenta`) para mantener la integridad histórica de las transacciones.

---

## 🧱 Arquitectura y Diseño

| Capa | Tecnología | Característica clave |
|------|------------|----------------------|
| **Modelo de Dominio** | Java 21 + Lombok | Herencia `JOINED` con `Producto` → `ProductoFisico` / `ProductoDigital` • Relaciones bidireccionales con `mappedBy` y `orphanRemoval = true` |
| **Persistencia** | Spring Data JPA + H2 | Consultas JPQL seguras (`findProductosConStockBajo()`) • `BigDecimal` para operaciones monetarias |
| **Servicios** | `@Service` + `@Transactional` | Lógica de negocio crítica en `VentaServiceImpl.finalizarVenta()` (validación, stock, coherencia) |
| **API** | Spring Web + OpenAPI 3 | Endpoints RESTful • Swagger UI en `/swagger-ui.html` |

---

## 🛠️ Tech Stack

| Componente | Tecnología |
|-----------|------------|
| **Lenguaje** | Java 21 |
| **Framework** | Spring Boot 3.5.6 |
| **Persistencia** | Spring Data JPA / Hibernate 6.6.29 |
| **Base de Datos** | H2 Database 2.3.232 *(en memoria, desarrollo)* |
| **Generación de Código** | Lombok 1.18.40 |
| **Pruebas** | JUnit 5 / AssertJ / Mockito |
| **Documentación** | Swagger UI (springdoc-openapi 2.6.0) |
| **Construcción** | Apache Maven |

---

## 📦 Endpoints Principales (Swagger UI)

| Operación | Endpoint | Descripción |
|----------|----------|-------------|
| **Registrar venta** | `POST /api/ventas` | Valida stock, reduce inventario y persiste en transacción única |
| **Stock bajo** | `GET /api/productos/stock/bajo?limite=5` | Retorna solo `ProductoFisico` con stock ≤ límite |
| **Precio final** | `GET /api/productos/{id}/precio-final` | Ejecuta `calcularPrecioFinal()` polimórfico |
| **Total vendido** | `GET /api/ventas/total?desde=2025-01-01&hasta=2025-12-31` | Agregación con `SUM` y `JOIN` |

---

