package com.fernandez.foro_hub.config;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fernandez.foro_hub.domain.curso.Categoria;
import com.fernandez.foro_hub.domain.curso.Curso;
import com.fernandez.foro_hub.domain.curso.CursoRepository;
import com.fernandez.foro_hub.domain.respuesta.Respuesta;
import com.fernandez.foro_hub.domain.respuesta.RespuestaRepository;
import com.fernandez.foro_hub.domain.pregunta.Status;
import com.fernandez.foro_hub.domain.pregunta.Pregunta;
import com.fernandez.foro_hub.domain.pregunta.PreguntaRepository;
import com.fernandez.foro_hub.domain.usuario.Perfil;
import com.fernandez.foro_hub.domain.usuario.Usuario;
import com.fernandez.foro_hub.domain.usuario.UsuarioRepository;

import jakarta.transaction.Transactional;
import net.datafaker.Faker;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaRepository respuestaRepository;
    private final PasswordEncoder passwordEncoder;

    private final Faker faker = new Faker();

    public DataSeeder(
            UsuarioRepository usuarioRepository,
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder,
            CursoRepository cursoRepository,
            PreguntaRepository preguntaRepository,
            RespuestaRepository respuestaRepository) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.cursoRepository = cursoRepository;
        this.preguntaRepository = preguntaRepository;
        this.respuestaRepository = respuestaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {      
        crearAdministrador();
        crearProfesores();

    if(usuarioRepository.count() < 23){
        crearUsuariosFake();
    }

    if(cursoRepository.count() == 0){
        crearCursos();
    }


    if(preguntaRepository.count() == 0){
        crearPreguntas();
    }

    if(respuestaRepository.count() == 0){
        crearRespuestas();
    }
    }


    private void crearAdministrador(){

    if(usuarioRepository.findByNombreUsuarioAndActivoTrue("admin") != null){
        return;
    }


    Usuario admin = new Usuario();


    admin.setNombre("Administrador");

    admin.setCorreoElectronico("admin@forohub.com");

    admin.setNombreUsuario("admin");

    admin.setContrasena(
"$2a$10$YuTRjFNqRNbsEyxXGnSFfOPcuoASR3.1CwLRQvdm06UpZ/DmgcMjG"
);

    admin.setPerfil(Perfil.ADMINISTRADOR);

    admin.setActivo(true);

    usuarioRepository.save(admin);

}

    private void crearProfesores(){

    if(usuarioRepository.findByNombreUsuarioAndActivoTrue("Profe1") != null){
        return;
    }


    Usuario profe1 = new Usuario();
    Usuario profe2 = new Usuario();


    profe1.setNombre("Profesor 1");
    profe2.setNombre("Profesor 2");

    profe1.setCorreoElectronico("profesor1@forohub.com");
    profe2.setCorreoElectronico("profesor2@forohub.com");

    profe1.setNombreUsuario("Profe1");
    profe2.setNombreUsuario("Profe2");

    profe1.setContrasena(
        passwordEncoder.encode("123456")
    );
    profe2.setContrasena(
        passwordEncoder.encode("123456")
    );


    profe1.setPerfil(Perfil.PROFESOR);
    profe2.setPerfil(Perfil.PROFESOR);

    profe1.setActivo(true);
    profe2.setActivo(true);

    usuarioRepository.save(profe1);
    usuarioRepository.save(profe2);

}

    private void crearUsuariosFake(){
        for(int i = 0; i < 20; i++){

            Usuario usuario = new Usuario();

            usuario.setNombre(faker.name().fullName());
            usuario.setCorreoElectronico(
                faker.internet().emailAddress()
            );
            usuario.setNombreUsuario(
                faker.internet().username()
            );
            usuario.setContrasena(
                passwordEncoder.encode("123456")
            );
            usuario.setPerfil(Perfil.ALUMNO);


            usuarioRepository.save(usuario);
        }

        System.out.println("Usuarios simulados creados");
    }

    private void crearCursos(){

    Curso curso;


    curso = new Curso();
    curso.setNombre("Spring Boot API REST");
    curso.setCategoria(Categoria.BACKEND);
    cursoRepository.save(curso);


    curso = new Curso();
    curso.setNombre("Spring Security con JWT");
    curso.setCategoria(Categoria.BACKEND);
    cursoRepository.save(curso);


    curso = new Curso();
    curso.setNombre("Hibernate y JPA");
    curso.setCategoria(Categoria.BACKEND);
    cursoRepository.save(curso);


    curso = new Curso();
    curso.setNombre("Java desde cero");
    curso.setCategoria(Categoria.PROGRAMACION);
    cursoRepository.save(curso);


    curso = new Curso();
    curso.setNombre("Bases de datos SQL");
    curso.setCategoria(Categoria.BASE_DATOS);
    cursoRepository.save(curso);


    curso = new Curso();
    curso.setNombre("React desde cero");
    curso.setCategoria(Categoria.FRONTEND);
    cursoRepository.save(curso);


    curso = new Curso();
    curso.setNombre("Angular avanzado");
    curso.setCategoria(Categoria.FRONTEND);
    cursoRepository.save(curso);


    curso = new Curso();
    curso.setNombre("Docker para desarrolladores");
    curso.setCategoria(Categoria.DEVOPS);
    cursoRepository.save(curso);


    curso = new Curso();
    curso.setNombre("Microservicios con Spring Cloud");
    curso.setCategoria(Categoria.BACKEND);
    cursoRepository.save(curso);


    curso = new Curso();
    curso.setNombre("Git y GitHub");
    curso.setCategoria(Categoria.HERRAMIENTAS);
    cursoRepository.save(curso);

}

    private void crearPreguntas() {

    Curso springBoot = cursoRepository.findByNombre("Spring Boot API REST").orElseThrow();

    Curso security = cursoRepository.findByNombre("Spring Security con JWT").orElseThrow();

    Curso jpa = cursoRepository.findByNombre("Hibernate y JPA").orElseThrow();

    Curso java = cursoRepository.findByNombre("Java desde cero").orElseThrow();

    Curso sql = cursoRepository.findByNombre("Bases de datos SQL").orElseThrow();

    Curso frontend = cursoRepository.findByNombre("React desde cero").orElseThrow();

    Curso docker = cursoRepository.findByNombre("Docker para desarrolladores").orElseThrow();

    Curso microservicios = cursoRepository.findByNombre("Microservicios con Spring Cloud").orElseThrow();

crearPregunta(
        "Error al levantar Spring Boot",
        "Mi aplicación no inicia al ejecutar el proyecto y muestra errores en consola.",
        springBoot,
        Status.RESUELTO
);


crearPregunta(
        "Problema con relación ManyToOne en JPA",
        "Al guardar una entidad relacionada recibo un error de persistencia.",
        jpa,
        Status.RESUELTO
);


crearPregunta(
        "JWT devuelve error 403",
        "El token se genera correctamente pero no puedo acceder a los endpoints protegidos.",
        security,
        Status.RESUELTO
);


crearPregunta(
        "Error al conectar MySQL con Spring Boot",
        "La aplicación no logra conectarse a la base de datos configurada.",
        sql,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Problema usando Pageable en Spring Data",
        "La paginación no devuelve los resultados esperados.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "Validaciones con @Valid no funcionan",
        "Al enviar datos incorrectos el controlador permite continuar.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "Error al convertir Entity a DTO",
        "Al convertir entidades pierdo información de las relaciones.",
        jpa,
        Status.RESUELTO
);


crearPregunta(
        "LazyInitializationException en Hibernate",
        "Al consultar una relación aparece un error de sesión cerrada.",
        jpa,
        Status.RESPONDIDO
);


crearPregunta(
        "Configurar variables de entorno en Spring Boot",
        "Quiero separar mis credenciales del archivo application.properties.",
        springBoot,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Crear relaciones bidireccionales en JPA",
        "No sé cuál es la mejor forma de mapear relaciones entre entidades.",
        jpa,
        Status.RESPONDIDO
);


crearPregunta(
        "Problema con anotación @Transactional",
        "Los cambios no se guardan aunque el método se ejecuta.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "Spring Security bloquea mi endpoint",
        "Un endpoint público está solicitando autenticación.",
        security,
        Status.RESUELTO
);


crearPregunta(
        "Error al crear un Bean en Spring",
        "El contexto no logra encontrar una dependencia.",
        springBoot,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Problema con inyección de dependencias",
        "Spring no crea correctamente una instancia del servicio.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "No funciona @Autowired",
        "La dependencia aparece como null.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "Error con migraciones Flyway",
        "La aplicación falla al agregar una migración.",
        sql,
        Status.RESUELTO
);


crearPregunta(
        "Problema con enums en JPA",
        "No puedo guardar correctamente un enum.",
        jpa,
        Status.RESPONDIDO
);


crearPregunta(
        "Error usando Lombok",
        "Las anotaciones no generan los métodos esperados.",
        java,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Configurar CORS en Spring Boot",
        "El frontend no puede consumir la API.",
        springBoot,
        Status.RESUELTO
);


crearPregunta(
        "Problema con consumo de API externa",
        "No logro mapear la respuesta JSON.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "RestTemplate no devuelve datos",
        "La llamada responde pero no obtengo el objeto.",
        springBoot,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Migrar de RestTemplate a WebClient",
        "Quiero usar programación reactiva.",
        microservicios,
        Status.RESPONDIDO
);


crearPregunta(
        "Error en endpoint POST",
        "El controlador devuelve error 400.",
        springBoot,
        Status.RESUELTO
);


crearPregunta(
        "Problema con RequestBody y DTO",
        "Los datos enviados no llegan correctamente.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "Manejo de excepciones global en Spring",
        "Quiero respuestas personalizadas para errores.",
        springBoot,
        Status.RESUELTO
);


crearPregunta(
        "Crear paginación con Spring Boot",
        "Necesito mostrar resultados por páginas.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "Problema con ordenamiento de consultas",
        "Los resultados no aparecen ordenados.",
        sql,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Consulta personalizada con JPQL",
        "Necesito filtros específicos en consultas.",
        jpa,
        Status.RESPONDIDO
);


crearPregunta(
        "Error usando nativeQuery",
        "La consulta SQL funciona pero falla en JPA.",
        sql,
        Status.RESUELTO
);


crearPregunta(
        "Problema con relaciones OneToMany",
        "Los elementos hijos no se guardan.",
        jpa,
        Status.RESPONDIDO
);


crearPregunta(
        "Eliminar entidades relacionadas en JPA",
        "Quedan registros relacionados al borrar.",
        jpa,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Problema con claves foráneas",
        "La base de datos rechaza una operación.",
        sql,
        Status.RESUELTO
);


crearPregunta(
        "Configurar PostgreSQL con Spring Boot",
        "Quiero cambiar MySQL por PostgreSQL.",
        sql,
        Status.RESPONDIDO
);


crearPregunta(
        "Pruebas con MockMvc",
        "Estoy probando controladores y falla la petición.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "Problema con Spring Boot Test",
        "Los tests no levantan el contexto.",
        springBoot,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Subir archivos con Spring Boot",
        "Necesito guardar imágenes enviadas desde formulario.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "Generar documentación con Swagger",
        "Quiero documentar mis endpoints REST.",
        springBoot,
        Status.RESUELTO
);


crearPregunta(
        "Problema con autenticación Bearer Token",
        "Insomnia no envía correctamente el token.",
        security,
        Status.RESPONDIDO
);


crearPregunta(
        "Guardar fechas con LocalDateTime",
        "La fecha almacenada no coincide.",
        java,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Error con zona horaria en Java",
        "Las fechas aparecen con horas diferentes.",
        java,
        Status.RESPONDIDO
);


crearPregunta(
        "Crear filtros dinámicos con JPA",
        "Necesito búsquedas con varios parámetros.",
        jpa,
        Status.RESUELTO
);


crearPregunta(
        "Problema con application.properties",
        "Spring sigue usando valores antiguos.",
        springBoot,
        Status.RESPONDIDO
);


crearPregunta(
        "Optimizar consultas Hibernate",
        "La aplicación hace demasiadas consultas.",
        jpa,
        Status.RESUELTO
);


crearPregunta(
        "Problema con N+1 en Hibernate",
        "Hibernate genera demasiadas consultas.",
        jpa,
        Status.RESPONDIDO
);


crearPregunta(
        "Dockerizar aplicación Spring Boot",
        "Quiero crear una imagen Docker.",
        docker,
        Status.RESUELTO
);


crearPregunta(
        "Configurar variables secretas en producción",
        "Necesito proteger credenciales.",
        docker,
        Status.SIN_RESPUESTA
);


crearPregunta(
        "Crear roles y permisos con Spring Security",
        "Quiero restringir acceso según usuario.",
        security,
        Status.RESUELTO
);


crearPregunta(
        "Problema al desplegar Spring Boot",
        "Funciona localmente pero falla en servidor.",
        docker,
        Status.RESPONDIDO
);


crearPregunta(
        "Conectar frontend React con Spring Boot",
        "El frontend no logra comunicarse con la API.",
        frontend,
        Status.RESUELTO
);


crearPregunta(
        "Error general en aplicación Spring Boot",
        "La aplicación falla y no sé qué capa está fallando.",
        springBoot,
        Status.RESPONDIDO
);
    }

private void crearPregunta(
        String titulo,
        String mensaje,
        Curso curso,
        Status status
){


    Pregunta pregunta = new Pregunta();


    pregunta.setTitulo(titulo);

    pregunta.setMensaje(mensaje);


    pregunta.setCurso(curso);


    pregunta.setAutor(
        faker.options()
        .nextElement(usuarioRepository.findAll())
    );


    pregunta.setFechaCreacion(
        LocalDateTime.now()
    );


    pregunta.setActivo(true);


    pregunta.setStatus(status);


    preguntaRepository.save(pregunta);



    }

    private void crearRespuestas() {

        List<Pregunta> preguntas = preguntaRepository.findAll();

        List<Usuario> usuarios = usuarioRepository.findAll();

        String[][] respuestasFake = {

                // 0 - Error al levantar Spring Boot
                {
                        "Revisa el mensaje completo del error en consola, normalmente la causa aparece al final del stacktrace.",
                        "Verifica que todas las dependencias estén correctamente declaradas en el pom.xml.",
                        "Puede ser un problema con la configuración de un Bean o alguna propiedad faltante."
                },

                // 1 - Problema con relación ManyToOne en JPA
                {
                        "Revisa que la relación tenga correctamente @ManyToOne y @JoinColumn.",
                        "Asegúrate de estar enviando una entidad existente y no un objeto vacío.",
                        "También puedes revisar si la clave foránea está creada correctamente en la base de datos."
                },

                // 2 - JWT devuelve error 403
                {
                        "Verifica que el token se envíe como Bearer Token en el header Authorization.",
                        "Revisa la configuración de SecurityFilterChain y la ubicación del filtro JWT.",
                        "Comprueba que el usuario tenga el rol necesario para acceder al recurso."
                },

                // 3 - Error al conectar MySQL con Spring Boot
                {
                        "Revisa usuario, contraseña y nombre de la base de datos en application.properties.",
                        "Comprueba que el servidor MySQL esté iniciado y aceptando conexiones.",
                        "También revisa que la dependencia del driver de MySQL esté agregada."
                },

                // 4 - Problema usando Pageable en Spring Data
                {
                        "Verifica que el método del Repository reciba correctamente Pageable.",
                        "Recuerda que la numeración de páginas normalmente empieza desde cero.",
                        "Revisa que la respuesta esté devolviendo Page y no una lista normal."
                },

                // 5 - Validaciones con @Valid no funcionan
                {
                        "Comprueba que tengas la anotación @Valid en el parámetro del controlador.",
                        "Revisa que los campos del DTO tengan las anotaciones correctas.",
                        "También necesitas tener la dependencia de Validation en Spring Boot."
                },

                // 6 - Error al convertir Entity a DTO
                {
                        "Puedes crear un método estático en el DTO para centralizar la conversión.",
                        "Revisa que estés incluyendo todos los campos necesarios en el constructor del DTO.",
                        "Si hay relaciones, evita devolver entidades completas y usa DTOs anidados."
                },

                // 7 - LazyInitializationException en Hibernate
                {
                        "El error ocurre cuando accedes a una relación fuera de la sesión activa.",
                        "Puedes usar fetch adecuado o cargar la relación antes de cerrar la transacción.",
                        "Otra opción es usar consultas con JOIN FETCH."
                },

                // 8 - Configurar variables de entorno en Spring Boot
                {
                        "Puedes reemplazar valores sensibles usando variables del sistema.",
                        "Revisa que el nombre de la variable coincida con el usado en application.properties.",
                        "Es recomendable no subir claves privadas al repositorio."
                },

                // 9 - Crear relaciones bidireccionales en JPA
                {
                        "Define bien cuál entidad será la dueña de la relación.",
                        "Usa mappedBy para evitar crear columnas duplicadas.",
                        "Ten cuidado con los ciclos infinitos al devolver JSON."
                },

                // 10 - Problema con @Transactional
                {
                        "Verifica que el método esté siendo llamado desde otro Bean de Spring.",
                        "Recuerda que las transacciones funcionan mediante proxies.",
                        "Comprueba que no estés atrapando excepciones antes de que Spring haga rollback."
                },

                // 11 - Spring Security bloquea mi endpoint
                {
                        "Revisa si el endpoint está agregado correctamente en requestMatchers.",
                        "Comprueba si estás usando permitAll donde corresponde.",
                        "Verifica la configuración del filtro de autenticación."
                },

                // 12 - Error al crear un Bean en Spring
                {
                        "Revisa que la clase tenga la anotación correcta como @Service o @Component.",
                        "Puede faltar un constructor o una dependencia requerida.",
                        "Revisa el paquete donde está la clase para que Spring pueda escanearla."
                },

                // 13 - Problema con inyección de dependencias
                {
                        "La inyección por constructor suele ser la opción recomendada.",
                        "Verifica que el Repository tenga la anotación correcta de Spring Data.",
                        "Revisa que no exista una dependencia circular entre servicios."
                },

                // 14 - No funciona @Autowired
                {
                        "Comprueba que la clase donde usas @Autowired sea administrada por Spring.",
                        "Evita crear objetos con new porque Spring no podrá inyectar dependencias.",
                        "Revisa si la dependencia realmente existe como Bean."
                },

                // 15 - Error con migraciones Flyway
                {
                        "Revisa que el nombre de la migración siga el formato correcto de Flyway.",
                        "Verifica si existe algún cambio incompatible con la estructura actual de la base de datos.",
                        "Puedes revisar la tabla flyway_schema_history para ver qué migraciones se ejecutaron."
                },

                // 16 - Problema con enums en JPA
                {
                        "Puedes usar @Enumerated(EnumType.STRING) para guardar el nombre del enum.",
                        "Revisa que el valor que llega coincida exactamente con el enum definido.",
                        "Evita guardar enums como números porque pueden cambiar si agregas nuevos valores."
                },

                // 17 - Error usando Lombok
                {
                        "Confirma que el plugin de Lombok esté instalado en el IDE.",
                        "Revisa que la dependencia de Lombok esté agregada correctamente en Maven.",
                        "Intenta recompilar el proyecto para regenerar los métodos automáticamente."
                },

                // 18 - Configurar CORS en Spring Boot
                {
                        "Puedes agregar una configuración global de CORS para permitir el origen del frontend.",
                        "Revisa que el método HTTP utilizado esté permitido en la configuración.",
                        "Comprueba que los headers necesarios estén incluidos."
                },

                // 19 - Problema con consumo de API externa
                {
                        "Revisa el JSON recibido antes de intentar mapearlo a un objeto.",
                        "Comprueba que los nombres de los campos coincidan con tu DTO.",
                        "Puedes usar logs para revisar exactamente la respuesta del servicio externo."
                },

                // 20 - RestTemplate no devuelve datos
                {
                        "Verifica que la URL utilizada sea correcta y accesible.",
                        "Comprueba que la clase destino tenga la estructura del JSON esperado.",
                        "Revisa si necesitas agregar headers en la petición."
                },

                // 21 - Migrar de RestTemplate a WebClient
                {
                        "WebClient permite hacer llamadas asíncronas y trabajar con programación reactiva.",
                        "Puedes comenzar reemplazando llamadas simples de RestTemplate por WebClient.",
                        "Revisa el manejo de Mono y Flux cuando cambies la implementación."
                },

                // 22 - Error en endpoint POST
                {
                        "Revisa que el JSON enviado coincida con la estructura del DTO.",
                        "Comprueba que las validaciones del objeto estén funcionando correctamente.",
                        "Verifica que el método tenga la anotación @PostMapping correcta."
                },

                // 23 - Problema con RequestBody y DTO
                {
                        "Comprueba que los nombres de las propiedades del JSON coincidan con el DTO.",
                        "Revisa si falta algún constructor o configuración de Jackson.",
                        "Puedes probar primero enviando un JSON más pequeño para detectar el campo que falla."
                },

                // 24 - Manejo de excepciones global en Spring
                {
                        "Puedes crear una clase con @RestControllerAdvice para centralizar errores.",
                        "Define diferentes ExceptionHandler según el tipo de excepción.",
                        "Devuelve respuestas consistentes usando DTOs de error."
                },

                // 25 - Crear paginación con Spring Boot
                {
                        "Usa Pageable en el Repository y devuelve un objeto Page.",
                        "Puedes controlar tamaño y página usando parámetros del endpoint.",
                        "Revisa que el frontend envíe correctamente page y size."
                },

                // 26 - Problema con ordenamiento de consultas
                {
                        "Puedes usar Sort junto con Pageable para ordenar resultados.",
                        "Verifica que el nombre del campo coincida con el atributo de la entidad.",
                        "Revisa la consulta generada por Hibernate si el orden no funciona."
                },

                // 27 - Consulta personalizada con JPQL
                {
                        "Puedes crear métodos con @Query para consultas específicas.",
                        "Verifica que uses nombres de atributos de la entidad y no columnas SQL.",
                        "Prueba primero la consulta directamente antes de integrarla."
                },

                // 28 - Error usando nativeQuery
                {
                        "Cuando usas SQL nativo debes revisar los nombres reales de las columnas.",
                        "Verifica que el resultado pueda convertirse correctamente al tipo esperado.",
                        "Evita nativeQuery si una consulta JPQL puede resolver el problema."
                },

                // 29 - Problema con relaciones OneToMany
                {
                        "Revisa cuál entidad es la dueña de la relación.",
                        "Puede ser necesario configurar cascade si quieres guardar hijos automáticamente.",
                        "Comprueba que la colección esté inicializada correctamente."
                },

                // 30 - Eliminar entidades relacionadas en JPA
                {
                        "Revisa si necesitas configurar orphanRemoval o CascadeType.REMOVE.",
                        "Comprueba primero las relaciones antes de eliminar el registro principal.",
                        "Puede que la base de datos esté bloqueando la eliminación por una clave foránea."
                },

                // 31 - Problema con claves foráneas
                {
                        "Verifica que el registro relacionado exista antes de guardar la entidad.",
                        "Revisa la definición de la columna con @JoinColumn.",
                        "El error normalmente indica que estás intentando guardar una relación inexistente."
                },

                // 32 - Configurar PostgreSQL con Spring Boot
                {
                        "Cambia el driver y la URL de conexión en application.properties.",
                        "Verifica que el usuario tenga permisos sobre la base de datos.",
                        "Revisa que la versión del driver sea compatible con tu versión de PostgreSQL."
                },

                // 33 - Pruebas con MockMvc
                {
                        "Comprueba que el contexto de prueba esté cargando correctamente los controladores.",
                        "Puedes usar @WebMvcTest para probar solamente la capa web.",
                        "Verifica que el JSON esperado coincida con la respuesta real."
                },

                // 34 - Problema con Spring Boot Test
                {
                        "Revisa si falta alguna configuración necesaria para levantar el contexto.",
                        "Puedes probar agregando perfiles específicos para pruebas.",
                        "Comprueba que los beans requeridos estén disponibles durante el test."
                },

                // 35 - Subir archivos con Spring Boot
                {
                        "Usa MultipartFile para recibir archivos enviados desde el cliente.",
                        "Verifica el límite de tamaño permitido en la configuración.",
                        "Comprueba la ruta donde estás almacenando los archivos."
                },

                // 36 - Generar documentación con Swagger
                {
                        "Agrega las dependencias necesarias de OpenAPI para Spring Boot.",
                        "Verifica la URL donde Swagger expone la documentación.",
                        "Puedes agregar anotaciones para describir mejor tus endpoints."
                },

                // 37 - Problema con autenticación Bearer Token
                {
                        "Revisa que el header Authorization tenga el formato Bearer seguido del token.",
                        "Comprueba que el token no esté expirado.",
                        "Verifica que el filtro de seguridad esté leyendo correctamente el header."
                },

                // 38 - Guardar fechas con LocalDateTime
                {
                        "Revisa el formato que recibe Jackson al convertir fechas.",
                        "Puedes configurar un formato específico con anotaciones de fecha.",
                        "Comprueba la zona horaria configurada en la aplicación."
                },

                // 39 - Error con zona horaria en Java
                {
                        "Usa ZonedDateTime si necesitas manejar diferentes zonas horarias.",
                        "Verifica la configuración del servidor donde corre la aplicación.",
                        "Muchas veces el problema está en la base de datos y no en Java."
                },

                // 40 - Crear filtros dinámicos con JPA
                {
                        "Puedes utilizar Specifications de Spring Data para filtros complejos.",
                        "Evita crear demasiados métodos diferentes en el Repository.",
                        "Centraliza las condiciones de búsqueda para mantener el código limpio."
                },

                // 41 - Problema con application.properties
                {
                        "Revisa que el perfil activo sea el correcto.",
                        "Comprueba si existe otro archivo de configuración sobrescribiendo valores.",
                        "Puedes imprimir las propiedades cargadas para verificar su valor."
                },

                // 42 - Optimizar consultas Hibernate
                {
                        "Revisa las consultas generadas por Hibernate usando logs SQL.",
                        "Evita cargar relaciones innecesarias cuando no se necesitan.",
                        "Puedes usar DTO projections para mejorar rendimiento."
                },

                // 43 - Problema con N+1 en Hibernate
                {
                        "El problema aparece cuando Hibernate hace consultas adicionales por cada relación.",
                        "Puedes solucionarlo usando JOIN FETCH en la consulta.",
                        "Revisa si estás usando correctamente fetch LAZY y EAGER."
                },

                // 44 - Dockerizar aplicación Spring Boot
                {
                        "Crea un Dockerfile con la imagen base de Java que necesites.",
                        "Verifica que el puerto del contenedor coincida con el de Spring Boot.",
                        "Puedes usar variables de entorno para configurar la aplicación."
                },

                // 45 - Configurar variables secretas en producción
                {
                        "No guardes contraseñas directamente en el repositorio.",
                        "Usa variables de entorno o servicios de secretos.",
                        "Revisa que las credenciales sean diferentes entre desarrollo y producción."
                },

                // 46 - Crear roles y permisos con Spring Security
                {
                        "Define claramente qué endpoints necesita proteger cada rol.",
                        "Puedes usar hasRole o hasAuthority según tu diseño.",
                        "Verifica que los roles almacenados coincidan con los esperados por Spring."
                },

                // 47 - Problema al desplegar Spring Boot
                {
                        "Revisa los logs del servidor y no solamente el error inicial.",
                        "Comprueba que las variables de entorno estén configuradas.",
                        "Verifica que la versión de Java del servidor sea compatible."
                },

                // 48 - Conectar frontend React con Spring Boot
                {
                        "Revisa la configuración CORS de la API.",
                        "Comprueba que el frontend esté enviando correctamente los headers.",
                        "Verifica que la URL del backend sea correcta."
                },

                // 49 - Error general en aplicación Spring Boot
                {
                        "Revisa primero el stacktrace completo antes de modificar código.",
                        "Prueba reproducir el problema con una petición mínima.",
                        "Divide el problema para identificar qué capa está fallando."
                }

        };

  for(int i = 0; i < preguntas.size(); i++){


        Pregunta pregunta = preguntas.get(i);



        if(pregunta.getStatus() == Status.SIN_RESPUESTA){

            continue;

        }



        String[] respuestas =
                respuestasFake[i];



        for(int j = 0; j < respuestas.length; j++){


            Respuesta respuesta = new Respuesta();


            respuesta.setMensaje(
                    respuestas[j]
            );


            respuesta.setPregunta(
                    pregunta
            );

            respuesta.setFechaCreacion(
                    LocalDateTime.now()
            );


            respuesta.setAutor(
                    faker.options()
                    .nextElement(usuarios)
            );



            if(
                pregunta.getStatus() == Status.RESUELTO
                &&
                j == 0
            ){

                respuesta.setSolucion(true);

            }else{

                respuesta.setSolucion(false);

            }



            respuestaRepository.save(respuesta);

        }

    }

    }
}