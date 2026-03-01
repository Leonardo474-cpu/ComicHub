# Baseline Funcional - Rama `desarrollo`

## Snapshot de referencia
- Rama: `desarrollo`
- Ultimo commit al momento del analisis: `d036d8f` (`Cambios en el diseño de la app`)
- Fecha de baseline: 2026-02-28

## Objetivo de este documento
Definir el comportamiento actual que debe preservarse al integrar cambios desde otra rama, manteniendo la app lo mas parecida posible en funcionalidad y flujo.

## Arquitectura actual
- Backend: Spring Boot MVC + Thymeleaf + JPA.
- Seguridad: sesion HTTP (`usuarioLogueado`) + interceptor en rutas `/admin/**`.
- Persistencia: MySQL.
- Archivos de portada de comics: Azure Blob Storage.

## Rutas y modulos funcionales

### Publico/cliente
- `GET /` y `GET /home`: landing + login/registro en modal.
- `POST /login`: autenticacion por correo/password.
- `GET /logout`: cierre de sesion.
- `GET /registro`: redireccion a `home?auth=register`.
- `POST /registro`: registro de cliente.
- `GET /suscripciones`: lista de planes activos y estado de suscripcion del usuario.
- `POST /suscripciones/seleccionar`: contratacion de plan.

### Admin (protegido por sesion + rol ADMIN)
- `GET /admin`: dashboard.
- CRUD funcional por modulo con listado paginado, filtro por estado y activacion/desactivacion:
  - `/admin/usuarios`
  - `/admin/autores`
  - `/admin/categorias`
  - `/admin/comics`

## Reglas de seguridad y sesion
- Todo `/admin/**` pasa por `AuthSessionInterceptor`.
- Si no hay sesion, no hay `usuarioLogueado`, o rol distinto de `ADMIN`, redirige a `/login`.
- Se envian cabeceras anti-cache en paginas privadas.
- Existe flujo de `postAuthRedirect` para volver a la ruta objetivo luego de login/registro.

## Reglas de negocio criticas (preservar)

### Usuarios
- Login exige:
  - correo existente
  - usuario activo
  - password exacto contra `passwordHash` (sin hashing actual en codigo)
- Registro:
  - correo normalizado (`trim + lowercase`)
  - nombre normalizado (`trim`)
  - correo unico
  - rol `CLIENTE` activo obligatorio
- Edicion admin:
  - conserva `passwordHash` y estado activo actual
  - valida correo unico (excepto mismo id)
  - no permite asignar rol inactivo
- Cambio de estado: toggle activo/inactivo.

### Suscripciones
- Solo usuario autenticado y activo puede contratar.
- El plan debe existir y estar activo.
- Antes de crear nueva suscripcion:
  - cierra todas las suscripciones `ACTIVA` del usuario
  - las marca `FINALIZADA`
  - `fechaFin = hoy - 1`
  - observacion de reemplazo
- Nueva suscripcion:
  - `estado = ACTIVA`
  - `fechaInicio = hoy`
  - `fechaFin = hoy + diasDuracion - 1` (minimo 1 dia)
  - `precioAplicado` desde precio del plan

### Comics
- Titulo obligatorio (trim).
- Autor y categoria deben existir y estar activos.
- Portada opcional en edicion, pero si se envia:
  - max 5MB
  - tipos permitidos: `image/png`, `image/jpeg`, `image/webp`
- Si falla guardado en BD tras subir imagen nueva, se elimina el blob nuevo.
- Si guardado exitoso y habia portada previa, intenta borrar portada anterior.
- Cambio de estado: toggle activo/inactivo.

### Autores/Categorias
- Validan campo principal obligatorio (`nombre` / `descripcion`).
- Validan duplicado case-insensitive.
- En edicion conservan estado activo previo.
- Cambio de estado: toggle activo/inactivo.

## Comportamiento de UI a mantener
- Listados admin:
  - filtro `TODOS|ACTIVOS|INACTIVOS`
  - paginacion (10 por pagina)
  - modales de confirmacion para activar/desactivar
  - feedback visual por query param (`estadoActualizado=true`)
- Layout admin unico: `templates/admin/components/layout.html`
- `home` y `suscripciones` comparten experiencia de autenticacion por modal y respetan `redirectTo`.

## Contratos de datos y repositorios importantes
- Se usan `EntityGraph` en `UsuarioRepository` y `ComicRepository` para cargar relaciones (`rol`, `autor`, `categoria`) y evitar problemas de carga perezosa en vistas.
- Columna booleana mapeada con `BooleanToIntegerConverter`.

## Dependencias/infra que no deben romperse
- Variables de entorno obligatorias:
  - datasource (`SPRING_DATASOURCE_*`)
  - JPA (`SPRING_JPA_*`)
  - Azure Blob (`AZURE_STORAGE_CONNECTION_STRING`, `AZURE_STORAGE_CONTAINER_NAME`)
- `multipart` limitado a 5MB por configuracion y validacion en servicio.

## Checklist de regresion para comparar contra otra rama
1. Login de ADMIN redirige a `/admin`; CLIENTE redirige a `/home`.
2. `/admin/**` sigue bloqueado sin sesion ADMIN.
3. Registro crea usuario `CLIENTE` activo con correo normalizado.
4. Listados admin mantienen filtro por estado + paginacion.
5. Toggle de estado funciona en usuarios/autores/categorias/comics.
6. En comics, validaciones de portada y manejo Azure siguen intactos.
7. Contratar plan finaliza suscripciones activas previas y crea una nueva activa.
8. `home` y `suscripciones` siguen respetando `postAuthRedirect`/`redirectTo`.
9. Repositorios conservan consultas por estado y `EntityGraph` necesarios.

## Nota de uso para la siguiente integracion
Cuando traigas cambios de otra rama, usa este baseline como referencia: si hay conflicto funcional, priorizar el comportamiento documentado aqui salvo que indiques explicitamente un cambio de regla.
