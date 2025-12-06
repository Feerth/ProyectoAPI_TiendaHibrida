# 🛒 API de Gestión para Tienda Híbrida

Sistema backend desarrollado en **Java con Spring Boot** que gestiona un catálogo mixto de productos. El núcleo del proyecto implementa principios **SOLID** y un modelo de dominio rico para manejar la lógica diferenciada entre bienes tangibles e intangibles.

### 🚀 Características Técnicas
* **Modelo de Dominio Polimórfico:** Implementación de herencia con estrategia `JOINED` en JPA para distinguir entre `ProductoFisico` y `ProductoDigital`.
* **Lógica de Negocio Dinámica:** Cálculo de precios finales con reglas específicas (descuentos por licencia digital vs. recargos por peso/flete en físicos).
* **Gestión de Inventario:** Control de stock atómico con validaciones de estado y manejo de excepciones personalizadas.
* **Persistencia Relacional:** Mapeo ORM avanzado con relaciones bidireccionales, carga diferida (`Lazy Loading`) y operaciones en cascada.
* **Integridad de Datos:** Snapshots de precios en el momento de la venta (`ItemVenta`) para mantener la integridad histórica de las transacciones.

### 🛠️ Tech Stack
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3
* **ORM:** Hibernate / Spring Data JPA
* **Base de Datos:** MySQL
* **Herramientas:** Lombok, Jackson (JSON), Maven.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
