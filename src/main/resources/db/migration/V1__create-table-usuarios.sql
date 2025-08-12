create table usuarios(

id bigint not null auto_increment,
nombre varchar(100) not null,
correo_electronico varchar(100) not null unique,
nombre_usuario varchar(100) not null unique,
contrasena varchar(100) not null,
perfil varchar(100) not null,
activo boolean not null,

primary key(id)

);