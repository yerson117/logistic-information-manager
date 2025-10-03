# 🚢 Logistic Information Manager

Sistema de gestión de información logística que permite el procesamiento progresivo de datos de bookings, contenedores, órdenes de compra e invoices, con capacidad de establecer relaciones entre entidades de forma incremental.

## 📋 Tabla de Contenidos

- [Arquitectura](#-arquitectura)
- [Funcionalidades](#-funcionalidades)
- [API Endpoints](#-api-endpoints)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Entidades del Dominio](#-entidades-del-dominio)
- [Casos de Uso](#-casos-de-uso)
- [Configuración](#-configuración)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Instalación y Ejecución](#-instalación-y-ejecución)

## 🏗️ Arquitectura

El proyecto implementa **Clean Architecture** con los siguientes módulos:

### Domain Layer
- **`domain/model`**: Entidades de negocio (BOs) que encapsulan la lógica del dominio
- **`domain/usecase`**: Casos de uso que orquestan la lógica de aplicación

### Infrastructure Layer
- **`infrastructure/entry-points/rest-api`**: Controladores REST y DTOs
- **`infrastructure/driven-adapters/jpa-repository`**: Implementación de persistencia con JPA

### Application Layer
- **`applications/app-service`**: Configuración de Spring y ensamblaje de módulos

## 🎯 Funcionalidades

### ✅ Procesamiento Progresivo de Datos
- Recibe información de bookings, contenedores, órdenes e invoices en diferentes momentos
- Establece relaciones automáticamente entre entidades existentes
- Crea entidades básicas cuando se especifican relaciones con entidades inexistentes

### ✅ Gestión de Relaciones
- **Container ↔ Orders**: Relación many-to-many
- **Order ↔ Invoices**: Relación one-to-many
- **Booking ↔ Containers/Orders**: Relación opcional

### ✅ Validaciones de Negocio
- Prevención de duplicados por cliente
- Validación de consistencia de datos
- Aislamiento de datos por cliente

### ✅ Paginación y Consultas Optimizadas
- Todos los endpoints de consulta implementan paginación
- Parámetros de ordenamiento y filtrado
- Respuestas estructuradas con metadatos de paginación
- Optimización de rendimiento para grandes volúmenes de datos

### ✅ Arquitectura Mejorada
- **Principio de Responsabilidad Única**: Cada caso de uso maneja su propia lógica de relaciones
- **Cohesión Alta**: Lógica de relaciones integrada en los casos de uso correspondientes
- **Acoplamiento Bajo**: Eliminación de dependencias innecesarias entre casos de uso
- **Mantenibilidad**: Código más limpio y fácil de mantener

## 🌐 API Endpoints

### POST /api/email
Procesa información logística y establece relaciones entre entidades.

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Request Body:**
```json
{
  "booking": "BK123",
  "containers": [
    {
      "container": "MEDU1234567",
      "relatedOrders": ["PO123", "PO456"]
    }
  ],
  "orders": [
    {
      "purchase": "PO123",
      "relatedContainers": ["MEDU1234567"],
      "invoices": [
        {
          "invoice": "IN123"
        }
      ]
    }
  ]
}
```

### GET /api/orders
Obtiene todas las órdenes de compra del cliente autenticado con paginación.

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Campo de ordenamiento

**Ejemplo:**
```bash
GET /api/orders?page=0&size=10&sort=purchaseCode
```

### GET /api/containers
Obtiene todos los contenedores del cliente autenticado con paginación.

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Campo de ordenamiento

**Ejemplo:**
```bash
GET /api/containers?page=0&size=10&sort=containerCode
```

### GET /api/orders/{purchaseId}/containers
Obtiene todos los contenedores asociados a una orden específica con paginación.

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Campo de ordenamiento

**Ejemplo:**
```bash
GET /api/orders/PO123456/containers?page=0&size=5
```

### GET /api/containers/{containerId}/orders
Obtiene todas las órdenes asociadas a un contenedor específico con paginación.

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Campo de ordenamiento

**Ejemplo:**
```bash
GET /api/containers/550e8400-e29b-41d4-a716-446655440001/orders?page=0&size=5
```

### Respuesta de Paginación
Todos los endpoints de consulta retornan una respuesta paginada con la siguiente estructura:

```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5,
  "first": true,
  "last": false,
  "numberOfElements": 20
}
```

## 🔧 Casos de Uso

### ProcessEmailUseCase
**Ubicación**: `domain/usecase/src/main/kotlin/co/com/nauta/usecase/ProcessEmailUseCase.kt`

Orquestador principal que:
1. Valida duplicados y consistencia de datos
2. Procesa bookings, containers, órdenes e invoices
3. Establece relaciones explícitas entre entidades
4. Crea entidades básicas cuando es necesario para relaciones

**Métodos principales:**
- `processEmail()`: Método principal de procesamiento
- `validateNoDuplicates()`: Validación de duplicados
- `validateDataConsistency()`: Validación de consistencia
- `processContainerRelations()`: Procesamiento de relaciones de containers
- `processOrderRelations()`: Procesamiento de relaciones de órdenes
- `createBasicOrder()`: Creación de órdenes básicas
- `createBasicContainer()`: Creación de containers básicos

### ProcessBookingUseCase
**Ubicación**: `domain/usecase/src/main/kotlin/co/com/nauta/usecase/ProcessBookingUseCase.kt`

Maneja la creación y actualización de bookings:
- `process()`: Crea o actualiza un booking existente

### ProcessContainerUseCase
**Ubicación**: `domain/usecase/src/main/kotlin/co/com/nauta/usecase/ProcessContainerUseCase.kt`

Maneja la creación y actualización de containers, incluyendo relaciones:
- `process()`: Crea o actualiza un container existente
- `processMultiple()`: Procesa múltiples containers
- `associateWithOrders()`: Establece relaciones con órdenes
- `getAssociatedOrders()`: Obtiene órdenes relacionadas

### ProcessOrderUseCase
**Ubicación**: `domain/usecase/src/main/kotlin/co/com/nauta/usecase/ProcessOrderUseCase.kt`

Maneja la creación y actualización de órdenes, incluyendo relaciones:
- `process()`: Crea o actualiza una orden existente
- `processMultiple()`: Procesa múltiples órdenes
- `associateWithContainers()`: Establece relaciones con containers
- `getAssociatedContainers()`: Obtiene containers relacionados

### ProcessInvoiceUseCase
**Ubicación**: `domain/usecase/src/main/kotlin/co/com/nauta/usecase/ProcessInvoiceUseCase.kt`

Maneja la creación y actualización de invoices:
- `process()`: Crea o actualiza una invoice existente
- `processMultiple()`: Procesa múltiples invoices

### GetContainersUseCase
**Ubicación**: `domain/usecase/src/main/kotlin/co/com/nauta/usecase/GetContainersUseCase.kt`

Gestiona la consulta de containers y sus relaciones:
- `getContainersByClient()`: Obtiene containers por cliente (con y sin paginación)
- `getContainerById()`: Obtiene un container específico
- `getContainerByCode()`: Obtiene container por código
- `getContainersByBooking()`: Obtiene containers por booking
- `getContainersByOrder()`: Obtiene containers por orden (con y sin paginación)
- `getOrdersByContainer()`: Obtiene órdenes por container (con y sin paginación)

### GetOrdersUseCase
**Ubicación**: `domain/usecase/src/main/kotlin/co/com/nauta/usecase/GetOrdersUseCase.kt`

Gestiona la consulta de órdenes y sus relaciones:
- `getOrdersByClient()`: Obtiene órdenes por cliente (con y sin paginación)
- `getOrderById()`: Obtiene una orden específica
- `getOrderByPurchaseCode()`: Obtiene orden por código de compra
- `getOrdersByBooking()`: Obtiene órdenes por booking
- `getOrdersByContainer()`: Obtiene órdenes por container
- `getContainersByOrder()`: Obtiene containers por orden (con y sin paginación)
- `getInvoicesByOrder()`: Obtiene invoices por orden

## 🧪 Pruebas de la API

### Obtener Token JWT
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "password": "demo"
  }'
```

### Probar Paginación de Contenedores
```bash
curl -X GET "http://localhost:8080/api/containers?page=0&size=10&sort=containerCode" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

### Probar Paginación de Órdenes
```bash
curl -X GET "http://localhost:8080/api/orders?page=0&size=5&sort=purchaseCode" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

### Probar Relaciones con Paginación
```bash
# Órdenes de un contenedor
curl -X GET "http://localhost:8080/api/containers/{containerId}/orders?page=0&size=3" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Contenedores de una orden
curl -X GET "http://localhost:8080/api/orders/{purchaseCode}/containers?page=0&size=2" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📝 Ejemplos de Uso

### Escenario 1: Container con Órdenes Relacionadas
```json
{
  "containers": [
    {
      "container": "MEDU1234567",
      "relatedOrders": ["PO123", "PO456"]
    }
  ],
  "orders": []
}
```
**Resultado**: Crea el container y las órdenes básicas, establece las relaciones.

### Escenario 2: Orden con Containers Relacionados
```json
{
  "containers": [],
  "orders": [
    {
      "purchase": "PO123",
      "relatedContainers": ["MEDU1234567"],
      "invoices": [{"invoice": "IN123"}]
    }
  ]
}
```
**Resultado**: Crea la orden con invoice, crea el container básico, establece la relación.

### Escenario 3: Relaciones Bidireccionales
```json
{
  "containers": [
    {
      "container": "MEDU1234567",
      "relatedOrders": ["PO123"]
    }
  ],
  "orders": [
    {
      "purchase": "PO123",
      "relatedContainers": ["MEDU1234567"],
      "invoices": [{"invoice": "IN123"}]
    }
  ]
}
```
**Resultado**: Crea todas las entidades y establece relaciones bidireccionales.

### Escenario 4: Sin Relaciones (Compatibilidad)
```json
{
  "containers": [{"container": "MEDU1234567"}],
  "orders": [{"purchase": "PO123", "invoices": [{"invoice": "IN123"}]}]
}
```
**Resultado**: Funciona igual que antes, sin relaciones explícitas.

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17+
- Gradle 8.0+
- Base de datos PostgreSQL

### Comandos
```bash
# Compilar el proyecto
./gradlew build

# Ejecutar tests
./gradlew test

# Ejecutar la aplicación
./gradlew :app-service:bootRun
```

### Variables de Entorno
La aplicación utiliza las siguientes variables de entorno para configuración:

```bash
# JWT Configuration (OBLIGATORIO en producción)
export JWT_SECRET="your-super-secret-key-at-least-32-characters-long"
export JWT_EXPIRATION="86400000"  # 24 horas en milisegundos

# Ejecutar con variables de entorno
JWT_SECRET="your-secret-key" JWT_EXPIRATION="86400000" ./gradlew :app-service:bootRun
```

**⚠️ IMPORTANTE**: 
- En **desarrollo**: Se usan valores por defecto si no se configuran las variables
- En **producción**: Es **OBLIGATORIO** configurar `JWT_SECRET` con una clave segura de al menos 32 caracteres
- El `JWT_SECRET` por defecto es solo para desarrollo y NO debe usarse en producción

### Documentación API con Swagger
Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de la API:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

La documentación incluye:
- ✅ Descripción completa de todos los endpoints
- ✅ Ejemplos de request y response
- ✅ Esquemas de datos detallados
- ✅ Autenticación JWT integrada
- ✅ Pruebas interactivas desde el navegador

## 🎯 Cumplimiento del Challenge Técnico

✅ **"Es posible solo recibir la información de las órdenes o solo de los contenedores"**
✅ **"solo con el tiempo se puede conectar todo"**
✅ **"Mantener consistencia de relaciones entre entidades"**
✅ **"evitando duplicados o cruces de datos entre clientes"**
✅ **"Consultar estas entidades de forma individual y mostrar la data relacionada"**

### Mejoras Adicionales Implementadas

✅ **Paginación Completa**: Todos los endpoints de consulta implementan paginación para optimizar el rendimiento
✅ **Arquitectura Limpia**: Refactorización siguiendo principios SOLID y Clean Architecture
✅ **Documentación API**: Swagger/OpenAPI integrado para documentación interactiva
✅ **Gestión de Relaciones Optimizada**: Lógica de relaciones integrada en los casos de uso correspondientes
✅ **Escalabilidad**: Sistema preparado para manejar grandes volúmenes de datos

El sistema no solo cumple completamente con todos los requisitos del challenge técnico, sino que va más allá proporcionando una solución robusta, escalable y mantenible para el manejo de información logística progresiva.
