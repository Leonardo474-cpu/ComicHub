# ComicHub - Documentacion Tecnica General

## 1. Resumen del proyecto
ComicHub es una aplicacion web monolitica construida con Spring Boot (MVC + Thymeleaf + JPA) conectada a MySQL.

Objetivo actual:
- Gestion de usuarios con autenticacion basica por correo/password.
- Modulo de administracion para listar, editar y eliminar usuarios.
- Estructura de dominio preparada para comics, autores, categorias, planes y suscripciones.

## 2. Stack y dependencias
Archivo fuente: `pom.xml`.

### Base del proyecto
- `org.springframework.boot:spring-boot-starter-parent:4.0.2`
- `java.version: 17`

### Dependencias de produccion
1. `spring-boot-starter-data-jpa`
2. `spring-boot-starter-thymeleaf`
3. `spring-boot-starter-webmvc`
4. `org.projectlombok:lombok` (provided/optional)
5. `mysql:mysql-connector-java:8.0.30` (optional)
6. `spring-boot-starter-validation`
7. `com.azure:azure-storage-blob:12.25.3`

### Dependencias de test
1. `spring-boot-starter-data-jpa-test`
2. `spring-boot-starter-thymeleaf-test`
3. `spring-boot-starter-webmvc-test`

### Plugins Maven
1. `maven-compiler-plugin` con annotation processor de Lombok.
2. `spring-boot-maven-plugin` excluyendo Lombok del artefacto final.

## 3. Configuracion de entorno
Archivo fuente: `src/main/resources/application.properties`.

Propiedades relevantes:
- `spring.application.name=ComicHub`
- `spring.datasource.url=...comichub_bd...`
- `spring.datasource.username=...`
- `spring.datasource.password=...`
- `server.port=${PORT:8080}`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.show-sql=true`
- `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect`

Nota tecnica:
- El proyecto usa `ddl-auto=update`, por lo que Hibernate intentara ajustar el esquema segun entidades.
- En entornos productivos se recomienda migraciones versionadas (Flyway/Liquibase) y no exponer credenciales en texto plano.

## 4. Arquitectura de paquetes
Paquete base: `com.comic.hub`.

Estructura:
1. `controller`: capa web MVC.
2. `service`: logica de negocio.
3. `service.impl`: implementaciones de servicios.
4. `repository`: acceso a datos con Spring Data JPA.
5. `model`: entidades de dominio.
6. `model.converter`: convertidores JPA.
7. `dto.request` y `dto.response`: modelos de entrada/salida para formularios/vistas.
8. `mapper`: mapeo DTO <-> entidad.

## 5. Capa web (controladores y rutas)
### `HomeController`
- `GET /` y `GET /home` -> vista `home.html`.

### `LoginController`
- `GET /login` -> formulario login.
- `POST /login` -> autentica usuario:
  - Si rol es `ADMIN` -> redirecciona `/admin`.
  - Si no -> redirecciona `/home`.
  - Si falla -> vuelve a `login` con mensaje de error.

### `AdminController`
- `GET /admin`:
  - Requiere `usuarioLogueado` en `HttpSession` y rol `ADMIN`.
  - Carga datos del nombre para dashboard.

### `UsuarioController`
- `GET /admin/usuarios` -> lista usuarios.
- `GET /registro` -> formulario de registro de cliente.
- `POST /registro` -> valida DTO y registra usuario.
- `POST /admin/usuarios/guardar` -> crea/actualiza datos editables de usuario desde admin.
- `GET /admin/usuarios/editar/{id}` -> carga formulario admin.
- `GET /admin/usuarios/eliminar/{id}` -> elimina usuario por id.

## 6. Capa servicio
### `UsuarioService` (interfaz)
Metodos:
1. `listarTodos()`
2. `buscarPorIdParaEdicion(Integer id)`
3. `eliminar(Integer id)`
4. `login(String correo, String password)`
5. `registrar(UsuarioRegistroRequestDto dto)`
6. `guardarDesdeAdmin(UsuarioAdminRequestDto dto)`

### `UsuarioServiceImpl` (implementacion)
Responsabilidades:
- Normaliza correo (`trim + lowercase`).
- Normaliza texto de nombre.
- Valida usuario activo en login.
- Valida password comparando con `password_hash`.
- Evita correo duplicado en registro y edicion admin.
- Verifica que rol destino exista y este activo.
- Usa `UsuarioMapper` para conversion DTO -> entidad.

## 7. Capa repository
### Repositorios existentes
1. `UsuarioRepository extends JpaRepository<Usuario, Integer>`
   - Sobrescribe `findAll()` con `@EntityGraph(attributePaths = "rol")`.
   - `findByCorreo(String correo)` con `@EntityGraph`.
2. `RolRepository extends JpaRepository<Rol, Integer>`
   - `findByNombreRol(String nombreRol)`.
3. `AutorRepository extends JpaRepository<Autor, Integer>`
   - `findByNombre(String nombre)`.
4. `CategoriaRepository extends JpaRepository<Categoria, Integer>`
   - `findByDescripcion(String descripcion)`.
5. `ComicRepository extends JpaRepository<Comic, Integer>`.
6. `PlanRepository extends JpaRepository<Plan, Integer>`
   - `findByNombrePlan(String nombrePlan)`.
7. `SuscripcionRepository extends JpaRepository<Suscripcion, Integer>`.

## 8. Modelo de dominio y mapeo con BD
DB objetivo: `comichub_bd`.

### `Usuario` -> `tb_usuario`
- `id` -> `id_usuario`
- `nombreCompleto` -> `nombre_completo`
- `correo` -> `correo` (unique)
- `passwordHash` -> `password_hash`
- `rol` (`ManyToOne`) -> `id_rol`
- `activo` con `BooleanToIntegerConverter` (int en DB)
- `fechaRegistro` -> `fecha_registro` (solo lectura para JPA)

### `Rol` -> `tb_rol`
- `codRol` -> `id_rol`
- `nombreRol` -> `nombre`
- `descripcion` -> `descripcion`
- `activo` -> `activo` (con converter)

### `Plan` -> `tb_plan`
- `idPlan` -> `id_plan`
- `nombrePlan` -> `nombre_plan`
- `precio` -> `precio`
- `diasDuracion` -> `dias_duracion`
- `descripcion` -> `descripcion`
- `activo` -> `activo` (con converter)
- `fechaRegistro` -> `fecha_registro`

### `Suscripcion` -> `tb_suscripcion`
- `idSuscripcion` -> `id_suscripcion`
- `usuario` (`ManyToOne`) -> `id_usuario`
- `plan` (`ManyToOne`) -> `id_plan`
- `fechaInicio` -> `fecha_inicio`
- `fechaFin` -> `fecha_fin`
- `estado` -> `estado`
- `precioAplicado` (`BigDecimal`) -> `precio_aplicado`
- `observacion` -> `observacion`
- `fechaRegistro` -> `fecha_registro`

### `Categoria` -> `tb_categoria`
- `idCategoria` -> `id_categoria`
- `descripcion` -> `descripcion`
- `activo` -> `activo` (con converter)

### `Autor` -> `tb_autor`
- `idAutor` -> `id_autor`
- `nombre` -> `nombre` (unique)
- `seudonimo` -> `seudonimo`
- `activo` -> `activo` (con converter)
- `fechaRegistro` -> `fecha_registro`

### `Comic` -> `tb_comic`
- `idComic` -> `id_comic`
- `titulo` -> `titulo`
- `sinopsis` -> `sinopsis`
- `autor` (`ManyToOne`) -> `id_autor`
- `categoria` (`ManyToOne`) -> `id_categoria`
- `rutaImagenPortada` -> `ruta_imagen_portada`
- `estado` (`EnumType.STRING`) -> `estado` (`EN_EMISION`, `FINALIZADO`, `PAUSADO`)
- `activo` -> `activo` (con converter)
- `fechaPublicacion` -> `fecha_publicacion`
- `fechaFinalizacion` -> `fecha_finalizacion`
- `fechaRegistro` -> `fecha_registro`

### Enum auxiliar
- `ComicEstado`: `EN_EMISION`, `FINALIZADO`, `PAUSADO`.

### Converter auxiliar
- `BooleanToIntegerConverter`: convierte `Boolean <-> Integer` para columnas booleanas modeladas como enteros.

## 9. DTO y mapeo
### DTO request
1. `UsuarioRegistroRequestDto`
   - Campos: `nombreCompleto`, `correo`, `password`.
   - Validaciones: `@NotBlank`, `@Email`.
2. `UsuarioAdminRequestDto`
   - Campos: `id`, `nombreCompleto`, `correo`, `rolId`.
   - Validaciones: `@NotBlank`, `@Email`, `@NotNull`.

### DTO response
1. `UsuarioListResponseDto`
   - Campos: `id`, `nombreCompleto`, `correo`, `nombreRol`, `activo`.

### Mapper
`UsuarioMapper`:
- `toEntityForRegistro(...)`
- `toEntityForAdmin(...)`
- `toAdminRequestDto(...)`
- `toListResponseDto(...)`

## 10. Vistas Thymeleaf
Rutas de templates:
1. `templates/login.html`
2. `templates/registro.html`
3. `templates/home.html`
4. `templates/admin/dashboard.html`
5. `templates/admin/usuarios.html`
6. `templates/admin/usuarios-form.html`
7. `templates/admin/components/layout.html` (layout reusable admin)
8. `templates/usuarios/components/usuario-form-fields.html` (fragmento reusable de campos usuario)

Detalles:
- `login.html` y `home.html` usan CSS inline.
- `registro.html` y modulo admin usan Tailwind via CDN.
- En formularios de registro y admin ya existe renderizado de errores de negocio con `${error}`.

## 11. Flujo funcional actual
1. Usuario entra a `/login`.
2. Ingresa correo/password.
3. Servicio valida existencia, estado activo y password.
4. Se guarda `usuarioLogueado` en sesion.
5. Redireccion:
   - ADMIN -> `/admin`
   - CLIENTE -> `/home`
6. Admin gestiona usuarios desde `/admin/usuarios`.

## 12. Inventario de importaciones por archivo Java
Formato: archivo -> imports.

### `src/main/java/com/comic/hub/ComicHubApplication.java`
- `org.springframework.boot.SpringApplication`
- `org.springframework.boot.autoconfigure.SpringBootApplication`

### `src/main/java/com/comic/hub/controller/AdminController.java`
- `org.springframework.stereotype.Controller`
- `org.springframework.ui.Model`
- `org.springframework.web.bind.annotation.GetMapping`
- `com.comic.hub.model.Usuario`
- `jakarta.servlet.http.HttpSession`

### `src/main/java/com/comic/hub/controller/HomeController.java`
- `org.springframework.stereotype.Controller`
- `org.springframework.web.bind.annotation.RequestMapping`

### `src/main/java/com/comic/hub/controller/LoginController.java`
- `org.springframework.stereotype.Controller`
- `org.springframework.ui.Model`
- `org.springframework.web.bind.annotation.GetMapping`
- `org.springframework.web.bind.annotation.PostMapping`
- `org.springframework.web.bind.annotation.RequestParam`
- `com.comic.hub.model.Usuario`
- `com.comic.hub.service.UsuarioService`
- `jakarta.servlet.http.HttpSession`

### `src/main/java/com/comic/hub/controller/UsuarioController.java`
- `org.springframework.stereotype.Controller`
- `org.springframework.ui.Model`
- `org.springframework.validation.BindingResult`
- `org.springframework.web.bind.annotation.GetMapping`
- `org.springframework.web.bind.annotation.ModelAttribute`
- `org.springframework.web.bind.annotation.PathVariable`
- `org.springframework.web.bind.annotation.PostMapping`
- `com.comic.hub.dto.request.UsuarioAdminRequestDto`
- `com.comic.hub.dto.request.UsuarioRegistroRequestDto`
- `com.comic.hub.repository.RolRepository`
- `com.comic.hub.service.UsuarioService`
- `jakarta.validation.Valid`

### `src/main/java/com/comic/hub/dto/request/UsuarioAdminRequestDto.java`
- `jakarta.validation.constraints.Email`
- `jakarta.validation.constraints.NotBlank`
- `jakarta.validation.constraints.NotNull`

### `src/main/java/com/comic/hub/dto/request/UsuarioRegistroRequestDto.java`
- `jakarta.validation.constraints.Email`
- `jakarta.validation.constraints.NotBlank`

### `src/main/java/com/comic/hub/dto/response/UsuarioListResponseDto.java`
- Sin imports externos.

### `src/main/java/com/comic/hub/mapper/UsuarioMapper.java`
- `com.comic.hub.dto.request.UsuarioAdminRequestDto`
- `com.comic.hub.dto.request.UsuarioRegistroRequestDto`
- `com.comic.hub.dto.response.UsuarioListResponseDto`
- `com.comic.hub.model.Rol`
- `com.comic.hub.model.Usuario`

### `src/main/java/com/comic/hub/model/Autor.java`
- `java.time.LocalDateTime`
- `com.comic.hub.model.converter.BooleanToIntegerConverter`
- `jakarta.persistence.Column`
- `jakarta.persistence.Convert`
- `jakarta.persistence.Entity`
- `jakarta.persistence.GeneratedValue`
- `jakarta.persistence.GenerationType`
- `jakarta.persistence.Id`
- `jakarta.persistence.Table`

### `src/main/java/com/comic/hub/model/Categoria.java`
- `jakarta.persistence.Column`
- `jakarta.persistence.Convert`
- `jakarta.persistence.Entity`
- `jakarta.persistence.GeneratedValue`
- `jakarta.persistence.GenerationType`
- `jakarta.persistence.Id`
- `jakarta.persistence.Table`
- `com.comic.hub.model.converter.BooleanToIntegerConverter`

### `src/main/java/com/comic/hub/model/Comic.java`
- `java.time.LocalDate`
- `java.time.LocalDateTime`
- `jakarta.persistence.Column`
- `jakarta.persistence.Convert`
- `jakarta.persistence.Entity`
- `jakarta.persistence.EnumType`
- `jakarta.persistence.Enumerated`
- `jakarta.persistence.GeneratedValue`
- `jakarta.persistence.GenerationType`
- `jakarta.persistence.Id`
- `jakarta.persistence.JoinColumn`
- `jakarta.persistence.ManyToOne`
- `jakarta.persistence.Table`
- `com.comic.hub.model.converter.BooleanToIntegerConverter`

### `src/main/java/com/comic/hub/model/ComicEstado.java`
- Sin imports externos.

### `src/main/java/com/comic/hub/model/Plan.java`
- `jakarta.persistence.*`
- `java.time.LocalDateTime`
- `com.comic.hub.model.converter.BooleanToIntegerConverter`

### `src/main/java/com/comic/hub/model/Rol.java`
- `jakarta.persistence.Column`
- `jakarta.persistence.Convert`
- `jakarta.persistence.Entity`
- `jakarta.persistence.GeneratedValue`
- `jakarta.persistence.GenerationType`
- `jakarta.persistence.Id`
- `jakarta.persistence.Table`
- `com.comic.hub.model.converter.BooleanToIntegerConverter`

### `src/main/java/com/comic/hub/model/Suscripcion.java`
- `java.math.BigDecimal`
- `java.time.LocalDate`
- `java.time.LocalDateTime`
- `jakarta.persistence.Column`
- `jakarta.persistence.Entity`
- `jakarta.persistence.GeneratedValue`
- `jakarta.persistence.GenerationType`
- `jakarta.persistence.Id`
- `jakarta.persistence.JoinColumn`
- `jakarta.persistence.ManyToOne`
- `jakarta.persistence.Table`

### `src/main/java/com/comic/hub/model/Usuario.java`
- `jakarta.persistence.*`
- `jakarta.validation.constraints.Email`
- `jakarta.validation.constraints.NotBlank`
- `java.time.LocalDateTime`
- `com.comic.hub.model.converter.BooleanToIntegerConverter`

### `src/main/java/com/comic/hub/model/converter/BooleanToIntegerConverter.java`
- `jakarta.persistence.AttributeConverter`
- `jakarta.persistence.Converter`

### `src/main/java/com/comic/hub/repository/AutorRepository.java`
- `java.util.Optional`
- `org.springframework.data.jpa.repository.JpaRepository`
- `com.comic.hub.model.Autor`

### `src/main/java/com/comic/hub/repository/CategoriaRepository.java`
- `java.util.Optional`
- `org.springframework.data.jpa.repository.JpaRepository`
- `com.comic.hub.model.Categoria`

### `src/main/java/com/comic/hub/repository/ComicRepository.java`
- `org.springframework.data.jpa.repository.JpaRepository`
- `com.comic.hub.model.Comic`

### `src/main/java/com/comic/hub/repository/PlanRepository.java`
- `java.util.Optional`
- `org.springframework.data.jpa.repository.JpaRepository`
- `com.comic.hub.model.Plan`

### `src/main/java/com/comic/hub/repository/RolRepository.java`
- `org.springframework.data.jpa.repository.JpaRepository`
- `com.comic.hub.model.Rol`

### `src/main/java/com/comic/hub/repository/SuscripcionRepository.java`
- `org.springframework.data.jpa.repository.JpaRepository`
- `com.comic.hub.model.Suscripcion`

### `src/main/java/com/comic/hub/repository/UsuarioRepository.java`
- `java.util.List`
- `java.util.Optional`
- `org.springframework.data.jpa.repository.EntityGraph`
- `org.springframework.data.jpa.repository.JpaRepository`
- `com.comic.hub.model.Usuario`

### `src/main/java/com/comic/hub/service/UsuarioService.java`
- `java.util.List`
- `com.comic.hub.dto.request.UsuarioAdminRequestDto`
- `com.comic.hub.dto.request.UsuarioRegistroRequestDto`
- `com.comic.hub.dto.response.UsuarioListResponseDto`
- `com.comic.hub.model.Usuario`

### `src/main/java/com/comic/hub/service/impl/UsuarioServiceImpl.java`
- `java.util.List`
- `org.springframework.stereotype.Service`
- `com.comic.hub.dto.request.UsuarioAdminRequestDto`
- `com.comic.hub.dto.request.UsuarioRegistroRequestDto`
- `com.comic.hub.dto.response.UsuarioListResponseDto`
- `com.comic.hub.mapper.UsuarioMapper`
- `com.comic.hub.model.Rol`
- `com.comic.hub.model.Usuario`
- `com.comic.hub.repository.RolRepository`
- `com.comic.hub.repository.UsuarioRepository`
- `com.comic.hub.service.UsuarioService`

### `src/test/java/com/comic/hub/ComicHubApplicationTests.java`
- `org.junit.jupiter.api.Test`
- `org.springframework.boot.test.context.SpringBootTest`

## 13. Estado actual y recomendaciones
Estado:
- El codigo esta alineado al esquema principal del dump MySQL para entidades clave.
- Hay base para extender CRUD de comics/autores/categorias/planes/suscripciones.

Recomendaciones tecnicas:
1. Implementar seguridad real de passwords (`BCryptPasswordEncoder`), no texto plano.
2. Migrar de `ddl-auto=update` a migraciones (Flyway/Liquibase).
3. Agregar manejo global de errores con `@ControllerAdvice`.
4. Agregar pruebas unitarias/integracion de servicio y controlador.
5. Externalizar secretos a variables de entorno o vault.

