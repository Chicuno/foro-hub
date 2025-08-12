create table topicos(

id bigint not null auto_increment,
titulo varchar(100) not null,
mensaje text not null,
unique key uk_mensaje (mensaje(255)),
fecha_creacion datetime not null,
status varchar(20) not null,
autor_id bigint not null,
curso_id bigint not null,
activo boolean not null,


primary key(id),

constraint fk_topicos_autor
foreign key(autor_id)
references usuarios(id),

constraint fk_topicos_curso
foreign key(curso_id)
references cursos(id)
);