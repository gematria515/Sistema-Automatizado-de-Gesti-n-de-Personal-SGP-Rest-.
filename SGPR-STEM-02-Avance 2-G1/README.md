# SGP-Rest – Avance 2 Grupo 1


¡Hola! Este es SGP-Rest, nuestro sistema para manejar el personal de un restaurante (lo armamos pensando en "La Mesa Restaurante"). La idea es sencilla: el administrador controla a los empleados y sus turnos, y cada empleado puede marcar su asistencia, ver su horario y pedir vacaciones o cambios de turno. Todo sigue el prototipo que hicimos en Figma en el Avance 1.

Somos el grupo 1 de Desarrollo de Aplicaciones Web y Patrones.

## A quién le tocó qué

**Leonardo Brenes** se encargó de todo lo de empleados y turnos del lado del administrador: agregar, editar y eliminar empleados, y armar los turnos de cada uno. Le tocaron las entidades `Empleado` y `Turno`, con sus controladores, servicios y repositorios, y las pantallas con el modal para agregar y editar.

**Daniel Mena** se encargó del control de asistencia. Le tocó que el empleado pueda marcar entrada, salir a descanso, volver y marcar salida, que el sistema detecte si llegó tarde, y la pantalla de "Mis turnos" donde cada uno ve su horario.

**Fernanda Vargas** se encargó de la parte del administrador con las solicitudes y la pre-nómina. Le tocó que el administrador pueda aprobar o rechazar lo que piden los empleados, y la consulta de jornadas trabajadas entre dos fechas para preparar la pre-nómina.

**Brandon Delgado** se encargó de las solicitudes del lado del empleado (pedir vacaciones o cambio de turno) y de la base de todo el sitio: el encabezado, el menú lateral, el pie de página, los mensajes que aparecen al guardar o eliminar, la pantalla para escoger perfil, los textos de `messages.properties` y el script de la base de datos.

## Con qué lo hicimos

Usamos lo que hemos ido viendo en el curso: Java 21, Maven, Spring Boot, JPA con MySQL, Thymeleaf con fragmentos, Bootstrap y Font Awesome con WebJars, y Lombok.

El código está organizado igual que el proyecto TechShop de clase:

- `domain` → las entidades (Empleado, Turno, Asistencia y Solicitud)
- `repository` → los repositorios, con consultas derivadas como `findByEntradaBetweenOrderByEntradaAsc`
- `service` → la lógica
- `controller` → las rutas y lo que se manda a cada pantalla

Cada módulo tiene su `listado.html`, que arma la página, y su `fragmentos.html`, con el formulario y la tabla.

## Para correrlo

El proyecto de NetBeans está en la carpeta `SGP-Rest/`.

1. Necesitás JDK 21, MySQL y NetBeans.
2. En MySQL Workbench, abrí la conexión local y corré `SGP-Rest/src/main/resources/db/crear_bd.sql`. Eso crea la base, el usuario, las tablas y unos datos de ejemplo para probar.
3. En NetBeans abrí la carpeta `SGP-Rest`, dale **Clean and Build** y después **Run**.
4. Entrá a http://localhost:8080 y escogé si querés entrar como Administrador o como Empleado.

## Cómo vamos

**Ya funciona:**
- Agregar, editar y eliminar empleados y turnos (US01)
- Marcar entrada, descanso y salida, y saber si alguien llegó tarde (US02, US03 y US04)
- Ver "Mis turnos" (US05)
- Consultar la pre-nómina por fechas (US06)
- Pedir vacaciones o cambio de turno (US09)
- Aprobar o rechazar solicitudes (US10)

**Lo que viene en el siguiente avance:**
- Ventas y cierre de caja (US07 y US08)
- Tareas e historial

**Cosas que todavía hay que mejorar:**
- Por ahora los turnos se crean día por día; no se repiten solos cada semana y el sistema no avisa si dos turnos chocan.
- Si se aprueba un cambio de turno, el calendario no se actualiza solo (hay que cambiarlo a mano).
- La pre-nómina muestra las jornadas y los minutos trabajados, pero todavía no calcula cuánto se paga.

## Sobre GitHub

En este avance subimos todo el código de una sola vez en lugar de que cada uno fuera subiendo sus cambios. Sabemos que así no se ve bien lo que hizo cada quien, y no va a volver a pasar. De ahora en adelante cada uno trabaja en su rama y sus cambios entran a `main` con un Pull Request que revisa y aprueba otro compañero.

| Integrante | Rama |
|---|---|
| Leonardo Brenes | `feature/empleados-turnos` |
| Daniel Mena | `feature/asistencia` |
| Fernanda Vargas | `feature/solicitudes-prenomina` |
| Brandon Delgado | `feature/solicitudes-plantilla` |