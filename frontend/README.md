# Foro Hub Frontend

Frontend moderno para la API de Foro Hub, construido con React, Vite, TailwindCSS y componentes UI personalizados.

## 🚀 Características

- **Autenticación JWT**: Login y registro de usuarios
- **Gestión de Preguntas**: Listar, crear, ver detalles y filtrar preguntas
- **Gestión de Respuestas**: Agregar respuestas a preguntas
- **Gestión de Cursos**: Ver cursos y sus preguntas asociadas
- **Gestión de Usuarios**: Ver usuarios y sus preguntas
- **Diseño Moderno**: UI limpia con TailwindCSS y componentes personalizados
- **Responsive**: Diseño adaptable a diferentes tamaños de pantalla

## 📋 Requisitos Previos

- Node.js (v18 o superior)
- npm (v9 o superior)

## 🛠️ Instalación

1. Navega al directorio del frontend:
```bash
cd frontend
```

2. Instala las dependencias:
```bash
npm install
```

## ▶️ Ejecutar en Desarrollo

1. Asegúrate de que tu API backend de Spring Boot esté corriendo en `http://localhost:8080`

2. Inicia el servidor de desarrollo:
```bash
npm run dev
```

3. Abre tu navegador en `http://localhost:5173`

## 🏗️ Construir para Producción

```bash
npm run build
```

Los archivos compilados estarán en el directorio `dist/`.

## 📁 Estructura del Proyecto

```
frontend/
├── src/
│   ├── components/       # Componentes reutilizables
│   │   ├── ui/          # Componentes UI base (Button, Input, Card, etc.)
│   │   └── Layout.jsx   # Layout principal con navegación
│   ├── context/         # Contextos de React
│   │   └── AuthContext.jsx  # Contexto de autenticación
│   ├── lib/             # Utilidades
│   │   └── utils.js     # Funciones helper (cn para className)
│   ├── pages/           # Páginas de la aplicación
│   │   ├── Login.jsx
│   │   ├── Register.jsx
│   │   ├── Home.jsx
│   │   ├── PreguntaDetail.jsx
│   │   ├── NuevaPregunta.jsx
│   │   ├── Cursos.jsx
│   │   └── Usuarios.jsx
│   ├── services/        # Servicios de API
│   │   └── api.js       # Cliente axios con interceptores
│   ├── App.jsx          # Componente principal con rutas
│   ├── main.jsx         # Punto de entrada
│   └── index.css        # Estilos globales y Tailwind
├── index.html           # HTML principal
├── package.json         # Dependencias y scripts
├── vite.config.js       # Configuración de Vite
├── tailwind.config.js   # Configuración de TailwindCSS
└── postcss.config.js    # Configuración de PostCSS
```

## 🔐 Autenticación

El frontend utiliza tokens JWT para la autenticación. El token se almacena en `localStorage` y se incluye automáticamente en todas las requests a la API.

- **Login**: `POST /login`
- **Registro**: `POST /usuarios`

## 📡 Endpoints de la API

El frontend se conecta a los siguientes endpoints de tu API Spring Boot:

### Autenticación
- `POST /login` - Iniciar sesión

### Preguntas
- `GET /preguntas` - Listar preguntas (paginado)
- `GET /preguntas/antiguos-primero` - Listar preguntas antiguas primero
- `GET /preguntas/por-status/{status}` - Filtrar por status
- `GET /preguntas/{id}` - Obtener detalle de pregunta
- `POST /preguntas` - Crear nueva pregunta
- `PUT /preguntas` - Actualizar pregunta
- `DELETE /preguntas/{id}` - Eliminar pregunta

### Respuestas
- `GET /respuestas` - Listar respuestas
- `GET /respuestas/{id}` - Obtener detalle de respuesta
- `POST /respuestas` - Crear nueva respuesta
- `PUT /respuestas` - Actualizar respuesta

### Usuarios
- `POST /usuarios` - Registrar usuario
- `GET /usuarios` - Listar usuarios
- `GET /usuarios/{id}/preguntas` - Obtener preguntas de usuario
- `DELETE /usuarios/{id}` - Eliminar usuario

### Cursos
- `GET /cursos` - Listar cursos
- `GET /cursos/{id}/preguntas` - Obtener preguntas de curso

## 🎨 Componentes UI

El proyecto incluye componentes UI personalizados inspirados en shadcn/ui:

- **Button**: Botones con diferentes variantes (default, destructive, outline, secondary, ghost, link)
- **Input**: Campos de entrada de texto
- **Textarea**: Áreas de texto multilínea
- **Card**: Tarjetas con header, title, description, content y footer
- **Badge**: Etiquetas/insignias para mostrar estados

## 🔧 Configuración del Proxy

Vite está configurado con un proxy para redirigir las requests de la API al backend:

```javascript
// vite.config.js
proxy: {
  '/login': { target: 'http://localhost:8080', changeOrigin: true },
  '/preguntas': { target: 'http://localhost:8080', changeOrigin: true },
  '/respuestas': { target: 'http://localhost:8080', changeOrigin: true },
  '/usuarios': { target: 'http://localhost:8080', changeOrigin: true },
  '/cursos': { target: 'http://localhost:8080', changeOrigin: true }
}
```

## 📝 Scripts Disponibles

- `npm run dev` - Inicia servidor de desarrollo
- `npm run build` - Construye para producción
- `npm run preview` - Previa la build de producción
- `npm run lint` - Ejecuta ESLint

## 🐛 Solución de Problemas

### El frontend no se conecta a la API
- Asegúrate de que el backend de Spring Boot esté corriendo en `http://localhost:8080`
- Verifica que no haya firewalls bloqueando la conexión

### Error 401 Unauthorized
- Verifica ότι el token JWT se está guardando correctamente en localStorage
- Revisa que el token no haya expirado

### Las páginas no cargan
- Asegúrate de haber instalado las dependencias con `npm install`
- Verifica que no haya errores en la consola del navegador

## 📄 Licencia

Este proyecto es parte de Foro Hub.
