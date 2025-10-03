# 📋 COLECCIÓN COMPLETA DE CURL PARA POSTMAN - LOGISTIC INFORMATION MANAGER API

## 🔐 **1. AUTHENTICATION ENDPOINTS**

### **POST /api/auth/register**
```bash
curl --location 'http://localhost:8080/api/auth/register' \
--header 'Content-Type: application/json' \
--data '{
    "email": "usuario@test.com",
    "password": "password123",
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93"
}'
```

**Respuesta esperada:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example",
    "userId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93"
}
```

### **POST /api/auth/login**
```bash
curl --location 'http://localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "email": "usuario@test.com",
    "password": "password123"
}'
```

**Respuesta esperada:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example",
    "userId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93"
}
```

## 👥 **2. CLIENTS ENDPOINTS**

### **POST /api/clients** (SIN AUTENTICACIÓN - Solo para crear cliente inicial)
```bash
curl --location 'http://localhost:8080/api/clients' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Acme Corporation",
    "email": "contact@acme.com"
}'
```

**Respuesta esperada:**
```json
{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "name": "Acme Corporation",
    "email": "contact@acme.com",
    "createdAt": "2025-10-03T11:32:29.5613207",
    "updatedAt": "2025-10-03T11:32:29.5613207"
}
```

### **GET /api/clients** (CON AUTENTICACIÓN)
```bash
curl --location 'http://localhost:8080/api/clients' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example'
```

**Respuesta esperada:**
```json
[
    {
        "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
        "name": "Acme Corporation",
        "email": "contact@acme.com",
        "createdAt": "2025-10-03T11:32:29.5613207",
        "updatedAt": "2025-10-03T11:32:29.5613207"
    }
]
```

### **GET /api/clients/{clientId}** (CON AUTENTICACIÓN)
```bash
curl --location 'http://localhost:8080/api/clients/3e0a39aa-1dd5-4103-903c-f59ffd7f6f93' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example'
```

**Respuesta esperada:**
```json
{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "name": "Acme Corporation",
    "email": "contact@acme.com",
    "createdAt": "2025-10-03T11:32:29.5613207",
    "updatedAt": "2025-10-03T11:32:29.5613207"
}
```

### **PUT /api/clients/{clientId}** (CON AUTENTICACIÓN)
```bash
curl --location --request PUT 'http://localhost:8080/api/clients/3e0a39aa-1dd5-4103-903c-f59ffd7f6f93' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example' \
--data '{
    "name": "Acme Corp. Inc.",
    "email": "new_contact@acme.com"
}'
```

**Respuesta esperada:**
```json
{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "name": "Acme Corp. Inc.",
    "email": "new_contact@acme.com",
    "createdAt": "2025-10-03T11:32:29.5613207",
    "updatedAt": "2025-10-03T11:35:45.1234567"
}
```

## 📧 **3. EMAIL PROCESSING ENDPOINTS** (TODOS CON AUTENTICACIÓN)

### **POST /api/email - Booking**
```bash
curl --location 'http://localhost:8080/api/email' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example' \
--data '{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "bookingCode": "BK001",
    "bookingType": "BOOKING"
}'
```

**Respuesta esperada:**
```json
{
    "message": "Information processed successfully",
    "processedEntities": [
        {
            "type": "BOOKING",
            "code": "BK001",
            "status": "CREATED"
        }
    ],
    "relationships": []
}
```

### **POST /api/email - Container**
```bash
curl --location 'http://localhost:8080/api/email' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example' \
--data '{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "containerCode": "CONT001",
    "containerType": "20FT",
    "bookingCode": "BK001",
    "containerType": "CONTAINER"
}'
```

**Respuesta esperada:**
```json
{
    "message": "Information processed successfully",
    "processedEntities": [
        {
            "type": "CONTAINER",
            "code": "CONT001",
            "status": "CREATED"
        }
    ],
    "relationships": [
        {
            "from": "BK001",
            "to": "CONT001",
            "type": "BOOKING_CONTAINER"
        }
    ]
}
```

### **POST /api/email - Order**
```bash
curl --location 'http://localhost:8080/api/email' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example' \
--data '{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "purchaseCode": "PO001",
    "bookingCode": "BK001",
    "orderType": "ORDER"
}'
```

**Respuesta esperada:**
```json
{
    "message": "Information processed successfully",
    "processedEntities": [
        {
            "type": "ORDER",
            "code": "PO001",
            "status": "CREATED"
        }
    ],
    "relationships": [
        {
            "from": "BK001",
            "to": "PO001",
            "type": "BOOKING_ORDER"
        }
    ]
}
```

### **POST /api/email - Invoice**
```bash
curl --location 'http://localhost:8080/api/email' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example' \
--data '{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "invoiceCode": "INV001",
    "amount": 1500.00,
    "purchaseCode": "PO001",
    "invoiceType": "INVOICE"
}'
```

**Respuesta esperada:**
```json
{
    "message": "Information processed successfully",
    "processedEntities": [
        {
            "type": "INVOICE",
            "code": "INV001",
            "status": "CREATED"
        }
    ],
    "relationships": [
        {
            "from": "PO001",
            "to": "INV001",
            "type": "ORDER_INVOICE"
        }
    ]
}
```

## 📦 **4. ORDERS ENDPOINTS** (TODOS CON AUTENTICACIÓN)

### **GET /api/orders**
```bash
curl --location 'http://localhost:8080/api/orders?page=0&size=20&sort=createdAt,desc' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example'
```

**Respuesta esperada:**
```json
{
    "content": [
        {
            "orderId": "550e8400-e29b-41d4-a716-446655440000",
            "purchaseCode": "PO001",
            "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
            "bookingId": "770e8400-e29b-41d4-a716-446655440002",
            "createdAt": "2025-10-03T11:32:29.5613207",
            "updatedAt": "2025-10-03T11:32:29.5613207",
            "containers": [
                {
                    "containerId": "660e8400-e29b-41d4-a716-446655440001",
                    "containerCode": "CONT001",
                    "containerType": "20FT"
                }
            ],
            "invoices": [
                {
                    "invoiceId": "880e8400-e29b-41d4-a716-446655440003",
                    "invoiceCode": "INV001",
                    "amount": 1500.00
                }
            ]
        }
    ],
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 1,
        "totalPages": 1,
        "first": true,
        "last": true
    }
}
```

### **GET /api/orders/{orderId}**
```bash
curl --location 'http://localhost:8080/api/orders/550e8400-e29b-41d4-a716-446655440000' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example'
```

**Respuesta esperada:**
```json
{
    "orderId": "550e8400-e29b-41d4-a716-446655440000",
    "purchaseCode": "PO001",
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "bookingId": "770e8400-e29b-41d4-a716-446655440002",
    "createdAt": "2025-10-03T11:32:29.5613207",
    "updatedAt": "2025-10-03T11:32:29.5613207",
    "containers": [
        {
            "containerId": "660e8400-e29b-41d4-a716-446655440001",
            "containerCode": "CONT001",
            "containerType": "20FT"
        }
    ],
    "invoices": [
        {
            "invoiceId": "880e8400-e29b-41d4-a716-446655440003",
            "invoiceCode": "INV001",
            "amount": 1500.00
        }
    ]
}
```

### **GET /api/orders/{orderId}/containers**
```bash
curl --location 'http://localhost:8080/api/orders/550e8400-e29b-41d4-a716-446655440000/containers' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example'
```

**Respuesta esperada:**
```json
[
    {
        "containerId": "660e8400-e29b-41d4-a716-446655440001",
        "containerCode": "CONT001",
        "containerType": "20FT",
        "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
        "bookingId": "770e8400-e29b-41d4-a716-446655440002",
        "createdAt": "2025-10-03T11:32:29.5613207",
        "updatedAt": "2025-10-03T11:32:29.5613207"
    }
]
```

## 🚢 **5. CONTAINERS ENDPOINTS** (TODOS CON AUTENTICACIÓN)

### **GET /api/containers**
```bash
curl --location 'http://localhost:8080/api/containers?page=0&size=20&sort=createdAt,desc' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example'
```

**Respuesta esperada:**
```json
{
    "content": [
        {
            "containerId": "660e8400-e29b-41d4-a716-446655440001",
            "containerCode": "CONT001",
            "containerType": "20FT",
            "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
            "bookingId": "770e8400-e29b-41d4-a716-446655440002",
            "createdAt": "2025-10-03T11:32:29.5613207",
            "updatedAt": "2025-10-03T11:32:29.5613207",
            "orders": [
                {
                    "orderId": "550e8400-e29b-41d4-a716-446655440000",
                    "purchaseCode": "PO001"
                }
            ]
        }
    ],
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 1,
        "totalPages": 1,
        "first": true,
        "last": true
    }
}
```

### **GET /api/containers/{containerId}**
```bash
curl --location 'http://localhost:8080/api/containers/660e8400-e29b-41d4-a716-446655440001' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example'
```

**Respuesta esperada:**
```json
{
    "containerId": "660e8400-e29b-41d4-a716-446655440001",
    "containerCode": "CONT001",
    "containerType": "20FT",
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "bookingId": "770e8400-e29b-41d4-a716-446655440002",
    "createdAt": "2025-10-03T11:32:29.5613207",
    "updatedAt": "2025-10-03T11:32:29.5613207",
    "orders": [
        {
            "orderId": "550e8400-e29b-41d4-a716-446655440000",
            "purchaseCode": "PO001"
        }
    ]
}
```

### **GET /api/containers/{containerId}/orders**
```bash
curl --location 'http://localhost:8080/api/containers/660e8400-e29b-41d4-a716-446655440001/orders' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example'
```

**Respuesta esperada:**
```json
[
    {
        "orderId": "550e8400-e29b-41d4-a716-446655440000",
        "purchaseCode": "PO001",
        "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
        "bookingId": "770e8400-e29b-41d4-a716-446655440002",
        "createdAt": "2025-10-03T11:32:29.5613207",
        "updatedAt": "2025-10-03T11:32:29.5613207"
    }
]
```

## 📊 **6. HEALTH CHECK** (SIN AUTENTICACIÓN)

### **GET /actuator/health**
```bash
curl --location 'http://localhost:8080/actuator/health'
```

**Respuesta esperada:**
```json
{
    "status": "UP",
    "components": {
        "db": {
            "status": "UP"
        },
        "diskSpace": {
            "status": "UP"
        },
        "ping": {
            "status": "UP"
        }
    }
}
```

---

## 🚀 **INSTRUCCIONES PARA POSTMAN**

### **Configuración de Variables de Entorno:**
1. **Crea una nueva colección** llamada "Logistic Information Manager API"
2. **Configura variables de entorno**:
   - `baseUrl`: `http://localhost:8080`
   - `jwtToken`: (se llenará automáticamente después del login)
   - `clientId`: `3e0a39aa-1dd5-4103-903c-f59ffd7f6f93`

### **Importación de CURLs:**
1. **Importa cada CURL** usando "Import" > "Raw text"
2. **Reemplaza** `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzZTBhMzlhYS0xZGQ1LTQxMDMtOTAzYy1mNTlmZmQ3ZjZmOTMiLCJpYXQiOjE3MzU4OTU1NDksImV4cCI6MTczNTkzMTU0OX0.example` con `{{jwtToken}}` en todos los endpoints que requieren autenticación
3. **Reemplaza** `http://localhost:8080` con `{{baseUrl}}` en todos los CURLs

### **Flujo de Testing Recomendado:**
1. **Crear cliente** (POST /api/clients) - SIN autenticación
2. **Registrar usuario** (POST /api/auth/register) - SIN autenticación
3. **Login** (POST /api/auth/login) - SIN autenticación
4. **Procesar datos logísticos** (POST /api/email) - CON autenticación
5. **Consultar datos** (GET endpoints) - CON autenticación

### **Notas Importantes:**
- **Solo el endpoint POST /api/clients NO requiere autenticación** (para crear el cliente inicial)
- **Todos los demás endpoints de clients requieren autenticación**
- **Todos los endpoints de email processing requieren autenticación**
- **Todos los endpoints de orders y containers requieren autenticación**
- **El health check NO requiere autenticación**

---

## 📝 **EJEMPLOS DE DTOs DE ENTRADA**

### **RegisterRequestDto**
```json
{
    "email": "usuario@test.com",
    "password": "password123",
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93"
}
```

### **AuthRequestDto**
```json
{
    "email": "usuario@test.com",
    "password": "password123"
}
```

### **CreateClientRequestDto**
```json
{
    "name": "Acme Corporation",
    "email": "contact@acme.com"
}
```

### **UpdateClientRequestDto**
```json
{
    "name": "Acme Corp. Inc.",
    "email": "new_contact@acme.com"
}
```

### **EmailRequestDto - Booking**
```json
{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "bookingCode": "BK001",
    "bookingType": "BOOKING"
}
```

### **EmailRequestDto - Container**
```json
{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "containerCode": "CONT001",
    "containerType": "20FT",
    "bookingCode": "BK001",
    "containerType": "CONTAINER"
}
```

### **EmailRequestDto - Order**
```json
{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "purchaseCode": "PO001",
    "bookingCode": "BK001",
    "orderType": "ORDER"
}
```

### **EmailRequestDto - Invoice**
```json
{
    "clientId": "3e0a39aa-1dd5-4103-903c-f59ffd7f6f93",
    "invoiceCode": "INV001",
    "amount": 1500.00,
    "purchaseCode": "PO001",
    "invoiceType": "INVOICE"
}
```

---

## ⚠️ **NOTAS DE SEGURIDAD**

- **POST /api/clients**: NO requiere autenticación (solo para crear cliente inicial)
- **GET /api/clients**: REQUIERE autenticación
- **GET /api/clients/{id}**: REQUIERE autenticación  
- **PUT /api/clients/{id}**: REQUIERE autenticación
- **Todos los endpoints de /api/email**: REQUIEREN autenticación
- **Todos los endpoints de /api/orders**: REQUIEREN autenticación
- **Todos los endpoints de /api/containers**: REQUIEREN autenticación
- **GET /actuator/health**: NO requiere autenticación
