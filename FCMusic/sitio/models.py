from django.db import models

class Artista(models.Model):
    artista_id = models.IntegerField(primary_key = True)
    nombre = models.CharField(max_length = 30)
    nombre_estilo = models.CharField(max_length = 30)


class Album(models.Model):
    album_id = models.IntegerField(primary_key = True)
    artistas = models.ManyToManyField(Artista)


class Genero(models.Model):
    genero_id = models.IntegerField(primary_key = True)
    nombre = models.CharField(max_length = 30)


class Cancion(models.Model):

    cancion_id = models.IntegerField(primary_key = True)
    artista_id = models.ForeignKey(Artista, on_delete = models.CASCADE)
    album_id = models.ForeignKey(Album, on_delete = models.CASCADE)

    genero = models.ManyToManyField(Genero)

    titulo = models.CharField(max_length = 30)
    titulo_estilo = models.CharField(max_length = 30)
    ruta = models.CharField(max_length = 50)
    año_lanzamiento = models.IntegerField()
    n_vistas = models.IntegerField()
    duracion = models.DurationField()

    # class Meta:
    #     db_table = "Cancion"


class Usuario(models.Model):
    usuario_id = models.IntegerField(primary_key = True)
    nombre = models.CharField(max_length = 30)
    correo = models.EmailField()

    artista_seguido = models.ManyToManyField(Artista)
    me_gusta = models.ManyToManyField(Cancion)
    estrellas = models.ManyToManyField(
        Cancion,
        related_name = "estrellas",
        through="Cancion_estrella")
    escuchadas_reciente = models.ManyToManyField(
        Cancion,
        related_name = "escuchadas_reciente",
        through="Escuchado_reciente")


class Cancion_estrella(models.Model):
    cancion_id = models.ForeignKey(Cancion, on_delete=models.CASCADE)
    usuario_id = models.ForeignKey(Usuario, on_delete=models.CASCADE)
    estrella = models.IntegerField()


class Escuchado_reciente(models.Model):
    cancion_id = models.ForeignKey(Cancion, on_delete=models.CASCADE)
    usuario_id = models.ForeignKey(Usuario, on_delete=models.CASCADE)
    tiempo_escuchado = models.DateTimeField()


class Lista_reproduccion(models.Model):
    lista_id = models.IntegerField(primary_key = True)
    usuario_id = models.IntegerField()
    estado_publico = models.BinaryField()
    nombre = models.CharField(max_length = 30)


class Cancion_lista_reproduccion(models.Model):
    lista_id = models.ForeignKey(Lista_reproduccion, on_delete=models.CASCADE)
    cancion_id = models.ForeignKey(Cancion, on_delete=models.CASCADE)
    indice = models.IntegerField()


class Notificacion(models.Model):
    notif_id = models.IntegerField(primary_key = True)
    usuario_id = models.OneToOneField(Usuario, on_delete=models.CASCADE)
    descripcion = models.TextField()
    tiempo_creado = models.DateTimeField()
    tiempo_visto = models.DateTimeField()
