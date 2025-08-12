create table respuestas(

id bigint not null auto_increment,
mensaje text not null,
topico_id bigint not null,
fecha_creacion datetime not null,
autor_id bigint not null,
solucion boolean not null,

primary key(id),

constraint fk_respuestas_topico
foreign key(topico_id)
references topicos(id),

constraint fk_respuestas_autor
foreign key(autor_id)
references usuarios(id)

);