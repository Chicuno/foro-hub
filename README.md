<h1 align="center">Foro Hub</h1>
<h2 align="center">API Rest para el manejo de un foro</h2>

Éste es un proyecto de práctica en mi formación Back-end para manejar las funcionalidades CRUD de un foro, incluyendo:

**USUARIOS:**

-crear 

-listar

-borrar.


**CURSOS:**

-crear

-listar

**TÓPICOS:**

-crear

-listar por defecto en orden por más reciente primero y listar en orden por más antiguo primero

-listar por status, por autor, o por curso

-detallar un tópico

-actualizar

-eliminar

**RESPUESTAS:**

-crear

-listar todas, por usuario o por tópico

-detallar

-editar

Se requiere autenticación para acceder a la API con autorización por medio de JSON Web Token. 
El acceso es por medio de nombre de usuario y contraseña, devolviendo un token con un tiempo de expiración de 2 horas. 

**La autorización** para acceder a las diferentes requisiciones se otorga a los roles de usuario de la siguiente manera:

-Inicio de sesión: TODOS

-Crear usuario: ADMINISTRADOR

-Listar usuarios: ADMINISTRADOR, PROFESOR

-Borrar usuarios: ADMINISTRADOR

-Crear un curso: ADMINISTRADOR

-Actualizar un tópico: ADMINISTRADOR, PROFESOR y el autor del tópico

-Borrar un tópico: ADMINISTRADOR, PROFESOR

-Editar una respuesta: ADMINISTRADOR, PROFESOR y el autor de la respuesta

-Todas las demás requisiciones están permitidas a los usuarios registrados con cualquier rol.

<br>

También se implementa el manejo de errores para devolver un mensaje más claro al usuario.


<br><br><br>


<h2>Aquí puedes ver la captura de pantalla de Swagger con sus funcionalidades</h2>

<br><br>

![Captura de pantalla de Swagger](images/swagger.jpeg)

<h2>Tecnologías utilizadas:</h2>
<h2>Java 17</h2>
<h2>Spring Boot 3.0.0</h2>
<h2>Maven 4.0.0</h2>
<h2>Flyway</h2>
<h2>Lombok</h2>
<h2>Java JWT 4.5.0</h2>
<h2>Spring Doc 2.5.0</h2>


