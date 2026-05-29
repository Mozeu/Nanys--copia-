# Nanys Care

Aplicación Android nativa para conectar familias con cuidadores infantiles.

## Stack
- Kotlin + Jetpack Compose (Material 3)
- Room (base de datos local)
- Navigation Compose
- WorkManager (recordatorios)
- Clean Architecture simplificada

## Requisitos
- Android Studio Panda 4 | 2025.3.4 Patch 1
- JDK 17
- minSdk 26, targetSdk 35

## Cuentas demo
| Rol | Email | Contraseña |
|-----|-------|------------|
| Tutor | tutor1@test.com | 123 |
| Cuidador | cuidador1@test.com | 123 |
| Admin | admin@nanys.com | admin123 |
| Supervisor | supervisor@nanys.com | super123 |

## Abrir en Android Studio
1. File → Open → seleccionar carpeta `Nanys`
2. Esperar sync de Gradle
3. Run en emulador o dispositivo

## Notas de demo
- Pagos, correos y push están simulados (Toast/diálogos/notificaciones locales)
- Geolocalización simulada con spinners Ciudad/Estado
- Verificación de antecedentes editable por Supervisor
