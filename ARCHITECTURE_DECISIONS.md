# Architecture Decisions for NanysCare

## Context
El proyecto es una aplicación Android nativa con Jetpack Compose que conecta tutores, cuidadores, administradores y supervisores en una plataforma local de gestión de reservas y comunicación.

El objetivo de este documento es capturar las decisiones de arquitectura actuales, su justificación y los impactos observados a partir del código existente.

---

## 1. Arquitectura de capas

### Decisión
Usar una arquitectura de capas con separación clara entre:
- `core/`: infraestructura, utilidades y servicios globales
- `data/`: persistencia local y repositorios
- `domain/`: modelos de negocio y tipos compartidos
- `presentation/`: UI Compose y navegación

### Justificación
Esta separación facilita el mantenimiento, permite tener modelos de dominio independientes de la persistencia y permite que la UI consuma datos a través de repositorios y un `ViewModel` central.

### Impacto
- Mejora la legibilidad y organización del código.
- Facilita el testing y la evolución futura.
- El `domain` contiene modelos de negocio puros, aislados del framework.

---

## 2. MVVM con un ViewModel centralizado

### Decisión
Mantener la lógica de estado de pantalla en un `NanysViewModel` central y exponer flujos de datos mediante `StateFlow`.

### Justificación
MVVM es un patrón natural con Jetpack Compose y permite que la UI observe estado reactivo sin depender de eventos específicos de Android.

### Impacto
- Simplifica el enlace entre UI y datos.
- Permite recargar estado de sesión y datos de usuario de forma central.
- Aumenta el riesgo de que el ViewModel crezca demasiado si no se divide en componentes más pequeños.

---

## 3. UI declarativa con Jetpack Compose

### Decisión
Construir toda la interfaz con Jetpack Compose y Material 3.

### Justificación
Compose permite desarrollar pantallas complejas con menos boilerplate, uso directo de funciones `@Composable` y navegación fluida con `NavHost`.

### Impacto
- La aplicación usa `compose.ui`, `compose.material3`, `navigation.compose` y `viewModel` de Compose.
- Las pantallas del dominio de cada rol (`Tutor`, `Caregiver`, `Admin`, `Supervisor`) están organizadas por paquete.
- Facilita el diseño de interfaces reactivas y la reutilización de componentes comunes.

---

## 4. Navegación con Navigation Compose

### Decisión
Usar `NavHost` y `composable` para modelar la navegación basada en rutas.

### Justificación
Navigation Compose es la forma recomendada para aplicaciones Compose y permite pasar argumentos tipados entre pantallas.

### Impacto
- El grafo de navegación se encuentra en `presentation/navigation/NanysNavGraph.kt`.
- El inicio de la aplicación depende del rol del usuario (`startDestinationForRole`).
- La navegación soporta rutas con argumentos como `caregiverEmail`, `bookingId` o `otherEmail`.

---

## 5. Persistencia local con Room

### Decisión
Persistir datos con Room en una base de datos local, con DAOs para cada entidad.

### Justificación
Room provee seguridad de tipos, consultas SQL declarativas y soporta flujos reactivas que se integran bien con Compose.

### Impacto
- Entidades principales: `UserEntity`, `BookingEntity`, `MessageEntity`, `ReviewEntity`, `PrivateNoteEntity`, `CatalogItemEntity`, `ChildEntity`, `CaregiverProfileEntity`, `TutorProfileEntity`.
- La base de datos se define en `data/local/db/NanysDatabase.kt` con versión `2`.
- Se usa migración de `1` a `2` para agregar el campo `childIds`.

---

## 6. DI manual con AppContainer

### Decisión
No usar Hilt o Dagger; inyectar dependencias manualmente en `AppContainer`.

### Justificación
Para mantener la simplicidad del proyecto y reducir la complejidad de configuración de DI.

### Impacto
- `NanysApplication` crea `AppContainer` en `onCreate()`.
- `MainActivity` obtiene `container` desde la aplicación y crea `NanysViewModelFactory`.
- Los repositorios, el `SessionManager`, el `LocalNotificationHelper` y el `SimulationService` se resuelven desde un único contenedor.

---

## 7. Simulación local de backend y servicios

### Decisión
No hay backend remoto real; la app simula correo electrónico, pagos y notificaciones desde el propio cliente.

### Justificación
La aplicación es una demo local que necesita mostrar flujos de negocio sin depender de servicios externos.

### Impacto
- `SimulationService` modela acciones simuladas como envío de email.
- `DatabaseSeeder` carga cuentas de ejemplo, usuarios, perfiles, reservas y mensajes.
- La app puede ejecutarse sin red, lo que simplifica pruebas locales.

---

## 8. Sesión persistente con SharedPreferences

### Decisión
Guardar el estado de sesión en `SharedPreferences` usando `SessionManager`.

### Justificación
Es un método ligero y suficiente para la persistencia de login local.

### Impacto
- El `ViewModel` consulta `SessionManager` para determinar `isLoggedIn`, `userEmail` y `userRole`.
- El inicio condicional (`login` vs rol) se basa en esos valores.
- Existe un riesgo identificado: la conversión de rol inválido a `TUTOR` puede causar comportamiento inesperado.

---

## 9. Gestión de notificaciones y WorkManager

### Decisión
Usar `LocalNotificationHelper` para notificaciones locales y `WorkManager` para recordatorios de reservas.

### Justificación
WorkManager es adecuado para tareas programadas que deben ejecutarse incluso si la app no está activa.

### Impacto
- `BookingReminderWorker` programa un recordatorio 15 minutos antes de la reserva.
- `LocalNotificationHelper` crea un canal de notificación y publica notificaciones locales.
- Se solicita permiso `POST_NOTIFICATIONS` en `MainActivity` para Android 13+.

---

## 10. Modelo de dominio y mapeo

### Decisión
Separar entidades de persistencia (`Entity`) de los modelos de dominio (`domain.model`).

### Justificación
Permite cambiar la persistencia sin afectar la lógica de negocio y facilita mantener una API limpia para la UI.

### Impacto
- Los mapeos se realizan en `data/mapper/Mappers.kt`.
- Los repositorios exponen objetos de dominio a la capa de presentación.
- Las entidades incluyen campos como `childIds` en cadena que se transforman en listas en el dominio.

---

## 11. Roles y navegación basada en contexto

### Decisión
Diferenciar claramente los flujos por rol de usuario: `CUIDADOR`, `TUTOR`, `ADMIN`, `SUPERVISOR`.

### Justificación
Cada rol tiene vistas y acciones distintas, por lo que la UI debe adaptarse a la autorización del usuario.

### Impacto
- El `NavGraph` define pantallas específicas para cada rol.
- La ruta de inicio se calcula según el rol almacenado en sesión.
- Existen pantallas comunes como chat y configuración que se reutilizan entre roles.

---

## 12. Manejo de datos de demo con DatabaseSeeder

### Decisión
Inicializar datos básicos de la aplicación en `NanysApplication` con `DatabaseSeeder.seedIfEmpty()`.

### Justificación
Permite tener una experiencia de demo funcional inmediata sin intervención manual.

### Impacto
- Se crean usuarios demo, perfiles, hijos, reservas, mensajes y reseñas.
- El seed se ejecuta en un `CoroutineScope` con `Dispatchers.IO`.
- La comprobación actual considera que basta con la existencia del usuario `admin@nanys.com`.

---

## Observaciones adicionales

- El proyecto está preparado para ser un prototipo/demo más que una aplicación de producción completa.
- `targetSdk = 35` y `compileSdk = 35` son válidos, pero podrían actualizarse.
- La base de código muestra una buena separación de responsabilidades y un uso moderno de Compose.
- Algunos riesgos detectados:
  - rol inválido se mapea silenciosamente a `TUTOR`
  - permisos declarados no usados (`READ_MEDIA_IMAGES`)
  - validaciones de `timeSlot` y rangos horarios no robustas
  - la base de datos no exporta esquema

---

## Recomendaciones de evolución

1. Considerar la migración a un contenedor de DI más estructurado si la app crece.
2. Añadir validaciones robustas en reservas (`timeSlot`, horario, duración).
3. Exponer el esquema de Room (`exportSchema = true`) y versionar migraciones.
4. Revisar el manejo de permisos de notificaciones y eliminar permisos no utilizados.
5. Dividir el `ViewModel` central en subcomponentes si el estado crece demasiado.
